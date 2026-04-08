package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.joml.Matrix3x2fc;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

public record ColoredPolygonRenderState(
			RenderPipeline pipeline,
			TextureSetup textureSetup,
			Matrix3x2fc pose,
			Vector2f[] points,
			int color,
			@Nullable ScreenRectangle scissorArea,
			@Nullable ScreenRectangle bounds
	) implements GuiElementRenderState {

	public ColoredPolygonRenderState(
			RenderPipeline pipeline,
			TextureSetup textureSetup,
			Matrix3x2fc pose,
			Vector2f[] points,
			int color
	) {
		this(pipeline, textureSetup, pose, points, color, null, createBounds(pose, points));
	}

	private static ScreenRectangle createBounds(Matrix3x2fc pose, Vector2f[] points) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Vector2f p : points) {
			Vector2f q = pose.transformPosition(p, new Vector2f());
			minX = Math.min(minX, (int)q.x);
			minY = Math.min(minY, (int)q.y);
			maxX = Math.max(maxX, (int)q.x);
			maxY = Math.max(maxY, (int)q.y);
		}
		return new ScreenRectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public void buildVertices(VertexConsumer vertexConsumer) {
		int nPoints = this.points.length;
		for (int i = 0; 2 * i + 4 <= nPoints; i++) {
			// last iteration should be
			// i + 1 = nPoints - i - 2 or i + 2 = nPoints - i - 2
			// 2i + 3 = nPoints or 2i + 4 = nPoints
			Vector2f[] quadPoints = new Vector2f[] {
					this.points[i],
					this.points[i + 1],
					this.points[nPoints - i - 2],
					this.points[nPoints - i - 1],
			};
			for (Vector2f p : quadPoints) {
				vertexConsumer.addVertexWith2DPose(this.pose, p.x, p.y).setColor(this.color);
			}
		}
	}
}