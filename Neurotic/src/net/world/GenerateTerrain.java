package net.world;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.render.Loader;
import net.render.models.RawModel;
import net.tools.Tools;

public class GenerateTerrain {

	private static final float MAX_HEIGHT = 80;

	private float xPos;
	private float zPos;
	private float x;
	private float z;
	private RawModel model;
	private int texture;

	private float[][] heights;
	private Vector3f[][] normals;
	public static final float SIZE = 800;
	private static final int VERTEX_COUNT = 550;

	Loader l;

	public GenerateTerrain(int x, int z, Loader loader, float[][] heights) {
		l = loader;
		xPos = x * SIZE;
		zPos = z * SIZE;
		this.x = x;
		this.z = z;
		this.heights = new float[(int) SIZE][(int) SIZE];
		for (int xa = 0; xa < VERTEX_COUNT; xa++) {
			for (int za = 0; za < VERTEX_COUNT; za++) {
				this.heights[xa][za] = heights[(int) ((xa * (SIZE / VERTEX_COUNT)) + this.xPos)][(int) (za
						* (SIZE / VERTEX_COUNT) + this.zPos)] * MAX_HEIGHT;
			}
		}
		generateTerrain(l);
		texture = loader.loadTexture("grassy");
	}

	public int getTexture() {
		return texture;
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.xPos;
		float terrainZ = worldZ - this.zPos;
		float gridSquareSize = SIZE / (float) (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = Tools.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Tools.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	public void generateTerrain(Loader loader) {
		normals = new Vector3f[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for (int z = 0; z < VERTEX_COUNT; z++) {
			for (int x = 0; x < VERTEX_COUNT; x++) {
				vertices[vertexPointer * 3] = (float) x / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = getHeight(x, z, 0);
				vertices[vertexPointer * 3 + 2] = (float) z / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(x, z);
				this.normals[x][z] = normal;
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) x / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) z / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		model = loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public Vector3f calculateNormal(int x, int z) {
		float heightL = getHeight(x - 1, z, 0);
		float heightR = getHeight(x + 1, z, 0);
		float heightD = getHeight(x, z - 1, 0);
		float heightU = getHeight(x, z + 1, 0);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	public Vector3f getNormal(float x, float z) {
		x = (int) ((x - this.xPos - 1) * (VERTEX_COUNT / SIZE));
		z = (int) ((z - this.zPos - 1) * (VERTEX_COUNT / SIZE));
		if ((int) x >= 0 && (int) z >= 0) {
			return normals[(int) x][(int) z];
		} else {
			return new Vector3f(0, 1, 0);
		}
	}

	public float getHeight(float x, float z, int affix) {
		if (affix == 1) {
			x = (int) ((x - this.xPos) * (VERTEX_COUNT / SIZE));
			z = (int) ((z - this.zPos) * (VERTEX_COUNT / SIZE));
		}
		if (affix == 2) {
			x = (int) (x - this.xPos) * (VERTEX_COUNT / SIZE);
			z = (int) (z - this.zPos) * (VERTEX_COUNT / SIZE);
		}
		if ((int) x >= 0 && (int) z >= 0) {
			return heights[(int) x][(int) z];
		} else {
			return 0;
		}
	}

	public float getChunkX() {
		return x;
	}

	public float getChunkZ() {
		return z;
	}

	public float getX() {
		return xPos;
	}

	public float getZ() {
		return zPos;
	}

	public RawModel getModel() {
		return model;
	}

	public static float getVertexCount() {
		return VERTEX_COUNT;
	}

}
