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

import org.bukkit.Bukkit;
import org.getspout.spoutapi.event.sound.BackgroundMusicEvent;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.Music;

public class PacketChangeMusic implements SpoutPacket {
	public int id;
	public int volumePercent;
	public boolean cancel = false;

	protected PacketChangeMusic() {
	}

	public PacketChangeMusic(int music, int volumePercent) {
		this.id = music;
		this.volumePercent = volumePercent;
	}

	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		id = buf.getInt();
		volumePercent = buf.getInt();
		cancel = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(id);
		buf.putInt(volumePercent);
		buf.putBoolean(cancel);
	}

	@Override
	public void handle(SpoutPlayer player) {
		Music music = Music.getMusicFromId(id);
		if (music != null) {
			final BackgroundMusicEvent event = new BackgroundMusicEvent(music, volumePercent, player);
			Bukkit.getServer().getPluginManager().callEvent(event);
			cancel = event.isCancelled();
			if (!cancel) {
				volumePercent = event.getVolumePercent();
			}
			player.sendPacket(this);
		}
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
