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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.getspout.spout.block.SpoutCraftChunk;
import org.getspout.spout.inventory.SimpleMaterialManager;
import org.getspout.spout.player.SpoutcraftPlayer;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.ServerTickEvent;

public class ServerTickTask implements Runnable {
	@Override
	public void run() {
		Spout.getInstance().playerListener.manager.onServerTick();
		((SimpleMaterialManager)SpoutManager.getMaterialManager()).onTick();
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		for (Player player : online) {
			if (player instanceof SpoutcraftPlayer) {
				((SpoutcraftPlayer)player).onTick();
			}
		}
		SpoutCraftChunk.updateTicks();
		ServerTickEvent event = new ServerTickEvent();
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
}
