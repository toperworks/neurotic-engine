package net.toper.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Matrix4f;

import Graphics.Camera;
import Graphics.Shader;
import Input.Input;
import Tutorial.Box;
import Tutorial.Crate;
import Tutorial.Grid;

public class Main implements Runnable {

	private Thread thread;
	private boolean running = true;

	public static long window;
	private static float delta = 0.0f;

	private static int WIDTH = 900, HEIGHT = WIDTH / 16 * 9;

	private GLFWKeyCallback keyCallback;

	private Box box;
	private Crate crate1;
	private Grid grid;
	public Camera camera = new Camera(new Matrix4f());

	public void start() {
		running = true;
		thread = new Thread(this, "N2");
		thread.start();
	}

	public void initWindow() {
		if (glfwInit() != GL_TRUE) {
			System.err.println("GLFW initialization failed!");
		}
		window = glfwCreateWindow(WIDTH, HEIGHT, "N2 Beta 0.14", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create our Window!");
		}
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);

		glfwSetKeyCallback(window, keyCallback = new Input());
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GLContext.createFromCurrent();
		glClearColor(0.3f, 0.7f, 0.92f, 1.0f);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		Matrix4f pr_matrix = getProjectionMatrix();
		Matrix4f mat = new Matrix4f();
		Matrix4f.translate(camera.position, mat, mat);

		Shader.loadAll();
		Shader shader1 = Shader.shader1;
		shader1.bind();
		shader1.loadUniformMat4f("vw_matrix", mat);
		shader1.loadUniformMat4f("pr_matrix", pr_matrix);
		shader1.setUniform1i("tex", 1);
		shader1.unbind();

		crate1 = new Crate();
		box = new Box();
		grid = new Grid();
	}

	public void update() {
		glfwPollEvents();
		Input.update(window);
		camera.update();
		box.update();
		grid.update();
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glCullFace(GL_BACK);
		camera.render();
		crate1.render();
		grid.render();
		int i = glGetError();
		if (i != GL_NO_ERROR)
			System.out.println(i);
		glfwSwapBuffers(window);
	}

	public void run() {
		initWindow();
		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			if (glfwWindowShouldClose(window) == GL_TRUE)
				running = false;
		}
		keyCallback.release();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String args[]) {
		Main game = new Main();
		game.start();
	}

	public Matrix4f getProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		float fieldOfView = 90f;
		float aspectRatio = (float) WIDTH / (float) HEIGHT;
		float near_plane = 0.1f;
		float far_plane = 100f;

		float y_scale = 1f / (float) Math.tan(Math.toRadians(fieldOfView / 2f));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = far_plane - near_plane;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	public static float getDelta() {
		return delta;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

}
