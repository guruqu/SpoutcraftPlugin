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

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketFullVersion implements SpoutPacket {
	private String versionString;

	protected PacketFullVersion() {
	}

	public PacketFullVersion(String versionString) {
		this.versionString = versionString;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		versionString = buf.getUTF8();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(versionString);
	}

	@Override
	public void handle(SpoutPlayer player) {
		SpoutManager.getPlayerChunkMap().setVersionString(player.getEntityId(), versionString);
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
