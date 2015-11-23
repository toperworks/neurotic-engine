package net.render.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.entities.Light;
import net.render.Camera;
import net.tools.Tools;

public class TerrainShader extends Shader {

	private static final String VERTEX_FILE = "src/net/render/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/net/render/shaders/terrainFragmentShader.txt";

	private int locationTransMatrix;
	private int locationProjMatrix;
	private int locationViewMatrix;
	private int locationLight;
	private int locationLightColor;
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationSkyColor;
	private int locationBackgroundTexture;
	private int locationRTexture;
	private int locationGTexture;
	private int locationBTexture;
	private int locationBlendMap;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	protected void getAllUniformLocations() {
		locationTransMatrix = super.getUniformLocation("transformationMatrix");
		locationProjMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationLight = super.getUniformLocation("lightPosition");
		locationLight = super.getUniformLocation("lightColor");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationSkyColor = super.getUniformLocation("skyColor");
		locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
		locationRTexture = super.getUniformLocation("rTexture");
		locationGTexture = super.getUniformLocation("gTexture");
		locationBTexture = super.getUniformLocation("bTexture");
		locationBlendMap = super.getUniformLocation("blendMap");
	}
	
	public void connectTextureUnits(){
		super.loadInt(locationBackgroundTexture, 0);
		super.loadInt(locationRTexture, 1);
		super.loadInt(locationGTexture, 2);
		super.loadInt(locationBTexture, 3);
		super.loadInt(locationBlendMap,4);
	}

	public void loadSkyColor(Vector3f color) {
		super.loadVector(locationSkyColor, color);
	}

	public void loadTransformationMatrix(Matrix4f m) {
		super.loadMatrix(locationTransMatrix, m);
	}

	public void loadProjectionMatrix(Matrix4f m) {
		super.loadMatrix(locationProjMatrix, m);
	}

	public void loadViewMatrix(Camera c) {
		Matrix4f view = Tools.createViewMatrix(c);
		super.loadMatrix(locationViewMatrix, view);
	}

	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}

	public void loadLight(Light l) {
		super.loadVector(locationLight, l.getPosition());
		super.loadVector(locationLightColor, l.getColor());
	}
}
