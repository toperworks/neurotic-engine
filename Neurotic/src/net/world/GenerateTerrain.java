package net.world;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.render.Loader;
import net.render.models.RawModel;
import net.tools.Tools;

public class GenerateTerrain {

	private static final float MAX_HEIGHT = 180;

	private float x;
	private float z;
	private RawModel model;
	private int texture;

	private float[][] heights;
	private Vector3f[][] normals;
	public static final int SIZE = 800;
	private static final int VERTEX_COUNT = 550;

	Loader l;

	public GenerateTerrain(int i, int j, Loader loader) {
		l = loader;
		x = i * SIZE;
		z = j * SIZE;
		model = generateTerrain(loader);
		texture = loader.loadTexture("grassy");
	}

	public int getTexture() {
		return texture;
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
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

	OpenSimplexNoise n1 = new OpenSimplexNoise(50);
	OpenSimplexNoise n2 = new OpenSimplexNoise(100);
	OpenSimplexNoise n3 = new OpenSimplexNoise(150);

	private RawModel generateTerrain(Loader loader) {
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		normals = new Vector3f[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for (int x = 0; x < VERTEX_COUNT; x++) {
			for (int z = 0; z < VERTEX_COUNT; z++) {
				int xa = (int) (x + this.x);
				int za = (int) (z + this.z);
				float value = (float) ((n1.eval(xa / 48.0, za / 48.0, 0.5) + n2.eval(xa / 24.0, za / 24.0, 0.5) * .5
						+ n3.eval(xa / 12.0, za / 12.0, 0.5) * .25) / (1 + .5 + .25));
				heights[x][z] = value * MAX_HEIGHT;
			}
		}
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = getHeight(i, j);
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i);
				this.normals[i][j] = normal;
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
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
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public Vector3f calculateNormal(int x, int z) {
		float heightL = getHeight(x - 1, z);
		float heightR = getHeight(x + 1, z);
		float heightD = getHeight(x, z - 1);
		float heightU = getHeight(x, z + 1);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	public Vector3f getNormal(int x, int z) {
		return calculateNormal(x, z);
	}

	private float getHeight(int x, int z) {
		return getHeightOfTerrain(this.x + x, this.z + z);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

}
