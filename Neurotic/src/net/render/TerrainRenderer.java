package net.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.render.models.RawModel;
import net.render.shaders.TerrainShader;
import net.render.textures.TerrainTexturePack;
import net.tools.Tools;
import net.world.Terrain;

public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
	}

	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1,0);
	}

	private void bindTextures(Terrain terrain) {
		TerrainTexturePack pack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getBackgroundTexture().getTexutreID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getrTexture().getTexutreID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getgTexture().getTexutreID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getbTexture().getTexutreID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getBlendMap().getTexutreID());

	}

	private void unbindTerrain() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Tools
				.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
