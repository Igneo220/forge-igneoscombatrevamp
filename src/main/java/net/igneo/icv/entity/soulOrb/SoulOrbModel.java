package net.igneo.icv.entity.soulOrb;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.snakeBite.SnakeBiteEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulOrbModel extends GeoModel<SoulOrbEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/placeholder.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/placeholder.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");


    @Override
    public ResourceLocation getModelResource(SoulOrbEntity soulOrbEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(SoulOrbEntity soulOrbEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(SoulOrbEntity soulOrbEntity) {
        return this.animations;
    }
}
