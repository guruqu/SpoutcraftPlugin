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

public class PacketOverrideBlock implements SpoutPacket {
	private int x, y, z;
	private short blockId;
	private byte data;

	protected PacketOverrideBlock() {
	}

	public PacketOverrideBlock(int x, int y, int z, short blockId, byte data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockId = blockId;
		this.data = data;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		//TODO: Should the server receive this?
		x = buf.getInt();
		y = buf.getInt();
		z = buf.getInt();
		blockId = buf.getShort();
		data = buf.get();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(x);
		buf.putInt(y);
		buf.putInt(z);
		buf.putShort(blockId);
		buf.put(data);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
