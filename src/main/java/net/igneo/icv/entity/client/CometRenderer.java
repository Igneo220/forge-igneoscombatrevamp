package net.igneo.icv.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.igneo.icv.ICV;
import net.igneo.icv.entity.custom.CometEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip.TEXTURE_LOCATION;

public class CometRenderer extends EntityRenderer<CometEntity> {
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);
    private final CometModel<CometEntity> model;
    public CometRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CometModel<>(pContext.bakeLayer(ModModelLayers.COMET_LAYER));
    }
    public void render(CometEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
        float f2 = (float)pEntity.tickCount + pPartialTicks;
        pPoseStack.translate(0.0F, 0.15F, 0.0F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        pPoseStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(pPoseStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CometEntity pEntity) {
        return new ResourceLocation(ICV.MOD_ID, "textures/entity/cometTexture.png");
    }


}
