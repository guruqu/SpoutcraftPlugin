package org.getspout.spoutapi.gui;

public class DecayingLabel extends GenericLabel {
	private int ticksAlive = 0;

	public DecayingLabel(String s, int ticksAlive) {
		super(s);
		this.ticksAlive = ticksAlive;
	}

	@Override
	public void onTick() {
		ticksAlive--;
		if (ticksAlive <= 0) {
			if (getScreen() != null) {
				getScreen().removeWidget(this);
			}
		}
	}
}
