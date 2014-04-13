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

public class GenericEntityWidget extends GenericWidget implements EntityWidget {
	private int entityId = 0;

	protected GenericEntityWidget() {
	}

	public GenericEntityWidget(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public WidgetType getType() {
		return WidgetType.EntityWidget;
	}

	@Override
	public EntityWidget setEntityId(int id) {
		if (entityId != id) {
			entityId = id;
			autoDirty();
		}
		return this;
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		entityId = buf.getInt();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putInt(entityId);
	}

	@Override
	public EntityWidget copy() {
		return ((EntityWidget) super.copy()).setEntityId(getEntityId());
	}
}
