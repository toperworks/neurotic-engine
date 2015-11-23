package net.tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.render.Camera;

public class Tools {

	public static Matrix4f createTransformationMatrix(Vector3f trans, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(trans, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera c) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(c.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(c.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(c.getRoll()), new Vector3f(0, 0, 1), matrix, matrix);
		Vector3f translate = new Vector3f(-c.getPosition().x, -c.getPosition().y, -c.getPosition().z);
		Matrix4f.translate(translate, matrix, matrix);
		return matrix;
	}

}
