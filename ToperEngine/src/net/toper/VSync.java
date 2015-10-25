package net.toper;

public class VSync implements Runnable {

	Frame f;

	long lastTime = System.nanoTime();
	final double ticks = 60D;
	double ns = 1000000000 / ticks;
	double delta = 0;

	public VSync(Frame f) {
		this.f = f;
		Thread updateThread = new Thread(this);
		updateThread.start();
	}

	public void run() {
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				f.endFrame();
				delta--;
			}
		}
	}
}
