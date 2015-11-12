package net.toper.shape;

public class Vector {
	private float x;
	private float y;

	public Vector() {

	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
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

	public void move(float amtX, float amtY) {
		x += amtX;
		y += amtY;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}