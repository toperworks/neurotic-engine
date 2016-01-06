package net.render.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.entities.Light;
import net.render.Camera;
import net.render.DisplayManager;
import net.render.Loader;
import net.render.models.RawModel;
import net.tools.Tools;

public class WaterRenderer {

	private static final String DUDV_MAP = "waterDUDV";
	private static final String NORMAL_MAP = "normalMap";
	private int dudvTexture;
	private int normalMapTexture;
	private static final float WAVE_SPEED = 0.045f;

	private RawModel quad;
	private WaterShader shader;
	WaterFrameBuffers fbos;

	private float moveFactor = 0;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		this.fbos = fbos;
		this.shader = shader;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		normalMapTexture = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Tools.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	private void prepareRender(Camera camera, Light sun) {
		moveFactor += WAVE_SPEED * DisplayManager.getDelta();
		moveFactor %= 1;
		shader.start();
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMapTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		float[] vertices = { 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);

	}
}
