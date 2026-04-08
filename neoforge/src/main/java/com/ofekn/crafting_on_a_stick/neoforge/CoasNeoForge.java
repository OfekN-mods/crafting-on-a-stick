package com.ofekn.crafting_on_a_stick.neoforge;


import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.item.CoasItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Coas.MID)
public class CoasNeoForge {
    public CoasNeoForge(IEventBus bus, ModContainer modContainer) {
        Coas.init();

        DeferredRegister.Items itemsReg = DeferredRegister.createItems(Coas.MID);
        for (CoasItem<?> item : CoasItem.getItems()) {
            registerItem(itemsReg, item);
        }
        itemsReg.register(bus);

        DeferredRegister<CreativeModeTab> tabsReg = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Coas.MID);
        tabsReg.register("tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.crafting_on_a_stick"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> CoasItem.CRAFTING_TABLE.get().getDefaultInstance())
                .displayItems((_, output) -> {
                    for (var item : CoasItem.getItems()) {
                        output.accept(item.get());
                    }
                }).build()
        );
        tabsReg.register(bus);

        NeoForgeConfigIntegration.register(modContainer);
    }

    private <I extends Item> void registerItem(DeferredRegister.Items itemsReg, CoasItem<I> item) {
        item.bind(itemsReg.registerItem(item.getName(), item.getConstructor(), item.getProperties()));
    }
}