package com.ofekn.crafting_on_a_stick.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.ofekn.crafting_on_a_stick.client.ColoredPolygonRenderState;
import com.ofekn.crafting_on_a_stick.client.IGuiGraphicsExtender;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsMixin implements IGuiGraphicsExtender {
	@Final
	@Shadow
    GuiRenderState guiRenderState;

	@Override
	public void coas$renderColoredPolygon(RenderPipeline pipeline, Vector2f[] points, int color) {
		guiRenderState.addGuiElement(new ColoredPolygonRenderState(
				pipeline,
				TextureSetup.noTexture(),
				new Matrix3x2f(((GuiGraphicsExtractor)(Object)this).pose()),
				points,
				color
		));
	}
}
