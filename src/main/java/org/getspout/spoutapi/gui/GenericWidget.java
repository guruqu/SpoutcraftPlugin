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
import java.util.UUID;
import javax.xml.bind.TypeConstraintException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.io.MinecraftExpandableByteBuffer;

public abstract class GenericWidget implements Widget {
	/**
	 * Set if this is Spoutcraft (client), cleared if it is Spout (server)...
	 */
	static final protected transient boolean isSpoutcraft = false;
	protected int X = 0;
	protected int Y = 0;
	protected int width = 50;
	protected int height = 50;
	protected boolean visible = true;
	protected transient boolean dirty = true;
	protected transient Screen screen = null;
	protected RenderPriority priority = RenderPriority.Normal;
	protected UUID id = UUID.randomUUID();
	protected String tooltip = "";
	protected String plugin = "Spoutcraft";
	protected WidgetAnchor anchor = WidgetAnchor.SCALE;
	// Server side layout
	protected Container container = null;
	protected boolean fixed = false;
	protected int marginTop = 0, marginRight = 0, marginBottom = 0, marginLeft = 0;
	protected int minWidth = 0, maxWidth = 427, minHeight = 0, maxHeight = 240;
	protected int orig_x = 0, orig_y = 0;
	protected boolean autoDirty = true;
	protected transient boolean hasPosition = false;
	protected transient boolean hasSize = false;
	// Animation
	protected WidgetAnim animType = WidgetAnim.NONE;
	protected float animValue = 1f;
	protected short animCount = 0;
	protected short animTicks = 20;
	protected final byte ANIM_REPEAT = (1 << 0);
	protected final byte ANIM_RESET = (1 << 1);
	protected final byte ANIM_RUNNING = (1 << 2);
	protected final byte ANIM_STOPPING = (1 << 3);
	protected byte animFlags = 0;
	protected transient int animTick = 0; // Current tick
	protected transient int animFrame = 0; // Current frame

	protected GenericWidget() {
	}

