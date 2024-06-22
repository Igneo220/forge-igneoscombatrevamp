package net.igneo.icv.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.igneo.icv.ICV;
import net.igneo.icv.entity.custom.BlackHoleEntity;
import net.igneo.icv.entity.custom.BoltEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static com.ibm.icu.text.PluralRules.Operand.v;

public class BlackHoleRenderer<T> extends EntityRenderer<BlackHoleEntity> {
    private static final ResourceLocation BLACK_HOLE_TEXTURE_LOCATION = new ResourceLocation(ICV.MOD_ID,"textures/entity/blackholetexture.png");
    private final BlackHoleModel<Entity> blackHoleModel;

    public BlackHoleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
        this.blackHoleModel = new BlackHoleModel<>(context.bakeLayer(ModModelLayers.BLACK_HOLE_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(BlackHoleEntity pEntity) {
        return BLACK_HOLE_TEXTURE_LOCATION;
    }

    @Override
    public void render(BlackHoleEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        this.blackHoleModel.setupAnim(pEntity, 0.0F, 0.0F, 9999999.0F, 1, 1);

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees (Mth.lerp(v.ordinal(), pEntity.yRotO, pEntity.getYRot())));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees (Mth.lerp(v.ordinal(), pEntity.xRotO, pEntity.getXRot())));
        VertexConsumer vertexconsumer = this.getBuffer((T) pEntity, blackHoleModel, BLACK_HOLE_TEXTURE_LOCATION,
                pBuffer);
        blackHoleModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

    }

    public VertexConsumer getBuffer(T BlackHoleEntity, net.igneo.icv.entity.client.BlackHoleModel<Entity> iceShardModelIn,
                                    ResourceLocation iceShardTextureIn, MultiBufferSource multiBufferSource) {
        VertexConsumer buffer;
        buffer = multiBufferSource.getBuffer(iceShardModelIn.renderType(iceShardTextureIn));
        return buffer;
    }

    public void vertex(Matrix4f pMatrix, Matrix3f pNormal, VertexConsumer pConsumer, int pX, int pY, int pZ, float pU, float pV, int pNormalX, int pNormalZ, int pNormalY, int pPackedLight) {
        pConsumer.vertex(pMatrix, (float)pX, (float)pY, (float)pZ).color(255, 255, 255, 255).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(pNormal, (float)pNormalX, (float)pNormalY, (float)pNormalZ).endVertex();
    }
}
