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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketEntityInformation implements CompressiblePacket {
	private boolean compressed = false;
	private byte[] data = null;

	protected PacketEntityInformation() {
	}

	public PacketEntityInformation(List<LivingEntity> entities) {
		ByteBuffer tempbuffer = ByteBuffer.allocate(entities.size() * 20); //4 bytes for entity id, 16 for uuid
		for (Entity e : entities) {
			tempbuffer.putLong(e.getUniqueId().getLeastSignificantBits());
			tempbuffer.putLong(e.getUniqueId().getMostSignificantBits());
			tempbuffer.putInt(e.getEntityId());
		}
		data = tempbuffer.array();
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		int size = buf.getInt();
		if (size > 0) {
			data = new byte[size];
			buf.get(data);
		}
		compressed = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		if (data != null) {
			buf.putInt(data.length);
			buf.put(data);
		} else {
			buf.putInt(0);
		}
		buf.putBoolean(compressed);
	}

	@Override
	public void handle(SpoutPlayer player) {
		if (player != null) {
			ByteBuffer rawData = ByteBuffer.allocate(data.length);
			rawData.put(data);
			ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>(data.length / 4 + 1);
			for (int i = 0; i < data.length / 4; i++) {
				int index = i * 4;
				int id = rawData.getInt(index);
				Entity entity = SpoutManager.getEntityFromId(id);
				if (entity != null && entity instanceof LivingEntity) {
					entities.add((LivingEntity) entity);
				}
			}
			if (entities.size() > 0) {
				player.sendPacket(new PacketEntityInformation(entities));
				player.updateEntitySkins(entities);
			}
		}
	}

	@Override
	public int getVersion() {
		return 0;
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
				} catch (DataFormatException e) {
				}
			}
			try {
				bos.close();
			} catch (IOException e) {
			}

			data = bos.toByteArray();
		}
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}
}
