package net.igneo.icv.entity.boots.stonePillar;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StonePillarModel extends GeoModel<StonePillarEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/stone_pillar.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/stone_pillar.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/stone_pillar.animation.json");
    
    @Override
    public ResourceLocation getModelResource(StonePillarEntity surfWaveEntity) {
        return model;
    }
    
    @Override
    public ResourceLocation getTextureResource(StonePillarEntity surfWaveEntity) {
        return texture;
    }
    
    @Override
    public ResourceLocation getAnimationResource(StonePillarEntity surfWaveEntity) {
        return animations;
    }
}
