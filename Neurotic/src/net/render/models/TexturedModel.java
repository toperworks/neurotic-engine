package net.render.models;

import net.render.textures.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture tex;

	public TexturedModel(RawModel model, ModelTexture tex) {
		this.rawModel = model;
		this.tex = tex;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTex() {
		return tex;
	}

}
