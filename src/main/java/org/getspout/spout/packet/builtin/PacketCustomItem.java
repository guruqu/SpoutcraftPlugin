package org.getspout.spout.packet.builtin;

import java.io.IOException;

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.material.Block;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.item.GenericCustomFood;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.material.item.GenericCustomTool;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketCustomItem extends SpoutPacket {
	private GenericCustomItem item;

	protected PacketCustomItem() {
	}

	public PacketCustomItem(GenericCustomItem item) {
		this.item = item;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		throw new IOException("The server should not receive custom items from the client (hack?)!");
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		if (item == null) {
			throw new IOException("Attempt made to send a null custom item to the client!");
		}
		buf.putInt(item.getCustomId());
		buf.putUTF8(item.getName());
		buf.putUTF8(item.getPlugin().getName());
		buf.putUTF8(item.getTexture());

		if (item instanceof GenericCustomFood) {
			buf.putInt(((GenericCustomFood) item).getHungerRestored());
		} else if (item instanceof GenericCustomTool) {
			final GenericCustomTool tool = (GenericCustomTool) item;
			buf.putShort(tool.getMaxDurability());
			Block[] mod = tool.getStrengthModifiedBlocks();
			buf.putShort((short) mod.length);
			for (Block block : mod) {
				if (block instanceof CustomBlock) {
					buf.putInt(((CustomBlock) block).getCustomId());
					buf.putShort((short) -1);
				} else {
					buf.putInt(block.getRawId());
					buf.putShort((short) block.getRawData());
				}
				buf.putFloat(tool.getStrengthModifier(block));
			}
		}
	}

	@Override
	public void handle(SpoutPlayer player) {
	}
}
