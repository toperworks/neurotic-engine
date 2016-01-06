package net.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.entities.Entity;
import net.entities.Light;
import net.entities.Player;
import net.render.models.RawModel;
import net.render.models.TexturedModel;
import net.render.shaders.StaticShader;
import net.render.shaders.TerrainShader;
import net.world.GenerateTerrain;
import net.world.TerrainHolder;

public class MasterRenderer {

	private static final float FOV = 90;
	private static final float NEAR_PLANE = 1f;
	private static final float FAR_PLANE = 2000.0f;

	private static final float RED = 0.9f;
	private static final float GREEN = .9f;
	private static final float BLUE = .9f;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private RenderEntity renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<RawModel, List<GenerateTerrain>> terrains = new HashMap<RawModel, List<GenerateTerrain>>();

	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		renderer = new RenderEntity(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(Light sun, Camera camera, GenerateTerrain[] terrain, List<Entity> ent, Player player,
			Vector4f clipPlane) {
		prepare();
		for (Entity entity : ent) {
			processEntity(entity);
		}
		for (int z = 0; z < TerrainHolder.getSize(); z++) {
			for (int x = 0; x < TerrainHolder.getSize(); x++) {
				processTerrain(terrain[x + z * TerrainHolder.getSize()]);
			}
		}
		processEntity(player);
		shader.start();
		shader.loadAmbientLight(0.35f);
		shader.loadClipPlace(clipPlane);
		shader.loadSkyColor(new Vector3f(RED, GREEN, BLUE));
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadAmbientLight(0.35f);
		terrainShader.loadClipPlace(clipPlane);
		terrainShader.loadSkyColor(new Vector3f(RED, GREEN, BLUE));
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		entities.clear();
		terrains.clear();
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public void processTerrain(GenerateTerrain newTerrain) {
		RawModel terrain = newTerrain.getModel();
		List<GenerateTerrain> batch = terrains.get(terrain);
		if (batch != null) {
			batch.add(newTerrain);
		} else {
			List<GenerateTerrain> newBatch = new ArrayList<GenerateTerrain>();
			newBatch.add(newTerrain);
			terrains.put(terrain, newBatch);
		}
	}

	public void clean() {
		shader.clean();
		terrainShader.clean();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 0);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}