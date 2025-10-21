package com.xm666.realisticbirdbehavior.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ModelMixin {
    //@Mixin(ParrotModel.class)
    //private static class ParrotModelMixin {
    //    @Shadow
    //    @Final
    //    private ModelPart body;

    //    @Inject(method = "prepareMobModel(Lnet/minecraft/world/entity/animal/Parrot;FFF)V", at = @At("TAIL"))
    //    private void prepare(Parrot entity, float limbSwing, float limbSwingAmount, float partialTick, CallbackInfo ci) {
    //        var self = (ParrotModel) (Object) this;
    //        self.young = entity.getAge() < 0;
    //        RealisticBirdBehavior.LOGGER.info("Parrot Model preparing and young is {}", self.young);
    //        if (self.young) {
    //            this.body.xScale = 0.5F;
    //            this.body.yScale = 0.5F;
    //            this.body.zScale = 0.5F;
    //        } else {
    //            this.body.xScale = 1.0F;
    //            this.body.yScale = 1.0F;
    //            this.body.zScale = 1.0F;
    //        }
    //    }
    //}

    //@Mixin(EntityModel.class)
    //private static class EntityModelMixin{
    //    @Inject(method = "render")
    //}

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
                var youngScaleFactor = 0.5F;
                var bodyYOffset = 24.0F;
                poseStack.pushPose();
                poseStack.scale(youngScaleFactor, youngScaleFactor, youngScaleFactor);
                poseStack.translate(0.0F, bodyYOffset / 16.0F, 0.0F);
                self.root().render(poseStack, buffer, packedLight, packedOverlay, color);
                poseStack.popPose();
                ci.cancel();
            }
        }
    }

    @Mixin(ParrotModel.class)
    private static class ParrotModelMixin {
        @Inject(method = "renderOnShoulder", at = @At("HEAD"), cancellable = true)
        private void renderOnShoulderHead(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, int tickCount, CallbackInfo ci) {
            var self = (ParrotModel) (Object) this;
            if (self.young) {
                var youngScaleFactor = 0.5F;
                var bodyYOffset = 24.0F;
                poseStack.pushPose();
                poseStack.scale(youngScaleFactor, youngScaleFactor, youngScaleFactor);
                poseStack.translate(0.0F, bodyYOffset / 16.0F, 0.0F);
                self.prepare(ParrotModel.State.ON_SHOULDER);
                self.setupAnim(ParrotModel.State.ON_SHOULDER, tickCount, limbSwing, limbSwingAmount, 0.0F, netHeadYaw, headPitch);
                self.root().render(poseStack, buffer, packedLight, packedOverlay);
                ci.cancel();
            }
        }

        //@Inject(method = "renderOnShoulder", at = @At("TAIL"))
        //private void renderOnShoulderTail(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, int tickCount, CallbackInfo ci) {
        //    var self = (ParrotModel) (Object) this;
        //    if (self.young) {
        //        poseStack.popPose();
        //    }
        //}
    }
}
