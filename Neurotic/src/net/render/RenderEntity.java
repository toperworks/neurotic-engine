package net.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import net.entities.Entity;
import net.render.models.RawModel;
import net.render.models.TexturedModel;
import net.render.shaders.StaticShader;
import net.tools.Tools;

public class RenderEntity {

	private StaticShader shader;

	public RenderEntity(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModeL(model);
			List<Entity> batch = entities.get(model);
			for (Entity e : batch) {
				prepareInstance(e);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModeL(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		if (model.getTex().hasTransparency())
			MasterRenderer.disableCulling();
		shader.loadFakeLightingVariable(model.getTex().hasTransparency());
		shader.loadShineVariables(model.getTex().getShineDamper(), model.getTex().getReflectivity());
		GL13.glActiveTexture(GL11.GL_TEXTURE);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTex().getID());

	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Tools.createTransfromationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
