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

public class GenericPopup extends GenericScreen implements PopupScreen {
	protected boolean transparent = false;

	public GenericPopup() {
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 0;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		this.setTransparent(input.readBoolean());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(isTransparent());
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
