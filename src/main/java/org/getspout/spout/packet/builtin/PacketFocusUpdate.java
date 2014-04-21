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
package org.getspout.spout.packet.builtin;

import java.io.IOException;
import java.util.UUID;

import org.getspout.spoutapi.gui.Control;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketFocusUpdate extends SpoutPacket {
	private Control control;
	private boolean focus;
	private UUID widgetId;

	protected PacketFocusUpdate() {
	}

	public PacketFocusUpdate(Control control, boolean focus) {
		this.control = control;
		this.focus = focus;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		widgetId = buf.getUUID();
		focus = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUUID(control.getId());
		buf.putBoolean(focus);
	}

	@Override
	public void handle(SpoutPlayer player) {
		PopupScreen popup = player.getMainScreen().getActivePopup();
		if (popup != null) {
			Widget w = popup.getWidget(widgetId);
			if (w != null && w instanceof Control) {
				((Control) w).setFocus(focus);
			}
		}
	}
}
