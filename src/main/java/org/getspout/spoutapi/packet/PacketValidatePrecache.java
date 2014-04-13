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
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;
import org.getspout.spout.precache.PrecacheTuple;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketValidatePrecache implements SpoutPacket {
	private int count;
	private PrecacheTuple[] plugins;

	protected PacketValidatePrecache() {
	}

	public PacketValidatePrecache(HashMap<Plugin, Long> pluginMap) {
		count = pluginMap.size();
		plugins = new PrecacheTuple[count];
		int i = 0;
		for (Entry entry : pluginMap.entrySet()) {
			Plugin p = (Plugin) entry.getKey();
			plugins[i] = new PrecacheTuple(
					p.getDescription().getName(),
					p.getDescription().getVersion(),
					(Long) entry.getValue()
			);
			i++;
		}
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		count = buf.getInt();
		if (count > 0) {
			plugins = new PrecacheTuple[count];
			for (int i = 0; i < count; i++) {
				String plugin = buf.getUTF8();
				String version = buf.getUTF8();
				long crc = buf.getLong();
				plugins[i] = new PrecacheTuple(plugin, version, crc);
			}
		}
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(count);
		for (int i = 0; i < count; i++) {
			buf.putUTF8(plugins[i].getPlugin());
			buf.putUTF8(plugins[i].getVersion());
			buf.putLong(plugins[i].getCrc());
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
