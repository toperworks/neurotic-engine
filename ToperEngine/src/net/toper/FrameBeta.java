package net.toper;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class FrameBeta extends Frame {

	public static final String ENGINE_VERSION = "Neurotic Engine Version 0.6b6 | Build Date 8/11/15";

	private JFrame f;
	private Canvas c;
	private Input i;

	protected int width = 0, height = 0, scale;
	protected String title;
	protected BufferedImage screen;
	private int[] pixelArray;
	private Graphics g;
	private BufferStrategy bs;

	private boolean shouldFrameSleep = false;
	private int sleepAmt;

	boolean shouldRender = false;
	int[] oldPixels;
	private static float delta = 0;
	private long lastTime = System.nanoTime();
	private double nsPerTick = 1E9 / 60;
	private long now;
	private int frames = 0;
	private int fps = 0;
	private long timer = System.currentTimeMillis();

	List<Integer[][][][]> oldChanges = new ArrayList<Integer[][][][]>();
	List<Integer[][][][]> changes = new ArrayList<Integer[][][][]>();

	public FrameBeta() {
		if (scale <= 0)
			scale = 1;
	}

	// Creates a frame with the given width, height, and title, scale is for
	// up-scaling
	public FrameBeta(int width, int height, String title, int scale) {
		setDimensions(width, height);
		this.title = title;
		if (scale <= 0)
			scale = 1;
		this.scale = scale;
	}

	// Changes the upscaling
	public void changeScale(int scale) {
		if (scale <= 0)
			scale = 1;
		if (this.scale != scale) {
			this.scale = scale;
			screen = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
			pixelArray = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
		}
	}

	public void hideCursor() {
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		f.getContentPane().setCursor(blankCursor);
	}

	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	// Opens the window
	public void open() {
		System.out.println(ENGINE_VERSION);
		if (width == 0 || height == 0) {
			System.out.println("Cannot create frame, use setDimensions(width,height) to set dimensions. Cannot be zero.");
			return;
		}
		screen = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
		pixelArray = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
		f = new JFrame();
		c = new Canvas();
		i = new Input(this);
		oldPixels = new int[width * height];
		f.setSize(width, height);
		f.setTitle(title);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(c);
		c.addKeyListener(i);
		c.addMouseMotionListener(i);
		c.addMouseListener(i);
		f.setResizable(false);
		f.setVisible(true);
		c.requestFocus();

		c.createBufferStrategy(3);
	}

	// Fills the array with the given hex to 'clear' it
	public void clear(int hex) {
		for (int i = 0; i < pixelArray.length; i++) {
			pixelArray[i] = hex;
		}
	}

	// Draws image to the buffer, call endFrame() to show it
	private void update() {
		bs = c.getBufferStrategy();
		g = bs.getDrawGraphics();
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
	}

	// Shows the completed frame on the screen
	public void endFrame() {
		update();
		bs.show();
		g.dispose();
		frames++;

		if (System.currentTimeMillis() - timer >= 1000) {
			timer += 1000;
			fps = frames;
			frames = 0;
		}
		if (shouldFrameSleep) {
			try {
				Thread.sleep(sleepAmt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		now = System.nanoTime();
		delta = (float) ((now - lastTime) / nsPerTick);
		lastTime = now;
	}

	// Sets the amount the CPU should wait to catch up, helps with CPU usage
	// greatly. Set to 0 to disengage again
	public void setCPUSleep(int num) {
		if (num > 0) {
			shouldFrameSleep = true;
			sleepAmt = num;
		} else {
			shouldFrameSleep = false;
			sleepAmt = num;
		}
	}

	// Returns the width after scaling
	public int getScaledWidth() {
		return getWidth() / scale;
	}

	// Returns the height after scaling
	public int getScaledHeight() {
		return getHeight() / scale;
	}

	// Returns the actual width of the window
	public int getWidth() {
		return width;
	}

	// Returns the actual height of the window
	public int getHeight() {
		return height;
	}

	// Appends a suffix to the title
	public void addToTitle(String string) {
		f.setTitle(title + string);
	}

	// Sets title equal to newTitle
	public void setTitle(String newTitle) {
		title = newTitle;
		f.setTitle(title);
	}

	// Returns the time between frames, use when animating
	public float getDelta() {
		float i = delta;
		return i;
	}

	// Returns Frames Per Second
	public int getFPS() {
		return fps;
	}

	public int[] pixelArray() {
		return pixelArray;
	}

}
