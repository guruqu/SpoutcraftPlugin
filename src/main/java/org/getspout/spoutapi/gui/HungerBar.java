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
import java.util.UUID;

import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;

public class HungerBar extends GenericWidget {
	private int icons = 10;
	private int iconOffset = 8;

	protected HungerBar() {
		super();
		setX(427 / 2 + 82); // 295
		setY(201);
		setWidth(getWidth()); // Don't know the default - ignored, but prevents warnings...
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		setNumOfIcons(buf.getInt());
		setIconOffset(buf.getInt());
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putInt(getNumOfIcons());
		buf.putInt(getIconOffset());
	}

	/**
	 * Gets the maximum number of food icons to display on the HUD. <p/> Hunger bar is scaled to fit the number of icons appropriately.
	 *
	 * @return icons displayed
	 */
	public int getNumOfIcons() {
		return icons;
	}

	/**
	 * Sets the maximum number of food icons to display on the HUD. <p/> Hunger bar is scaled to fit the number of icons appropriately.
	 *
	 * @param icons to display
	 * @return this
	 */
	public HungerBar setNumOfIcons(int icons) {
		this.icons = icons;
		return this;
	}

	/**
	 * Gets the number of pixels each icon is offset when drawing the next icon.
	 *
	 * @return pixel offset
	 */
	public int getIconOffset() {
		return iconOffset;
	}

	/**
	 * Sets the number of pixels each icon is offset when drawing the next icon.
	 *
	 * @param iconOffset when drawing icons
	 * @return this
	 */
	public HungerBar setIconOffset(int iconOffset) {
		this.iconOffset = iconOffset;
		return this;
	}

	public WidgetType getType() {
		return WidgetType.HungerBar;
	}

	public UUID getId() {
		return new UUID(0, 5);
	}

	@Override
	public int getVersion() {
		return super.getVersion();
	}
}
