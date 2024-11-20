package com.dev.core.entity;

// 모델 클래스 -> 엔티티의 물리적 껍질이라고 생각할 수 있음
public class Model {
    // 모델 id
    private int id;

    // 정점 개수
    private int vertexCount;

    // 모델의 텍스쳐
    private Texture texture;

    // 생성자
    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
    }


    public Model(int id, int vertexCount, Texture texture) {
        this.id = id;
        this.vertexCount =vertexCount;
        this.texture = texture;
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.texture = texture;
    }

    // getter / setter
    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
