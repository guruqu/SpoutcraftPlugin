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

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.particle.Particle;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketParticle implements SpoutPacket {
	Particle particle;

	protected PacketParticle() {
	}

	public PacketParticle(Particle particle) {
		this.particle = particle;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUTF8(particle.getName());
		buf.putLocation(particle.getLocation());
		buf.putVector(particle.getMotion());
		buf.putFloat(particle.getScale());
		buf.putFloat(particle.getGravity());
		buf.putFloat(particle.getParticleRed());
		buf.putFloat(particle.getParticleBlue());
		buf.putFloat(particle.getParticleGreen());
		buf.putInt(particle.getMaxAge());
		buf.putInt(particle.getAmount());
	}

	@Override
	public void handle(SpoutPlayer player) {
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
