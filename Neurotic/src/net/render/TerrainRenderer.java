package net.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.render.models.RawModel;
import net.render.shaders.TerrainShader;
import net.tools.Tools;
import net.world.GenerateTerrain;

public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<RawModel, List<GenerateTerrain>> terrains) {
		for (RawModel model : terrains.keySet()) {
			List<GenerateTerrain> batch = terrains.get(model);
			for (GenerateTerrain terrain : batch) {
				prepareTerrain(terrain);
				loadModelMatrix(terrain);
				GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTerrain();
		}
	}

	private void prepareTerrain(GenerateTerrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadTextureColor(new Vector3f(0.56f, 0.33f, 0.12f));
		shader.loadShineVariables(500, 0.1f);
	}

	private void bindTextures(GenerateTerrain terrain) {
	}

	private void unbindTerrain() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(GenerateTerrain terrain) {
		Matrix4f transformationMatrix = Tools
				.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
