package net.igneo.icv.entity.soulOrb;

import net.igneo.icv.entity.snakeBite.SnakeBiteEntity;
import net.igneo.icv.entity.snakeBite.SnakeBiteModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SoulOrbRenderer extends GeoEntityRenderer<SoulOrbEntity> {

    public SoulOrbRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SoulOrbModel());
    }
}
