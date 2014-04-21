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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.getspout.spoutapi.event.screen.ScreenshotReceivedEvent;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketScreenshot extends SpoutPacket {
	byte[] ssAsPng = null;
	boolean isRequest = false;

	public PacketScreenshot() {
		isRequest = true;
	}

	public PacketScreenshot(BufferedImage ss) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(ss, "png", baos);
		baos.flush();
		ssAsPng = baos.toByteArray();
		baos.close();
	}

	public int getNumBytes() {
		if (ssAsPng == null) {
			return 1;
		}
		return ssAsPng.length + 5;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		isRequest = buf.getBoolean();
		if (!isRequest) {
			int ssLen = buf.getInt();
			ssAsPng = new byte[ssLen];
			buf.get(ssAsPng);
		}
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		if (ssAsPng == null) {
			buf.putBoolean(true);
		} else {
			buf.putBoolean(false);
			buf.putInt(ssAsPng.length);
			buf.put(ssAsPng);
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
		if (isRequest) {
			return; // We can't do anything!
		}
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(ssAsPng);
			BufferedImage ss = ImageIO.read(bais);
			ScreenshotReceivedEvent sre = new ScreenshotReceivedEvent(player, ss);
			Bukkit.getServer().getPluginManager().callEvent(sre);
			player.sendNotification("Sending screenshot...", "Screenshot received", Material.PAINTING);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
