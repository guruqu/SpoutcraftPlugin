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
package org.getspout.spout.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.getspout.spout.Spout;

import org.getspout.spout.player.SpoutcraftPlayer;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spout.packet.builtin.PacketWaypoint;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SpoutEntityListener implements Listener {
	public SpoutEntityListener(Spout plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof SpoutPlayer) {
			event.setCancelled(event.isCancelled() || !((SpoutPlayer)event.getEntity()).isPreCachingComplete());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof SpoutPlayer) {
			event.setCancelled(event.isCancelled() || !((SpoutPlayer)event.getTarget()).isPreCachingComplete());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityExplode(EntityExplodeEvent event) {
		if  (!event.isCancelled()) {
			for (Block block : event.blockList()) {
				SpoutBlock sb = (SpoutBlock)block;
				sb.setCustomBlock(null);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof SpoutPlayer) {
			Location l = event.getEntity().getLocation();
			((SpoutPlayer)event.getEntity()).sendPacket(new PacketWaypoint(l.getX(), l.getY(), l.getZ(), "", true));
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Spout.getInstance(), new PostDeath((SpoutcraftPlayer)event.getEntity()), 200);
		}
	}
}

class PostDeath implements Runnable {
	SpoutcraftPlayer player;
	public PostDeath(SpoutcraftPlayer player) {
		this.player = player;
	}

	@Override
	public void run() {
		player.updateAppearance(player);
		player.updateWaypoints();
	}
}
