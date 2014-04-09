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
package org.getspout.spoutapi.packet;

import java.io.IOException;
import java.util.UUID;

import org.getspout.spoutapi.gui.GenericComboBox;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketComboBox implements SpoutPacket {
	private GenericComboBox box;
	private UUID uuid;
	private boolean open;
	private int selection;

	public PacketComboBox() {
	}

	public PacketComboBox(GenericComboBox box) {
		this.box = box;
		this.uuid = box.getId();
		this.open = box.isOpen();
		this.selection = box.getSelectedRow();
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		uuid = buf.getUUID();
		open = buf.getBoolean();
		selection = buf.getInt();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putUUID(uuid);
		buf.putBoolean(open);
		buf.putInt(selection);
	}

	@Override
	public void handle(SpoutPlayer player) {
		Widget w = null;
		if (player.getCurrentScreen() != null) {
			w = player.getCurrentScreen().getWidget(uuid);
		}
		if (w == null) {
			w = player.getMainScreen().getWidget(uuid);
		}
		if (w == null && player.getMainScreen().getActivePopup() != null) {
			w = player.getMainScreen().getActivePopup().getWidget(uuid);
		}

		if (w != null && w instanceof GenericComboBox) {
			box = (GenericComboBox) w;
			box.setOpen(open, false);
			box.setSelection(selection);
		}
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
