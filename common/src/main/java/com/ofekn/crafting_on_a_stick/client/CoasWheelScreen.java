package com.ofekn.crafting_on_a_stick.client;

import com.ofekn.crafting_on_a_stick.CoasUtils;
import com.ofekn.crafting_on_a_stick.api.IWheelItem;
import com.ofekn.crafting_on_a_stick.api.Ref;
import com.ofekn.crafting_on_a_stick.integration.CoasIntegrations;
import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CoasWheelScreen extends Screen {
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
		if (options.size() == 1 || !CoasKeyMappings.OPEN_CURIOS_KEY.isDown()) {
            CoasIntegrations.PLATFORM.sendPacketToServer(new SBOpen(firstOption));
			return;
		}
		minecraft.setScreen(new CoasWheelScreen(Component.empty(), player, firstOption));
	}

	protected CoasWheelScreen(Component title, Player player, ItemStack firstOption) {
		super(title);
		this.player = player;
		this.selectionItem = firstOption;
		this.selectionIndex = 0;
		this.openTick = 0;

		String layoutName = CoasIntegrations.CONFIG.getWheelType();
		for (WheelLayoutSupplier possibleLayout : POSSIBLE_LAYOUTS) {
			if (possibleLayout.getSerializedName().equalsIgnoreCase(layoutName)) {
				this.layoutSupplier = possibleLayout;
				break;
			}
		}
		if (this.layoutSupplier == null) {
			this.layoutSupplier = POSSIBLE_LAYOUTS.getFirst();
            CoasIntegrations.CONFIG.setWheelType(this.layoutSupplier.getSerializedName());
		}
	}

	public static List<ItemStack> getOptions(Player player) {
		List<ItemStack> allOptions = CoasUtils.getFullInventory(player)
				.stream()
				.map(Ref::get)
				.map(stack -> stack.getItem() instanceof IWheelItem item ? item.getWheelRepresentative(player, stack) : ItemStack.EMPTY)
				.filter(stack -> !stack.isEmpty())
				.toList();
		
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
	public boolean mouseReleased(MouseButtonEvent event) {
		if (event.button() == 0) {
			select();
			return true;
		}
		if (event.button() == 1) {
			int index = POSSIBLE_LAYOUTS.indexOf(layoutSupplier);
			int newIndex = (index + 1) % POSSIBLE_LAYOUTS.size();
			this.layoutSupplier = POSSIBLE_LAYOUTS.get(newIndex);
            CoasIntegrations.CONFIG.setWheelType(this.layoutSupplier.getSerializedName());
			return true;
		}
		return super.mouseReleased(event);
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
	public boolean keyReleased(KeyEvent event) {
		if (!CoasKeyMappings.OPEN_CURIOS_KEY.isDown()) {
			select();
			return true;
		}
		return super.keyReleased(event);
	}

	private void select() {
		this.onClose();
		if (!selectionItem.isEmpty()) {
			lastSelection = selectionItem;
            CoasIntegrations.PLATFORM.sendPacketToServer(new SBOpen(selectionItem));
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
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
		Matrix3x2fStack pos = graphics.pose();

		float t = Math.min(1.0f, getAnimationT(partialTick));

		List<ItemStack> options = getOptions(player);

		int numOptions = options.size();
		float centerX = width * 0.5f;
		float centerY = height * 0.5f;
		if (numOptions == 0) {
			int textColor = applyAlpha(0xFFFFFFFF, t);
            graphics.centeredText(font, Component.translatable("gui.crafting_on_a_stick.selection_wheel.no_tool"), (int)centerX, (int)centerY, textColor);
			return;
		}
		float scale = easeScale(t);
		pos.pushMatrix();
		pos.translate(centerX, centerY);
		pos.scale(scale, scale);


		WheelPolygon[] layout = getLayout(numOptions);


		for (int i = 0; i < numOptions; i++) {
			WheelPolygon polygon = layout[i];
			int baseColor = selectionIndex == i ? 0xFFFFFFFF : 0x80FFFFFF;
			polygon.fill(graphics, RenderPipelines.GUI, 0, applyAlpha(baseColor, t));

			float x = polygon.center().x;
			float y = polygon.center().y;
			pos.pushMatrix();
			pos.translate(x, y);
            graphics.fakeItem(options.get(i), -8, -8);
			pos.popMatrix();

			if (selectionIndex == i) {
				int overlayColor = applyAlpha(0x7FFFFFFF, t);
				polygon.fill(graphics, RenderPipelines.GUI, 10, overlayColor);
			}
		}
		pos.popMatrix();
		if (t > 0.99f) { // Only show tooltip when mostly faded in
            graphics.setTooltipForNextFrame(
					this.font,
					getTooltipFromItem(minecraft, selectionItem),
					selectionItem.getTooltipImage(),
					mouseX,
					mouseY,
					selectionItem.get(DataComponents.TOOLTIP_STYLE)
			);
		}
		// this is cool looking, but can have issues if it's too wide compared to the window
//		guiGraphics.renderTooltip(font, selection, (int)(centerX + radius + 8), (int)(centerY - radius));
	}

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		float t = getAnimationT(partialTick);
		graphics.fill(0, 0, width, height, applyAlpha(0x40000000, t));
        this.minecraft.gui.extractDeferredSubtitles();
	}

	private float easeScale(float t) {
		// alternative easing
//		return = (float) Math.pow(t, 4);
		float c1 = 1.70158f;
		float c3 = c1 + 1;
		return (float) (1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2));

	}

	private float getAnimationT(float partialTick) {
		// snap opens
//		return openTick > 4 ? 1 : 0;
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
