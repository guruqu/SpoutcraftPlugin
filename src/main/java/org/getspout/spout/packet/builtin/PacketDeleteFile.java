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

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketDeleteFile extends SpoutPacket {
	private String plugin;
	private String fileName;

	protected PacketDeleteFile() {
	}

	public PacketDeleteFile(String plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		throw new IOException("The server should not recieve a PacketDeleteFile from the client (hack?)!");
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(fileName);
		buf.putUTF8(plugin);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}
}