	@Override
	final public boolean isSpoutcraft() {
		return isSpoutcraft;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	public GenericWidget(int X, int Y, int width, int height) {
		this.X = X;
		this.Y = Y;
		this.width = width;
		this.height = height;
	}

	@Override
	public Widget setAnchor(WidgetAnchor anchor) {
		if (anchor != null && !getAnchor().equals(anchor)) {
			this.anchor = anchor;
			autoDirty();
		}
		return this;
	}

	@Override
	public WidgetAnchor getAnchor() {
		return anchor;
	}

	@Override
	public Plugin getPlugin() {
		return Bukkit.getServer().getPluginManager().getPlugin(plugin == null || plugin.equals("Spoutcraft") ? "Spout" : plugin);
	}

	@Override
	public Widget setPlugin(Plugin plugin) {
		if (plugin != null) {
			this.plugin = plugin.getDescription().getName();
		}
		return this;
	}

	@Override
	public void decode(MinecraftExpandableByteBuffer buf) throws IOException {
		setX(buf.getInt());
		setY(buf.getInt());
		setWidth(buf.getInt());
		setHeight(buf.getInt());
		setAnchor(WidgetAnchor.getAnchorFromId(buf.get()));
		setVisible(buf.getBoolean());
		setPriority(RenderPriority.getRenderPriorityFromId(buf.getInt()));
		setTooltip(buf.getUTF8());
		setPlugin(Bukkit.getServer().getPluginManager().getPlugin(buf.getUTF8()));
		animType = WidgetAnim.getAnimationFromId(buf.get());
		animFlags = buf.get();
		animValue = buf.getFloat();
		animTicks = buf.getShort();
		animCount = buf.getShort();
	}

	@Override
	public void encode(MinecraftExpandableByteBuffer buf) throws IOException {
		buf.putInt(getX());
		buf.putInt(getY());
		buf.putInt(getWidth());
		buf.putInt(getHeight());
		buf.put((byte) getAnchor().getId());
		buf.putBoolean(isVisible());
		buf.putInt(priority.getId());
		buf.putUTF8(getTooltip());
		buf.putUTF8(plugin != null ? plugin : "Spoutcraft");
		buf.put((byte) animType.getId());
		buf.put(animFlags);
		buf.putFloat(animValue);
		buf.putShort(animTicks);
		buf.putShort(animCount);
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public Screen getScreen() {
		return screen;
	}

	@Override
	@Deprecated
	public Widget setScreen(Screen screen) {
		return setScreen(null, screen);
	}

	@Override
	public Widget setScreen(Plugin plugin, Screen screen) {
		if (getScreen() != null && screen != null && !getScreen().equals(screen)) {
			getScreen().removeWidget(this);
		}
		this.screen = screen;
		if (plugin != null) {
			this.plugin = plugin.getDescription().getName();
		}
		return this;
	}

	@Override
	public RenderPriority getPriority() {
		return priority;
	}

	@Override
	public Widget setPriority(RenderPriority priority) {
		if (priority != null && !getPriority().equals(priority)) {
			this.priority = priority;
			autoDirty();
		}
		return this;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public Widget setWidth(int width) {
		hasSize = true;
		width = Math.max(getMinWidth(), Math.min(width, getMaxWidth()));
		if (getWidth() != width) {
			this.width = width;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Widget setHeight(int height) {
		hasSize = true;
		height = Math.max(getMinHeight(), Math.min(height, getMaxHeight()));
		if (getHeight() != height) {
			this.height = height;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public int getX() {
		return X;
	}

	@Override
	public int getY() {
		return Y;
	}

	@Override
	public Widget setX(int pos) {
		hasPosition = true;
		if (getX() != pos) {
			X = pos;
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget setY(int pos) {
		hasPosition = true;
		if (getY() != pos) {
			Y = pos;
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget shiftXPos(int modX) {
		setX(getX() + modX);
		return this;
	}

	@Override
	public Widget shiftYPos(int modY) {
		setY(getY() + modY);
		return this;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public Widget setVisible(boolean enable) {
		if (isVisible() != enable) {
			visible = enable;
			updateSize();
			if (hasContainer()) {
				getContainer().deferLayout();
			}
			autoDirty();
		}
		return this;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Widget && other.hashCode() == hashCode();
	}

	@Override
	public void onTick() {
	}

	@Override
	public Widget setTooltip(String t) {
		if (t != null && !getTooltip().equals(t)) {
			tooltip = t;
			autoDirty();
		}
		return this;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public boolean hasContainer() {
		return container != null;
	}

	@Override
	public void setContainer(Container container) {
		if (hasContainer() && container != null && !getContainer().equals(container)) {
			getContainer().removeChild(this);
		}
		this.container = container;
	}

	@Override
	public Widget setFixed(boolean fixed) {
		if (isFixed() != fixed) {
			this.fixed = fixed;
			updateSize();
		}
		return this;
	}

	@Override
	@Deprecated
	public boolean getFixed() {
		return isFixed();
	}

	@Override
	public boolean isFixed() {
		return fixed;
	}

	@Override
	public Widget setMargin(int marginAll) {
		return setMargin(marginAll, marginAll, marginAll, marginAll);
	}

	@Override
	public Widget setMargin(int marginTopBottom, int marginLeftRight) {
		return setMargin(marginTopBottom, marginLeftRight, marginTopBottom, marginLeftRight);
	}

	@Override
	public Widget setMargin(int marginTop, int marginLeftRight, int marginBottom) {
		return setMargin(marginTop, marginLeftRight, marginBottom, marginLeftRight);
	}

	@Override
	public Widget setMargin(int marginTop, int marginRight, int marginBottom, int marginLeft) {
		if (getMarginTop() != marginTop || getMarginRight() != marginRight || getMarginBottom() != marginBottom || getMarginLeft() != marginLeft) {
			this.marginTop = marginTop;
			this.marginRight = marginRight;
			this.marginBottom = marginBottom;
			this.marginLeft = marginLeft;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget setMarginTop(int marginTop) {
		if (getMarginTop() != marginTop) {
			this.marginTop = marginTop;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget setMarginRight(int marginRight) {
		if (getMarginRight() != marginRight) {
			this.marginRight = marginRight;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget setMarginBottom(int marginBottom) {
		if (getMarginBottom() != marginBottom) {
			this.marginBottom = marginBottom;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget setMarginLeft(int marginLeft) {
		if (getMarginLeft() != marginLeft) {
			this.marginLeft = marginLeft;
			updateSize();
			autoDirty();
		}
		return this;
	}

	@Override
	public int getMarginTop() {
		return marginTop;
	}

	@Override
	public int getMarginRight() {
		return marginRight;
	}

	@Override
	public int getMarginBottom() {
		return marginBottom;
	}

	@Override
	public int getMarginLeft() {
		return marginLeft;
	}

	@Override
	public Widget setMinWidth(int min) {
		min = Math.max(min, 0);
		if (getMinWidth() != min) {
			minWidth = min;
			updateSize();
			setWidth(width); // Enforce our new size if needed
		}
		return this;
	}

	@Override
	public int getMinWidth() {
		return minWidth;
	}

	@Override
	public Widget setMaxWidth(int max) {
		max = max <= 0 ? 427 : max;
		if (getMaxWidth() != max) {
			maxWidth = max;
			updateSize();
			setWidth(width); // Enforce our new size if needed
		}
		return this;
	}

	@Override
	public int getMaxWidth() {
		return maxWidth;
	}

	@Override
	public Widget setMinHeight(int min) {
		min = Math.max(min, 0);
		if (getMinHeight() != min) {
			minHeight = min;
			updateSize();
			setHeight(height); // Enforce our new size if needed
		}
		return this;
	}

	@Override
	public int getMinHeight() {
		return minHeight;
	}

	@Override
	public Widget setMaxHeight(int max) {
		max = max <= 0 ? 240 : max;
		if (getMaxHeight() != max) {
			maxHeight = max;
			updateSize();
			setHeight(height); // Enforce our new size if needed
		}
		return this;
	}

	@Override
	public int getMaxHeight() {
		return maxHeight;
	}

	@Override
	public Widget savePos() {
		orig_x = getX();
		orig_y = getY();
		return this;
	}

	@Override
	public Widget restorePos() {
		setX(orig_x);
		setY(orig_y);
		return this;
	}

	@Override
	public Widget copy() {
		try {
			Widget copy = getType().getWidgetClass().newInstance();
			copy.setX(getX()) // Easier reading
					.setY(getY())
					.setWidth(getWidth())
					.setHeight(getHeight())
					.setVisible(isVisible())
					.setPriority(getPriority())
					.setTooltip(getTooltip())
					.setAnchor(getAnchor())
					.setMargin(getMarginTop(), getMarginRight(), getMarginBottom(), getMarginLeft()) //
					.setMinWidth(getMinWidth())
					.setMaxWidth(getMaxWidth())
					.setMinHeight(getMinHeight())
					.setMaxHeight(getMaxHeight())
					.setFixed(isFixed())
					.setAutoDirty(isAutoDirty())
					.animate(animType, animValue, animCount, animTicks, (animFlags & ANIM_REPEAT) != 0, (animFlags & ANIM_RESET) != 0);
			return copy;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to create a copy of " + getClass() + ". Does it have a valid widget type?");
		}
	}

	@Override
	public Widget updateSize() {
		if (hasContainer()) {
			container.updateSize();
		}
		return this;
	}

	@Override
	public Widget setAutoDirty(boolean dirty) {
		return this;
	}

	@Override
	public boolean isAutoDirty() {
		return autoDirty;
	}

	@Override
	public void autoDirty() {
		if (isAutoDirty()) {
			setDirty(true);
		}
	}

	@Override
	public Widget animate(WidgetAnim type, float value, short count, short ticks) {
		animate(type, value, count, ticks, true, true);
		return this;
	}

	@Override
	public Widget animate(WidgetAnim type, float value, short count, short ticks, boolean repeat) {
		animate(type, value, count, ticks, repeat, true);
		return this;
	}

	@Override
	public Widget animate(WidgetAnim type, float value, short count, short ticks, boolean repeat, boolean reset) {
		if (!type.check(this)) {
			throw new TypeConstraintException("Cannot use Animation." + type.name() + " on " + getType().toString());
		}
		animType = type;
		animValue = value;
		animCount = count;
		animTicks = ticks;
		animFlags = (byte) ((repeat ? ANIM_REPEAT : 0) | (reset ? ANIM_RESET : 0));
		animTick = 0;
		animFrame = 0;
		autoDirty();
		return this;
	}

	@Override
	public Widget animateStart() {
		if (animType != WidgetAnim.NONE) {
			animFlags |= ANIM_RUNNING;
			autoDirty();
		}
		return this;
	}

	@Override
	public Widget animateStop(boolean finish) {
		if ((animFlags & ANIM_RUNNING) != 0 && finish) {
			animFlags |= ANIM_STOPPING;
			autoDirty();
		} else {
			animFlags &= ~ANIM_RUNNING;
			autoDirty();
		}
		return this;
	}

	@Override
	public void onAnimate() {
		if ((animFlags & ANIM_RUNNING) == 0 || animTicks == 0 || ++animTick % animTicks != 0) {
			return;
		}
		// We're running, and it's ready for our next frame...
		if (++animFrame == animCount) {
			animFrame = 0;
			if ((animFlags & ANIM_STOPPING) != 0 || (animFlags & ANIM_REPEAT) == 0) {
				animFlags &= ~ANIM_RUNNING;
			}
		}
	}

	@Override
	public void onAnimateStop() {
	}

	@Override
	public boolean hasPosition() {
		return hasPosition;
	}

	@Override
	public boolean hasSize() {
		return hasSize;
	}
}
