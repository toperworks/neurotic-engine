package net.toper.graphics;

import net.toper.Frame;
import net.toper.Render;
import net.toper.ResourceManager;
import net.toper.Texture;

public class RenderEngine implements Runnable {

	private Frame f;
	private Texture t;

	public RenderEngine(Frame f) {
		t = ResourceManager.loadTexture("/rarer.jpg", false);
		this.f = f;
	}

	public Frame getFrame() {
		return f;
	}

	public void run() {
		while (true) {
			f.setTitle("FPS: " + f.getFPS());
			render();
		}
	}

	public void render() {
		f.clear(0xfff);
		Render.simpleRender(f, t, 599, 599);
		f.endFrame();
	}

	float i;

	public void renderLine() {
		Render.drawLine(f, 200, 200, 400, 400, 0XFF00FF00);
	}

	public void renderCircle() {
		i += 0.05f;
		Render.drawCircle(f, 200, 100, (int) i, 0XFFFFFF00);
	}
}
