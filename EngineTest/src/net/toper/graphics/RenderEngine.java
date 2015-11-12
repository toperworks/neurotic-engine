package net.toper.graphics;

import net.toper.Frame;
import net.toper.Render;
import net.toper.shape.Polygon;
import net.toper.shape.Vector;

public class RenderEngine {

	private Frame f;

	public RenderEngine(Frame f) {
		this.f = f;
		while (true) {
			render();
		}
	}

	public Frame getFrame() {
		return f;
	}

	public void run() {

	}

	float i;
	int fps;
	Polygon p1, p2;
	double degrees;

	public void render() {
		p2 = new Polygon();
		p1 = new Polygon();
		p1.setColor(0xff00ff00);
		p1.addVertex(new Vector(100, 10));
		p1.addVertex(new Vector(100, 100));
		p1.addVertex(new Vector(10, 100));
		p2.addVertex(new Vector(100, 10));
		p2.addVertex(new Vector(100, 100));
		p2.addVertex(new Vector(10, 100));
		p2.setColor(0xff00ff00);
		f.clear(0x000);
		degrees += 1 * f.getDelta();
		p1.setColor(0xccffff00);
		p1.rotateAroundCenter(45);
		drawShape();
		f.endFrame();
		f.setTitle("" + f.getFPS());
	}

	public void drawShape() {
		Polygon p = p1.isInside(p1, p2);
		Render.simplePolyDraw(f, p1);
		Render.simplePolyDraw(f, p2);
		p.setColor(0xffFF0000);
		Render.simplePolyDraw(f, p);
	}

}
