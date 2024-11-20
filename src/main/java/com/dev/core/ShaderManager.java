package com.dev.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import java.util.HashMap;
import java.util.Map;

// 쉐이더 매니저 클래스, 쉐이딩을 관리함.
public class ShaderManager {

    // 쉐이딩을 적용할 프로그램의 ID / 윈도우 상
    private final int programID;

    // 버텍스 쉐이터 및 프래그먼트 쉐이더
    private int vertexShaderID, fragmentShaderID;

    private final Map<String, Integer> uniforms;

    // 프로그램에 대한 쉐이더 작성. 프로그램 ID가 0 -> 즉 비정상적으로 실행되었을 때에는 쉐이더를 생성하지 않음.
    public ShaderManager() throws Exception {
        programID = GL20.glCreateProgram();
        if(programID == 0)
            throw new Exception("Could not create shader");

        uniforms = new HashMap<>();
    }

    // 렌더 메니저에서 설정한 uniform들에 대한 쉐이딩을 직접적으로 적용하는 구간.
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if(uniformLocation < 0)
            throw new Exception("Could not find uniform " + uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    // 각 쉐이더 처리에 대한 메모리 관리 / 메모리 스택에 대한 접근 및 푸쉬 관리
    public void setUniform(String uniformname, Matrix4f value) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(uniforms.get(uniformname), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformname, Vector4f value) {
        GL20.glUniform4f(uniforms.get(uniformname), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformname, Vector3f value) {
        GL20.glUniform3f(uniforms.get(uniformname), value.x, value.y, value.z);
    }

    public void setUniform(String uniformname, boolean value) {
        float res = 0;
        if(value)
            res = 1;
        GL20.glUniform1f(uniforms.get(uniformname), res);
    }

    // uniform 정보 설정 함수
    public void setUniform(String uniformname, int value) {
        GL20.glUniform1i(uniforms.get(uniformname), value);
    }

    public void setUniform(String uniformname, float value) {
        GL20.glUniform1f(uniforms.get(uniformname), value);
    }

    // 버텍스 쉐이터 생성 함수.
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    // 프래그먼트 쉐이더 생성 함수.
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    // 메인 쉐이더 생성 함수
    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0)
            throw new Exception("Error creating shader. Type : " + shaderType);

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compling shader code: TYPe: " + shaderType
            + " Info " + GL20.glGetShaderInfoLog(shaderID, 1024));

        GL20.glAttachShader(programID, shaderID);

        return shaderID;
    }

    // 쉐이더 병합 함수
    public void link() throws Exception {
        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0)
            throw new Exception("Error linking shader code "
            + " Info " + GL20.glGetProgramInfoLog(programID, 1024));

        if(vertexShaderID != 0)
            GL20.glDetachShader(programID, vertexShaderID);

        if(fragmentShaderID != 0)
            GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0)
            throw new Exception("Unable to validate shader code: " + GL20.glGetProgramInfoLog(programID, 1024));
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if(programID != 0)
            GL20.glDeleteProgram(programID);
    }
}
