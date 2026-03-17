package net.igneo.icv.entity.leggings.wave;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WaveModel extends GeoModel<WaveEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/surf.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/surf.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/surf.animation.json");


    @Override
    public ResourceLocation getModelResource(WaveEntity surfWaveEntity) {
        return model;
    }
    
    @Override
    public ResourceLocation getTextureResource(WaveEntity surfWaveEntity) {
        return texture;
    }
    
    @Override
    public ResourceLocation getAnimationResource(WaveEntity surfWaveEntity) {
        return animations;
    }
}
