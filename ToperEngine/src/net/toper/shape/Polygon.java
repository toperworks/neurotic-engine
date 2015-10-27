package net.toper.shape;

public class Polygon {
	public int x, y, width, height;
	public int colorHex;
	private Vector[] points = new Vector[0];
	private int[] contains;

	public Polygon() {
	}

	public void addVertex(Vector v) {
		Vector[] temp = new Vector[points.length + 1];
		System.arraycopy(points, 0, temp, 0, points.length);
		temp[points.length] = v;
		points = temp;
		checkDimensions();
	}

	public void setColor(int colorHex) {
		this.colorHex = colorHex;
	}

	public int getColor() {
		return colorHex;
	}

	private void checkDimensions() {
		int x = 0, y = 0;
		for (int i = 0; i < points.length; i++) {
			if (i == 0) {
				x = (int) points[i].x;
				y = (int) points[i].y;
			} else {
				if (points[i].x < x) {
					x = (int) points[i].x;
				}
				if (points[i].y < y) {
					y = (int) points[i].y;
				}
			}
		}
		this.x = x;
		this.y = y;
		int width = 0, height = 0;
		for (int i = 0; i < points.length; i++) {
			if (i == 0) {
				width = (int) points[i].x - this.x;
				height = (int) points[i].y - this.y;
			} else {
				if (points[i].x > this.x + width) {
					width = (int) points[i].x - this.x;
				}
				if (points[i].y > this.y + height) {
					height = (int) points[i].y - this.y;
				}
			}
		}
		this.width = width;
		this.height = height;

		contains = new int[(x + width) * (y + height)];

		for (int ya = y; ya < y + height; ya++) {
			for (int xa = x; xa < x + width; xa++) {
				boolean inside = false;
				for (int i = 0, a = points.length - 1; i < points.length; a = i++) {
					if ((points[i].y > ya) != (points[a].y > ya)
							&& (xa < (points[a].x - points[i].x) * (ya - points[i].y) / (points[a].y - points[i].y)
									+ points[i].x)) {
						inside = !inside;
					}
				}
				if (inside)
					contains[xa + ya * width] = 1;
			}
		}
	}

	public boolean pointInPolygon(int x, int y) {
		int index = x + y * width;
		if (index >= 0 && index < contains.length)
			return contains[index] == 1;
		else
			return false;
	}
}