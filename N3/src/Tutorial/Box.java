package Tutorial;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Graphics.Shader;
import Input.Input;
import net.toper.main.Main;

public class Box extends GameObject {

	private static float width = 3.5f;
	private static float height = 3.5f;
	private Vector3f delta = new Vector3f();

	private boolean running = false;
	private boolean jumping = false;
	private boolean idle = true;
	private boolean walking = false;

	private int spritePos = 0;
	private int counter = 0;
	private int animState = 0;

	private Vector3f rot = new Vector3f(0, 0, 0);

	private static float[] vertices = new float[] { -width, -height, 0.2f, -width, height, 0.2f, width, height, 0.2f,
			width, -height, 0.2f };

	private static float[] texCoords = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

	private static byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };

	private static String texPath = "assets/crate.jpg";

	Shader boxShader;

	public Box() {
		super(vertices, indices, texCoords, texPath);
		boxShader = Shader.shader1;
	}

	public void render() {
		tex.bind();
		boxShader.bind();
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.rotate(rot.y, new Vector3f(1, 0, 0));
		rotMatrix.rotate(rot.x, new Vector3f(0, 1, 0));
		rotMatrix.rotate(rot.z, new Vector3f(0, 0, 1));
		boxShader.loadUniformMat4f("ml_matrix", rotMatrix);
		VAO.render();
		boxShader.unbind();
		tex.unbind();
	}

	public void update() {
	}

}
