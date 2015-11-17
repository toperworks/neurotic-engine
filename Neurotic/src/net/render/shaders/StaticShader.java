package net.render.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.entities.Light;
import net.render.Camera;
import net.tools.Tools;

public class StaticShader extends Shader {

	private static final String VERTEX_FILE = "src/net/render/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/net/render/shaders/fragmentShader.txt";

	private int locationTransMatrix;
	private int locationProjMatrix;
	private int locationViewMatrix;
	private int locationLight;
	private int locationLightColor;
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationFakeLight;
	private int locationSkyColor;

	public StaticShader() {
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
		locationFakeLight = super.getUniformLocation("useFakeLighting");
		locationSkyColor = super.getUniformLocation("skyColor");
	}

	public void loadSkyColor(Vector3f color) {
		super.loadVector(locationSkyColor, color);
	}

	public void loadFakeLightingVariable(boolean useFake) {
		super.loadBoolean(locationFakeLight, useFake);
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
