package com.xm666.realisticbirdbehavior.handler;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

@EventBusSubscriber
public class SpawnHandler {
    @SubscribeEvent
    public static void onBabyEntitySpawn(BabyEntitySpawnEvent event) {
        if (event.getParentA() instanceof Parrot parrotA && event.getParentB() instanceof Parrot parrotB) {
            var child = EntityType.PARROT.create(parrotA.level());
            if (child != null) {
                if (parrotA.getRandom().nextBoolean()) {
                    child.setVariant(parrotA.getVariant());
                } else {
                    child.setVariant(parrotB.getVariant());
                }
                if (parrotA.isTame()) {
                    child.setOwnerUUID(parrotA.getOwnerUUID());
                    child.setTame(true, true);
                }
                event.setChild(child);
            }
        }
    }
}
