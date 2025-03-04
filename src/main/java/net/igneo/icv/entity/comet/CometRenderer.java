package net.igneo.icv.entity.comet;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CometRenderer extends GeoEntityRenderer<CometEntity> {
    public CometRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CometModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
