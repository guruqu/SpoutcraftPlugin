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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.server.v1_6_R3.Connection;
import net.minecraft.server.v1_6_R3.Packet;
import org.getspout.spout.Spout;
import org.getspout.spout.SpoutPlayerConnection;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spout.packet.builtin.SpoutPacket;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CustomPacket extends Packet {
	private SpoutPacket packet;

	protected CustomPacket() {
	}

	public CustomPacket(SpoutPacket packet) {
		this.packet = packet;
	}

	/**
	 * Decodes a {@link net.minecraft.server.v1_6_R3.Packet} from the Minecraft server as a {@link org.getspout.spout.packet.builtin.SpoutPacket}.
	 * @param input Holds the data of this decode
	 * @throws IOException If the decode fails for any reason
	 */
	@Override
	public void a(DataInput input) throws IOException {
		packet = Spout.getInstance().getPipeline().decode(input);
	}

	@Override
	public void a(DataOutput output) throws IOException {
		if (packet == null) {
			throw new IOException("Cannot send a CustomPacket with a null SpoutPacket instance!");
		}
		Spout.getInstance().getPipeline().encode(packet, output);
	}

	@Override
	public void handle(Connection connection) {
		if (connection instanceof SpoutPlayerConnection) {
			SpoutPlayer player = SpoutManager.getPlayerFromId(((SpoutPlayerConnection) connection).getPlayer().getEntityId());
			if (player != null) {
				packet.handle(player);
			}
		}
	}

	@Override
	public int a() {
		return 8;
	}

	public static void addClassMapping() {
		try {
			Class<?>[] params = {int.class, boolean.class, boolean.class, Class.class};
			Method addClassMapping = Packet.class.getDeclaredMethod("a", params);
			addClassMapping.setAccessible(true);
			addClassMapping.invoke(null, 195, true, true, CustomPacket.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings ("rawtypes")
	public static void removeClassMapping() {
		try {
			Packet.l.d(195);
			Field field = Packet.class.getDeclaredField("a");
			field.setAccessible(true);
			Map temp = (Map) field.get(null);
			temp.remove(CustomPacket.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
