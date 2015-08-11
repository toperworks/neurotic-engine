package net.toper.main;

import net.toper.Frame;
import net.toper.graphics.RenderEngine;

public class Main {

	Frame f;

	public Main() {
		initFrame();
		Thread t = new Thread(new RenderEngine(f));
		t.start();
	}

	public void initFrame() {
		f = new Frame();
		f.setDimensions(1440, 1440 / 16 * 9);
		f.open();
	}

	public static void main(String args[]) {
		new Main();
	}

}
