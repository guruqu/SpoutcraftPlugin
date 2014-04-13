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
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;

public class GenericSlot extends GenericControl implements Slot {
	private ItemStack stack = new ItemStack(Material.AIR);
	private int depth = 16;
	private boolean renderAmount = true;

	public WidgetType getType() {
		return WidgetType.Slot;
	}

	public ItemStack getItem() {
		if (stack == null) {
			stack = new ItemStack(Material.AIR);
		}
		return stack.clone();
	}

	public Slot setItem(ItemStack item) {
		if (item == null || item.getAmount() == 0) {
			stack = new ItemStack(Material.AIR);
			return this;
		}
		stack = item.clone();
		setDirty(true);
		return this;
	}

	public boolean doesRenderAmount() {
		return renderAmount;
	}

	public GenericSlot setRenderAmount(boolean renderAmount) {
		this.renderAmount = renderAmount;
		setDirty(true);
		return this;
	}

	public boolean onItemPut(ItemStack item) {
		return true;
	}

	public boolean onItemTake(ItemStack item) {
		return true;
	}

	public void onItemShiftClicked() {
	}

	public boolean onItemExchange(ItemStack current, ItemStack cursor) {
		return true;
	}

	public int getDepth() {
		return depth;
	}

	public Slot setDepth(int depth) {
		this.depth = depth;
		setDirty(true);
		return this;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		stack.setTypeId(buf.getInt());
		stack.setAmount((int) buf.getShort());
		stack.setDurability(buf.getShort());
		depth = buf.getInt();
		renderAmount = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putInt(stack.getTypeId());
		buf.putShort((short) stack.getAmount());
		buf.putShort(stack.getDurability());
		buf.putInt(depth);
		buf.putBoolean(renderAmount);

		if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
			buf.putBoolean(true);
			buf.putUTF8(stack.getItemMeta().getDisplayName());
		} else {
			buf.putBoolean(false);
		}

		if (stack.hasItemMeta() && stack.getItemMeta().hasLore()) {
			buf.putBoolean(true);
			buf.putInt(stack.getItemMeta().getLore().size());
			for (String l : stack.getItemMeta().getLore()) {
				buf.putUTF8(l);
			}
		} else {
			buf.putBoolean(false);
		}

		if (stack.hasItemMeta() && stack.getItemMeta().hasEnchants()) {
			buf.putBoolean(true);
			buf.putInt(stack.getItemMeta().getEnchants().size());
			for (Entry e : stack.getItemMeta().getEnchants().entrySet()) {
				buf.putInt(((Enchantment) e.getKey()).getId());
				buf.putInt((Integer) e.getValue());
			}
		} else {
			buf.putBoolean(false);
		}
	}
}
