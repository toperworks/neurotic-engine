package Graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Input.Input;
import net.toper.main.Main;

public class Camera {

	public Matrix4f cameraMat = new Matrix4f();
	public Vector3f position = new Vector3f();

	Vector3f rotation = new Vector3f(0, 90, 0);

	public Camera(Matrix4f cameraMat) {
		this.cameraMat = cameraMat;
	}

	public Matrix4f getMatrix() {
		return cameraMat;
	}

	public Matrix4f getCameraMat() {
		return cameraMat;
	}

	public void setCameraMat(Matrix4f cameraMat) {
		this.cameraMat = cameraMat;
	}

	public void setPosition(Vector3f pos) {
		this.position = pos;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRot() {
		return rotation;
	}

	public float getPitch() {
		return rotation.z;
	}

	public float getYaw() {
		return rotation.y;
	}

	public float getRoll() {
		return rotation.z;
	}

	public void update() {
		float mouseSensitivity = 0.01f;
		rotation.y += Input.getDX() * mouseSensitivity;
		rotation.x += Input.getDY() * mouseSensitivity;
        rotation.x = Math.max(-90, Math.min(90, rotation.x));
		if (rotation.y / 360 > 1) {
			rotation.y -= 360;
		} else if (rotation.y / 360 < -1) {
			rotation.y += 360;
		}

		float speed = 0.1f;
		if (Input.isKeyDown(GLFW_KEY_W)) {
			position.x+= Math.sin(Math.toRadians(rotation.y)) * speed;
			position.z -= Math.cos(Math.toRadians(rotation.y)) * speed;
		}
		if (Input.isKeyDown(GLFW_KEY_S)) {
			position.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
			position.z+= Math.cos(Math.toRadians(rotation.y)) * speed;
		}
		if (Input.isKeyDown(GLFW_KEY_D)) {
			position.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
			position.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
		}
		if (Input.isKeyDown(GLFW_KEY_A)) {
			position.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
			position.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
		}
	}

	public Matrix4f setupViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;
	}

	public void render() {
		Matrix4f mat = new Matrix4f();
		mat.rotate(rotation.x, new Vector3f(1, 0, 0));
		mat.rotate(-rotation.y, new Vector3f(0, 1, 0));
		mat.rotate(rotation.z, new Vector3f(0, 0, 1));
		Shader.shader1.bind();
		Shader.shader1.loadUniformMat4f("vw_matrix",
				Matrix4f.translate(new Vector3f(-position.x, -position.y, -position.z), mat, mat));

		Shader.shader1.unbind();
	}
}
