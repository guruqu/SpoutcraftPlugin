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
package org.getspout.spoutapi.packet.standard;

public interface MCPacket51MapChunk {
	/**
	 * @return the x world block coordinate
	 */
	public int getX();

	/**
	 * @param x sets the x world block coordinate
	 */
	public void setX(int x);

	/**
	 * @return the y world block coordinate
	 */
	public int getY();

	/**
	 * @param y sets the y world block coordinate
	 */
	public void setY(int y);

	/**
	 * @return the z world block coordinate
	 */
	public int getZ();

	/**
	 * @param z sets the z world block coordinate
	 */
	public void setZ(int z);

	/**
	 * @return (X size of the cuboid) - 1
	 */
	public int getSizeX();

	/**
	 * The cuboid to update must be completely within 1 chunk
	 *
	 * @param x (Z size of cuboid) - 1
	 */
	public void setSizeX(int x);

	/**
	 * @return (Y size of the cuboid) - 1
	 */
	public int getSizeY();

	/**
	 * The cuboid to update must be completely within 1 chunk
	 *
	 * @param y (Y size of cuboid) - 1
	 */
	public void setSizeY(int y);

	/**
	 * @return (Z size of the cuboid) - 1
	 */
	public int getSizeZ();

	/**
	 * The cuboid to update must be completely within 1 chunk
	 *
	 * @param z (Z size of cuboid) - 1
	 */
	public void setSizeZ(int z);

	/**
	 * @return chunk data compressed using Deflate
	 */
	public byte[] getCompressedChunkData();
}
