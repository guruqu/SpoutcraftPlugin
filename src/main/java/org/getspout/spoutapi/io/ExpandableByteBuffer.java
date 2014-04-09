package org.getspout.spoutapi.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ExpandableByteBuffer {
	private static final int INITIAL_SIZE = 256;
	private ByteBuffer buf;

	public ExpandableByteBuffer() {
		buf = ByteBuffer.allocate(INITIAL_SIZE);
	}

	public ExpandableByteBuffer(ByteBuffer buf) {
		this.buf = buf;
	}

	public ExpandableByteBuffer(int initialSize) {
		buf = ByteBuffer.allocate(initialSize);
	}

	public ExpandableByteBuffer(byte[] data) {
		this(data, 0, data.length);
	}

	public ExpandableByteBuffer(byte[] data, int offset, int length) {
		buf = ByteBuffer.wrap(data, offset, length);
	}

	public void put(byte b) {
		expandIfNeeded(1);
		buf.put(b);
	}

	public byte get() {
		return buf.get();
	}

	public void put(byte[] bytes) {
		expandIfNeeded(bytes.length);
		buf.put(bytes);
	}

	public ByteBuffer get(byte[] dst) {
		return buf.get(dst);
	}

	public void put(byte[] bytes, int offset, int length) {
		expandIfNeeded(length - offset);
		buf.put(bytes, offset, length);
	}

	public ByteBuffer get(byte[] dst, int offset, int length) {
		return buf.get(dst, offset, length);
	}

	public void put(ByteBuffer from) {
		expandIfNeeded(from.capacity() - from.remaining());
		buf.put(from);
	}

	public void putChar(char c) {
		expandIfNeeded(2);
		buf.putChar(c);
	}

	public char getChar() {
		return buf.getChar();
	}

	public void putShort(short s) {
		expandIfNeeded(2);
		buf.putShort(s);
	}

	public short getShort() {
		return buf.getShort();
	}

	public void putInt(int i) {
		expandIfNeeded(4);
		buf.putInt(i);
	}

	public int getInt() {
		return buf.getInt();
	}

	public void putFloat(float f) {
		expandIfNeeded(4);
		buf.putFloat(f);
	}

	public float getFloat() {
		return buf.getFloat();
	}

	public void putDouble(double d) {
		expandIfNeeded(8);
		buf.putDouble(d);
	}

	public double getDouble() {
		return buf.getDouble();
	}

	public void putLong(long l) {
		expandIfNeeded(8);
		buf.putLong(l);
	}

	public long getLong() {
		return buf.getLong();
	}

	public void putUTF8(String s) throws IOException {
		final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
		if (bytes.length >= Short.MAX_VALUE) {
			throw new IOException("Attempt to write a string with a length greater than Short.MAX_VALUE to ByteBuffer!");
		}
		expandIfNeeded(2 + bytes.length);
		buf.putShort((short) bytes.length);
		buf.put(bytes);
	}

	public String getUTF8() throws IOException {
		final short len = buf.getShort();
		final byte[] bytes = new byte[len];
		buf.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public void putUUID(UUID uuid) {
		putLong(uuid.getMostSignificantBits());
		putLong(uuid.getLeastSignificantBits());
	}

	public UUID getUUID() {
		return new UUID(getLong(), getLong());
	}

	public ByteBuffer asReadOnlyBuffer() {
		return buf.asReadOnlyBuffer();
	}

	private void expandIfNeeded(int amount) {
		if (buf.remaining() > amount) {
			return;
		}
		final ByteBuffer temp = ByteBuffer.allocate(buf.capacity() + ++amount);
		temp.put(buf);
		temp.position(buf.position());
		buf = temp;
	}
}
