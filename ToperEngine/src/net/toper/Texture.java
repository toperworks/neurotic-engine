package net.toper;

public class Texture {

	private int[] origPixelArray, pixelArray;
	private int width, height, origWidth, origHeight;
	private double[] rotData = new double[8];
	private float[] simRotData = new float[2];
	private double xScale, yScale;

	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
		createArray(width, height);
	}

	// Sets am integer at the index of the pixel array to the given data
	public void setData(int index, int data) {
		if (pixelArray != null && index < pixelArray.length) {
			pixelArray[index] = data;
		} else {
			System.out.println("Please create an array using createArray(w,h) before setting data");
		}
	}

	public void makeGrayscale() {
		for (int i = 0; i < pixelArray.length; i++) {
			int hex = pixelArray[i];
			pixelArray[i] = hex << 24 | hex << 16 | hex << 8 | hex;

		}
	}

	public void setData(int data[]) {
		if (pixelArray != null) {
			pixelArray = data;
		} else {
			System.out.println("Please create an array using createArray(w,h) before setting data");
		}
	}

	// Creates a separate array to hold the original information so that
	// transformations can be applied
	public void clampData() {
		origPixelArray = pixelArray;
	}

	// Creates the array of pixels that the color data is stored in
	public void createArray(int w, int h) {
		xScale = 1;
		yScale = 1;
		width = w;
		height = h;
		origWidth = w;
		origHeight = h;
		pixelArray = new int[w * h];
	}

	public void resetArray() {
		width = origWidth;
		height = origHeight;
		pixelArray = origPixelArray;
	}

	// Returns the width of the texture
	public int getWidth() {
		return width;
	}

	// Returns the height of the texture
	public int getHeight() {
		return height;
	}

	// Returns the width of the texture
	public int getScaledWidth() {
		return (int) (origWidth * xScale);
	}

	// Returns the height of the texture
	public int getScaledHeight() {
		return (int) (origHeight * yScale);
	}

	// Returns the color data at the given index
	public int getData(int index) {
		if (index >= 0 && index < pixelArray.length)
			return pixelArray[index];
		else
			return 0xff00ff00;
	}

	public int getData(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			if (x + y * width < pixelArray.length)
				return pixelArray[x + y * width];
			return 0;
		} else {
			return 0;
		}
	}

	public int getOrigData(int x, int y) {
		int xa = (int) (x / xScale);
		int ya = (int) (y / yScale);
		if (xa >= 0 && xa < origWidth && ya >= 0 && ya < height / yScale) {
			if (xa + ya * origWidth < origPixelArray.length)
				return origPixelArray[(int) (xa + ya * origWidth)];
			return 0;
		} else {
			return 0;
		}
	}

	// Returns the whole pixel color array
	public int[] getData() {
		return pixelArray;
	}

	// Returns the exact unedited pixel color array

	public int getOrigData(int index) {
		if (index >= 0 && index < origPixelArray.length)
			return origPixelArray[index];
		else
			return 0x00000000;
	}

	// Rotates a section of the texture around its center
	public void rotate(double angle, int nullHex) {
		int centerX = (width / 2);
		int centerY = (height / 2);
		rotateFromPoint(angle, centerX, centerY, nullHex);
	}

	// Rotates the whole texture around the center point, will only rotate when
	// one of
	// the variables change to increase performance
	public void simplexRotate(float angle, int nullHex) {
		if (simRotData[0] != angle || simRotData[1] != nullHex) {
			simRotData[0] = angle;
			simRotData[1] = nullHex;
			int centerX = (width / 2);
			int centerY = (height / 2);
			rotateFromPoint(angle, centerX, centerY, nullHex);
		}
	}

	// Rotates a portion of the texture around a given point, will only rotate
	// when one of
	// the variables change to increase performance
	public void rotateFromPoint(double angle, int rotPointX, int rotPointY, int nullHex) {
		if (rotData[0] != angle || rotData[1] != rotPointX || rotData[2] != rotPointY || rotData[3] != nullHex) {
			rotData[0] = angle;
			rotData[1] = rotPointX;
			rotData[2] = rotPointY;
			rotData[3] = nullHex;
			double radians = Math.toRadians(angle);
			double cos = Math.cos(radians);
			double sin = Math.sin(radians);
			int[] pixels2 = new int[pixelArray.length];
			int centerx = rotPointX;
			int centery = rotPointY;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int m = x - centerx;
					int n = y - centery;
					int j = (int) (((m * cos + n * sin) + centerx));
					int k = (int) (((n * cos - m * sin) + centery));
					int pix = (y * width + x);
					if (pix < pixels2.length && j >= 0 && j < width && k >= 0 && k < height) {
						pixels2[pix] = getData((int) (j), (int) (k));
					} else if (pix < pixels2.length) {
						pixels2[pix] = nullHex;
					}
				}
			}
			setData(pixels2);
		}
	}

	public void tint(int hex) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				blend(x, y, hex, (hex >> 24 & 0xff) / 255f);
			}
		}
	}

	public void blend(int x, int y, int newHex, double ratio) {
		if (ratio > 1f) {
			ratio = 1f;
		} else if (ratio < 0f) {
			ratio = 0f;
		}

		int oldHex = getData(x, y);

		if (getAlphaAtPoint(x, y) > 0) {

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

			setData(x + y * width, alpha << 24 | red << 16 | green << 8 | blue);
		}
	}

	public void setScale(float scale) {
		setScale(scale, scale);
	}

	public void setScale(float xScale, float yScale) {
		xScale = Math.abs(xScale);
		yScale = Math.abs(yScale);
		if ((xScale != 0 && xScale != this.xScale) || (yScale != 0 && yScale != this.yScale)) {
			this.xScale = xScale;
			this.yScale = yScale;
			width = (int) (origWidth * xScale);
			height = (int) (origHeight * yScale);
			int pixels[] = new int[(int) (width * height)];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int index = (x + y * (int) (width));
					if (index >= 0 && index < pixels.length)
						pixels[index] = getOrigData((int) (x), (int) (y));
				}
			}
			setData(pixels);
		}
	}

	public double getXScale() {
		return xScale;
	}

	public double getYScale() {
		return yScale;
	}

	public float getAlphaAtPoint(int x, int y) {
		return (getData(x, y) >> 24 & 0xff) / 255f;
	}

}
