package net.toper;

public class Render {

	private static boolean defaultTrans, shouldTrans = false;

	// The main render method for everything, this is what puts things on the
	// screen.
	//
	// T is the texture that gets drawn on the screen
	//
	// xPos and yPos are the top left coordinates of where the texture will be
	// drawn
	//
	// xStart and yStart change where the render method starts scanning the
	// texture, essentially offsets for start points
	//
	// xStop and yStop change where the method stops scanning the texture
	//
	// Exclude hex is the color that doesn't get rendered, that one color
	// becomes transparent essentially
	//
	// Flip changes how the image is rendered, it will flip the image
	// 0 is normal, 1 is flipped around the y axis, 2 is flipped around the x
	// axis, 3 is both
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

	// Returns the status of transparency rendering
	public static boolean isRenderingTransparency() {
		return shouldTrans;
	}

	// Enables all transparency for everything
	public static void enableTransparency(boolean shouldRender) {
		defaultTrans = shouldRender;
	}

	// Enables or disables transparency after global transparency options are
	// set
	// Useful to enable transparency for just one render call and then disable
	// it
	public static void shouldRenderTransparency(boolean bool) {
		shouldTrans = bool;
	}

	// Simplest way to render a texture, draws full texture (t) at xPos and yPos
	// on the screen.
	// Still a simple method, just a few more options on how it should be
	// rendered
	//
	// Exclude hex gets removed from the texture when it renders, becomes
	// transparent
	//
	// Flip flips the image, 0 is normal, 1 is flipped around the y axis, 2 is
	// flipped around the x axis, 3 is both
	public static void simpleRender(Frame f, Texture t, int xPos, int yPos, int excludeHex, int flip) {
		render(f, t, xPos, yPos, 0, 0, t.getWidth(), t.getHeight(), excludeHex, flip);
	}

	// Simplest way to render a texture, draws full texture (t) at xPos and yPos
	// on the screen.
	public static void simpleRender(Frame f, Texture t, int xPos, int yPos) {
		render(f, t, xPos, yPos, 0, 0, t.getWidth(), t.getHeight(), -9, 0);
	}

	// Blends two hexadecimal colors, hex1 and hex2, together, and the amount of
	// each in the final result is determined by the float ratio. Ex: 0.6f = 40%
	// hex1, 60% hex2
	public static int blendF(int hex1, int hex2, double ratio) {
		if (ratio > 1f) {
			ratio = 1f;
		} else if (ratio < 0f) {
			ratio = 0f;
		}
		double invRatio = 1.0f - ratio;

		int a1 = (hex1 >> 24 & 0xff);
		int r1 = ((hex1 & 0xff0000) >> 16);
		int g1 = ((hex1 & 0xff00) >> 8);
		int b1 = (hex1 & 0xff);

		int a2 = (hex2 >> 24 & 0xff);
		int r2 = ((hex2 & 0xff0000) >> 16);
		int g2 = ((hex2 & 0xff00) >> 8);
		int b2 = (hex2 & 0xff);

		int alpha = (int) ((a1 * invRatio) + (a2 * ratio));
		int red = (int) ((r1 * invRatio) + (r2 * ratio));
		int green = (int) ((g1 * invRatio) + (g2 * ratio));
		int blue = (int) ((b1 * invRatio) + (b2 * ratio));

		return alpha << 24 | red << 16 | green << 8 | blue;
	}

	// Blends two hexadecimal colors, hex1 and hex2, together and returns one
	// value
	public static int blend(int hex1, int hex2) {
		float amt = (hex1 >> 24 & 0xff) / 256f;
		return blendF(hex2, hex1, amt);

	}

	// Fills a rectangle, x and y are the top-left corner
	public static void fillRect(Frame f, int xPos, int yPos, int width, int height, int color) {
		for (int y = yPos; y < yPos + height; y++) {
			for (int x = xPos; x < xPos + width; x++) {
				if (y >= 0 && y <= f.getScaledHeight() && x >= 0 && x <= f.getScaledWidth())
					setPixel(f, x, y, color);
			}
		}
	}

