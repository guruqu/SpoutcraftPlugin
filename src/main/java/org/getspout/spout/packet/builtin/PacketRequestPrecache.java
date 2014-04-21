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

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.getspout.spout.precache.PrecacheManager;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketRequestPrecache extends SpoutPacket {
	private String plugin;

	protected PacketRequestPrecache() {
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		plugin = buf.getUTF8();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(plugin);
	}

	@Override
	public void handle(SpoutPlayer player) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(this.plugin);

		File preCache = PrecacheManager.getPluginCacheZip(plugin);
		if (preCache.exists()) {
			if (player != null) {
				player.sendPacket(new PacketSendPrecache(plugin, preCache));
			}
		}
	}
}
