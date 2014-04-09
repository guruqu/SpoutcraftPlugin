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
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.material.Block;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketBlockData implements CompressiblePacket {
	private byte[] data;
	private boolean compressed = false;

	protected PacketBlockData() {
	}

	public PacketBlockData(Set<Block> modifiedData) {
		if (modifiedData.size() > 0) {
			ByteBuffer rawData = ByteBuffer.allocate(modifiedData.size() * (15));

			for (Block next : modifiedData) {
				rawData.put((byte) next.getRawId());
				rawData.put((byte) next.getRawData());
				rawData.putFloat(next.getHardness());
				rawData.putInt(next.getLightLevel());
				rawData.putFloat(next.getFriction());
				rawData.put((byte) (next.isOpaque() ? 1 : 0));
			}

			data = rawData.array();
		}
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		int size = buf.getInt();
		compressed = buf.getBoolean();
		if (size > 0) {
			data = new byte[size];
			buf.get(data);
		}
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(data == null ? 0 : data.length);
		buf.putBoolean(compressed);
		if (data != null) {
			buf.put(data);
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}

	public void compress() {
		if (!compressed) {
			if (data != null) {
				Deflater deflater = new Deflater();
				deflater.setInput(data);
				deflater.setLevel(Deflater.BEST_COMPRESSION);
				deflater.finish();
				ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
				byte[] buffer = new byte[1024];
				while (!deflater.finished()) {
					int bytesCompressed = deflater.deflate(buffer);
					bos.write(buffer, 0, bytesCompressed);
				}
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				data = bos.toByteArray();
			}
			compressed = true;
		}
	}

	public void decompress() {
		if (compressed) {
			Inflater decompressor = new Inflater();
			decompressor.setInput(data);

			ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

			byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				try {
					int count = decompressor.inflate(buf);
					bos.write(buf, 0, count);
				} catch (DataFormatException ignored) {
				}
			}
			try {
				bos.close();
			} catch (IOException ignored) {
			}

			data = bos.toByteArray();
		}
	}

	public boolean isCompressed() {
		return compressed || (data == null || data.length < 256);
	}
}
