package com.dev.core;

import com.dev.core.entity.Model;
import com.dev.core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

// 오브젝트 로더 클래스 -> 텍스쳐와 같은 오브젝트를 직접적으로 가져오는 클래스
public class ObjectLoader {
    // 모두 동적할당을 할 수 있게 설정함.

    // VAO(Vertex Array Object)의 ID를 저장하는 리스트
    private List<Integer> vaos = new ArrayList<>();

    // VBO(Vertex Buffer Object)의 ID를 저장하는 리스트
    private List<Integer> vbos = new ArrayList<>();

    // 텍스쳐의 ID를 저장하는 리스트
    private List<Integer> textures = new ArrayList<>();

    // 텍스쳐를 적용할 모델을 불러옴. 3D 모델 또는 오브젝트등.
    public Model loadModel(float[] vertices, float[] textCoords, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttribList(0,3,vertices);
        storeDataInAttribList(1,2,textCoords);
        unbind();
        return new Model(id, indices.length);
    }

    // 텍스처 로드 및 초기화를 수행
    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if(buffer == null)
                throw new Exception("Image File " + filename + " not loaded " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }
        // 텍스쳐 바인딩 및 밉맵 생성. 또한 버퍼에 생성된 텍스쳐 저장.
        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    // VAO 생성
    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    // 인덱스 버퍼 생성 및 초기화, 주로 정점 재활용 등으로 메모리 효율성을 높이는데 쓰임.
    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    // 정점 데이터를 GPU에 업로드함. + 버퍼에 저장.
    private void storeDataInAttribList(int attribNo, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    // 바인드 해제. 쉐이더 / 렌더링에서 벗어남
    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    // 오브젝트의 버텍스 계산 초기화
    public void cleanup() {
        for(int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for(int vbo : vbos)
            GL30.glDeleteBuffers(vbo);
        for(int texture : textures)
            GL11.glDeleteTextures(texture);
    }
}
