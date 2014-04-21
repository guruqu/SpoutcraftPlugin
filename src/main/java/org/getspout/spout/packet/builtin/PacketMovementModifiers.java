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

public class PacketMovementModifiers implements SpoutPacket {
	double gravityMod = 1;
	double walkingMod = 1;
	double swimmingMod = 1;
	double jumpingMod = 1;
	double airspeedMod = 1;

	protected PacketMovementModifiers() {
	}

	public PacketMovementModifiers(double gravity, double walking, double swimming, double jumping, double airspeed) {
		gravityMod = gravity;
		walkingMod = walking;
		swimmingMod = swimming;
		jumpingMod = jumping;
		airspeedMod = airspeed;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		gravityMod = buf.getDouble();
		walkingMod = buf.getDouble();
		swimmingMod = buf.getDouble();
		jumpingMod = buf.getDouble();
		airspeedMod = buf.getDouble();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putDouble(gravityMod);
		buf.putDouble(walkingMod);
		buf.putDouble(swimmingMod);
		buf.putDouble(jumpingMod);
		buf.putDouble(airspeedMod);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
