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
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.Music;
import org.getspout.spoutapi.sound.SoundEffect;

public class PacketPlaySound implements SpoutPacket {
	private short soundId;
	private boolean location = false;
	private int x, y, z;
	private int volume, distance;

	protected PacketPlaySound() {
	}

	public PacketPlaySound(SoundEffect sound, int distance, int volume) {
		soundId = (short) sound.getId();
		this.volume = volume;
		this.distance = distance;
	}

	public PacketPlaySound(SoundEffect sound, Location loc, int distance, int volume) {
		soundId = (short) sound.getId();
		location = true;
		x = loc.getBlockX();
		y = loc.getBlockY();
		z = loc.getBlockZ();
		this.volume = volume;
		this.distance = distance;
	}

	public PacketPlaySound(Music music, int volume) {
		soundId = (short) (music.getId() + (1 + SoundEffect.getMaxId()));
		this.volume = volume;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		soundId = buf.getShort();
		location = buf.getBoolean();
		x = buf.getInt();
		y = buf.getInt();
		z = buf.getInt();
		distance = buf.getInt();
		volume = buf.getInt();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putShort(soundId);
		buf.putBoolean(location);
		if (!location) {
			buf.putInt(-1);
			buf.putInt(-1);
			buf.putInt(-1);
			buf.putInt(-1);
		} else {
			buf.putInt(x);
			buf.putInt(y);
			buf.putInt(z);
			buf.putInt(distance);
		}
		buf.putInt(volume);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
