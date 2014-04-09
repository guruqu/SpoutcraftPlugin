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

import net.minecraft.server.v1_6_R3.EntityPlayer;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;

public class PacketShowAchievement implements SpoutPacket {
	private String message;
	private String title;
	private int itemId;

	public PacketShowAchievement() {
	}

	public PacketShowAchievement(String title, String message, int itemId) {
		this.title = title;
		this.message = message;
		this.itemId = itemId;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		title = buf.getUTF8();
		message = buf.getUTF8();
		itemId = buf.getInt();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(title);
		buf.putUTF8(message);
		buf.putInt(itemId);
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void handle(EntityPlayer player) {
	}

	@Override
	public void failure(EntityPlayer player) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketShowAchievement;
	}
}
