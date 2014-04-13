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

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;

public class GenericPopup extends GenericScreen implements PopupScreen {
	protected boolean transparent = false;

	protected GenericPopup() {
	}

	@Override
	public int getVersion() {
		return super.getVersion();
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		this.setTransparent(buf.getBoolean());
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putBoolean(isTransparent());
	}

	@Override
	public boolean isTransparent() {
		return transparent;
	}

	@Override
	public PopupScreen setTransparent(boolean value) {
		this.transparent = value;
		return this;
	}

	@Override
	public Widget setScreen(Plugin plugin, Screen screen) {
		if (this.screen != null && screen != null && screen != this.screen) {
			((InGameHUD) this.screen).closePopup();
		}
		return super.setScreen(plugin, screen);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.PopupScreen;
	}

	@Override
	public boolean close() {
		if (getScreen() instanceof InGameScreen) {
			return ((InGameScreen) getScreen()).closePopup();
		}
		return false;
	}

	@Override
	public ScreenType getScreenType() {
		return ScreenType.CUSTOM_SCREEN;
	}

	@Override
	public void handleItemOnCursor(ItemStack itemOnCursor) {
		// Do nothing with the item by default.
	}
}
