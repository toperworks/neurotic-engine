package net.toper.shape;

import java.util.Collections;

public class Polygon {
	public int x, y, width, height, origWidth, origHeight, origX, origY;
	public int colorHex;
	private Vector[] points = new Vector[0];
	private Vector center;

	public Polygon() {
	}

	public Polygon(Vector v) {
		addVertex(v);
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
			double x = points[i].getX();
			double y = points[i].getY();
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
			center = new Vector((origX + (origWidth / 2)), (origY + (origHeight / 2)));

		}
		// generateContainsArray();
	}

	public boolean contains(Vector test) {
		int i;
		int j;
		boolean result = false;
		for (i = 0, j = points.length - 1; i < points.length; j = i++) {
			if ((points[i].getY() > test.getY()) != (points[j].getY() > test.getY())
					&& (test.getX() < (points[j].getX() - points[i].getX()) * (test.getY() - points[i].getY())
							/ (points[j].getY() - points[i].getY()) + points[i].getX())) {
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
		return contains(new Vector(x, y));
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

	private double angle;

	public void rotateAroundCenter(double changeAngle) {
		if (this.angle != changeAngle) {
			rotateAround(center.getX(), center.getY(), changeAngle);
			this.angle = changeAngle;
		}
	}

	public void rotateAround(float x, float y, double changeAngle) {
		if (this.angle != changeAngle) {
			double angle = changeAngle - this.angle;
			angle = Math.toRadians(angle);
			double sin = Math.sin(angle);
			double cos = Math.cos(angle);
			for (int i = 0; i < points.length; i++) {
				points[i].move(-x, -y);

				double newX = points[i].getX() * cos - points[i].getY() * sin;
				double newY = points[i].getX() * sin + points[i].getY() * cos;

				float tempX = (float) (newX + x);
				float tempY = (float) (newY + y);
				changeVertex(i, new Vector(tempX, tempY));
			}
			checkDimensions(false);
			this.angle = changeAngle;
		}
	}

	public String toString() {
		return "Point { X = " + x + ", Y = " + y + " }";
	}

	public int getCenterX() {
		return (int) center.getX();
	}

	public int getCenterY() {
		return (int) center.getY();
	}

	public double getRotAngle() {
		return angle;
	}

	public void move(float amt) {
		for (int i = 0; i < points.length; i++) {
			points[i].move(amt, amt);
		}
	}

	public Polygon intersect(Polygon p1, Polygon p2) {
		Polygon p = new Polygon();
		// for (int i = 0; i < p1.getNumVerticies(); i++) {
		for (int i2 = 0; i2 < p2.getNumVerticies() - 1; i2++) {
			double x1 = p1.points[i2 + 1].getX();
			double x2 = p2.points[i2 + 1].getX();
			double y1 = p1.points[i2 + 1].getY();
			double y2 = p2.points[i2 + 1].getY();
			double x3 = p1.points[i2].getX();
			double x4 = p2.points[i2].getX();
			double y3 = p1.points[i2].getY();
			double y4 = p2.points[i2].getY();
			double d = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
			if (d != 0) {
				double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
				double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
				Vector v = new Vector(xi, yi);
				p.addVertex(v);
				// }
			}

		}
		return p;
	}

	int orientation(Vector p, Vector q, Vector r) {
		double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
		if (val == 0)
			return 0; // colinear
		return (val > 0) ? 1 : 2; // clock or counterclock wise
	}

	boolean doIntersect(Vector p1, Vector q1, Vector p2, Vector q2) {
		// Find the four orientations needed for general and
		// special cases
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		// General case
		if (o1 != o2 && o3 != o4)
			return true;

		// Special Cases
		// p1, q1 and p2 are colinear and p2 lies on segment p1q1
		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;

		// p1, q1 and p2 are colinear and q2 lies on segment p1q1
		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;

		// p2, q2 and p1 are colinear and p1 lies on segment p2q2
		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;

		// p2, q2 and q1 are colinear and q1 lies on segment p2q2
		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false; // Doesn't fall in any of the above cases
	}

	boolean onSegment(Vector p, Vector q, Vector r) {
		if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
				&& q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
			return true;
		return false;
	}

	public boolean containsVertex(Vector v) {
		for (int i = 0; i < points.length; i++) {
			return (v.getX() != points[i].getX() && v.getY() != points[i].getY());
		}
		return false;
	}

	public Polygon isInside(Polygon poly, Polygon poly2) {
		Polygon temp = new Polygon();
		// There must be at least 3 vertices in polygon[]
		if (poly.getNumVerticies() >= 3 && poly2.getNumVerticies() >= 3) {

			// Create a point for line segment from p to infinite
			// Count intersections of the above line with sides of polygon
			for (int i = 0; i < poly.getNumVerticies(); i++) {
				int next = (i + 1) % poly.getNumVerticies();
				for (int i2 = 0; i2 < poly2.getNumVerticies(); i2++) {
					int next2 = Math.abs((i2 + 1) % poly2.getNumVerticies());

					if (doIntersect(poly.points[i], poly.points[next], poly2.points[i2], poly2.points[next2])) {
						Vector v = intersection(poly.points[i].getX(), poly.points[i].getY(), poly.points[next].getX(),
								poly.points[next].getY(), poly2.points[i2].getX(), poly2.points[i2].getY(),
								poly2.points[next2].getX(), poly2.points[next2].getY());
						if (v != null && !temp.contains(v))
							temp.addVertex(v);
					}
				}
			}
			sort(temp);
		}

		return temp;
	}

	public void sort(Polygon p) {
		Vector[] temp = p.points;
		double[] distances = new double[p.points.length];
		for (int i = 0; i < temp.length; i++) {
			distances[i] = Math.atan2(temp[i].getY() - p.center.getY(), temp[i].getX() - p.center.getX());
		}
		for (int i = 0; i < temp.length; i++) {
			for (int d = 0; d < distances.length; d++) {
				if (distances[i] < distances[d]) {
					Vector tempV = temp[i];
					temp[i] = temp[d];
					temp[d] = tempV;
				}
			}
		}
		p.points = temp;
	}

	public Vector intersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float t = ((y4 - y3) * (x3 - x1) - (y3 - y1) * (x4 - x3)) / ((y4 - y3) * (x2 - x1) - (y2 - y1) * (x4 - x3));
		float x = x1 + ((x2 - x1) * t);
		float y = y1 + ((y2 - y1) * t);
		return new Vector(x, y);
	}

}