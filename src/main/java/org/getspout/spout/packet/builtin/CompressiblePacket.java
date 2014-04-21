package org.getspout.spout.packet.builtin;

public abstract class CompressiblePacket extends SpoutPacket {
	public abstract void compress();

	public abstract void decompress();

	public abstract boolean isCompressed();
}
