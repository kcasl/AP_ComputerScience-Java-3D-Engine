package com.dev.core;

import org.joml.Vector3f;

// 카메라 클래스
public class Camera {

    // 위치 및 회전에 대한 벡터 정의
    private Vector3f position, rotation;

    // 위치 및 회전 초기화
    public Camera() {
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    // 생성자. 마찬가지로 초기화
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    // 위치 변화 계산 함수
    public void movePosition(float x, float y, float z) {
        if(z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.sin(Math.toRadians(rotation.y)) * z;
        }
        if(x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.sin(Math.toRadians(rotation.y - 90)) * x;
        }

        position.y += y;
    }

    // 위치 정의 함수
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    // 회전 정의 함수
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    // 회전에 대한 이동 계산 함수
    public void moveRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    // 위치 getter
    public Vector3f getPosition() {
        return position;
    }

    // 회전 getter
    public Vector3f getRotation() {
        return rotation;
    }
}
