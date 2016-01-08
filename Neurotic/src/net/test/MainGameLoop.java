package net.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.entities.Entity;
import net.entities.Light;
import net.entities.Player;
import net.render.Camera;
import net.render.DisplayManager;
import net.render.Loader;
import net.render.MasterRenderer;
import net.render.models.ModelData;
import net.render.models.OBJFileLoader;
import net.render.models.OBJLoader;
import net.render.models.RawModel;
import net.render.models.TexturedModel;
import net.render.textures.ModelTexture;
import net.render.water.WaterFrameBuffers;
import net.render.water.WaterRenderer;
import net.render.water.WaterShader;
import net.render.water.WaterTile;
import net.world.GenerateTerrain;
import net.world.TerrainHolder;

public class MainGameLoop {
	public static final Vector3f SKY_COLOR = new Vector3f(0.7f, 0.7f, 0.9f);
	Loader loader = new Loader();
	List<Entity> entities = new ArrayList<Entity>();
	ModelData data = OBJFileLoader.loadOBJ("tree");
	MasterRenderer renderer = new MasterRenderer(loader);
	RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
			data.getIndices());

	WaterFrameBuffers fbos = new WaterFrameBuffers();

	WaterShader waterShader = new WaterShader();
	WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
	Thread t = new Thread(new TerrainHolder(10, loader));

	TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader),
			new ModelTexture(loader.loadTexture("grassTexture")));
	TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader),
			new ModelTexture(loader.loadTexture("fern")));
	TexturedModel staticModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
	ModelTexture texture = staticModel.getTexture();
	Light light = new Light(new Vector3f(3000, 1000, 3000), new Vector3f(1.0f, 1.0f, 1.0f));

	RawModel bunnyModel = OBJLoader.loadOBJModel("tree", loader);
	TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("blendmap")));
	Player player = new Player(stanfordBunny, new Vector3f(0, 0, 0), 0, 0, 0, 10);
	Camera camera = new Camera(player);

	public MainGameLoop() {
		t.start();
		grass.getTexture().setTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		fern.getTexture().setTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			if (i % 7 == 0) {
				float x = random.nextInt(1600);
				float z = random.nextInt(1600);
				GenerateTerrain t = TerrainHolder.getChunk(x, z);
				if (t != null) {
					entities.add(new Entity(grass, new Vector3f(x, t.getHeightOfTerrain(x, z), z),
							t.getNormal((int) x, (int) z).x, t.getNormal((int) x, (int) z).y,
							t.getNormal((int) x, (int) z).z, 3));
				}
			}
			if (i % 3 == 0) {
				float x = random.nextInt(1600);
				float z = random.nextInt(1600);
				GenerateTerrain t = TerrainHolder.getChunk(x, z);
				if (t != null) {
					entities.add(new Entity(staticModel, new Vector3f(x, t.getHeight(x, z, 1), z), t.getNormal(x, z).x,
							t.getNormal(x, z).y, t.getNormal(x, z).z, 10f));
				}
			}
		}

		texture.setShineDamper(10);
		texture.setReflectivity(2);

		while (!Display.isCloseRequested()) {
			render();
		}
		renderer.clean();
		fbos.clean();
		loader.clean();
		DisplayManager.closeDisplay();
	}

	public void render() {
		camera.move();
		player.move(TerrainHolder.getChunk(player.getPosition().x, player.getPosition().z));
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		fbos.bindReflectionFrameBuffer();
		float distance = 2 * camera.getPosition().y;

		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderer.render(light, camera, TerrainHolder.getTerrains(), entities, player, new Vector4f(0, 1, 0, -0 + 1f));
		camera.getPosition().y += distance;
		camera.invertPitch();

		fbos.bindRefractionFrameBuffer();
		renderer.render(light, camera, TerrainHolder.getTerrains(), entities, player, new Vector4f(0, 0, 0, 0));

		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		fbos.unbindCurrentFrameBuffer();
		renderer.render(light, camera, TerrainHolder.getTerrains(), entities, player, new Vector4f(0, 0, 0, 10000));
		waterRenderer.render(TerrainHolder.getWater(), camera, light);

		DisplayManager.updateDisplay();
	}

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		new MainGameLoop();
	}

}
