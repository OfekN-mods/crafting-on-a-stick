package com.ofekn.crafting_on_a_stick.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;

public class DummyContainerLevelAccess implements ContainerLevelAccess {
    private final Entity entity;
    @Nullable
    private BiFunction<Level, BlockPos, @Nullable Object> dummySupplier;

    public DummyContainerLevelAccess(Entity entity) {
        this.entity = entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, @Nullable T> func) {
        if (dummySupplier != null) {
            return Optional.ofNullable((T)dummySupplier.apply(entity.level(), entity.blockPosition()));
        }
        return Optional.ofNullable(func.apply(entity.level(), entity.blockPosition()));
    }

    public void setDummySupplier(@Nullable BiFunction<Level, BlockPos, @Nullable Object> dummySupplier) {
        this.dummySupplier = dummySupplier;
    }
}