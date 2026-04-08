package com.ofekn.crafting_on_a_stick.neoforge;


import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.item.CoasItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Coas.MID)
public class CoasNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public CoasNeoForge(IEventBus bus) {
        LOGGER.info("Hello NeoForge world!");
        Coas.init();
        DeferredRegister.Items itemsReg = DeferredRegister.createItems(Coas.MID);
        List<DeferredItem<?>> items = new ArrayList<>();
        for (CoasItem<?> item : CoasItem.getItems()) {
            items.add(registerItem(itemsReg, item));
        }
        itemsReg.register(bus);

        DeferredRegister<CreativeModeTab> tabsReg = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Coas.MID);
        tabsReg.register("tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.crafting_on_a_stick"))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> CoasItem.CRAFTING_TABLE.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    for (DeferredItem<?> item : items) {
                        output.accept(item);
                    }
                }).build()
        );
        tabsReg.register(bus);

    }

    private <I extends Item> DeferredItem<I> registerItem(DeferredRegister.Items itemsReg, CoasItem<I> item) {
        DeferredItem<I> deferredItem = itemsReg.registerItem(item.getName(), item.getConstructor(), item.getProperties());
        item.bind(deferredItem);
        return deferredItem;
    }
}