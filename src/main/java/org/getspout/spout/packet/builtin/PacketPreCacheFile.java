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

import java.io.File;
import java.io.IOException;

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketPreCacheFile extends SpoutPacket {
	private boolean cached = false;
	private boolean url = false;
	private long expectedCRC;
	private String file;
	private String plugin;

	protected PacketPreCacheFile() {
	}

	public PacketPreCacheFile(String plugin, String file, long expectedCRC, boolean url) {
		this.file = file;
		this.plugin = plugin;
		this.expectedCRC = expectedCRC;
		this.url = url;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		cached = buf.getBoolean();
		url = buf.getBoolean();
		expectedCRC = buf.getLong();
		file = buf.getUTF8();
		plugin = buf.getUTF8();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putBoolean(this.cached);
		buf.putBoolean(this.url);
		buf.putLong(this.expectedCRC);
		buf.putUTF8(this.file);
		buf.putUTF8(this.plugin);
	}

	@Override
	public void handle(SpoutPlayer player) {
		if (!cached) {
			File file = new File(this.file);
			if (file.exists()) {
				player.sendPacket(new PacketDownloadFile(plugin, file));
			}
		}
	}
}
