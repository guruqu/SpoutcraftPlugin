package org.getspout.spoutapi.event.input;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PlayerClipboardEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private String newClipboard;

	public PlayerClipboardEvent(SpoutPlayer p, String newClipboard) {
		super(p);
		this.newClipboard = newClipboard;
	}

	public String getClipboard() {
		return SpoutManager.getPlayer(player).getClipboardText();
	}

	public String getNewClipboard() {
		return newClipboard;
	}

	public void setNewClipboard(String newClipboard) {
		this.newClipboard = newClipboard;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean b) {
		cancel = b;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
