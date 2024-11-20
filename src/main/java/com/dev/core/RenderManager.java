package com.dev.core;

import com.dev.core.entity.Entity;
import com.dev.core.utils.Transformation;
import com.dev.core.utils.Utils;
import com.dev.test.Launcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

// 메인 로직에서의 렌더링을 처리하는 매니저 클래스
public class RenderManager {

    // 윈도우 창에 대한 변수 참조
    private final WindowManager window;
    // 쉐이딩 메니저에서 쉐이더 참조
    private ShaderManager shader;

    // 생성자, 윈도우를 런처상의 윈도우 정보로 초기화
    public RenderManager() {
        window = Launcher.getWindow();
    }

    // 렌더링 init 함수, 쉐이딩 매니저에서 설정한 정보, 버텍스 쉐이더와 프래그먼트 쉐이더의 정보대로 쉐이더 렌더링 처리, 이후 쉐이딩을 병합하여 하나로 볼 수 있게 함.
    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();
        // 아래 4가지 요소에 대한 렌더링 진행.
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
    }

    // 렌더러 함수 / 쉐이더를 바인드 하여 렌더링을 할 수 있게 함. 시작 전 초기화, 샘플텍스쳐, 변환 행렬, 투영행렬, 물체를 모아주는 뷰 행렬에 각각 쉐이더를 적용함.
    public void render(Entity entity, Camera camera) {
        clear();
        shader.bind();
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));

        // 엔티티 클래스에서 정의한 getModel 함수 사용. 외부에서 불러온 텍스쳐에 대한 처리.
        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
        // 텍스처의 버텍스 수를 센 후 렌더링을 할 수 있게 함.
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup();
    }
}
