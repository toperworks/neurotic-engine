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

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		ModelData data = OBJFileLoader.loadOBJ("tree");

		MasterRenderer renderer = new MasterRenderer();
		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());

		WaterFrameBuffers fbos = new WaterFrameBuffers();

		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(0, 0, 0);
		waters.add(water);

		TerrainHolder terrain = new TerrainHolder(2);
		terrain.generate(loader);

		TexturedModel staticModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));

		TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setTransparency(true);
		fern.getTexture().setUseFakeLighting(true);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for (int i = 0; i < 500; i++) {
			if (i % 7 == 0) {
				float x = random.nextInt(1600);
				float z = random.nextInt(1600);
				GenerateTerrain t = terrain.getChunk(x, z);
				if (t != null) {
					entities.add(new Entity(grass, new Vector3f(x, t.getHeightOfTerrain(x, z), z),
							t.getNormal((int) x, (int) z).x, t.getNormal((int) x, (int) z).y,
							t.getNormal((int) x, (int) z).z, 3));
				}
			}
			if (i % 3 == 0) {
				float x = random.nextInt(1600);
				float z = random.nextInt(1600);
				GenerateTerrain t = terrain.getChunk(x, z);
				if (t != null) {
					System.out.println(t.getNormal((int) x, (int) z).x);
					entities.add(new Entity(staticModel, new Vector3f(x, t.getHeightOfTerrain(x, z), z),
							t.getNormal((int) x, (int) z).x*90f, t.getNormal((int) x, (int) z).y*90f,
							t.getNormal((int) x, (int) z).z*90f, random.nextFloat() * 5 + 4));
				}
			}
		}

		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(2);

		Light light = new Light(new Vector3f(3000, 1000, 3000), new Vector3f(1.0f, 1.0f, 1.0f));

		Camera camera = new Camera();

		RawModel bunnyModel = OBJLoader.loadOBJModel("dragon", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
		Player player = new Player(stanfordBunny, new Vector3f(0, 0, 0), 0, 0, 0, 10);

		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * camera.getPosition().y - water.getHeight();

			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.render(light, camera, terrain.getTerrains(), entities, player,
					new Vector4f(0, 1, 0, -water.getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();

			fbos.bindRefractionFrameBuffer();
			renderer.render(light, camera, terrain.getTerrains(), entities, player,
					new Vector4f(0, -1, 0, water.getHeight()));

			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			renderer.render(light, camera, terrain.getTerrains(), entities, player, new Vector4f(0, -1, 0, 10000));
			waterRenderer.render(waters, camera, light);

			DisplayManager.updateDisplay();

		}
		renderer.clean();
		fbos.clean();
		loader.clean();
		DisplayManager.closeDisplay();
	}

}
