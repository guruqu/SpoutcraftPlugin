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
package org.getspout.spoutapi.material.item;

import java.io.IOException;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.material.Food;
<<<<<<< Updated upstream
=======
>>>>>>> Stashed changes

public class GenericCustomFood extends GenericCustomItem implements Food {
	private int hunger;

	public GenericCustomFood(Plugin plugin, String name, String texture, int hungerRestored) {
		super(plugin, name, texture);
		hunger = hungerRestored;
	}

	@Override
	public int getHungerRestored() {
		return hunger;
	}

	@Override
    public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		hunger = buf.getInt();
	}

	@Override
    public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
        buf.putInt(getHungerRestored());
	}
}
