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
		p.addVertex(new Vector(100, 10));
		p.addVertex(new Vector(250, 150));
		p.addVertex(new Vector(100, 50));
		p.addVertex(new Vector(250, 10));
		while (true) {
			render();
		}
	}

	float i;
	int fps;
	Polygon p;

	public void render() {
		Render.enableTransparency(false);
		f.clear(0x000);
		double degrees = getAngle(Input.getScaledMouseX(), Input.getScaledMouseY());
		p.rotateAround(degrees);
		p.setColor(0xccffff00);
		drawShape();
		f.endFrame();
		f.setTitle("" + f.getFPS());
	}

	public void drawShape() {
		Render.simplePolyDraw(f, p);
	}

	public float getAngle(int x, int y) {
		float angle = (float) Math.toDegrees(Math.atan2(p.getCenterY() - y, p.getCenterX() - x));

		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

}
