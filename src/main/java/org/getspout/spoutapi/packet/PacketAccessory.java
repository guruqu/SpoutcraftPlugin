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

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.player.accessories.AccessoryType;

public class PacketAccessory implements SpoutPacket {
	private AccessoryType type;
	private String url, who;
	private boolean add;

	protected PacketAccessory() {
	}

	public PacketAccessory(String who, AccessoryType type, String url) {
		this(who, type, url, true);
	}

	public PacketAccessory(String who, AccessoryType type, String url, boolean add) {
		this.who = who;
		this.type = type;
		this.url = url;
		this.add = add;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		who = buf.getUTF8();
		type = AccessoryType.get(buf.getInt());
		url = buf.getUTF8();
		add = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(who);
		buf.putInt(type.getId());
		buf.putUTF8(url);
		buf.putBoolean(add);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
