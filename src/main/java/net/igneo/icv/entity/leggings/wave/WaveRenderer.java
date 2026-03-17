package net.igneo.icv.entity.leggings.wave;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.igneo.icv.entity.boots.surfWave.SurfWaveEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaveRenderer extends GeoEntityRenderer<WaveEntity> {
    public WaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WaveModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, WaveEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        this.applyRotations(animatable, poseStack, (float)animatable.tickCount + partialTick, Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()),partialTick);

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
