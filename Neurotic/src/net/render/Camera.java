package net.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(10, 100, 10);
	private float pitch = -30;
	private float yaw = 90;
	private float roll;

	/*
	 * public Camera(){}
	 * 
	 * public void move(){ if(Keyboard.isKeyDown(Keyboard.KEY_W)){
	 * position.z-=0.3; } if(Keyboard.isKeyDown(Keyboard.KEY_D)){
	 * position.x+=0.3; } if(Keyboard.isKeyDown(Keyboard.KEY_A)){
	 * position.x-=0.3; } if(Keyboard.isKeyDown(Keyboard.KEY_S)){
	 * position.z+=0.3; } }
	 */

	private float speed;

	public Camera() {

		this.speed = 0.015f;

	}

	public void move() {
		float speed = this.speed * DisplayManager.getDelta();
		yaw = -(Display.getWidth() - Mouse.getX() / 2) / 2;
		pitch = (Display.getHeight() / 2) - Mouse.getY();
		if (pitch >= 90) {
			pitch = 90;
		} else if (pitch <= -90) {
			pitch = -90;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z += -(float) Math.cos(Math.toRadians(yaw)) * speed;
			position.x += (float) Math.sin(Math.toRadians(yaw)) * speed;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z -= -(float) Math.cos(Math.toRadians(yaw)) * speed;
			position.x -= (float) Math.sin(Math.toRadians(yaw)) * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.z += (float) Math.sin(Math.toRadians(yaw)) * speed;
			position.x += (float) Math.cos(Math.toRadians(yaw)) * speed;

		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.z -= (float) Math.sin(Math.toRadians(yaw)) * speed;
			position.x -= (float) Math.cos(Math.toRadians(yaw)) * speed;

		} else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += speed;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}