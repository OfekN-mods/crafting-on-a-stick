package com.ofekn.crafting_on_a_stick.client;

import com.ofekn.crafting_on_a_stick.COASUtils;
import com.ofekn.crafting_on_a_stick.ItemOnAStick;
import com.ofekn.crafting_on_a_stick.Ref;
import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.minecraft.client.Minecraft;
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

        List<ItemStack> options = getOptions(player);

        int numOptions = options.size();
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        if (numOptions == 0) {
            // TODO translatable
            guiGraphics.drawCenteredString(font, Component.literal("You don't have any tool"), (int)centerX, (int)centerY, 0xFFFFFFFF);
            return;
        }
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

        for (int i = 0; i < numOptions; i++) {
            int color = selectionIndex == i ? 0xFFFFFFFF : 0x7FFFFFFF;
            float dx, dy;
            if (i == 0) {
                dx = 0;
                dy = 0;
            } else {
                float rad = (i - 1) * anglePerSection;
                dx = Math.sin(rad) * radius;
                dy = -Math.cos(rad) * radius;
            }
            int x = (int)(centerX + dx);
            int y = (int)(centerY + dy);
            guiGraphics.fill(x - 9, y - 9, x + 9, y + 9, color);
            guiGraphics.renderFakeItem(options.get(i), x - 8, y - 8);
        }
//        guiGraphics.renderTooltip(minecraft.font, selection, mouseX, mouseY);
        guiGraphics.renderTooltip(font, selection, (int)(centerX + radius + 8), (int)(centerY - radius));
    }
}
