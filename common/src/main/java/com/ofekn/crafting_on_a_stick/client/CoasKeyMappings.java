package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.List;

public final class CoasKeyMappings {
	private CoasKeyMappings() {}

	public static final KeyMapping OPEN_CURIOS_KEY = new KeyMapping(
			"crafting_on_a_stick.key.open_curios",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_V,
			KeyMapping.Category.INVENTORY
	);

    public static final List<KeyMapping> LIST = List.of(
            OPEN_CURIOS_KEY
    );
}
