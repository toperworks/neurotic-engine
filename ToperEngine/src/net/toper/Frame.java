package net.toper;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

public class Frame {

	public static final String ENGINE_VERSION = "Neurotic Engine Version 0.6b6 | Build Date 8/11/15";

	private JFrame f;
	private Canvas c;
	private Input i;

	protected int width = 0, height = 0, scale;
	protected String title;
	protected BufferedImage screen;
	private int[] pixelArray, screenPixels;
	private Graphics2D g;
	private BufferStrategy bs;

	private boolean shouldFrameSleep = false;
	private int sleepAmt;

	boolean shouldRender = false;
	private static float delta = 0;
	private long lastTime = System.nanoTime();
	private double nsPerTick = 1E9 / 60;
	private long now;
	private int frames = 0;
	private int fps = 0;
	private long timer = System.currentTimeMillis();

	private int clearHex;
	boolean showFrameUpdates = false;
	List<List<Integer>> previousChanges = new ArrayList<>();
	List<List<Integer>> currentChanges = new ArrayList<>();

	public Frame() {
		if (scale <= 0)
			scale = 1;
	}

	// Creates a frame with the given width, height, and title, scale is for
	// up-scaling
	public Frame(int width, int height, String title, int scale) {
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
			screenPixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
			pixelArray = screenPixels;
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

	public void showFrameUpdates(boolean show) {
		showFrameUpdates = show;
	}

	// Opens the window
	public void open() {
		System.out.println(ENGINE_VERSION);
		if (width == 0 || height == 0) {
			System.out.println("Cannot create frame, use setDimensions(width,height) to set dimensions. Cannot be zero.");
			return;
		}
		screen = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
		screenPixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();

		pixelArray = new int[getScaledWidth() * getScaledHeight()];
		f = new JFrame();
		c = new Canvas();
		i = new Input(this);
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
		bs = c.getBufferStrategy();
		g = (Graphics2D) bs.getDrawGraphics();
		flagChange(0, 0, width, height);
	}

	// Fills the array with the given hex to 'clear' it
	public void clear(int hex) {
		clearHex = hex;
		for (int i = 0; i < previousChanges.size(); i++) {
			List<Integer> numbers = previousChanges.get(i);
			int xStart = numbers.get(0);
			int yStart = numbers.get(1);
			int xStop = numbers.get(2);
			int yStop = numbers.get(3);
			for (int y = yStart; y <= yStop; y++) {
				if (y >= 0 && y < getScaledHeight()) {
					for (int x = xStart; x <= xStop; x++) {
						if (x >= 0 && x < getScaledWidth()) {
							pixelArray[x + y * width] = clearHex;
							screenPixels[x + y * width] = clearHex;
						}
					}
				}
			}
		}
	}

	private boolean equalLists(List<Integer> one, List<Integer> two) {
		if (one == null && two == null) {
			return true;
		}

		if ((one == null && two != null) || one != null && two == null || one.size() != two.size()) {
			return false;
		}

		// to avoid messing the order of the lists we will use a copy
		// as noted in comments by A. R. S.
		one = new ArrayList<Integer>(one);
		two = new ArrayList<Integer>(two);

		Collections.sort(one);
		Collections.sort(two);
		return one.equals(two);
	}

	// Draws image to the buffer, call endFrame() to show it
	private void update() {
		previousChanges.clear();
		for (int i = 0; i < currentChanges.size(); i++) {
			List<Integer> data = (List<Integer>) currentChanges.get(i);
			int xStart = data.get(0);
			int yStart = data.get(1);
			int xStop = data.get(2);
			int yStop = data.get(3);
			for (int y = yStart; y <= yStop; y++) {
				if (y >= 0 && y < getScaledHeight()) {
					for (int x = xStart; x <= xStop; x++) {
						if (x >= 0 && x < getScaledWidth()) {
							int pixel = pixelArray[x + y * getScaledWidth()];
							float amt = (pixel >> 24 & 0xff) / 255f;
							if (amt > (0x00000000 >> 24 & 0xff) / 255f) {
								if (showFrameUpdates) {
									pixel += 0xff666666;
								}
								screenPixels[x + y * getScaledWidth()] = pixel;
							}
						}
					}
				}
			}
			previousChanges.add(data);
		}
		bs = c.getBufferStrategy();
		g = (Graphics2D) bs.getDrawGraphics();
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
		currentChanges.clear();
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

	public void flagChange(int xPos, int yPos, int xStop, int yStop) {
		List<Integer> pos = new ArrayList<Integer>();
		pos.add(xPos);
		pos.add(yPos);
		pos.add(xStop);
		pos.add(yStop);
		boolean add = true;
		for (int i = 0; i < previousChanges.size(); i++) {
			List<Integer> old = previousChanges.get(i);
			if (equalLists(old, pos)) {
				previousChanges.add(pos);
				add = false;
				break;
			}
		}
		if (add) {
			currentChanges.add(pos);
		}
	}

}
