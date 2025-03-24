package net.igneo.icv.entity.snakeBite;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SnakeBiteModel extends GeoModel<SnakeBiteEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");


    @Override
    public ResourceLocation getModelResource(SnakeBiteEntity snakeBiteEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(SnakeBiteEntity snakeBiteEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(SnakeBiteEntity snakeBiteEntity) {
        return this.animations;
    }
}
