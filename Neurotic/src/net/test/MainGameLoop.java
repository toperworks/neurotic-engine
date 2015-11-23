package net.test;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.entities.Light;
import net.entities.Player;
import net.render.Camera;
import net.render.DisplayManager;
import net.render.Loader;
import net.render.MasterRenderer;
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
		Loader load = new Loader();

		RawModel model = OBJLoader.loadOBJModel("stall", load);
		TexturedModel texModel = new TexturedModel(model, new ModelTexture(load.loadTexture("dirt")));
		TerrainTexture backgroundTexture = new TerrainTexture(load.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(load.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(load.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(load.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(load.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture,
				blendMap);
		Light light = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Camera c = new Camera();
		Terrain terrain = new Terrain(0, -1, load, texturePack, blendMap);

		Player p = new Player(texModel, new Vector3f(100, 0, -50), 0, 0, 0, 1);

		MasterRenderer render = new MasterRenderer();

		while (!Display.isCloseRequested()) {
			// c.move();
			p.move();
			render.processEntity(p);
			// render.processTerrain(terrain);
			render.render(light, c);
			DisplayManager.updateDisplay();
		}
		render.clean();
		load.clean();
		DisplayManager.closeDisplay();
	}

}