	// Draws the outline of a rectangle, x and y are the top-left corner
	public static void drawRect(Frame f, int xPos, int yPos, int width, int height, int color) {
		for (int y = yPos; y < yPos + height; y++) {
			if (y >= 0 && y <= f.getScaledHeight())
				setPixel(f, xPos, y, color);
		}
		for (int y = yPos; y < yPos + height; y++) {
			if (y >= 0 && y <= f.getScaledHeight())
				setPixel(f, xPos + width, y, color);
		}
		for (int x = xPos; x < xPos + width; x++) {
			if (x >= 0 && x <= f.getScaledWidth())
				setPixel(f, x, yPos, color);
		}
		for (int x = xPos; x < xPos + width; x++) {
			if (x >= 0 && x <= f.getScaledWidth())
				setPixel(f, x, yPos + height, color);
		}
	}

	// Draws an outline of a circle, x and y are the center, r is the radius
	public static void drawCircle(Frame f, int xPos, int yPos, int radius, int color) {
		for (float y = -radius; y <= radius; y += 0.5) {
			int ya = (int) (yPos + y + y);
			if (ya >= 0 && ya <= f.getScaledHeight()) {
				for (float x = -radius; x <= radius; x += 0.5) {
					int xa = (int) (xPos + x + x);
					if (xa >= 0 && xa <= f.getScaledWidth()) {
						if (x * x + y * y < radius * radius + radius * 0.8f && x * x + y * y > (radius * radius + radius * 0.8f) - radius) {
							setPixel(f, xa, ya, color);
						}
					}
				}
			}
		}
	}

	// Fills a circular area, x and y are the center, and r is the radius
	public static void fillCircle(Frame f, int xPos, int yPos, int radius, int color) {
		for (float y = -radius; y <= radius; y += 0.5) {
			int ya = (int) (yPos + y + y);
			if (ya >= 0 && ya <= f.getScaledHeight()) {
				for (float x = -radius; x <= radius; x += 0.5) {
					int xa = (int) (xPos + x + x);
					if (xa >= 0 && xa <= f.getScaledWidth()) {
						if (x * x + y * y <= radius * radius + radius * 0.8f) {
							setPixel(f, xa, ya, color);
						}
					}
				}
			}
		}
	}

	// Draws a line from the x and y start points to the x and y stop points,
	// color is the color of the line
	public static void drawLine(Frame f, int xStart, int yStart, int xStop, int yStop, int color) {
		int width = xStop - xStart;
		int height = yStop - yStart;
		int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		if (width < 0) {
			x1 = -1;
		} else if (width > 0) {
			x1 = 1;
		}
		if (height < 0) {
			y1 = -1;
		} else if (height > 0) {
			y1 = 1;
		}
		if (width < 0) {
			x2 = -1;
		} else if (width > 0) {
			x2 = 1;
		}
		int longest = Math.abs(width);
		int shortest = Math.abs(height);
		if (!(longest > shortest)) {
			longest = Math.abs(height);
			shortest = Math.abs(width);
			if (height < 0)
				y2 = -1;
			else if (height > 0)
				y2 = 1;
			x2 = 0;
		}
		int num = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			if (yStart >= 0 && yStart <= f.getScaledHeight() && xStart >= 0 && xStart <= f.getScaledWidth())
				setPixel(f, xStart, yStart, color);
			num += shortest;
			if (!(num < longest)) {
				num -= longest;
				xStart += x1;
				yStart += y1;
			} else {
				xStart += x2;
				yStart += y2;
			}
		}
	}

	// Sets one individual pixel on a screen at x and y to the given hexadecimal
	// color
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

	// Sets one individual pixel on the screen at x+y*width to the given
	// hexadecimal color
	public static void setPixel(Frame f, int pixel, int color) {
		if (pixel >= 0 && pixel < f.pixelArray().length)
			if (defaultTrans) {
				if (shouldTrans)
					f.pixelArray()[pixel] = blend(color, f.pixelArray()[pixel]);
			} else {
				f.pixelArray()[pixel] = color;
			}
	}

	// Returns the hexadecimal color of a given pixel
	public static int getPixel(Frame f, int x, int y) {
		int pixel = x + y * f.getScaledWidth();
		if (x >= 0 && y >= 0 && y < f.getScaledHeight() && x < f.getScaledWidth() && pixel < f.pixelArray().length)
			return f.pixelArray()[pixel];
		else
			return 0;
	}

}