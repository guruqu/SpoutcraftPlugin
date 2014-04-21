package org.getspout.spout.packet.builtin;

import java.io.IOException;

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.material.block.GenericCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketCustomBlock extends SpoutPacket {
	private GenericCustomBlock block;

	protected PacketCustomBlock() {
	}

	public PacketCustomBlock(GenericCustomBlock block) {
		this.block = block;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		throw new IOException("The server should not receive a custom block (hack?)!");
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(block.getCustomId());
		buf.putUTF8(block.getName());
		buf.putUTF8(block.getPlugin().getName());
		buf.putBoolean(block.isOpaque());
		buf.putFloat(block.getFriction());
		buf.putFloat(block.getHardness());
		buf.putInt(block.getLightLevel());
	}

	@Override
	public void handle(SpoutPlayer player) {
	}
}
