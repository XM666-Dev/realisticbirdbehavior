package com.xm666.realisticbirdbehavior;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = RealisticBirdBehavior.MODID, dist = Dist.CLIENT)
public class RealisticBirdBehaviorClient {
    public RealisticBirdBehaviorClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
