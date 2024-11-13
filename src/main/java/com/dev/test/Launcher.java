package com.dev.test;

import com.dev.core.WindowManager;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

public class Launcher {

    public static void main(String[] args) {

        System.out.println(Version.getVersion());
        WindowManager window = new WindowManager("Java 3D", 1600, 900, false);
        window.init();

        while (!window.windowShouldClose()) {
            window.update();
        }

        window.cleanup();
    }
}