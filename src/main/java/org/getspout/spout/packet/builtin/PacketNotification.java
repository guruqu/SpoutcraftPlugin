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

public class PacketNotification extends PacketShowAchievement {
	private int time;
	private short data;

	protected PacketNotification() {
	}

	public PacketNotification(String title, String message, int itemId, short data, int time) {
		super(title, message, itemId);
		this.time = time;
		this.data = data;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		this.data = buf.getShort();
		this.time = buf.getInt();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putShort(data);
		buf.putInt(time);
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
