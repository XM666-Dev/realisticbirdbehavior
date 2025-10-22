package com.xm666.realisticbirdbehavior;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(RealisticBirdBehavior.MODID)
public class RealisticBirdBehavior {
    public static final String MODID = "realisticbirdbehavior";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealisticBirdBehavior(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
