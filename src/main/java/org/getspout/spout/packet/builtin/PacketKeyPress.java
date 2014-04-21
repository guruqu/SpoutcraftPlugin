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

import org.bukkit.Bukkit;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketKeyPress implements SpoutPacket {
	public boolean pressDown;
	public int key;
	public byte settingKeys[] = new byte[10];
	public int screenType = -1;

	protected PacketKeyPress() {
	}

	public PacketKeyPress(int key, boolean pressDown) {
		this.key = key;
		this.pressDown = pressDown;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		key = buf.getInt();
		pressDown = buf.getBoolean();
		screenType = buf.getInt();
		for (int i = 0; i < 10; i++) {
			settingKeys[i] = buf.get();
		}
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(key);
		buf.putBoolean(pressDown);
		buf.putInt(screenType);
		for (int i = 0; i < 10; i++) {
			buf.put(settingKeys[i]);
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
		if (player != null) {
			player.updateKeys(settingKeys);
			if (pressDown) {
				Bukkit.getServer().getPluginManager().callEvent(new KeyPressedEvent(key, player, ScreenType.getType(screenType)));
			} else {
				Bukkit.getServer().getPluginManager().callEvent(new KeyReleasedEvent(key, player, ScreenType.getType(screenType)));
			}
		}
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
