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
import java.util.UUID;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.keyboard.KeyBinding;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketKeyBinding implements SpoutPacket {
	KeyBinding binding;
	Keyboard key;
	String id;
	boolean pressed;
	UUID uniqueId;

	protected PacketKeyBinding() {
	}

	public PacketKeyBinding(KeyBinding binding) {
		this.binding = binding;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		key = Keyboard.getKey(buf.getInt());
		pressed = buf.getBoolean();
		uniqueId = new UUID(buf.getLong(), buf.getLong());
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(binding.getId());
		buf.putUTF8(binding.getDescription());
		buf.putUTF8(binding.getPlugin().getDescription().getName());
		buf.putInt(binding.getDefaultKey().getKeyCode());
		buf.putLong(binding.getUniqueId().getMostSignificantBits());
		buf.putLong(binding.getUniqueId().getLeastSignificantBits());
	}

	@Override
	public void handle(SpoutPlayer player) {
		SpoutManager.getKeyBindingManager().summonKey(uniqueId, SpoutManager.getPlayerFromId(player.getEntityId()), key, pressed);
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
