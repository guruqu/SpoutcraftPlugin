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
package org.getspout.spoutapi.packet;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.getspout.spoutapi.event.input.RenderDistanceChangeEvent;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.RenderDistance;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketChangeRenderDistance implements SpoutPacket {
	public RenderDistance distance = RenderDistance.TINY;
	public RenderDistance max = RenderDistance.TINY;
	public RenderDistance min = RenderDistance.TINY;

	protected PacketChangeRenderDistance() {
	}

	public PacketChangeRenderDistance(boolean resetMax, boolean resetMin) {
		if (resetMax) {
			max = RenderDistance.RESET;
		}
		if (resetMin) {
			min = RenderDistance.RESET;
		}
	}

	public PacketChangeRenderDistance(RenderDistance distance, RenderDistance max, RenderDistance min) {
		if (distance != null) {
			this.distance = distance;
		}
		if (max != null) {
			this.max = max;
		}
		if (min != null) {
			this.min = min;
		}
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		distance = RenderDistance.get(buf.get());
		max = RenderDistance.get(buf.get());
		min = RenderDistance.get(buf.get());
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.put(distance.getValue());
		buf.put(max.getValue());
		buf.put(min.getValue());
	}

	@Override
	public void handle(SpoutPlayer player) {
		final RenderDistanceChangeEvent event = new RenderDistanceChangeEvent(player, distance);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			player.setRenderDistance(event.getNewRenderDistance(), false);
			player.setMaximumRenderDistance(event.getNewMaxRenderDistance());
			player.setMinimumRenderDistance(event.getNewMinRenderDistance());
		} else {
			player.sendPacket(new PacketChangeRenderDistance(player.getRenderDistance(), null, null));
		}
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
