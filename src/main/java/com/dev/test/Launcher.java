package com.dev.test;

import com.dev.core.EngineManager;
import com.dev.core.WindowManager;
import com.dev.core.utils.Consts;

// 런처 클래스. 엔진 구동시 사용.
public class Launcher {

    // 윈도우 매니저에서 창 참조
    private static WindowManager window;

    // 게임 클래스 참조. -> 화면에 띄우는 용
    private static TestGame game;

    // 메인 함수. 창의 크기와 실행시 오류여부 판단.
    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, 1600, 900, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();
        try {
            engine.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}