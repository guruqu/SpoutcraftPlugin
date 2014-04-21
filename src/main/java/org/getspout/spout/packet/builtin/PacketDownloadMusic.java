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
package org.getspout.spout.packet.builtin;

import java.io.IOException;

import org.bukkit.Location;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketDownloadMusic extends SpoutPacket {
	int x, y, z;
	int volume, distance;
	boolean soundEffect, notify;
	String URL, plugin;

	protected PacketDownloadMusic() {
	}

	public PacketDownloadMusic(String plugin, String URL, Location loc, int distance, int volume, boolean soundEffect, boolean notify) {
		this.plugin = plugin;
		this.URL = URL;
		this.volume = volume;
		this.soundEffect = soundEffect;
		this.notify = notify;
		if (loc != null) {
			x = loc.getBlockX();
			y = loc.getBlockY();
			z = loc.getBlockZ();
			this.distance = distance;
		} else {
			this.distance = -1;
		}
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		URL = buf.getUTF8();
		plugin = buf.getUTF8();
		distance = buf.getInt();
		x = buf.getInt();
		y = buf.getInt();
		z = buf.getInt();
		volume = buf.getInt();
		soundEffect = buf.getBoolean();
		notify = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(URL);
		buf.putUTF8(plugin);
		buf.putInt(distance);
		buf.putInt(x);
		buf.putInt(y);
		buf.putInt(z);
		buf.putInt(volume);
		buf.putBoolean(soundEffect);
		buf.putBoolean(notify);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
