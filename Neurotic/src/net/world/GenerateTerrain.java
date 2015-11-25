package net.world;

import org.lwjgl.util.vector.Vector3f;

public class GenerateTerrain {

	public static void main(String[] args) {
		System.out.println(calculateNormal(1, 1, 1, 2, 2, 2, 3, 3, 3).getX());
	}

	private static Vector3f calculateNormal(float vX1, float vY1, float vZ1, float vX2, float vY2, float vZ2, float vX3,
			float vY3, float vZ3) {
		Vector3f edge1 = new Vector3f(vX1, vY1, vZ1).negate(new Vector3f(vX2, vY2, vZ2));
		Vector3f edge2 = new Vector3f(vX2, vY2, vZ2).negate(new Vector3f(vX3, vY3, vZ3));
		Vector3f crsProd = new Vector3f(0, 0, 0);
		Vector3f.cross(edge1, edge2, crsProd);
		Vector3f normal = (Vector3f) crsProd.normalise();
		return normal;
	}

}
