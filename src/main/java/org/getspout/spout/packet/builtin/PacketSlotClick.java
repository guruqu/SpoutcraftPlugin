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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.slot.SlotEvent;
import org.getspout.spoutapi.event.slot.SlotExchangeEvent;
import org.getspout.spoutapi.event.slot.SlotPutEvent;
import org.getspout.spoutapi.event.slot.SlotShiftClickEvent;
import org.getspout.spoutapi.event.slot.SlotTakeEvent;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.Slot;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketSlotClick extends SpoutPacket {
	private UUID screen;
	private UUID slot;
	private int button;
	private boolean holdingShift;

	protected PacketSlotClick() {
	}

	public PacketSlotClick(Slot slot, int button, boolean holdingShift) {
		screen = slot.getScreen().getId();
		this.slot = slot.getId();
		this.button = button;
		this.holdingShift = holdingShift;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		long msb = buf.getLong();
		long lsb = buf.getLong();
		screen = new UUID(msb, lsb);
		msb = buf.getLong();
		lsb = buf.getLong();
		slot = new UUID(msb, lsb);
		button = buf.getInt();
		holdingShift = buf.getBoolean();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putLong(screen.getMostSignificantBits());
		buf.putLong(screen.getLeastSignificantBits()); // 16
		buf.putLong(slot.getMostSignificantBits());
		buf.putLong(slot.getLeastSignificantBits()); // 32
		buf.putInt(button); // mouseClick will usually be 0 (left) or 1 (right) - so this is safe unless the mouse has... 257 buttons :P
		buf.putBoolean(holdingShift);//34
	}

	@Override
	public void handle(SpoutPlayer player) {
		InGameHUD mainScreen = player.getMainScreen();
		PopupScreen popup = mainScreen.getActivePopup();
		Screen current = player.getCurrentScreen();

		Screen in = null;
		if (mainScreen != null && screen.equals(mainScreen.getId())) {
			in = mainScreen;
		}
		if (popup != null && screen.equals(popup.getId())) {
			in = popup;
		}
		if (current != null && screen.equals(current.getId())) {
			in = current;
		}
		if (in == null) {
			return;
		}
		if (!in.containsWidget(slot)) {
			return;
		}

		// Slot handling code goes here.
		Slot slot = (Slot) in.getWidget(this.slot);
		try {
			ItemStack stackOnCursor = player.getItemOnCursor();
			if (stackOnCursor == null) {
				stackOnCursor = new ItemStack(Material.AIR);
			}
			ItemStack stackInSlot = slot.getItem();
			if ((stackOnCursor == null || stackOnCursor.getTypeId() == 0) && stackInSlot.getTypeId() == 0) {
				return; // Nothing to do
			}
			if (stackOnCursor.getTypeId() == 0 && stackInSlot.getTypeId() != 0 && button == 1) { // Split item
				int amountSlot = stackInSlot.getAmount() / 2;
				int amountCursor = stackInSlot.getAmount() - amountSlot;
				if (stackInSlot.getAmount() == 1) {
					amountSlot = 0;
					amountCursor = 1;
				}
				stackOnCursor = stackInSlot.clone();
				stackOnCursor.setAmount(amountCursor);
				stackInSlot.setAmount(amountSlot);
				if (amountSlot == 0) {
					stackInSlot = new ItemStack(Material.AIR);
				}
				SlotEvent s = new SlotTakeEvent(player, slot, stackInSlot, !slot.onItemTake(stackOnCursor));
				Bukkit.getPluginManager().callEvent(s);
				if (!s.isCancelled()) {
					slot.setItem(stackInSlot);
				} else {
					slot.setDirty(true); // We need to tell the client that the operation was denied.
					return;
				}
			} else if (stackOnCursor != null && (stackInSlot.getTypeId() == 0 || (stackInSlot.getTypeId() == stackOnCursor.getTypeId() && stackInSlot.getDurability() == stackOnCursor.getDurability()))) { // Put item
				ItemStack toPut = stackOnCursor.clone();
				int putAmount = toPut.getAmount();
				if (button == 1) {
					putAmount = 1;
				}
				int amount = stackInSlot.getTypeId() == 0 ? 0 : stackInSlot.getAmount();
				amount += putAmount;
				int maxStackSize = toPut.getMaxStackSize();
				if (maxStackSize == -1) {
					maxStackSize = 64;
				}
				if (amount > maxStackSize) {
					putAmount -= amount - maxStackSize;
					amount = maxStackSize;
				}
				if (putAmount <= 0) {
					return;
				}
				toPut.setAmount(putAmount);
				SlotEvent s = new SlotPutEvent(player, slot, stackInSlot, !slot.onItemPut(toPut));
				Bukkit.getPluginManager().callEvent(s);
				if (!s.isCancelled()) {
					stackOnCursor.setAmount(stackOnCursor.getAmount() - putAmount);
					if (stackOnCursor.getAmount() == 0) {
						stackOnCursor = new ItemStack(Material.AIR);
					}
					ItemStack put = toPut.clone();
					put.setAmount(amount);
					slot.setItem(put);
				} else {
					slot.setDirty(true); // We need to tell the client that the operation was denied.
				}
			} else if (stackOnCursor == null || stackOnCursor.getTypeId() == 0) { //Take item or shift click
				if (holdingShift) {
					slot.onItemShiftClicked();
					SlotEvent s = new SlotShiftClickEvent(player, slot);
					Bukkit.getPluginManager().callEvent(s);
				} else { // Take item
					SlotEvent s = new SlotTakeEvent(player, slot, stackInSlot, !slot.onItemTake(stackInSlot));
					Bukkit.getPluginManager().callEvent(s);
					if (!s.isCancelled()) {
						stackOnCursor = stackInSlot;
						slot.setItem(new ItemStack(Material.AIR));
					} else {
						slot.setDirty(true); // We need to tell the client that the operation was denied.
					}
				}
			} else if (stackOnCursor.getTypeId() != stackInSlot.getTypeId() || stackOnCursor.getDurability() != stackInSlot.getDurability()) { // Exchange slot stack and cursor stack
				SlotEvent s = new SlotExchangeEvent(player, slot, stackInSlot, stackOnCursor.clone(), !slot.onItemExchange(stackInSlot, stackOnCursor.clone()));
				Bukkit.getPluginManager().callEvent(s);
				if (!s.isCancelled()) {
					slot.setItem(stackOnCursor.clone());
					stackOnCursor = stackInSlot;
				} else {
					slot.setDirty(true); // We need to tell the client that the operation was denied.
				}
			}

			if (stackOnCursor == null || stackOnCursor.getTypeId() == 0) {
				player.setItemOnCursor(null);
			} else {
				player.setItemOnCursor(stackOnCursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
