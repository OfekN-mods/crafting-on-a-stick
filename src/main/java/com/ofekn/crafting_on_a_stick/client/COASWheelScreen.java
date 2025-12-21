package com.ofekn.crafting_on_a_stick.client;

import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Math;

import java.util.List;

public class COASWheelScreen extends Screen {
    private final List<ItemStack> options;
    private int choiceIndex;

    protected COASWheelScreen(Component title, List<ItemStack> options) {
        super(title);
        this.options = options;
        this.choiceIndex = 0;
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
            if (choiceIndex < options.size()) {
                PacketDistributor.sendToServer(new SBOpen(options.get(choiceIndex)));
            }
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int numOptions = options.size();
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        float dmx = mouseX - centerX;
        float dmy = mouseY - centerY;
        float maxRadius = Math.min(centerX, centerY) * 0.75f;
        // 2 * PI * r <= numOptions * 16 * sqrt(2)
        float radius = numOptions == 1 ? 0 : Math.clamp(32, maxRadius, numOptions * 4);
        float choiceRadians = Math.atan2(dmx, -dmy);
        choiceIndex = (int)Math.round(Math.toDegrees(choiceRadians) * numOptions / 360);
        // ensure between 0 and numOptions - 1
        choiceIndex = (choiceIndex % numOptions + numOptions) % numOptions;

        if (dmx * dmx + dmy * dmy <= 16 * 16) {
            choiceIndex = 0;
        }

        for (int i = 0; i < numOptions; i++) {
            int color = choiceIndex == i ? 0xFFFFFFFF : 0x7FFFFFFF;
            float rad = Math.toRadians(i * 360 / (float)numOptions);
            int x = (int)(centerX + Math.sin(rad) * radius);
            int y = (int)(centerY - Math.cos(rad) * radius);
            guiGraphics.fill(x - 9, y - 9, x + 9, y + 9, color);
            guiGraphics.renderFakeItem(options.get(i), x - 8, y - 8);
        }

    }

//    @Override
//    protected void renderMenuBackground(GuiGraphics partialTick) {
//        super.renderMenuBackground(partialTick);
//    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }
}
