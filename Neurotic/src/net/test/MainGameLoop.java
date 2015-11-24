package net.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

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
import net.render.textures.TerrainTexture;
import net.render.textures.TerrainTexturePack;
import net.world.Terrain;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture,
				blendMap);

		ModelData data = OBJFileLoader.loadOBJ("tree");

		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());

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
				entities.add(new Entity(grass,
						new Vector3f(random.nextFloat() * 800 - 500, 0, random.nextFloat() * -600), 0, 0, 0, 3));
			}
			if (i % 3 == 0) {
				entities.add(new Entity(staticModel,
						new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0,
						random.nextFloat() * 1 + 4));
			}
		}

		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(2);

		Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);

		Camera camera = new Camera();

		RawModel bunnyModel = OBJLoader.loadOBJModel("stall", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel,
				new ModelTexture(loader.loadTexture("stallTexture")));
		Player player = new Player(stanfordBunny, new Vector3f(0, 0, -50), 0, 0, 0, 1);

		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);

			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}

			renderer.render(light, camera);
			DisplayManager.updateDisplay();

		}

		renderer.clean();
		loader.clean();
		DisplayManager.closeDisplay();
	}

}
