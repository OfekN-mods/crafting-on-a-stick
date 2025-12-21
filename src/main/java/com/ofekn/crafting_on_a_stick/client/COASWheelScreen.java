package com.ofekn.crafting_on_a_stick.client;

import com.ofekn.crafting_on_a_stick.COASUtils;
import com.ofekn.crafting_on_a_stick.ItemOnAStick;
import com.ofekn.crafting_on_a_stick.Ref;
import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Math;

import java.util.List;
import java.util.stream.Collectors;

public class COASWheelScreen extends Screen {
    private static ItemStack lastSelection = ItemStack.EMPTY;
    private final Player player;
    private ItemStack selection;

    protected COASWheelScreen(Component title, Player player) {
        super(title);
        this.player = player;
        List<ItemStack> options = getOptions();
        this.selection = options.isEmpty() ? ItemStack.EMPTY : options.getFirst();
    }

    public List<ItemStack> getOptions() {
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
            lastSelection = selection;
            if (!selection.isEmpty()) {
                PacketDistributor.sendToServer(new SBOpen(selection));
            }
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        List<ItemStack> options = getOptions();
        System.out.println(options);

        int numOptions = options.size();
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        if (numOptions == 0) {
            guiGraphics.drawCenteredString(minecraft.font, Component.literal("You don't have any tool"), (int)centerX, (int)centerY, 0xFFFFFFFF);
            return;
        }
        float dmx = mouseX - centerX;
        float dmy = mouseY - centerY;
        float maxRadius = Math.min(centerX, centerY) * 0.75f;
        // 2 * PI * r <= numOptions * 16 * sqrt(2)
        float radius = numOptions == 1 ? 0 : Math.clamp(32, maxRadius, numOptions * 4);
        float selectionRadians = Math.atan2(dmx, -dmy);
        int selectionIndex = (int)Math.round(Math.toDegrees(selectionRadians) * numOptions / 360);
        // ensure between 0 and numOptions - 1
        selectionIndex = (selectionIndex % numOptions + numOptions) % numOptions;

        selection = options.get(selectionIndex);

        if (dmx * dmx + dmy * dmy <= 16 * 16) {
            selectionIndex = 0;
        }

        for (int i = 0; i < numOptions; i++) {
            int color = selectionIndex == i ? 0xFFFFFFFF : 0x7FFFFFFF;
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
