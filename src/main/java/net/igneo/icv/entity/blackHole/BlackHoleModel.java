package net.igneo.icv.entity.blackHole;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BlackHoleModel extends GeoModel<BlackHoleEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/blackhole.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/blackhole.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/blackhole.animation.json");


    @Override
    public ResourceLocation getModelResource(BlackHoleEntity blackHoleEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(BlackHoleEntity blackHoleEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(BlackHoleEntity blackHoleEntity) {
        return this.animations;
    }
}
