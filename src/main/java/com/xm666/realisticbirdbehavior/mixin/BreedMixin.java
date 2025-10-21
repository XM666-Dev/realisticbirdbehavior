package com.xm666.realisticbirdbehavior.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BreedMixin {
    @Mixin(Parrot.class)
    private static class ParrotMixin {
        @Inject(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 6))
        private void registerGoals(CallbackInfo ci) {
            var self = (Parrot) (Object) this;
            self.goalSelector.addGoal(3, new BreedGoal(self, 1.0F));
        }

        @ModifyReturnValue(method = "canMate", at = @At("RETURN"))
        private boolean canMate(boolean original, Animal animal) {
            var self = (Parrot) (Object) this;
            return animal != self && animal instanceof Parrot && self.isInLove() && animal.isInLove();
        }

        @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Parrot;setOrderedToSit(Z)V"), cancellable = true)
        private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack itemstack) {
            if (itemstack.is(ItemTags.PARROT_FOOD)) {
                var self = (Parrot) (Object) this;
                itemstack.consume(1, player);
                self.setInLove(player);
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }
}
