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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
<<<<<<< Updated upstream
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;
import org.getspout.spoutapi.packet.PacketWidget;
import org.getspout.spoutapi.packet.PacketWidgetRemove;
=======
import org.getspout.spout.packet.builtin.PacketWidget;
import org.getspout.spout.packet.builtin.PacketWidgetRemove;
>>>>>>> Stashed changes
import org.getspout.spoutapi.player.SpoutPlayer;

public abstract class GenericScreen extends GenericWidget implements Screen {
	protected Map<Widget, Plugin> widgets = new ConcurrentHashMap<Widget, Plugin>();
	protected int playerId;
	protected boolean bg = true;

	protected GenericScreen() {
	}

	@Override
	public int getVersion() {
		return super.getVersion();
	}

	public GenericScreen(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public Widget[] getAttachedWidgets() {
		Widget[] list = new Widget[widgets.size()];
		widgets.keySet().toArray(list);
		return list;
	}

	@Override
	public Screen attachWidget(Plugin plugin, Widget widget) {
		if (plugin == null) {
			throw new NullPointerException("Plugin can not be null!");
		}
		if (widget == null) {
			throw new NullPointerException("Widget can not be null!");
		}
		widgets.put(widget, plugin);
		widget.setPlugin(plugin);
		widget.setDirty(true);
		widget.setScreen(this);
		return this;
	}

	@Override
	public Screen attachWidgets(Plugin plugin, Widget... widgets) {
		for (Widget widget : widgets) {
			attachWidget(plugin, widget);
		}
		return this;
	}

	@Override
	public Screen removeWidget(Widget widget) {
		SpoutPlayer player = SpoutManager.getPlayerFromId(playerId);
		if (player != null) {
			if (widgets.containsKey(widget)) {
				widgets.remove(widget);
				if (!widget.getType().isServerOnly()) {
					SpoutManager.getPlayerFromId(playerId).sendPacket(new PacketWidgetRemove(widget, getId()));
				}
				widget.setScreen(null);
			}
		}
		return this;
	}

	@Override
	public Screen removeWidgets(Plugin p) {
		if (p != Bukkit.getServer().getPluginManager().getPlugin("Spout")) {
			for (Widget i : getAttachedWidgets()) {
				if (widgets.get(i) != null && widgets.get(i).equals(p)) {
					removeWidget(i);
				}
			}
		}
		return this;
	}

	@Override
	public boolean containsWidget(Widget widget) {
		return containsWidget(widget.getId());
	}

	@Override
	public boolean containsWidget(UUID id) {
		return getWidget(id) != null;
	}

	@Override
	public Widget getWidget(UUID id) {
		for (Widget w : widgets.keySet()) {
			if (w.getId().equals(id)) {
				return w;
			}
		}
		return null;
	}

	@Override
	public boolean updateWidget(Widget widget) {
		if (widgets.containsKey(widget)) {
			Plugin plugin = widgets.get(widget);
			widgets.remove(widget);
			widgets.put(widget, plugin);
			widget.setScreen(this);
			return true;
		}
		return false;
	}

	@Override
	public void onTick() {
		SpoutPlayer player = SpoutManager.getPlayerFromId(playerId);
		if (player != null) {
			// Create a copy because onTick may remove the widget
			Set<Widget> widgetCopy = new HashSet<Widget>(widgets.keySet());
			for (Widget widget : widgetCopy) {
				try {
					widget.onTick();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (Widget widget : widgets.keySet()) {
				try {
					widget.onAnimate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (Widget widget : widgets.keySet()) {
				if (widget.isDirty()) {
					if (!widget.hasSize()/* || !widget.hasPosition()*/) {
						String type = "Unknown";
						try {
							type = widget.getType().getWidgetClass().getSimpleName();
						} catch (Exception e) {
						}
						Logger.getLogger("Minecraft").log(Level.WARNING,
								type
										+ " belonging to " + widget.getPlugin().getDescription().getName()
										+ " does not have a default "
										+ (!widget.hasSize() ? "size" : "") + (!widget.hasSize() && !widget.hasPosition() ? " or " : "") + (!widget.hasPosition() ? "position" : "")
										+ "!"
						);
						widget.setX(widget.getX());
						widget.setHeight(widget.getHeight());
					}
					if (!widget.getType().isServerOnly()) {
						player.sendPacket(new PacketWidget(widget, getId()));
					}
					widget.setDirty(false);
				}
			}
		}
	}

	@Override
	public Screen setBgVisible(boolean enable) {
		bg = enable;
		return this;
	}

	@Override
	public boolean isBgVisible() {
		return bg;
	}

	@Override
	public SpoutPlayer getPlayer() {
		return SpoutManager.getPlayerFromId(playerId);
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.decode(buf);
		setBgVisible(buf.getBoolean());
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		super.encode(buf);
		buf.putBoolean(isBgVisible());
	}

	@Override
	public void setDirty(boolean dirty) {
		super.setDirty(dirty);
		if (dirty) {
			for (Widget widget : getAttachedWidgets()) {
				widget.setDirty(true);
			}
		}
	}

	@Override
	public Widget copy() {
		throw new UnsupportedOperationException("You can not create a copy of a screen");
	}

	@Override
	public Set<Widget> getAttachedWidgetsAsSet(boolean recursive) {
		Set<Widget> set = new HashSet<Widget>();
		for (Widget w : widgets.keySet()) {
			set.add(w);
			if (w instanceof Screen && recursive) {
				set.addAll(((Screen) w).getAttachedWidgetsAsSet(true));
			}
		}
		return set;
	}

	@Override
	public void onScreenClose(ScreenCloseEvent e) {
	}
}
