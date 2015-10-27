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
		while (true) {
			System.out.println(f.getFPS());
			render();
		}
	}

	float i;
	int fps;

	public void render() {
		p.setColor(0xfffffff);
		f.clear(0x000);
		drawShape();
		f.endFrame();
	}

	boolean down1 = false;

	public void drawShape() {
		if (Input.getMouseButton() == 1 && !down1) {
			p.addVertex(new Vector(Input.getActualMouseX(), Input.getActualMouseY()));
			down1 = true;
		} else if (down1) {
			down1 = false;
		}
		Render.simplePolyDraw(f, p);
	}

}
