package net.render.water;

public class WaterTile {

	public static float TILE_SIZE;

	private float height;
	private float x, z;

	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public WaterTile(int x2, int z2, int i, float size) {
		this.x = x2;
		this.z = z2;
		TILE_SIZE = size;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

}