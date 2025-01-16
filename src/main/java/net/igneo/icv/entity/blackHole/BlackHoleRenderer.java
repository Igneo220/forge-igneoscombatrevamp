package net.igneo.icv.entity.blackHole;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BlackHoleRenderer extends GeoEntityRenderer<BlackHoleEntity> {

    public BlackHoleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BlackHoleModel());
    }
}
