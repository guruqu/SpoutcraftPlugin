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
package org.getspout.spout.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import net.minecraft.server.v1_6_R3.Connection;
import org.getspout.spout.Spout;
import org.getspout.spout.SpoutPlayerConnection;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spout.packet.builtin.PacketAccessory;
import org.getspout.spout.packet.builtin.SpoutPacket;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CustomPacketPipeline {
	private final Spout game;
	private final LinkedList<Class<? extends SpoutPacket>> packets = new LinkedList<Class<? extends SpoutPacket>>();

	public CustomPacketPipeline(Spout game) {
		this.game = game;
	}

	public void register(Class<? extends SpoutPacket> clazz) {
		if (packets.size() > Short.MAX_VALUE) {
			game.getLogger().info("A maximum of Short.MAX_VALUE messages can only be registered at one time!");
			return;
		}
		if (packets.contains(clazz)) {
			game.getLogger().severe("Attempt made to register " + clazz + " twice!");
			return;
		}
		packets.add(clazz);
	}

	protected SpoutPacket decode(DataInput input) throws IOException {
		final int packetid = input.readInt();
		final Class<? extends SpoutPacket> clazz = packets.get(packetid);
		if (clazz == null) {
			throw new IOException("Unknown custom packet with SpoutPacket id [" + packetid + "]!");
		}
		final short version = input.readShort();
		final int length = input.readInt();
		try {
			final SpoutPacket packet = clazz.newInstance();
			if (packet.getVersion() > version) {
				throw new IOException("Client sent packet that is a newer version than what is on the server (hack?)!");
			} else if (packet.getVersion() < version) {
				throw new IOException("Client sent packet that is an older version than what is on the server (hack?)!");
			}
			final byte[] data = new byte[length];
			input.readFully(data);

			packet.decode(new MinecraftExpandableByteBuffer(data));
			return packet;
		} catch (IllegalAccessException | InstantiationException ex) {
			throw new IOException("Failed to create SpoutPacket instance for packet id [" + packetid + "] with a length of [" + length + "] and version [" + version + "]!");
		}
	}

	protected void encode(SpoutPacket packet, DataOutput output) throws IOException {
		final int packetid = getId(packet.getClass());
		if (packetid == -1) {
			throw new IOException("Attempt to send custom packet with SpoutPacket [" + packet.getClass().getSimpleName() + "] that is not registered!");
		}
		output.writeInt(packetid);
		output.writeShort(packet.getVersion());
		final MinecraftExpandableByteBuffer buf = new MinecraftExpandableByteBuffer();
		packet.encode(buf);
		buf.flip();
		output.writeInt(buf.remaining());
		output.write(buf.array());
	}

	protected void handle(SpoutPacket packet, Connection connection) {
		if (connection instanceof SpoutPlayerConnection) {
			final SpoutPlayer player = SpoutManager.getPlayerFromId(((SpoutPlayerConnection) connection).getPlayer().getEntityId());
			if (player != null) {
				packet.handle(player);
			}
		}
	}

	public void onEnable() {
		registerSpoutPackets();
		Collections.sort(packets, new Comparator<Class<? extends SpoutPacket>>() {
			@Override
			public int compare(Class<? extends SpoutPacket> clazz1, Class<? extends SpoutPacket> clazz2) {
				int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
				if (com == 0) {
					com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
				}

				return com;
			}
		});
	}

	private int getId(Class<? extends SpoutPacket> clazz) {
		for (int i = 0; i < packets.size(); i++) {
			if (packets.get(i) == clazz) {
				return i;
			}
		}
		return -1;
	}

	private void registerSpoutPackets() {
		register(PacketAccessory.class);
	}
}
