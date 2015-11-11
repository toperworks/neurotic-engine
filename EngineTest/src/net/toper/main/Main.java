package net.toper.main;


import net.toper.Frame;
import net.toper.graphics.RenderEngine;

public class Main {

	static Frame f;

	public Main() {
		initFrame();
		Thread t = new Thread(new RenderEngine(f));
		t.start();
	}

	public void initFrame() {
		f = new Frame();
		f.setDimensions(1200, 1200 / 16 * 9);
		f.changeScale(3);
		f.open();
	}

	public static void main(String args[]) {
		new Main();
	}

}
