package com.ofekn.crafting_on_a_stick.fabric;

import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.item.CoasItem;
import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class CoasFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Coas.init();
        registerItems();
        registerPackets();
        GsonConfigIntegration.load();
    }

    private void registerItems() {
        for (CoasItem<?> item : CoasItem.getItems()) {
            registerItem(item);
        }

        CreativeModeTab tab = FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.crafting_on_a_stick"))
                .icon(() -> CoasItem.CRAFTING_TABLE.get().getDefaultInstance())
                .displayItems((_, output) -> {
                    for (var item : CoasItem.getItems()) {
                        output.accept(item.get());
                    }
                }).build();

        ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
                Registries.CREATIVE_MODE_TAB, Coas.id("creative_tab")
        );
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tabKey, tab);
    }

    private <I extends Item> void registerItem(CoasItem<I> item) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Coas.id(item.getName()));
        I result = item.getConstructor().apply(item.getProperties().apply(new Item.Properties()).setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, result);
        item.bind(() -> result);
    }

    private void registerPackets() {
        PayloadTypeRegistry.serverboundPlay().register(SBOpen.TYPE, SBOpen.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SBOpen.TYPE, (payload, context) -> payload.handle(context.player()));
    }
}
