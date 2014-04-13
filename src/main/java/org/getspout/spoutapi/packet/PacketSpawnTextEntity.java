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
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketSpawnTextEntity implements SpoutPacket {
	private String text;
	private Location location;
	private int duration;
	private Vector movement;
	private float scale;

	protected PacketSpawnTextEntity() {
	}

	public PacketSpawnTextEntity(String text, Location location, float scale, int duration, Vector movement) {
		this.text = text;
		this.location = location;
		this.duration = duration;
		this.movement = movement;
		this.scale = scale;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		text = buf.getUTF8();
		double x, y, z;
		x = buf.getDouble();
		y = buf.getDouble();
		z = buf.getDouble();
		location = new Location(null, x, y, z);
		scale = buf.getFloat();
		duration = buf.getInt();
		x = buf.getDouble();
		y = buf.getDouble();
		z = buf.getDouble();
		movement = new Vector(x, y, z);
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(text);
		buf.putDouble(location.getX());
		buf.putDouble(location.getY());
		buf.putDouble(location.getZ());
		buf.putFloat(scale);
		buf.putInt(duration);
		buf.putDouble(movement.getX());
		buf.putDouble(movement.getY());
		buf.putDouble(movement.getZ());
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
