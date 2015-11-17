package net.test;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import net.entities.Entity;
import net.entities.Light;
import net.render.Camera;
import net.render.DisplayManager;
import net.render.Loader;
import net.render.MasterRenderer;
import net.render.models.ModelData;
import net.render.models.OBJFileLoader;
import net.render.models.RawModel;
import net.render.models.TexturedModel;
import net.render.textures.ModelTexture;
import net.world.Terrain;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader load = new Loader();

		ModelData data = OBJFileLoader.loadOBJ("bunny");
		RawModel model = load.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());

		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(load.loadTexture("tree")));
		ModelTexture texture = staticModel.getTex();
		texture.setShineDamper(5);
		texture.setReflectivity(0.5f);
		Entity e = new Entity(staticModel, new Vector3f(0, 0, -20), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(20, 20, 20), new Vector3f(1, 1, 1));
		Camera c = new Camera();
		Terrain terrain = new Terrain(0, 0, load, new ModelTexture(load.loadTexture("image")));

		MasterRenderer render = new MasterRenderer();

		while (!Display.isCloseRequested()) {
			c.move();
			render.processTerrain(terrain);
			render.processEntity(e);
			render.render(light, c);
			DisplayManager.updateDisplay();
		}
		render.clean();
		load.clean();
		DisplayManager.closeDisplay();
	}

}
