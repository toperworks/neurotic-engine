package net.toper.main;

import net.toper.Frame;
import net.toper.graphics.RenderEngine;

public class Main {

	static Frame f;

	public Main() {
		initFrame();
		new RenderEngine(f);
	}

	public void initFrame() {
		f = new Frame();
		f.setDimensions(1200, 1200 / 16 * 9);
		f.changeScale(0);
		f.open();
	}

	public static void main(String args[]) {
		new Main();
	}

}
