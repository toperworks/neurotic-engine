package net.toper.graphics;

import net.toper.Frame;
import net.toper.Input;
import net.toper.Render;
import net.toper.shape.Polygon;
import net.toper.shape.Vector;

public class RenderEngine implements Runnable {

	private Frame f;

	public RenderEngine(Frame f) {
		this.f = f;
	}

	public Frame getFrame() {
		return f;
	}

	Polygon p;

	public void run() {
		p = new Polygon();
		p.addVertex(new Vector(370, 150));
		p.addVertex(new Vector(70, 50));
		p.addVertex(new Vector(10, 0));
		while (true) {
			f.setTitle("FPS: " + f.getFPS());
			render();
		}
	}

	float i;

	public void render() {
		p.setColor(0xfffffff);
		f.clear(0x000);
		drawShape();
		f.endFrame();
	}

	public void drawShape() {
		Render.simplePolyDraw(f, p);
	}

}
