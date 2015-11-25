package net.render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private final static String TITLE = "Neurotic";
	private final static int WIDTH = 900;
	private final static int HEIGHT = WIDTH / 16 * 9;
	static long lastFrame;

	/** frames per second */
	static int fps;
	private static float delta;
	/** last fps time */
	static long lastFPS;

	public static void createDisplay() {
		lastFPS = getTime();
		ContextAttribs att = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), att);
			Display.setTitle(TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	public static void updateDisplay() {
		updateFPS();
		Display.update();
		long time = getTime();
		delta = (int) (time - lastFrame) / 1000f;
		lastFrame = time;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	public static float getDelta() {
		return delta;
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add one second
		}
		fps++;
	}

}
