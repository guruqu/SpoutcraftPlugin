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

import java.lang.reflect.Field;

import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.INetworkManager;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.PlayerConnection;
import net.minecraft.server.v1_6_R3.NetworkManager;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet14BlockDig;
import net.minecraft.server.v1_6_R3.Packet18ArmAnimation;
import net.minecraft.server.v1_6_R3.Packet20NamedEntitySpawn;
import net.minecraft.server.v1_6_R3.Packet24MobSpawn;
import net.minecraft.server.v1_6_R3.Packet3Chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
;
import org.getspout.spout.player.SpoutcraftPlayer;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.DecayingLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SpoutPlayerConnection extends PlayerConnection {
	public SpoutPlayerConnection(MinecraftServer minecraftserver, INetworkManager networkmanager, EntityPlayer entityplayer) {
		super(minecraftserver, networkmanager, entityplayer);
		// Lower the active packet queue size in bytes by 9 megabytes, to allow for 10mb of data in a players queue
		try {
			Field z = NetworkManager.class.getDeclaredField("z");
			z.setAccessible(true);
			int size = (Integer) z.get(this.networkManager);
			z.set(this.networkManager, size - 1024 * 1024 * 9);
		} catch (Exception ignored) {
		}
	}

	@Override
	public void a(Packet3Chat packet) {
		String chat = packet.message;
		if (chat.trim().isEmpty()) {
			return;
		}
		if (chat.equalsIgnoreCase("/reload")) {
			final SpoutcraftPlayer player = (SpoutcraftPlayer) SpoutcraftPlayer.getPlayer(getPlayer());
			if (!player.isSpoutCraftEnabled()) {
				player.sendMessage(ChatColor.RED + "Spout does not support the /reload command.");
				player.sendMessage(ChatColor.RED + "Unexpected behavior may occur.");
				player.sendMessage(ChatColor.RED + "We recommend using /stop and restarting.");
				player.sendMessage(ChatColor.RED + "Or you can use /spout reload to reload the config.");
				player.sendMessage(ChatColor.RED + "If you want to use /reload anyway, use the command again.");
			} else {
				final Label warning = new DecayingLabel(ChatColor.RED + "Spout does not support the /reload command." + "\n" + ChatColor.RED + "Unexpected behavior may occur." + "\n" + ChatColor.RED + "We recommend using /stop and restarting." + " \n" + ChatColor.RED + "Or you can use /spout reload to reload the config." + "\n" + ChatColor.RED + "If you want to use /reload anyway, use the command again.", 200);
				warning.setX(100).setY(100).setPriority(RenderPriority.Lowest);
				player.getMainScreen().attachWidget(Spout.getInstance(), warning);
			}
			return;
		}
		super.a(packet);
	}

	@Override
	public void a(Packet18ArmAnimation packet) {
		if (packet.a == -42) {
			SpoutcraftPlayer player = (SpoutcraftPlayer) SpoutcraftPlayer.getPlayer(getPlayer());
			player.setBuildVersion(1); // Don't know yet, just set above zero
			try {
				Spout.getInstance().playerListener.manager.onSpoutcraftEnable((SpoutPlayer) getPlayer());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			super.a(packet);
		}
	}

	@Override
	public void a(Packet14BlockDig packet) {
		final SpoutcraftPlayer player = (SpoutcraftPlayer) SpoutcraftPlayer.getPlayer(getPlayer());
		boolean inAir = false;
		if (player.canFly() && !player.getHandle().onGround) {
			inAir = true;
			player.getHandle().onGround = true;
		}
		super.a(packet);
		if (inAir) {
			player.getHandle().onGround = false;
		}
	}

	@Override
	public void sendPacket(Packet packet) {
		if (packet instanceof Packet20NamedEntitySpawn) {
			final SpoutPlayer player = SpoutManager.getPlayerFromId(((Packet20NamedEntitySpawn) packet).a);
			if (player != null) {
				((SpoutcraftPlayer) player).updateAppearance((SpoutPlayer)getPlayer());
			}
		} else if (packet instanceof Packet24MobSpawn) {
			final SpoutPlayer player = SpoutManager.getPlayer(getPlayer());
			final LivingEntity entity = (LivingEntity) SpoutManager.getEntityFromId(((Packet24MobSpawn) packet).a);
			if (player != null) {
				((SpoutcraftPlayer) player).updateEntitySkins(entity);
			}
		}
	}
}
