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
import java.net.MalformedURLException;
import java.net.URL;

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketSendLink extends SpoutPacket {
	private URL link;

	protected PacketSendLink() {
		link = null;
	}

	public PacketSendLink(URL link) {
		this.link = link;
	}

	public PacketSendLink(String link) throws MalformedURLException {
		this.link = new URL(link);
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		throw new IOException("The server cannot receive a link from the client!");
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		if (link == null) {
			throw new IOException("Attempt made to serialize " + this + " with a null URL link reference!");
		}
		buf.putUTF8(link.toString());
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public String toString() {
		return "PacketSendLink{ version= " + getVersion() + ", link= " + (link == null ? "null" : link.toString()) + " }";
	}
}
