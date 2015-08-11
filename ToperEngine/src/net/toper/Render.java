package net.toper;

public class Render {

	private static boolean defaultTrans, shouldTrans = false;

	public static void render(Frame f, Texture t, int xPos, int yPos, int xStart, int yStart, int xStop, int yStop, int excludeHex, int flip) {
		if (yStart <= 0) {
			yStart = 0;
		}
		if (xStart <= 0) {
			xStart = 0;
		}
		for (int y = yStart; y < yStop && y < t.getScaledHeight(); y++) {
			int yTex = y;
			int yScreen = (int) (y + yPos);
			if (yScreen >= 0 && yScreen <= f.getScaledHeight())
				for (int x = xStart; x < xStop && x < t.getScaledWidth(); x++) {
					int xTex = x;
					if (t.getAlphaAtPoint(xTex, yTex) > (int) 0) {
						if (flip == 1 || flip == 3)
							xTex = (xStop - x) + xStart - 1;
						if (flip == 2 || flip == 3)
							yTex = (yStop - y) + yStart;
						int xScreen = (int) (x + xPos);
						if (xScreen >= 0 && xScreen <= f.getScaledWidth()) {
							int hex = t.getData(xTex + yTex * t.getScaledWidth());
							if (hex != excludeHex) {
								setPixel(f, xScreen, yScreen, hex);
							}
						}
					}
				}
		}
	}

	public static boolean isRenderingTransparency() {
		return shouldTrans;
	}

	public static void enableTransparency(boolean shouldRender) {
		defaultTrans = shouldRender;
	}

	public static void shouldRenderTransparency(boolean bool) {
		shouldTrans = bool;
	}

	public static void simpleRender(Frame f, Texture t, int xPos, int yPos, int excludeHex, int flip) {
		render(f, t, xPos, yPos, 0, 0, t.getWidth(), t.getHeight(), excludeHex, flip);
	}

	public static void simpleRender(Frame f, Texture t, int xPos, int yPos) {
		render(f, t, xPos, yPos, 0, 0, t.getWidth(), t.getHeight(), -1, 0);
	}

	public static int blendF(int oldHex, int newHex, double ratio) {
		if (ratio > 1f) {
			ratio = 1f;
		} else if (ratio < 0f) {
			ratio = 0f;
		}
		double invRatio = 1.0f - ratio;

		int a1 = (oldHex >> 24 & 0xff);
		int r1 = ((oldHex & 0xff0000) >> 16);
		int g1 = ((oldHex & 0xff00) >> 8);
		int b1 = (oldHex & 0xff);

		int a2 = (newHex >> 24 & 0xff);
		int r2 = ((newHex & 0xff0000) >> 16);
		int g2 = ((newHex & 0xff00) >> 8);
		int b2 = (newHex & 0xff);

		int alpha = (int) ((a1 * invRatio) + (a2 * ratio));
		int red = (int) ((r1 * invRatio) + (r2 * ratio));
		int green = (int) ((g1 * invRatio) + (g2 * ratio));
		int blue = (int) ((b1 * invRatio) + (b2 * ratio));

		return alpha << 24 | red << 16 | green << 8 | blue;
	}

	public static int blend(int top, int bottom) {
		float amt = (top >> 24 & 0xff) / 256f;
		return blendF(bottom, top, amt);

	}

	// Fills a rectangle, x and y are the top-left corner
	public static void fillRect(Frame f, int xPos, int yPos, int width, int height, int color) {
		for (int y = yPos; y < yPos + height; y++) {
			for (int x = xPos; x < xPos + width; x++) {
				setPixel(f, x, y, color);
			}
		}
	}

	// Draws the outline of a rectangle, x and y are the top-left corner
	public static void drawRect(Frame f, int xPos, int yPos, int width, int height, int color) {
		for (int y = yPos; y < yPos + height; y++) {
			setPixel(f, xPos, y, color);
		}
		for (int y = yPos; y < yPos + height; y++) {
			setPixel(f, xPos + width, y, color);
		}
		for (int x = xPos; x < xPos + width; x++) {
			setPixel(f, x, yPos, color);
		}
		for (int x = xPos; x < xPos + width; x++) {
			setPixel(f, x, yPos + height, color);
		}
	}

	// Draws an outline of a circle, x and y are the center, r is the radius
	public static void drawCircle(Frame f, int xPos, int yPos, int radius, int color) {
		for (double i = 0; i < 360; i += 0.05) {
			double angle = i;
			int y = (int) (radius * Math.sin(angle * Math.PI / 180)) + yPos;
			int x = (int) (radius * Math.cos(angle * Math.PI / 180)) + xPos;
			setPixel(f, x, y, color);
		}
	}

	// Fills a circular area, x and y are the center, and r is the radius
	public static void fillCircle(Frame f, int xPos, int yPos, int r, int color) {
		for (int i = 0; i < r * 2; i++)
			for (int j = 0; j < r * 2; j++) {
				int d = (int) Math.sqrt((i - r) * (i - r) + (j - r) * (j - r));
				if (d < r && i >= 0 && j >= 0) {
					setPixel(f, i + xPos - r, j + yPos - r, color);
				}
			}
	}

	public static void drawLine(Frame f, int xStart, int yStart, int xStop, int yStop, int color) {
		int w = xStop - xStart;
		int h = yStop - yStart;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			setPixel(f, xStart, yStart, color);
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				xStart += dx1;
				yStart += dy1;
			} else {
				xStart += dx2;
				yStart += dy2;
			}
		}
	}

	public static void setPixel(Frame f, int x, int y, int color) {
		int pixel = x + y * f.getScaledWidth();
		if (x >= 0 && y >= 0 && y < f.getScaledHeight() && x < f.getScaledWidth() && pixel < f.pixelArray().length)
			if (defaultTrans) {
				if (shouldTrans)
					f.pixelArray()[pixel] = blend(color, getPixel(f, x, y));
				else
					f.pixelArray()[pixel] = color;
			} else {
				f.pixelArray()[pixel] = color;
			}
	}

	public static void setPixel(Frame f, int pixel, int color) {
		if (pixel >= 0 && pixel < f.pixelArray().length)
			if (defaultTrans) {
				if (shouldTrans)
					f.pixelArray()[pixel] = blend(color, f.pixelArray()[pixel]);
			} else {
				f.pixelArray()[pixel] = color;
			}
	}

	public static int getPixel(Frame f, int x, int y) {
		int pixel = x + y * f.getScaledWidth();
		if (x >= 0 && y >= 0 && y < f.getScaledHeight() && x < f.getScaledWidth() && pixel < f.pixelArray().length)
			return f.pixelArray()[pixel];
		else
			return 0;
	}

}