package com.xm666.realisticbirdbehavior.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.xm666.realisticbirdbehavior.Config;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ModelMixin {
    private static final float youngScaleFactor = 0.5F;
    private static final float bodyYOffset = 24.0F;
    private static final float bodyXOffset = 0.4F;

    @Mixin(Parrot.class)
    private static class ParrotMixin {
        @ModifyReturnValue(method = "isBaby", at = @At("RETURN"))
        private boolean isBaby(boolean original) {
            var self = (Parrot) (Object) this;
            return original || self.getAge() < 0;
        }
    }

    @Mixin(HierarchicalModel.class)
    private static class HierarchicalModelMixin {
        @Inject(method = "renderToBuffer", at = @At("HEAD"), cancellable = true)
        private void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color, CallbackInfo ci) {
            var self = (HierarchicalModel<?>) (Object) this;
            if (self instanceof ParrotModel && self.young) {
                poseStack.pushPose();
                poseStack.scale(youngScaleFactor, youngScaleFactor, youngScaleFactor);
                poseStack.translate(0.0F, bodyYOffset / 16.0F, 0.0F);
                self.root().render(poseStack, buffer, packedLight, packedOverlay, color);
                poseStack.popPose();
                ci.cancel();
            }
        }
    }

    @Mixin(ParrotOnShoulderLayer.class)
    private static class ParrotOnShoulderLayerMixin {
        @Inject(method = "lambda$render$1", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER))
        private void render(PoseStack poseStack, boolean leftShoulder, Player livingEntity, CompoundTag compoundtag, MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, EntityType<?> p_262538_, CallbackInfo ci) {
            if (compoundtag.getInt("Age") < 0) {
                var bodyXOffset = ModelMixin.bodyXOffset - Config.SHOULDER_BODY_X_OFFSET.get();
                poseStack.scale(youngScaleFactor, youngScaleFactor, youngScaleFactor);
                poseStack.translate(leftShoulder ? bodyXOffset : -bodyXOffset, 0.0F, 0.0F);
            }
        }
    }
}
