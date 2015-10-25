package net.toper;

public class VSync implements Runnable {

	Frame f;

	public VSync(Frame f) {
		this.f = f;
		Thread updateThread = new Thread(this);
		updateThread.start();
	}

	public void run() {
		while (true) {
			f.endFrame();
		}
	}
}
