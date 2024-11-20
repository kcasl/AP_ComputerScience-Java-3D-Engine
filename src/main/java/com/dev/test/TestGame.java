package com.dev.test;

import com.dev.core.*;
import com.dev.core.entity.Entity;
import com.dev.core.entity.Model;
import com.dev.core.entity.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

// 게임 테스트를 위한 클래스. ILogic -> 게임 로직에 대한 내용의 함수들을 참조.
public class TestGame implements ILogic {

    // 카메라 움직임에 대한 속도
    private static final float CAMERA_MOVE_SPEED = 0.05f;

    // 각 매니저 클래스 선언
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    // 엔티티 및 카메라 클래스 선언
    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    // 기본 객체 생성.
    public TestGame() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
    }

    // 렌더링 및 쉐이딩 시작.
    @Override
    public void init() throws Exception {
        renderer.init();

        // 사용하는 3D 모델의 정점, 비율등에 대한 정보
        float[] vertices = new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7,
        };

        //모델 및 텍스쳐 로드. 엔티티 객체로 그룹화 시킨 후 화면에 배치.
        Model model = loader.loadModel(vertices, textCoords, indices);
        model.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")));
        entity = new Entity(model, new Vector3f(0,0,-3), new Vector3f(0,0,0), 1);
    }

    // 키보드 입력
    @Override
    public void input() {
        cameraInc.set(0,0,0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if(window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;
    }

    // 카메라의 움직임에 대한 업데이트.
    @Override
    public void update() {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);

        entity.incRotation(0.0f, 0.5f, 0.0f);
    }

    // 엔티티 및 카메라 렌더링 함수,
    @Override
    public void render() {
        if(window.isResize()) {
            GL11.glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResize(false);
        }

        window.setClearColour(0.0f,0.0f,0.0f,0.0f);
        renderer.render(entity, camera);
    }

    // 초기화
    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
