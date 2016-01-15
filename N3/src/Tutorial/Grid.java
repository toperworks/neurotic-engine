
package Tutorial;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Graphics.Shader;
import Input.Input;

public class Grid extends GameObject{

	public static float width = 3.5f;
	public static float height = 3.5f;
	public Vector3f delta = new Vector3f();
	
	public boolean running = false;
	public boolean jumping = false;
	public boolean idle = true;
	public boolean walking = false;
	
	
	public int spritePos = 0;
	public int counter = 0;
	public int animState = 0;
	
	public float rot = 0.0f;
	
	private static float[] vertices = new float[]{
			-width, 0.2f,-height, 
			-width,  0.2f,height, 
			  width, 0.2f,  height,
			  width, 0.2f, -height
	};
	
	private static float[] texCoords = new float[]{
			0, 1,
			0, 0,
			1, 0,
			1, 1
	};
	
	private static byte[] indices = new byte[]{
			0,1,2,
			2,3,0
	};
	
	private static String texPath = "assets/grid.png";
	
	public Grid(){
		super(vertices, indices, texCoords, texPath);
	}
	

	
	@Override
	public void render(){
		
		tex.bind();
		Shader.shader1.bind();
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.rotate(rot, new Vector3f(1, 0, 0));
		rotMatrix.rotate(rot, new Vector3f(0, 1, 0));
		rotMatrix.rotate(rot, new Vector3f(0, 0, 1));
		Shader.shader1.loadUniformMat4f("ml_matrix", rotMatrix);
		VAO.render();
		Shader.shader1.unbind();
		tex.unbind();
		
	}
	
	@Override
	public void update(){
//			rot += 0.1f;
		// Uncomment different lines to see different translation effects
//			position.x += 0.01f;
//			position.y += 0.01f;
			if(Input.isKeyDown(GLFW_KEY_SPACE))
				position.z += 0.1f;
	}

		
}



