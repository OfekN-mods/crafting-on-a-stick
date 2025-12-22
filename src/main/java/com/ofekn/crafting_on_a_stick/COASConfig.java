package com.ofekn.crafting_on_a_stick;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class COASConfig {
	private COASConfig() {}

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.BooleanValue STORE_ITEMS = BUILDER
			.comment("Whether to store items in the crafting stations when they are closed")
			.define("storeItems", true);

	static final ModConfigSpec SPEC = BUILDER.build();

	public static boolean getStoreItems() {
		return STORE_ITEMS.get();
	}

    public static final class Client {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        public static final ModConfigSpec.ConfigValue<String> WHEEL_TYPE = BUILDER
                .comment("The kind of wheel to use, currently there are only \"round\" and \"list\"")
                .define("wheelType", "round");

        static final ModConfigSpec SPEC = BUILDER.build();

    }
}
