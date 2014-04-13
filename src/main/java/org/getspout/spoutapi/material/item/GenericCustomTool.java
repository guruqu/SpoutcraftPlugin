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

import gnu.trove.map.hash.TObjectFloatHashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.getspout.spoutapi.inventory.SpoutEnchantment;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.material.Block;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.Material;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.Tool;

public class GenericCustomTool extends GenericCustomItem implements Tool {
	private short maxdurability = 100;
	private TObjectFloatHashMap<Block> strengthMods = new TObjectFloatHashMap<Block>();

	public GenericCustomTool(Plugin plugin, String name, String texture) {
		super(plugin, name, texture);
	}

	@Override
	public Tool setMaxDurability(short durability) {
		maxdurability = durability;
		return this;
	}

	@Override
	public short getMaxDurability() {
		return maxdurability;
	}

	@Override
	public float getStrengthModifier(Block block) {
		if (strengthMods.contains(block)) {
			return strengthMods.get(block);
		}
		return 1.0F;
	}

	@Override
	public Tool setStrengthModifier(Block block, float modifier) {
		strengthMods.put(block, modifier);
		return this;
	}

	@Override
	public Block[] getStrengthModifiedBlocks() {
		Object[] keys = strengthMods.keys();
		Block[] modified = new Block[keys.length];
		for (int i = 0; i < keys.length; i++) {
			modified[i] = (Block) keys[i];
		}
		return modified;
	}

	@Override
    public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		setMaxDurability(buf.getShort());
		short size = buf.getShort();
		for (int i = 0; i < size; i++) {
			int id = buf.getInt();
			int data = buf.getShort();
			float mod = buf.getFloat();
			Block block = MaterialData.getBlock(id, (short) data);
			if (data == -1) {
				block = MaterialData.getCustomBlock(id);
			}
			setStrengthModifier(block, mod);
		}
	}

	@Override
    public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
        buf.putShort(getMaxDurability());
		Block[] mod = getStrengthModifiedBlocks();
        buf.putShort((short) mod.length);
        for (Block block : mod) {
            if (block instanceof CustomBlock) {
                buf.putInt(((CustomBlock) block).getCustomId());
                buf.putShort((short) -1);
            } else {
                buf.putInt(block.getRawId());
                buf.putShort((short) block.getRawData());
            }
            buf.putFloat(getStrengthModifier(block));
        }
	}

	@Override
	public int getVersion() {
		return super.getVersion();
	}

	public static short getDurability(ItemStack is) {
		Material m = MaterialData.getMaterial(is.getTypeId(), is.getDurability());
		if (!(m instanceof Tool)) {
			throw new IllegalArgumentException("Itemstack must be a tool!");
		}
		return (short) is.getEnchantmentLevel(SpoutEnchantment.DURABILITY);
	}

	public static void setDurability(ItemStack is, short durability) {
		Material m = MaterialData.getMaterial(is.getTypeId(), is.getDurability());
		if (!(m instanceof Tool)) {
			throw new IllegalArgumentException("Itemstack must be a tool!");
		}
		is.removeEnchantment(SpoutEnchantment.DURABILITY);
		is.addUnsafeEnchantment(SpoutEnchantment.DURABILITY, durability);
	}
}
