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
package org.getspout.spoutapi.event.input;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.RenderDistance;
import org.getspout.spoutapi.player.SpoutPlayer;

public class RenderDistanceChangeEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private RenderDistance newDistance, maxDistance, minDistance;
	private boolean cancel = false;

	public RenderDistanceChangeEvent(SpoutPlayer player, RenderDistance newDistance) {
		this(player, newDistance, null, null);
	}

	public RenderDistanceChangeEvent(SpoutPlayer player, RenderDistance newDistance, RenderDistance maxDistance, RenderDistance minDistance) {
		super(player);
		this.newDistance = newDistance;
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
	}

	public RenderDistance getCurrentRenderDistance() {
		return SpoutManager.getPlayer(player).getRenderDistance();
	}

	public RenderDistance getNewRenderDistance() {
		return newDistance;
	}

	public RenderDistance getCurrentMaxRenderDistance() {
		return SpoutManager.getPlayer(player).getMaximumRenderDistance();
	}

	public RenderDistance getNewMaxRenderDistance() {
		return maxDistance;
	}

	public RenderDistance getCurrentMinRenderDistance() {
		return SpoutManager.getPlayer(player).getMinimumRenderDistance();
	}

	public RenderDistance getNewMinRenderDistance() {
		return minDistance;
	}

	public void setNewDistance(RenderDistance newDistance) {
		this.newDistance = newDistance;
	}

	public void setMaxDistance(RenderDistance maxDistance) {
		this.maxDistance = maxDistance;
	}

	public void setMinDistance(RenderDistance minDistance) {
		this.minDistance = minDistance;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
