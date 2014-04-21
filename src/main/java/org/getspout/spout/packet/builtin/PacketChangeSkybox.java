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

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketChangeSkybox extends SpoutPacket {
	private String sun = "";
	private String moon = "";
	private int cloudY = 0, stars = 0, sunPercent = 0, moonPercent = 0;
	private Color skyColor = Color.ignore(), fogColor = Color.ignore(), cloudColor = Color.ignore();

	protected PacketChangeSkybox() {
	}

	public PacketChangeSkybox(int cloudY, int stars, int sunPercent, int moonPercent) {
		this.cloudY = cloudY;
		this.stars = stars;
		this.sunPercent = sunPercent;
		this.moonPercent = moonPercent;
	}

	public PacketChangeSkybox(String sunUrl, String moonUrl) {
		this.cloudY = 0;
		this.stars = 0;
		this.sunPercent = 0;
		this.moonPercent = 0;
		this.sun = sunUrl;
		this.moon = moonUrl;
	}

	public PacketChangeSkybox(Color sky, Color fog, Color cloud) {
		if (sky != null) {
			skyColor = sky.clone();
		}
		if (fog != null) {
			fogColor = fog.clone();
		}
		if (cloud != null) {
			cloudColor = cloud.clone();
		}
	}

	public PacketChangeSkybox(int cloudY, int stars, int sunPercent, int moonPercent, Color sky, Color fog, Color cloud, String sunUrl, String moonUrl) {
		this.cloudY = cloudY;
		this.stars = stars;
		this.sunPercent = sunPercent;
		this.moonPercent = moonPercent;
		this.sun = sunUrl;
		this.moon = moonUrl;
		if (sky != null) {
			skyColor = sky.clone();
		}
		if (fog != null) {
			fogColor = fog.clone();
		}
		if (cloud != null) {
			cloudColor = cloud.clone();
		}
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		cloudY = buf.getInt();
		stars = buf.getInt();
		sunPercent = buf.getInt();
		moonPercent = buf.getInt();
		sun = buf.getUTF8();
		moon = buf.getUTF8();
		skyColor = buf.getColor();
		fogColor = buf.getColor();
		cloudColor = buf.getColor();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(cloudY);
		buf.putInt(stars);
		buf.putInt(sunPercent);
		buf.putInt(moonPercent);
		buf.putUTF8(sun);
		buf.putUTF8(moon);
		buf.putColor(skyColor);
		buf.putColor(fogColor);
		buf.putColor(cloudColor);
	}

	@Override
	public void handle(SpoutPlayer player) {
		// TODO: Fire cancellable event for the server to control this?
	}
}
