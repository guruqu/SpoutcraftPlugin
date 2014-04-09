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
package org.getspout.spoutapi.player;

import gnu.trove.map.hash.TByteObjectHashMap;

public enum RenderDistance {
	RESET((byte) -2),
	FAR((byte) 0),
	NORMAL((byte) 1),
	SHORT((byte) 2),
	TINY((byte) 3);
	private final byte value;
	private static final TByteObjectHashMap<RenderDistance> renderDistancesByID = new TByteObjectHashMap<RenderDistance>();

	static {
		for (RenderDistance rd : values()) {
			renderDistancesByID.put(rd.getValue(), rd);
		}
	}

	public static RenderDistance get(byte value) {
		return renderDistancesByID.get(value);
	}

	RenderDistance(byte value) {
		this.value = value;
	}

	public final byte getValue() {
		return value;
	}
}
