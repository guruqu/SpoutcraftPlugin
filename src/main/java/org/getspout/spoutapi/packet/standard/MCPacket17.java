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

public interface MCPacket17 extends MCPacket {
	/**
	 * @return the player's entity id
	 */
	public int getEntityId();

	/**
	 * @param the player's entity id
	 */
	public void setEntityId(int entityId);

	/**
	 * 0: Player entered bed?
	 *
	 * @return the bed field
	 */
	public int getBed();

	/**
	 * @param the bed field
	 */
	public void setBed(int bed);

	public int getBlockX();

	public void setBlockX(int x);

	public int getBlockY();

	public void setBlockY(int y);

	public int getBlockZ();

	public void setBlockZ(int z);
}
