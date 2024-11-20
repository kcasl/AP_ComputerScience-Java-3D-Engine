package com.dev.core.entity;

import org.joml.Vector3f;

import java.util.Vector;

// 엔티티 클래스 -> 물체 등..
public class Entity {
    // 모델 클래스에서 관련 정보 참조
    private Model model;

    // 엔티티 위치 및 회전 정보
    private Vector3f pos, rotation;

    // 크기 정의
    private float scale;

    // 엔티티 생성자.
    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    // 위치 (수동) 이동에 대한 변화 계산 함수
    public void incPos(float x, float y, float z) {
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    // 시작 위치 설정 함수
    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    // 회전 (수동) 에 대한 변화 계산 함수
    public void incRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    // 회전 설정
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    // getter / setter
    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
}
