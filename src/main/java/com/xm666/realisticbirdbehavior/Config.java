package com.xm666.realisticbirdbehavior;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue SHOULDER_BODY_X_OFFSET = BUILDER
            .defineInRange("shoulderBodyXOffset", 0.1F, Double.MIN_VALUE, Double.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
