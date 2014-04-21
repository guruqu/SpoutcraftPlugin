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

import org.getspout.spoutapi.block.design.BlockDesign;
import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketBlockModel extends SpoutPacket {
	private short customId;
	private byte data;
	private BlockDesign design;

	protected PacketBlockModel() {
	}

	public PacketBlockModel(short customId, BlockDesign design, byte data) {
		this.design = design;
		this.customId = customId;
		this.data = data;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		customId = buf.getShort();
		data = buf.get();
		design = new GenericBlockDesign();
		design.decode(buf);
		if (design.getReset()) {
			design = null;
		}
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putShort(customId);
		buf.put(data);
		if (design != null) {
			design.encode(buf);
		} else {
			buf.putUTF8(GenericBlockDesign.RESET_STRING);
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
	}
}
