package Input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;

import net.toper.main.Main;

// Notice how this class 'extends' GLFWKeyCallback
// this allows us to inherit most of the functionality
// of the GLFWKeyCallback class and add in some extra
// stuff that we'll need for our game.
public class Input extends GLFWKeyCallback {

	// a boolean array of all our keys.
	public static boolean[] keys = new boolean[65535];

	// Overrides GLFW's own implementation of the Invoke method
	// This gets called everytime a key is pressed.
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

	public static boolean isKeyUp(int keycode) {
		return keys[keycode];
	}

	private static int oldX, oldY, dx, dy, x, y;

	public static void update(long windowID) {
		DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowID, posX, posY);
		x = (int) posX.get(0);
		y = (int) posY.get(0);
		dx += x - oldX;
		dy += y - oldY;
		oldY = y;
		oldX = x;
		glfwSetCursorPos(windowID, Main.getWidth() / 2, Main.getHeight() / 2);

	}

	public static float getDX() {
		return dx;
	}

	public static float getDY() {
		return dy;
	}

}
