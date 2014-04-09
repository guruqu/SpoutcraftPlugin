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
import java.nio.ByteBuffer;

import org.getspout.spoutapi.io.SpoutInputStream;
import org.getspout.spoutapi.io.SpoutOutputStream;
import org.getspout.spoutapi.player.accessories.AccessoryType;

public class PacketAccessory implements SpoutPacket{
	private AccessoryType type;
	private String url, who;
	private boolean add;

	public PacketAccessory() {
	}

	public PacketAccessory(String who,AccessoryType type, String url) {
		this(who, type, url, true);
	}

	public PacketAccessory(String who,AccessoryType type, String url, boolean add) {
		this.who = who;
		this.type = type;
		this.url = url;
		this.add = add;
	}

	@Override
	public void decode(ByteBuffer buf) throws IOException {
		who = buf.();
		type = AccessoryType.get(buf.getInt());
		url = buf.get();
		add = buf.get();
	}

	@Override
	public void encode(ByteBuffer buf) throws IOException {
		output.writeString(who);
		output.writeInt(type.getId());
		output.writeString(url);
		output.writeBoolean(add);
	}

	@Override
	public void run(int playerId) {
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketAccessory;
	}

	@Override
	public int getVersion() {
		return 2;
	}
}
