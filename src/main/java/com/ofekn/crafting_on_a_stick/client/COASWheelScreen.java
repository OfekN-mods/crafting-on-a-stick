package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ofekn.crafting_on_a_stick.COASUtils;
import com.ofekn.crafting_on_a_stick.ItemOnAStick;
import com.ofekn.crafting_on_a_stick.Ref;
import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.List;
import java.util.stream.Collectors;

public class COASWheelScreen extends Screen {
    private static ItemStack lastSelection = ItemStack.EMPTY;
    private final Player player;
    private ItemStack selection;

    public static void trigger(Minecraft minecraft, Player player) {
        List<ItemStack> options = getOptions(player);
        if (options.isEmpty()) {
            return;
        }
        ItemStack firstOption = options.getFirst();
        if (options.size() == 1) {
            PacketDistributor.sendToServer(new SBOpen(firstOption));
            return;
        }
        minecraft.setScreen(new COASWheelScreen(Component.literal("Select tool"), player, firstOption));
    }

    protected COASWheelScreen(Component title, Player player, ItemStack firstOption) {
        super(title);
        this.player = player;
        this.selection = firstOption;
    }

    public static List<ItemStack> getOptions(Player player) {
        List<ItemStack> result = COASUtils.getFullInventory(player)
                .stream()
                .map(Ref::get)
                .filter(item -> item.getItem() instanceof ItemOnAStick)
                .distinct()
                .collect(Collectors.toList());
        for (int i = 0; i < result.size(); i++) {
            if (ItemStack.isSameItemSameComponents(result.get(i), lastSelection)) {
                result.remove(i);
                result.addFirst(lastSelection);
                break;
            }
        }
        return result;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!COASKeyMappings.OPEN_CURIOS_KEY.isDown()) {
            this.onClose();
            if (!selection.isEmpty()) {
                lastSelection = selection;
                PacketDistributor.sendToServer(new SBOpen(selection));
            }
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        PoseStack pos = guiGraphics.pose();

        List<ItemStack> options = getOptions(player);

        int numOptions = options.size();
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        if (numOptions == 0) {
            // TODO translatable
            guiGraphics.drawCenteredString(font, Component.literal("You don't have any tool"), (int)centerX, (int)centerY, 0xFFFFFFFF);
            return;
        }
        pos.pushPose();
        pos.translate(centerX, centerY, 0);
        int numAround = numOptions - 1;
        float anglePerSection = 2 * (float)Math.PI / numAround;

        float dmx = mouseX - centerX;
        float dmy = mouseY - centerY;
        float maxRadius = Math.min(centerX, centerY) * 0.75f;
        // 2 * PI * r <= numOptions * 16 * sqrt(2)
        float radius = numOptions == 1 ? 0 : Math.clamp(32, maxRadius, numOptions * 4);
        float selectionRadians = Math.atan2(dmx, -dmy);

        int selectionIndex;
        if (dmx * dmx + dmy * dmy <= 16 * 16) {
            selectionIndex = 0;
        } else {
            if (selectionRadians < 0) {
                selectionRadians += (float) (2 * Math.PI);
            }
            selectionIndex = Math.round(selectionRadians / anglePerSection);
            // ensure between 0 and numAround - 1
            selectionIndex = (selectionIndex % numAround + numAround) % numAround;
            // ensure between 1 and numOptions - 1
            selectionIndex++;
        }
        selection = options.get(selectionIndex);

        OptionPolygon[] layout = getLayoutCircular(numOptions);

        for (int i = 0; i < numOptions; i++) {
            OptionPolygon polygon = layout[i];
            int color = selectionIndex == i ? 0xFFFFFFFF : 0x80FFFFFF;
            int x = (int)polygon.center.x;
            int y = (int)polygon.center.y;
            drawPolygon(guiGraphics, RenderType.gui(), polygon, 0, color);

            guiGraphics.renderFakeItem(options.get(i), x - 8, y - 8);
            if (selectionIndex == i) {
                drawPolygon(guiGraphics, RenderType.guiOverlay(), polygon, 10, 0x7FFFFFFF);
            }
        }
        pos.popPose();
        guiGraphics.renderTooltip(minecraft.font, selection, mouseX, mouseY);
        // this is cool looking, but can have issues if it's too wide compared to the window
//        guiGraphics.renderTooltip(font, selection, (int)(centerX + radius + 8), (int)(centerY - radius));
    }

