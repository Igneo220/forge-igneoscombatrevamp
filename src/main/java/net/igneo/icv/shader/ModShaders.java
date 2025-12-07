package net.igneo.icv.shader;

import net.igneo.icv.shader.postProcessors.BlinkPostProcessor;
import net.igneo.icv.shader.postProcessors.StasisPostProcessor;
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

public class ModShaders {
    private static void registerShader(MultiInstancePostProcessor shader) {
        PostProcessHandler.addInstance(shader);
    }
    
    public static void register() {
        registerShader(BlinkPostProcessor.INSTANCE);
        registerShader(StasisPostProcessor.INSTANCE);
    }
}
