package com.ofekn.crafting_on_a_stick.integration;

public enum ClothConfigIntegration implements IConfigIntegration {
    INSTANCE;
    // TODO

    @Override
    public String getWheelType() {
        return "";
    }

    @Override
    public void setWheelType(String wheelType) {

    }

    @Override
    public boolean getStoreItems() {
        return false;
    }
}
