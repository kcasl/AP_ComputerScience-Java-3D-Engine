package com.dev.core;

import com.dev.core.utils.Consts;
import com.dev.test.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

// 엔진 관리 클래스
public class EngineManager {

    // 나노 초 정의
    public static final long NANOSECOND = 1000000000L;
    // Framerate 정의
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    // 엔진이 실행중인지 판단하는 변수
    private boolean isRunning;

    //윈도우 매니저에서 창을 참조
    private WindowManager window;

    // GLFW 내장 라이브러리. 에러 콜백 변수
    private GLFWErrorCallback errorCallback;

    // 게임 로직 -> 함수 기본형 정의해둠
    private ILogic gameLogic;

    // 엔진 init 함수. 시작하기 전 에러가 있다면 종료. 문제가 없다면 창을 띄우고, 게임 로직 함수를 실행할 수 있게 함.
    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        window.init();
        gameLogic.init();
    }

    // start 함수, 중복 실행 방지
    public void start() throws Exception {
        init();
        if(isRunning)
            return;
        run();
    }

    // 프레임 제어 및 렌더링 관리. 변경 사항을 업데이트 하고, 이를 렌더링 한 후 다음 프레임으로 전송하여 움직이는 것을 볼 수 있게끔 함.
    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if(window.windowShouldClose())
                    stop();
                if(frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle(Consts.TITLE + " FPS : " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render) {
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    // 실행 중지 -> 그냥 종료 할 때 실행됨.
    private void stop() {
        if(!isRunning)
            return;
        isRunning = false;
    }

    // 게임 내에서 입력을 할때 사용.
    private void input() {
        gameLogic.input();
    }

    // 게임에 대한 변경사항을 업데이트 및 렌더링
    private void render() {
        gameLogic.render();
        window.update();
    }

    // update 함수
    private void update() {
        gameLogic.update();
    }

    // 초기화 cleanup 함수 -> default로 되돌림.
    private void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    // Fps에 대한 getter
    public static int getFps() {
        return fps;
    }

    // Fps에 대한 setter
    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

}
