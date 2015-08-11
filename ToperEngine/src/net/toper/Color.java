package net.toper;

public class Color {

	private float r, g, b, a;

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.b = b;
		this.g = g;
		this.a = a;
	}

	public Color(float r, float g, float b) {
		this.r = r;
		this.b = b;
		this.g = g;
		a = 1f;
	}

	public Color(int hex) {
		float colors[] = decode(hex);
		this.r = colors[1];
		this.g = colors[2];
		this.b = colors[3];
		this.a = colors[0];
	}

	public float[] decode(int hex) {
		float colors[] = new float[4];
		colors[0] = (((int) hex & 0xff) << 24);
		colors[1] = (((int) hex & 0xff) << 16);
		colors[2] = (((int) hex & 0xff) << 8);
		colors[3] = (((int) hex & 0xff));
		return colors;
	}

	public float getRed() {
		return r;
	}

	public float getBlue() {
		return b;
	}

	public float getGreen() {
		return g;
	}

	public float getAlpha() {
		return a;
	}

	public void setRed(float red) {
		r = red;

	}

	public void setBlue(float blue) {
		b = blue;

	}

	public void setGreen(float green) {
		g = green;

	}

	public void setAlpha(float alpha) {
		a = alpha;

	}

}
