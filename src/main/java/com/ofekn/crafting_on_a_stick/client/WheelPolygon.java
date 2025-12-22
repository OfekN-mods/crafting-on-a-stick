package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public record WheelPolygon(Vector2f[] points, Vector2f center) {

    public void fill(GuiGraphics guiGraphics, RenderType renderType, float z, int color) {
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        VertexConsumer consumer = guiGraphics.bufferSource().getBuffer(renderType);
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
                consumer.addVertex(matrix4f, p.x, p.y, z).setColor(color);
            }
        }
        guiGraphics.flush();
    }
}
