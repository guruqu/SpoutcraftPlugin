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

public class PacketWaypoint implements SpoutPacket {
	private double x, y, z;
	private String name;
	private boolean death = false;

	protected PacketWaypoint() {
	}

	public PacketWaypoint(double x, double y, double z, String name) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
	}

	public PacketWaypoint(double x, double y, double z, String name, boolean death) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
		this.death = death;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		x = buf.getDouble();
		y = buf.getDouble();
		z = buf.getDouble();
		name = buf.getUTF8();
		death = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putDouble(x);
		buf.putDouble(y);
		buf.putDouble(z);
		buf.putUTF8(name);
		buf.putBoolean(death);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
