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
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TIntArrayList;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketOverrideMultiBlock extends CompressiblePacket {
	private int chunkX;
	private int chunkZ;
	private boolean compressed = false;
	private byte[] data;

	public PacketOverrideMultiBlock(TIntArrayList xCoords, TIntArrayList yCoords, TIntArrayList zCoords, TIntArrayList blockTypeIds, TByteArrayList blockData) {
		short size = (short) xCoords.size();
		ByteBuffer rawData = ByteBuffer.allocate(size * 7);
		chunkX = xCoords.get(0) >> 4;
		chunkZ = zCoords.get(0) >> 4;
		for (int i = 0; i < size; i++) {
			rawData.put((byte) (xCoords.get(i) - chunkX * 16));
			rawData.putShort((short) yCoords.get(i));
			rawData.put((byte) (zCoords.get(i) - chunkZ * 16));
			rawData.putShort((short) blockTypeIds.get(i));
			rawData.put(blockData.get(i));
		}
		data = rawData.array();
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		chunkX = buf.getInt();
		chunkZ = buf.getInt();
		data = new byte[buf.getInt()];
		buf.get(data);
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(chunkX);
		buf.putInt(chunkZ);
		buf.putInt(data.length);
		buf.put(data);
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
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

	@Override
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

	@Override
	public boolean isCompressed() {
		return compressed;
	}
}
