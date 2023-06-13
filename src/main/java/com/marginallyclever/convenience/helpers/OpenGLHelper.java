package com.marginallyclever.convenience.helpers;

import com.jogamp.opengl.GL3;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A collection of static methods to help with OpenGL.
 *
 * @author Dan Royer
 * @since 2.5.0
 */
public class OpenGLHelper {
	static public int drawAtopEverythingStart(GL3 gl) {
		IntBuffer depthFunc = IntBuffer.allocate(1);
		gl.glGetIntegerv(GL3.GL_DEPTH_FUNC, depthFunc);
		gl.glDepthFunc(GL3.GL_ALWAYS);
		return depthFunc.get();
	}
	
	static public void drawAtopEverythingEnd(GL3 gl, int previousState) {
		gl.glDepthFunc(previousState);
	}

	static public float setLineWidth(GL3 gl,float newWidth) {
		FloatBuffer lineWidth = FloatBuffer.allocate(1);
		gl.glGetFloatv(GL3.GL_LINE_WIDTH, lineWidth);
		gl.glLineWidth(newWidth);
		return lineWidth.get(0);
	}

	public static boolean disableTextureStart(GL3 gl) {
		boolean b = gl.glIsEnabled(GL3.GL_TEXTURE_2D);
		gl.glDisable(GL3.GL_TEXTURE_2D);
		return b;
	}
	
	public static void disableTextureEnd(GL3 gl,boolean oldState) {
		if(oldState) gl.glEnable(GL3.GL_TEXTURE_2D);
	}
}
