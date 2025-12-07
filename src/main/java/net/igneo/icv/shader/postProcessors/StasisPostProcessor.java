package net.igneo.icv.shader.postProcessors;

import com.mojang.blaze3d.vertex.PoseStack;
import net.igneo.icv.ICV;
import net.igneo.icv.shader.shader.StasisFx;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import java.util.ArrayList;

public class StasisPostProcessor extends MultiInstancePostProcessor<StasisFx> {
    public static final StasisPostProcessor INSTANCE = new StasisPostProcessor();
    private EffectInstance effectStasis;

    private final ArrayList<StasisFx> instances = new ArrayList<>();

    @Override
    public ResourceLocation getPostChainLocation() {
        return new ResourceLocation(ICV.MOD_ID, "stasis_post");
    }

    // Max amount of FxInstances that can be added to the post processor at once
    @Override
    protected int getMaxInstances() {
        return 16;
    }

    // We passed in a total of 6 floats/uniforms to the shader inside our LightingFx class so this should return 6, will crash if it doesn't match
    @Override
    protected int getDataSizePerInstance() {
        return 6;
    }

    @Override
    public void init() {
        super.init();
        if (postChain != null) {
            effectStasis = effects[0];
        }
    }

    @Nullable
    @Override
    public StasisFx addFxInstance(StasisFx instance) {
        System.out.println("adding instance");
        instances.add(instance);
        return super.addFxInstance(instance);
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        super.beforeProcess(viewModelStack);
        setDataBufferUniform(effectStasis, "DataBuffer", "InstanceCount");
    }

    @Override
    public void afterProcess() {

    }
}