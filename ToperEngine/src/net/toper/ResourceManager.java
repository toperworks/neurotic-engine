package net.toper;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ResourceManager {

	private static boolean logging = false;
	private static ArrayList<String> loadedCache = new ArrayList<String>();
	private static ArrayList<Texture> textureCache = new ArrayList<Texture>();

	public static void enableLogging(boolean log) {
		logging = log;
	}

	public static Texture loadTexture(String path, boolean cache) {
		boolean exists = false;
		if (cache)
			for (int i = 0; i < loadedCache.size(); i++) {
				if (loadedCache.get(i).contains(path)) {
					exists = true;
					String temp = loadedCache.get(i);
					String split[] = temp.split(":");
					if (logging)
						System.out.println("Resource cached before, loaded, completed for " + path);
					return textureCache.get(Integer.parseInt(split[1]));
				}
			}
		if (!exists) {
			try {
				BufferedImage temp = ImageIO.read(ResourceManager.class.getResourceAsStream(path));
				Texture tempTex = new Texture(temp.getWidth(), temp.getHeight());
				tempTex.setData(convert(temp));
				tempTex.clampData();
				if (!cache) {
					if (logging)
						System.out.println("Resource load completed for " + path);
				} else {
					if (logging)
						System.out.println("Resource load completed for " + path + ". Cached in memory");
					textureCache.add(tempTex);
					loadedCache.add(path + ":" + (textureCache.size() - 1));
				}
				return tempTex;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Resource load failed for " + path);
			}
		}
		return null;
	}

	public static Texture getTexFromSheet(Texture sheet, int startX, int startY, int width, int height) {
		Texture tempTex = new Texture(width, height);
		for (int y = 0; y < height && y + startY < sheet.getHeight(); y++) {
			for (int x = 0; x < width && x + startX < sheet.getWidth(); x++) {
				tempTex.setData(x + y * width, sheet.getData(x + startX, y + startY));
			}
		}
		tempTex.clampData();
		return tempTex;
	}

	public static void dump() {
		loadedCache.clear();
		textureCache.clear();
		if (logging)
			System.out.println("Cache Cleared");
	}

	public static Texture loadTexture(BufferedImage temp) {
		Texture tempTex = new Texture(temp.getWidth(), temp.getHeight());
		for (int y = 0; y < tempTex.getHeight(); y++) {
			for (int x = 0; x < tempTex.getWidth(); x++) {
				tempTex.setData(x + y * temp.getWidth(), temp.getRGB(x, y));
			}
		}
		tempTex.clampData();
		return tempTex;
	}

	public static Texture loadTexture(BufferedImage temp, int startX, int startY, int stopX, int stopY) {
		Texture tempTex = new Texture(temp.getWidth(), temp.getHeight());
		for (int y = startY; y < stopY && y < tempTex.getHeight(); y++) {
			for (int x = startX; x < stopX && x < tempTex.getWidth(); x++) {
				tempTex.setData(x + y * temp.getWidth(), temp.getRGB(x, y));
			}
		}
		tempTex.clampData();
		return tempTex;
	}

	private static int[] convert(BufferedImage image) {
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[] result = new int[height * width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red

				if (col + row < result.length)
					result[col + row] = argb;
				if (col == width) {
					col = 0;
					row += width;
				}
				col++;
			}

		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				if (col + row < result.length)
					result[col + row] = argb;
				if (col == width) {
					col = 0;
					row += width;
				}
				col++;
			}

		}
		return result;
	}
}
