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

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketServerPlugins implements SpoutPacket {
	private String[] plugins;
	private String[] versions;

	protected PacketServerPlugins() {
	}

	public PacketServerPlugins(Plugin[] plugins) {
		this.plugins = new String[plugins.length];
		this.versions = new String[plugins.length];
		for (int i = 0; i < plugins.length; i++) {
			this.plugins[i] = plugins[i].getDescription().getName();
			this.versions[i] = plugins[i].getDescription().getVersion();
		}
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		final int size = buf.getShort();
		plugins = new String[size];
		versions = new String[size];
		for (int i = 0; i < size; i++) {
			plugins[i] = buf.getUTF8();
			versions[i] = buf.getUTF8();
		}
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putShort((short) plugins.length);
		for (int i = 0; i < plugins.length; i++) {
			buf.putUTF8(plugins[i]);
			buf.putUTF8(versions[i]);
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
