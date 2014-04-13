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
package org.getspout.spoutapi.gui;

import java.io.IOException;

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;

public class GenericTexture extends GenericWidget implements Texture {
	protected String url = null;
	protected boolean drawAlpha = false;
	protected int top = -1;
	protected int left = -1;

	protected GenericTexture() {
	}

	public GenericTexture(String url) {
		this.url = url;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.Texture;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		setUrl(buf.getUTF8());
		setDrawAlphaChannel(buf.getBoolean());
		setTop(buf.getShort());
		setLeft(buf.getShort());
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putUTF8(getUrl());
		buf.putBoolean(isDrawingAlphaChannel());
		buf.putShort((short) top);
		buf.putShort((short) left);
	}

	@Override
	public int getVersion() {
		return super.getVersion();
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Texture setUrl(String Url) {
		if ((getUrl() != null && !getUrl().equals(Url)) || (getUrl() == null && Url != null)) {
			this.url = Url;
			autoDirty();
		}
		return this;
	}

	@Override
	public Texture copy() {
		return ((Texture) super.copy()).setUrl(getUrl()).setDrawAlphaChannel(isDrawingAlphaChannel());
	}

	@Override
	public boolean isDrawingAlphaChannel() {
		return drawAlpha;
	}

	@Override
	public Texture setDrawAlphaChannel(boolean draw) {
		if (isDrawingAlphaChannel() != draw) {
			drawAlpha = draw;
			autoDirty();
		}
		return this;
	}

	@Override
	public Texture setTop(int top) {
		if (getTop() != top) {
			this.top = top;
			autoDirty();
		}
		return this;
	}

	@Override
	public int getTop() {
		return top;
	}

	@Override
	public Texture setLeft(int left) {
		if (getLeft() != left) {
			this.left = left;
			autoDirty();
		}
		return this;
	}

	@Override
	public int getLeft() {
		return left;
	}
}
