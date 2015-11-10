package net.toper.shape;

public class Polygon {
	public int x, y, width, height, origWidth, origHeight, origX, origY;
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
		checkDimensions(true);
	}

	public void setColor(int colorHex) {
		this.colorHex = colorHex;
	}

	public int getColor() {
		return colorHex;
	}

	private void checkDimensions(boolean add) {
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		for (int i = 0; i < points.length; i++) {
			double x = points[i].x;
			double y = points[i].y;
			minX = Math.min(minX, x);
			maxX = Math.max(maxX, x);
			minY = Math.min(minY, y);
			maxY = Math.max(maxY, y);
		}
		x = (int) minX;
		y = (int) minY;
		width = (int) (maxX - minX);
		height = (int) (maxY - minY);
		if (add) {
			origX = x;
			origY = y;
			origWidth = width;
			origHeight = height;
		}
		generateContainsArray();
	}

	private void generateContainsArray() {
		contains = new int[(Math.abs(x) + Math.abs(width)) * (Math.abs(y) + Math.abs(height))];
		for (int ya = 0; ya < height; ya++) {
			for (int xa = 0; xa < width; xa++) {
				int xi = xa + x;
				int yi = ya + y;
				int index = xi + yi * width;
				if (index >= 0 && index < contains.length)
					if (contains(new Vector(xi, yi))) {
						contains[index] = 1;
					}
			}
		}
	}

	public boolean contains(Vector test) {
		int i;
		int j;
		boolean result = false;
		for (i = 0, j = points.length - 1; i < points.length; j = i++) {
			if ((points[i].y > test.y) != (points[j].y > test.y)
					&& (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y)
							+ points[i].x)) {
				result = !result;
			}
		}
		return result;
	}

	public void changeVertex(int point, Vector newVector) {
		if (point >= 0 && point < points.length) {
			points[point] = newVector;
		}
		checkDimensions(false);
	}

	public boolean pointInPolygon(int x, int y) {
		int index = x + y * width;
		if (index >= 0 && index < contains.length)
			return contains[index] == 1;
		else
			return false;
	}

	public int getNumVerticies() {
		return points.length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void rotateAround(double angle) {
		Vector center = new Vector((origX + (origWidth / 2)), (origY + (origHeight / 2)));
		angle = Math.toRadians(angle);
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		for (int i = 0; i < points.length; i++) {
			points[i].x -= center.x;
			points[i].y -= center.y;

			double newX = points[i].x * cos - points[i].y * sin;
			double newY = points[i].x * sin + points[i].y * cos;

			float tempX = (float) (newX + center.x);
			float tempY = (float) (newY + center.y);
			changeVertex(i, new Vector(tempX, tempY));
		}
		checkDimensions(false);
	}

	public String toString() {
		return "Point { X = " + x + ", Y = " + y + " }";
	}
}