    private OptionPolygon[] getLayoutPolygonal(int numOptions) {
        // TODO fix for 1 or 2 items
        int numAround = numOptions - 1;
        Vector2f[] points = new Vector2f[numAround * 2];
        float anglePerSection = 2 * (float)Math.PI / numAround;
        float r1 = 32;
        float r2 = 64;
        for (int i = 0; i < numAround; i++) {
            float rad = (i - 0.5f) * anglePerSection;
            float dx = Math.sin(rad);
            float dy = -Math.cos(rad);
            points[i] = new Vector2f(r1 * dx, r1 * dy);
            points[i + numAround] = new Vector2f(r2 * dx, r2 * dy);
        }

        OptionPolygon[] result = new OptionPolygon[numOptions];
        Vector2f[] innerPoints = new Vector2f[numAround];
        for (int i = 0; i < numAround; i++) {
            innerPoints[i] = points[numAround - i - 1];
        }
        result[0] = new OptionPolygon(
                innerPoints,
//                Arrays.copyOf(points, numAround),
                new Vector2f(0, 0)
        );

        float r = 48;
        for (int i = 0; i < numAround; i++) {
            float rad = i * anglePerSection;
            float dx = Math.sin(rad);
            float dy = -Math.cos(rad);
            Vector2f center = new Vector2f(r * dx, r * dy);

            int i2 = (i + 1) % numAround;
            result[i + 1] = new OptionPolygon(new Vector2f[] {
                    points[i + numAround],
                    points[i],
                    points[i2],
                    points[i2 + numAround],
            }, center);
        }

        return result;
    }

    private OptionPolygon[] getLayoutCircular(int numOptions) {
        int numAround = numOptions - 1;
        int circlePoints = 360;
        Vector2f[] points = new Vector2f[circlePoints * 2];
        float r1 = 32;
        float r2 = 64;
        for (int i = 0; i < circlePoints; i++) {
            float rad = (float) (i * Math.PI * 2 / circlePoints);
            float dx = Math.sin(rad);
            float dy = -Math.cos(rad);
            points[i] = new Vector2f(r1 * dx, r1 * dy);
            points[i + circlePoints] = new Vector2f(r2 * dx, r2 * dy);
        }

        OptionPolygon[] result = new OptionPolygon[numOptions];

        Vector2f[] innerPoints = new Vector2f[circlePoints];
        for (int i = 0; i < circlePoints; i++) {
            innerPoints[i] = points[circlePoints - i - 1];
        }
        result[0] = new OptionPolygon(
                innerPoints,
                new Vector2f(0, 0)
        );

        int[] startIndices = new int[numAround];
        for (int i = 0; i < numAround; i++) {
            // ((i - 0.5) / numAround) * circlePoints
            int startIndex = (i * circlePoints - circlePoints / 2) / numAround;
            startIndices[i] = positiveMod(startIndex, circlePoints);
        }

        float anglePerSection = 2 * (float)Math.PI / numAround;
        float r = 48;
        for (int i = 0; i < numAround; i++) {
            float rad = i * anglePerSection;
            float dx = Math.sin(rad);
            float dy = -Math.cos(rad);
            Vector2f center = new Vector2f(r * dx, r * dy);

            int i2 = (i + 1) % numAround;
            int startIndex = startIndices[i];
            int endIndex = startIndices[i2];
            int halfPolygonPoints = i == i2 ? circlePoints + 1 : positiveMod(endIndex - startIndex, circlePoints) + 1;
            Vector2f[] polygonPoints = new Vector2f[halfPolygonPoints * 2];
            for (int j = 0; j < halfPolygonPoints; j++) {
                int pointIndex = positiveMod(startIndex + j, circlePoints);
                polygonPoints[j] = points[pointIndex];
                polygonPoints[polygonPoints.length - j - 1] = points[pointIndex + circlePoints];
            }
            result[i + 1] = new OptionPolygon(polygonPoints, center);
        }

        return result;
    }

    private int positiveMod(int a, int b) {
        return (a % b + b) % b;
    }

    private void drawPolygon(GuiGraphics guiGraphics, RenderType renderType, OptionPolygon polygon, float z, int color) {
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        VertexConsumer consumer = guiGraphics.bufferSource().getBuffer(renderType);
        int nPoints = polygon.points.length;
        for (int i = 0; 2 * i + 4 <= nPoints; i++) {
            // last iteration should be
            // i + 1 = nPoints - i - 2 or i + 2 = nPoints - i - 2
            // 2i + 3 = nPoints or 2i + 4 = nPoints
            Vector2f[] quadPoints = new Vector2f[] {
                    polygon.points[i],
                    polygon.points[i + 1],
                    polygon.points[nPoints - i - 2],
                    polygon.points[nPoints - i - 1],
            };
            for (Vector2f p : quadPoints) {
                consumer.addVertex(matrix4f, p.x, p.y, z).setColor(color);
            }
        }
        guiGraphics.flush();
    }


    record OptionPolygon(Vector2f[] points, Vector2f center) {}
}
