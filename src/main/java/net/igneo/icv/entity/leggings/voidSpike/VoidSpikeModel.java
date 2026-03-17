package net.igneo.icv.entity.leggings.voidSpike;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidSpikeModel extends GeoModel<VoidSpikeEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/voidspike.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/voidspike.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/placeholder.animation.json");
    
    @Override
    public ResourceLocation getModelResource(VoidSpikeEntity surfWaveEntity) {
        return model;
    }
    
    @Override
    public ResourceLocation getTextureResource(VoidSpikeEntity surfWaveEntity) {
        return texture;
    }
    
    @Override
    public ResourceLocation getAnimationResource(VoidSpikeEntity surfWaveEntity) {
        return animations;
    }
}
