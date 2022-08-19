package com.tom.cpm.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import com.tom.cpm.shared.model.render.BatchedBuffers.BufferOutput;

public class VBufferOut implements BufferOutput<VertexConsumer> {
	private int light, overlay;
	private Matrix4f mat4;
	private Matrix3f mat3;

	public VBufferOut(int light, int overlay, PoseStack matrixStackIn) {
		this.light = light;
		this.overlay = overlay;
		mat4 = matrixStackIn.last().pose();
		mat3 = matrixStackIn.last().normal();
	}

	@Override
	public void push(VertexConsumer buffer, float x, float y, float z, float red, float green,
			float blue, float alpha, float u, float v, float nx, float ny, float nz) {
		buffer.vertex(mat4, x, y, z);
		buffer.color(red, green, blue, alpha);
		buffer.uv(u, v);
		buffer.overlayCoords(overlay);
		buffer.uv2(light);
		buffer.normal(mat3, nx, ny, nz);
		buffer.endVertex();
	}
}
