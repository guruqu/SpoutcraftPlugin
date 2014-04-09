package org.getspout.spoutapi.packet;

public interface CompressiblePacket extends SpoutPacket {
	public void compress();

	public void decompress();

	public boolean isCompressed();
}
