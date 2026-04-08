package com.ofekn.crafting_on_a_stick.integration;

public interface IConfigIntegration {
    String getWheelType();
    void setWheelType(String wheelType);

    boolean getStoreItems();

    IConfigIntegration DEFAULT = new IConfigIntegration() {
        String wheelType = "round";

        @Override
        public String getWheelType() {
            return wheelType;
        }

        @Override
        public void setWheelType(String wheelType) {
            this.wheelType = wheelType;
        }

        @Override
        public boolean getStoreItems() {
            return true;
        }
    };
}
