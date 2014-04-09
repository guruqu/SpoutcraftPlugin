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

import gnu.trove.map.hash.TShortObjectHashMap;
import org.getspout.spoutapi.material.block.GenericCustomBlock;
import org.getspout.spoutapi.material.item.GenericCustomFood;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.material.item.GenericCustomTool;

public enum PacketType {
	KEY_PRESS((short) 0, PacketKeyPress.class),
	AIR_CAPACITY((short) 1, PacketAirCapacity.class),
	DOWNLOAD_TEXTURE_HTTP((short) 2, PacketDownloadTextureHTTP.class),
	ENTITY_NAMEPLATE((short) 3, PacketEntityNameplate.class),
	CHANGE_RENDER_DISTANCE((short) 4, PacketChangeRenderDistance.class),
	SHOW_ACHIEVEMENT((short) 5, PacketShowAchievement.class),
	PLAY_SOUND((short) 6, PacketPlaySound.class),
	DOWNLOAD_MUSIC((short) 7, PacketDownloadMusic.class),
	CLIPBOARD_TEXT((short) 8, PacketClipboardText.class),
	CHANGE_MUSIC((short) 9, PacketChangeMusic.class),
	WIDGET((short) 10, PacketWidget.class),
	STOP_MUSIC((short) 11, PacketStopMusic.class),
	CHANGE_ITEM_DISPLAY_NAME((short) 12, PacketChangeItemDisplayName.class),
	CHANGE_SKYBOX((short) 13, PacketChangeSkybox.class),
	TEXTURE_PACK((short) 14, PacketTexturePack.class),
	NOTIFICATION((short) 15, PacketNotification.class),
	SCREEN_ACTION((short) 16, PacketScreenAction.class),
	CONTROL_ACTION((short) 17, PacketControlAction.class),
	TOGGLE_CHEATS((short) 18, PacketToggleCheats.class),
	WIDGET_REMOVE((short) 19, PacketWidgetRemove.class),
	ENTITY_TEXTURE((short) 20, PacketEntitySkin.class),
	OPEN_SCREEN(26, PacketOpenScreen.class),
	PacketPreCacheFile(27, PacketPreCacheFile.class),
	PacketCacheFile(28, PacketDownloadFile.class),
	PacketCacheDeleteFile(29, PacketDeleteFile.class),
	PacketPreCacheCompleted(30, PacketPreCacheCompleted.class),
	PacketMovementModifiers(31, PacketMovementModifiers.class),
	PacketSetVelocity(32, PacketSetVelocity.class),
	PacketFullVersion(33, PacketFullVersion.class),
	//PacketCustomId(34, PacketCustomId.class),
	//PacketItemTexture(35, PacketItemTexture.class),
	//PacketBlockHardness(36, PacketBlockHardness.class),
	PacketOpenSignGUI(37, PacketOpenSignGUI.class),
	PacketCustomBlockOverride(38, PacketOverrideBlock.class),
	PacketCustomBlockDesign(39, PacketBlockModel.class),
	PacketKeyBinding(41, PacketKeyBinding.class),
	PacketBlockData(42, PacketBlockData.class),
	PacketCustomMultiBlockOverride(43, PacketOverrideMultiBlock.class),
	PacketScreenshot(47, PacketScreenshot.class),
	PacketCustomItem(48, GenericCustomItem.class),
	PacketCustomTool(49, GenericCustomTool.class),
	PacketCustomBlock(50, GenericCustomBlock.class),
	PacketCustomBlockChunkOverride(51, PacketOverrideChunk.class),
	PacketCustomFood(52, GenericCustomFood.class),
	PacketEntityInformation(53, PacketEntityInformation.class),
	PacketComboBox(54, PacketComboBox.class),
	PacketFocusUpdate(55, PacketFocusUpdate.class),
	PacketClientAddons(56, org.getspout.spoutapi.packet.PacketClientAddons.class),
	PacketPermissionUpdate(57, PacketPermissionUpdate.class),
	PacketSpawnTextEntity(58, PacketSpawnTextEntity.class),
	PacketSlotClick(59, PacketSlotClick.class),
	PacketWaypoint(60, PacketWaypoint.class),
	PacketParticle(61, PacketParticle.class),
	PacketAccessory(62, PacketAccessory.class),
	PacketValidatePrecache(63, PacketValidatePrecache.class),
	PacketRequestPrecache(64, PacketRequestPrecache.class),
	PacketSendPrecache(65, PacketSendPrecache.class),
	PacketSendLink(66, PacketSendLink.class);
	private static final TShortObjectHashMap<PacketType> packetsById = new TShortObjectHashMap<PacketType>();
	private final short id;
	private final Class<? extends SpoutPacket> clazz;

	PacketType(short id, Class<? extends SpoutPacket> clazz) {
		this.id = id;
		this.clazz = clazz;
	}

	public static PacketType getFromId(short id) {
		return packetsById.get(id);
	}

	static {
		for (PacketType packet : values()) {
			packetsById.put(packet.getId(), packet);
		}
	}

	public short getId() {
		return id;
	}

	public Class<? extends SpoutPacket> getPacketClass() {
		return clazz;
	}
}
