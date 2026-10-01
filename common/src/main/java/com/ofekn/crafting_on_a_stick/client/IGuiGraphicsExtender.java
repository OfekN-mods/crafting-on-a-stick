package com.ofekn.crafting_on_a_stick.client;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import org.joml.Vector2f;

public interface IGuiGraphicsExtender {
	void coas$renderColoredPolygon(RenderPipeline pipeline, Vector2f[] points, int color);
}
