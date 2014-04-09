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
import org.getspout.spoutapi.event.input.PlayerClipboardEvent;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketClipboardText implements SpoutPacket {
	private String text;

	public PacketClipboardText() {
	}

	public PacketClipboardText(String text) {
		this.text = text;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		text = buf.getUTF8();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(text);
	}

	@Override
	public void handle(SpoutPlayer player) {
		final PlayerClipboardEvent event = new PlayerClipboardEvent(player, text);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			player.setClipboardText(text, false);
		} else {
			player.setClipboardText(event.getNewClipboard(), true);
		}
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
