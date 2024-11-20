package com.dev.core.entity;

// 텍스쳐에 대한 기본 변수 정의 클래스
public class Texture {

    // 텍스쳐의 id
    private final int id;

    // 생성자
    public Texture(int id) {
        this.id = id;
    }

    // ID getter
    public int getId() {
        return id;
    }
}
