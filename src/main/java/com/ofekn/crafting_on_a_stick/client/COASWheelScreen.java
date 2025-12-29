package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ofekn.crafting_on_a_stick.COASConfig;
import com.ofekn.crafting_on_a_stick.COASUtils;
import com.ofekn.crafting_on_a_stick.api.Ref;
import com.ofekn.crafting_on_a_stick.api.IWheelItem;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class COASWheelScreen extends Screen {
    public static final List<WheelLayoutSupplier> POSSIBLE_LAYOUTS = new ArrayList<>();
    static {
        POSSIBLE_LAYOUTS.add(RoundWheelLayout.INSTANCE);
        POSSIBLE_LAYOUTS.add(ListWheelLayout.INSTANCE);
    }
    private static ItemStack lastSelection = ItemStack.EMPTY;
    private final Player player;
    private ItemStack selectionItem;
    private int selectionIndex;
    private WheelLayoutSupplier layoutSupplier = RoundWheelLayout.INSTANCE;
    private int openTick;

    public static void trigger(Minecraft minecraft, Player player) {
        List<ItemStack> options = getOptions(player);
        if (options.isEmpty()) {
            return;
        }
        ItemStack firstOption = options.getFirst();
        if (options.size() == 1 || !COASKeyMappings.OPEN_CURIOS_KEY.isDown()) {
            PacketDistributor.sendToServer(new SBOpen(firstOption));
            return;
        }
        minecraft.setScreen(new COASWheelScreen(Component.empty(), player, firstOption));
    }

    protected COASWheelScreen(Component title, Player player, ItemStack firstOption) {
        super(title);
        this.player = player;
        this.selectionItem = firstOption;
        this.selectionIndex = 0;
        this.openTick = 0;

        String layoutName = COASConfig.Client.WHEEL_TYPE.get().toLowerCase();
        for (WheelLayoutSupplier possibleLayout : POSSIBLE_LAYOUTS) {
            if (possibleLayout.getSerializedName().equals(layoutName)) {
                this.layoutSupplier = possibleLayout;
                break;
            }
        }
        if (this.layoutSupplier == null) {
            this.layoutSupplier = POSSIBLE_LAYOUTS.getFirst();
            COASConfig.Client.WHEEL_TYPE.set(this.layoutSupplier.getSerializedName());
        }
    }

    public static List<ItemStack> getOptions(Player player) {
        List<ItemStack> allOptions = COASUtils.getFullInventory(player)
                .stream()
                .map(Ref::get)
                .map(stack -> stack.getItem() instanceof IWheelItem item ? item.getWheelRepresentative(player, stack) : ItemStack.EMPTY)
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
        
        // Deduplicate using ItemStack.isSameItemSameComponents
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack stack : allOptions) {
            boolean isDuplicate = false;
            for (ItemStack existing : result) {
                if (ItemStack.isSameItemSameComponents(stack, existing)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                result.add(stack);
            }
        }
        
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            select();
            return true;
        }
        if (button == 1) {
            int index = POSSIBLE_LAYOUTS.indexOf(layoutSupplier);
            int newIndex = (index + 1) % POSSIBLE_LAYOUTS.size();
            this.layoutSupplier = POSSIBLE_LAYOUTS.get(newIndex);
            COASConfig.Client.WHEEL_TYPE.set(this.layoutSupplier.getSerializedName());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        openTick++;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!COASKeyMappings.OPEN_CURIOS_KEY.isDown()) {
            select();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void select() {
        this.onClose();
        if (!selectionItem.isEmpty()) {
            lastSelection = selectionItem;
            PacketDistributor.sendToServer(new SBOpen(selectionItem));
        }
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

        float t = Math.min(1.0f, getAnimationT(partialTick));

        List<ItemStack> options = getOptions(player);

        int numOptions = options.size();
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        if (numOptions == 0) {
            int textColor = applyAlpha(0xFFFFFFFF, t);
            guiGraphics.drawCenteredString(font, Component.translatable("gui.crafting_on_a_stick.selection_wheel.no_tool"), (int)centerX, (int)centerY, textColor);
            return;
        }
        float scale = easeScale(t);
        pos.pushPose();
        pos.translate(centerX, centerY, 0);
        pos.scale(scale, scale, 1.0f);


        WheelPolygon[] layout = getLayout(numOptions);


        for (int i = 0; i < numOptions; i++) {
            WheelPolygon polygon = layout[i];
            int baseColor = selectionIndex == i ? 0xFFFFFFFF : 0x80FFFFFF;
            polygon.fill(guiGraphics, RenderType.gui(), 0, applyAlpha(baseColor, t));

            float x = polygon.center().x;
            float y = polygon.center().y;
            pos.pushPose();
            pos.translate(x, y, 0);
            guiGraphics.renderFakeItem(options.get(i), -8, -8);
            pos.popPose();

            if (selectionIndex == i) {
                int overlayColor = applyAlpha(0x7FFFFFFF, t);
                polygon.fill(guiGraphics, RenderType.guiOverlay(), 10, overlayColor);
            }
        }
        pos.popPose();
        if (t > 0.99f) { // Only show tooltip when mostly faded in
            guiGraphics.renderTooltip(font, selectionItem, mouseX, mouseY);
        }
        // this is cool looking, but can have issues if it's too wide compared to the window
//        guiGraphics.renderTooltip(font, selection, (int)(centerX + radius + 8), (int)(centerY - radius));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float t = getAnimationT(partialTick);
        guiGraphics.fill(0, 0, width, height, applyAlpha(0x40000000, t));
    }

    private float easeScale(float t) {
        // alternative easing
//        return = (float) Math.pow(t, 4);
        float c1 = 1.70158f;
        float c3 = c1 + 1;
        return (float) (1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2));

    }

    private float getAnimationT(float partialTick) {
        // snap opens
//        return openTick > 4 ? 1 : 0;
        float t = (openTick + partialTick - 4) / 6;
        return Math.clamp(t, 0.0f, 1.0f);
    }

    private int applyAlpha(int color, float t) {
        int a = (int)((color >>> 24) * t) & 0xFF;
        return (a << 24) | (color & 0x00FFFFFF);
    }

    private WheelPolygon[] getLayout(int numOptions) {
        return layoutSupplier.apply(numOptions);
    }
}
