package net.toper.shape;

public class Vector {
	public float x;
	public float y;

	public Vector() {

	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void updatePosition(float x, float y) {
		this.y = y;
		this.x = x;
	}

	public void multiply(float num) {
		y *= num;
		x *= num;
	}

	public void multiply(float nx, float ny) {
		y *= ny;
		x *= nx;
	}
}