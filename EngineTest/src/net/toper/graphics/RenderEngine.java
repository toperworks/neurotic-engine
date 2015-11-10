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

	public void run() {
		p = new Polygon();
		p.setColor(0xffffff00);
		p.addVertex(new Vector(400, 300));
		p.addVertex(new Vector(650, 500));
		p.addVertex(new Vector(400, 150));
		p.addVertex(new Vector(100, 250));
		p.addVertex(new Vector(300, 50));
		p.addVertex(new Vector(200, 250));
		p.addVertex(new Vector(150, 150));
		p.addVertex(new Vector(105, 350));
		while (true) {
			render();
		}
	}

	float i;
	int fps;
	Polygon p;

	public void render() {
		f.clear(0x000);
		p.rotateAround(0.1f * f.getDelta());
		drawShape();
		f.endFrame();
		f.setTitle("" + f.getFPS());
	}

	public void drawShape() {
		Render.simplePolyDraw(f, p);
	}

}
