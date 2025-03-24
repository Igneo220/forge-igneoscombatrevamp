package net.igneo.icv.entity.soulSpider;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.snakeBite.SnakeBiteEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SoulSpiderModel extends GeoModel<SoulSpiderEntity> {
    private final ResourceLocation model = new ResourceLocation(ICV.MOD_ID, "geo/soul_spider.geo.json");
    private final ResourceLocation texture = new ResourceLocation(ICV.MOD_ID, "textures/entity/soul_spider.png");
    private final ResourceLocation animations = new ResourceLocation(ICV.MOD_ID, "animations/soul_spider.animation.json");


    @Override
    public ResourceLocation getModelResource(SoulSpiderEntity soulSpiderEntity) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(SoulSpiderEntity soulSpiderEntity) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(SoulSpiderEntity soulSpiderEntity) {
        return this.animations;
    }
}
