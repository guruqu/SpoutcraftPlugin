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

public class GenericGradient extends GenericWidget implements Gradient {
	protected Color color1 = new Color(0, 0, 0, 0), color2 = new Color(0, 0, 0, 0);
	protected Orientation axis = Orientation.VERTICAL;

	protected GenericGradient() {
	}

	public GenericGradient(Color both) {
		this.color1 = this.color2 = both;
	}

	public GenericGradient(Color top, Color bottom) {
		this.color1 = top;
		this.color2 = bottom;
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 2;
	}

	@Override
	public Gradient setTopColor(Color color) {
		if (color != null && !getTopColor().equals(color)) {
			this.color1 = color;
			autoDirty();
		}
		return this;
	}

	@Override
	public Gradient setBottomColor(Color color) {
		if (color != null && !getBottomColor().equals(color)) {
			this.color2 = color;
			autoDirty();
		}
		return this;
	}

	@Override
	public Gradient setColor(Color color) {
		setTopColor(color);
		setBottomColor(color);
		return this;
	}

	@Override
	public Color getTopColor() {
		return this.color1;
	}

	@Override
	public Color getBottomColor() {
		return this.color2;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.Gradient;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		this.setTopColor(buf.getColor());
		this.setBottomColor(buf.getColor());
		this.setOrientation(Orientation.getOrientationFromId(buf.get()));
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putColor(getTopColor());
		buf.putColor(getBottomColor());
		buf.put((byte) getOrientation().getId());
	}

	@Override
	public Gradient copy() {
		return ((Gradient) super.copy()).setTopColor(getTopColor()).setBottomColor(getBottomColor());
	}

	@Override
	public Gradient setOrientation(Orientation axis) {
		if (getOrientation() != axis) {
			this.axis = axis;
			autoDirty();
		}
		return this;
	}

	@Override
	public Orientation getOrientation() {
		return axis;
	}
}
