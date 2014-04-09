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

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PacketSpawnTextEntity implements SpoutPacket {
	private String text;
	private Location location;
	private int duration;
	private Vector movement;
	private float scale;

	public PacketSpawnTextEntity(String text, Location location, float scale, int duration, Vector movement) {
		this.text = text;
		this.location = location;
		this.duration = duration;
		this.movement = movement;
		this.scale = scale;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		text = input.readString();
		double x, y, z;
		x = input.readDouble();
		y = input.readDouble();
		z = input.readDouble();
		location = new Location(null, x, y, z);
		scale = input.readFloat();
		duration = input.readInt();
		x = input.readDouble();
		y = input.readDouble();
		z = input.readDouble();
		movement = new Vector(x, y, z);
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(text);
		output.writeDouble(location.getX());
		output.writeDouble(location.getY());
		output.writeDouble(location.getZ());
		output.writeFloat(scale);
		output.writeInt(duration);
		output.writeDouble(movement.getX());
		output.writeDouble(movement.getY());
		output.writeDouble(movement.getZ());
	}

	@Override
	public void run(int playerId) {
		// Nothing to do here
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketSpawnTextEntity;
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
