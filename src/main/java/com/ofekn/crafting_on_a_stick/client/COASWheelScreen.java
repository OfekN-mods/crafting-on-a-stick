package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.vertex.PoseStack;
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
import org.joml.Vector2f;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class COASWheelScreen extends Screen {
    private static ItemStack lastSelection = ItemStack.EMPTY;
    private final Player player;
    private ItemStack selectionItem;
    private int selectionIndex;
    // TODO config
    private final IntFunction<WheelPolygon[]> layoutSupplier = RoundWheelLayout.INSTANCE;

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
        // TODO translatable
        minecraft.setScreen(new COASWheelScreen(Component.literal("Select tool"), player, firstOption));
    }

    protected COASWheelScreen(Component title, Player player, ItemStack firstOption) {
        super(title);
        this.player = player;
        this.selectionItem = firstOption;
        this.selectionIndex = 0;
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
            if (!selectionItem.isEmpty()) {
                lastSelection = selectionItem;
                PacketDistributor.sendToServer(new SBOpen(selectionItem));
            }
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        float dmx = (float) mouseX - (float) width / 2;
        float dmy = (float) mouseY - (float) height / 2;

        List<ItemStack> options = getOptions(player);
        WheelPolygon[] layout = getLayout(options.size());
        float smallestDistance = Float.POSITIVE_INFINITY;
        int newSelection = 0;
        for (int i = 0; i < layout.length; i++) {
            for (Vector2f point : layout[i].points()) {
                float distance = point.distanceSquared(dmx, dmy);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    newSelection = i;
                }
            }
        }
        selectionIndex = newSelection;
        selectionItem = newSelection < options.size() ? options.get(newSelection) : ItemStack.EMPTY;
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


        WheelPolygon[] layout = getLayout(numOptions);


        for (int i = 0; i < numOptions; i++) {
            WheelPolygon polygon = layout[i];
            int color = selectionIndex == i ? 0xFFFFFFFF : 0x80FFFFFF;
            polygon.fill(guiGraphics, RenderType.gui(), 0, color);

            int x = (int)polygon.center().x;
            int y = (int)polygon.center().y;
            guiGraphics.renderFakeItem(options.get(i), x - 8, y - 8);

            if (selectionIndex == i) {
                polygon.fill(guiGraphics, RenderType.guiOverlay(), 10, 0x7FFFFFFF);
            }
        }
        pos.popPose();
        guiGraphics.renderTooltip(font, selectionItem, mouseX, mouseY);
        // this is cool looking, but can have issues if it's too wide compared to the window
//        guiGraphics.renderTooltip(font, selection, (int)(centerX + radius + 8), (int)(centerY - radius));
    }

    private WheelPolygon[] getLayout(int numOptions) {
        return layoutSupplier.apply(numOptions);
    }
}
