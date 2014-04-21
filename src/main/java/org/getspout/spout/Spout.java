/*
 * This file is part of SpoutcraftPlugin.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org//>
 * SpoutcraftPlugin is licensed under the GNU Lesser General Public License.
 *
 * SpoutcraftPlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_6_R3.Packet18ArmAnimation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.getspout.spout.block.SpoutCraftChunk;
import org.getspout.spout.block.mcblock.CustomMCBlock;
import org.getspout.spout.command.SpoutCommand;
import org.getspout.spout.config.ConfigReader;
import org.getspout.spout.inventory.SimpleMaterialManager;
import org.getspout.spout.inventory.SpoutInventoryBuilder;
import org.getspout.spout.item.mcitem.CustomItemFlint;
import org.getspout.spout.item.mcitem.CustomItemPickaxe;
import org.getspout.spout.item.mcitem.CustomItemSpade;
import org.getspout.spout.keyboard.SimpleKeyBindingManager;
import org.getspout.spout.listeners.SpoutBlockListener;
import org.getspout.spout.listeners.SpoutPlayerListener;
import org.getspout.spout.listeners.PluginListener;
import org.getspout.spout.listeners.SpoutWorldListener;
import org.getspout.spout.listeners.SpoutEntityListener;
import org.getspout.spout.listeners.InventoryListener;
import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.CustomPacketPipeline;
import org.getspout.spout.player.SimpleBiomeManager;
import org.getspout.spout.player.SimpleFileManager;
import org.getspout.spout.player.SimplePlayerChunkMap;
import org.getspout.spout.player.SimpleSkyManager;
import org.getspout.spout.player.SpoutcraftPlayer;
import org.getspout.spout.sound.SimpleSoundManager;
import org.getspout.spout.util.DeadlockMonitor;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.chunkstore.SimpleChunkDataManager;
import org.getspout.spoutapi.inventory.ItemMap;
import org.getspout.spoutapi.io.CRCStore;
import org.getspout.spoutapi.io.store.FlatFileStore;
import org.getspout.spout.packet.builtin.PacketChangeRenderDistance;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Spout extends JavaPlugin {
	public SpoutPlayerListener playerListener;
	protected SpoutWorldListener chunkListener;
	protected SpoutWorldMonitorListener chunkMonitorListener;
	protected SpoutBlockListener blockListener;
	protected SpoutEntityListener entityListener;
	protected CustomPacketPipeline pipeline;
	protected PluginListener pluginListener;
	protected SpoutCustomBlockMonitor blockMonitor;
	protected static Spout instance;
	protected FlatFileStore<String> CRCConfig;
	protected FlatFileStore<Integer> itemMapConfig;
	protected ItemMap serverItemMap;
	protected final List<SpoutPlayer> playersOnline = new ArrayList<SpoutPlayer>();
	protected Thread shutdownThread = null;
	protected InventoryListener invListener;
	private boolean hardDisable = false;

	public Spout() {
		super();
		Spout.instance = this;
		SpoutManager.getInstance().setSoundManager(new SimpleSoundManager());
		SpoutManager.getInstance().setSkyManager(new SimpleSkyManager());
		SpoutManager.getInstance().setInventoryBuilder(new SpoutInventoryBuilder());
		SpoutManager.getInstance().setPlayerChunkMap(new SimplePlayerChunkMap());
		SpoutManager.getInstance().setChunkDataManager(new SimpleChunkDataManager());
		SpoutManager.getInstance().setBiomeManager(new SimpleBiomeManager());
		SpoutManager.getInstance().setFileManager(new SimpleFileManager());
		SpoutManager.getInstance().setKeyBindingManager(new SimpleKeyBindingManager());
		SpoutManager.getInstance().setMaterialManager(new SimpleMaterialManager());
		SpoutManager.getInstance().setWorldManager(new SimpleWorldManager());
		pipeline = new CustomPacketPipeline(this);
		shutdownThread = new ShutdownThread();
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}

	@Override
	public void onDisable() {
		if (hardDisable) {
			Runtime.getRuntime().removeShutdownHook(shutdownThread);
			return;
		}
		// Order matters
		CustomMCBlock.resetBlocks();
		((SimpleMaterialManager) SpoutManager.getMaterialManager()).reset();
		((SimpleSkyManager) SpoutManager.getSkyManager()).reset();
		((SimplePlayerChunkMap) SpoutManager.getPlayerChunkMap()).onPluginDisable();
		Player[] online = getServer().getOnlinePlayers();
		for (Player player : online) {
			try {
				SpoutcraftPlayer scp = (SpoutcraftPlayer) SpoutcraftPlayer.getPlayer(player);
				scp.resetMovement();
				if (scp.isSpoutCraftEnabled()) {
					scp.sendPacket(new PacketChangeRenderDistance(true, true));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		SpoutCraftChunk.resetAllBukkitChunks();

		getServer().getScheduler().cancelTasks(this);

		SimpleChunkDataManager dm = (SimpleChunkDataManager) SpoutManager.getChunkDataManager();
		dm.unloadAllChunks();
		dm.closeAllFiles();

		CRCConfig.save();

		if (itemMapConfig != null) {
			synchronized (itemMapConfig) {
				itemMapConfig.save();
			}
		}

		SimpleFileManager.clearTempDirectory();

		// End the thread
		PacketCompressionThread.endThread();

		Runtime.getRuntime().removeShutdownHook(shutdownThread);
		super.onDisable();
	}

	@Override
	public void onEnable() {
		(new ConfigReader()).read();

		// The infamous SpoutPlugin build check
		if (ConfigReader.isBuildCheck()) {
			InputStream is = getResource("plugin.yml");
			final YamlConfiguration config = YamlConfiguration.loadConfiguration(is);

			// Format the output from Bukkit.getVersion()
			String output = "";
			if (Bukkit.getServer().getVersion().contains("(MC: ")) {
				String[] temp = Bukkit.getServer().getVersion().split("\\(MC: ");
				output = temp[1].trim().substring(0, 5);
			}

			final String bukkitVersion = output;
			final String minecraftVersion = config.getString("mcversion");

			if (!minecraftVersion.equals(bukkitVersion)) {
				warnMessage(minecraftVersion, bukkitVersion);
				hardDisable = true;
				Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
						warnMessage(minecraftVersion, bukkitVersion);
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (player.isOp()) {
								player.sendMessage("[" + ChatColor.BLUE + "Spout" + ChatColor.WHITE + "] " + ChatColor.RED + "SpoutPlugin is not working correctly, please check the console.");
							}
						}
					}
				}, 1200L, 1200L);
			}
		}
		if (!hardDisable) {
			pipeline.onEnable();
			playerListener = new SpoutPlayerListener(this);
			chunkListener = new SpoutWorldListener(this);
			chunkMonitorListener = new SpoutWorldMonitorListener(this);
			pluginListener = new PluginListener(this);
			entityListener = new SpoutEntityListener(this);
			blockMonitor = new SpoutCustomBlockMonitor(this);
			blockListener = new SpoutBlockListener(this);
			invListener = new InventoryListener(this);

			for (SpoutPlayer player : org.getspout.spoutapi.Spout.getServer().getOnlinePlayers()) {
				SpoutcraftPlayer.resetPlayerConnection(player);
				SpoutcraftPlayer.updatePlayerConnection(player);
				SpoutcraftPlayer.updateBukkitEntity(player);
				authenticate(player);
				playerListener.manager.onPlayerJoin(player);
				((SimplePlayerChunkMap) SpoutManager.getPlayerChunkMap()).onPlayerJoin(player);
				player.setPreCachingComplete(true); // Already done if we are already online!
				synchronized (playersOnline) {
					playersOnline.add(player);
				}
			}

			SpoutCraftChunk.replaceAllBukkitChunks();
			((SimplePlayerChunkMap) SpoutManager.getPlayerChunkMap()).onPluginEnable();

			CustomItemSpade.replaceSpades();
			CustomItemPickaxe.replacePickaxes();
			CustomItemFlint.replaceFlint();
			CustomMCBlock.replaceBlocks();

			PacketCompressionThread.startThread();

			// Start counting ticks
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new ServerTickTask(), 0, 1);

			// Remove mappings from previous loads
			// Can not remove them on disable because the packets will still be in the send queue
			CustomPacket.removeClassMapping();
			CustomPacket.addClassMapping();

			SimpleChunkDataManager dm = (SimpleChunkDataManager) SpoutManager.getChunkDataManager();
			dm.loadAllChunks();

			SimpleMaterialManager.disableFlintStackMix();

			try {
				Class.forName("org.getspout.spoutapi.inventory.SpoutEnchantment");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		// These are safe even if the build check fails
		getCommand("spout").setExecutor(new SpoutCommand(this));

		CRCConfig = new FlatFileStore<String>(new File(this.getDataFolder(), "CRCCache.txt"), String.class);
		CRCConfig.load();

		CRCStore.setConfigFile(CRCConfig);

		itemMapConfig = new FlatFileStore<>(new File(this.getDataFolder(), "itemMap.txt"), Integer.class);
		if (!itemMapConfig.load()) {
			System.out.println("[SpoutPlugin] Unable to load global item map");
		} else {
			serverItemMap = new ItemMap(null, itemMapConfig, null);
		}
		ItemMap.setRootMap(serverItemMap);

		if (ConfigReader.runDeadlockMonitor()) {
			new DeadlockMonitor().start();
		}

		super.onEnable();
	}

	public List<SpoutPlayer> getOnlinePlayers() {
		return playersOnline;
	}
	
	/**
	 * Gets the singleton instance of the Spout plugin
	 * @return Spout plugin
	 */
	public static Spout getInstance() {
		return instance;
	}

	public CustomPacketPipeline getPipeline() {
		return pipeline;
	}

	public void authenticate(Player player) {
			Packet18ArmAnimation packet = new Packet18ArmAnimation();
			packet.a = -42;
			((SpoutcraftPlayer) SpoutcraftPlayer.getPlayer(player)).getPlayerConnection().sendPacket(packet);
	}

	public void warnMessage(String minecraftVersion, String bukkitVersion) {
		Bukkit.getServer().getLogger().info(
				"\n-----------------------------------------------------\n" +
						"|| SpoutPlugin is not working correctly due to version mismatch.\n" +
						"|| Expected Minecraft Server version: " + minecraftVersion + "\n" +
						"|| Current Minecraft Server version: " + bukkitVersion + "\n" +
						"|| Either disable MinecraftVersionCheck in /plugins/Spout/config.yml or update CraftBukkit.\n" +
						"-------------------------------------------------------"
		);
	}
}

class ShutdownThread extends Thread {
	public void run() {
		SimpleChunkDataManager dm = (SimpleChunkDataManager) SpoutManager.getChunkDataManager();
		dm.unloadAllChunks();
		dm.closeAllFiles();
	}
}
