package net.igneo.icv.entity.comet;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.blackHole.BlackHoleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CometModel extends GeoModel<CometEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/comet.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/comet.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/comet.animation.json");


    @Override
    public ResourceLocation getModelResource(CometEntity cometEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(CometEntity cometEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(CometEntity cometEntity) {
        return this.animations;
    }
}
