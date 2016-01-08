package net.render.water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.entities.Light;
import net.render.Camera;
import net.render.shaders.Shader;
import net.tools.Tools;

public class WaterShader extends Shader {
	private final static String VERTEX_FILE = "src/net/render/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/net/render/water/waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int locationReflection;
	private int locationRefraction;
	private int locationDUDV;
	private int locationNormalMap;
	private int locationMoveFactor;
	private int locationCameraPos;
	private int locationLightPos;
	private int locationLightColor;
	private int locationDepthMap;
	private int locationSkyColor;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		locationReflection = getUniformLocation("reflectionTexture");
		locationRefraction = getUniformLocation("refractionTexture");
		locationDUDV = getUniformLocation("dudvMap");
		locationNormalMap = getUniformLocation("normalMap");
		locationMoveFactor = getUniformLocation("moveFactor");
		locationCameraPos = getUniformLocation("cameraPosition");
		locationLightColor = getUniformLocation("lightColor");
		locationLightPos = getUniformLocation("lightPosition");
		locationDepthMap = getUniformLocation("depthMap");
		locationSkyColor = getUniformLocation("skyColor");
	}

	public void loadLight(Light sun) {
		super.loadVector(locationLightColor, sun.getColor());
		super.loadVector(locationLightPos, sun.getPosition());
	}

	public void loadSkyColor(Vector3f color) {
		super.loadVector(locationSkyColor, color);
	}

	public void loadMoveFactor(float factor) {
		super.loadFloat(locationMoveFactor, factor);
	}

	public void connectTextureUnits() {
		super.loadInt(locationReflection, 0);
		super.loadInt(locationRefraction, 1);
		super.loadInt(locationDUDV, 2);
		super.loadInt(locationNormalMap, 3);
		super.loadInt(locationDepthMap, 4);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Tools.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(locationCameraPos, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix) {
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
