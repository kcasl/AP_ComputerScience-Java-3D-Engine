package com.dev.core.utils;

import com.dev.core.Camera;
import com.dev.core.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Vector;

// 변환 관련 유틸 클래스
public class Transformation {

    // Matrix4f에 대한 변형을 계산하는 클래스. -> 업데이트 / 렌더링 과정에서 같이 사용됨.
    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos()).
                rotateX((float) Math.toRadians(entity.getRotation().x)).
                rotateY((float) Math.toRadians(entity.getRotation().y)).
                rotateZ((float) Math.toRadians(entity.getRotation().z)).
                scale(entity.getScale());
        return matrix;
    }

    // 카메라를 통해 보이는 뷰 매트릭스를 계산하는 클래스. -> 3차원 투영 시점에 대한 계산.
    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1,0,0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0,1,0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0,0,1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }
}
