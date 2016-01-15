package Tutorial;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Graphics.Shader;
import Graphics.Texture;
import Graphics.VertexArray;

public class GameObject {

	public VertexArray VAO;
	public Texture tex;
	public float[] vertices, texCoords;
	public byte[] indices;
	public static Shader shader;
	
	public Vector3f position = new Vector3f();
	
	public float delta = 0.01f;
	
	public GameObject(float[] vertices, byte[] indices, float[] texCoords, String texPath){	
		this.vertices = vertices;
		this.indices = indices;
		this.texCoords = texCoords;
		tex = new Texture(texPath);
		VAO = new VertexArray(this.vertices, this.indices, this.texCoords);
	}
	
	public void loadShader(){
		shader = new Shader("shaders/bg.vert", "shaders/bg.frag");
	}
	
	public void translate(Vector3f vector){
		position.x += vector.x;
		position.y += vector.y;
		position.z += vector.z;
	}

	public void sinUpdate(){
		position.y += (float) Math.sin(delta)/105.0f;
	}
	
	public void render(){
		tex.bind();
		Shader.shader1.bind();
		VAO.render();
		Shader.shader1.unbind();
		tex.unbind();
		
	}
	
	public void update(){
	}
	
	
}
