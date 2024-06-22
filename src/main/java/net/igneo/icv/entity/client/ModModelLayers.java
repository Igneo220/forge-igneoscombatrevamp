package net.igneo.icv.entity.client;

import net.igneo.icv.ICV;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation COMET_LAYER = new ModelLayerLocation(
            new ResourceLocation(ICV.MOD_ID, "comet_layer"), "main");
    public static final ModelLayerLocation BLACK_HOLE_LAYER = new ModelLayerLocation(
            new ResourceLocation(ICV.MOD_ID, "black_hole_layer"), "main");
    public static final ModelLayerLocation BOLT_LAYER = new ModelLayerLocation(
            new ResourceLocation(ICV.MOD_ID, "bolt"), "main");
    public static final ModelLayerLocation ICICLE_LAYER = new ModelLayerLocation(
            new ResourceLocation(ICV.MOD_ID, "icicle"), "main");
}
