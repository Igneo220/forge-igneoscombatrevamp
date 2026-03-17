package net.igneo.icv.entity.leggings.voidSpike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.igneo.icv.entity.leggings.wave.WaveEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class VoidSpikeRenderer extends GeoEntityRenderer<VoidSpikeEntity> {
    public VoidSpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VoidSpikeModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, VoidSpikeEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        Vec3 move = animatable.facing;

        if (!animatable.onGround()) {
            move = animatable.getDeltaMovement();
        } else if (animatable.launched) {
            move = new Vec3(1,0,1);
        }
        if (move.length() == 0) {
            move = new Vec3(0,1,0);
        }

        animatable.faceDirection(move);
        poseStack.mulPose(Axis.XP.rotationDegrees((float) -Math.toDegrees(Math.asin(move.y/move.length()))));


        this.applyRotations(animatable, poseStack, (float)animatable.tickCount + partialTick, Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()),partialTick);
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

    }

}
