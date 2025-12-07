package net.igneo.icv.init;

import net.igneo.icv.particle.LodestoneModParticles;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import javax.annotation.Nullable;
import java.awt.*;

public class LodestoneParticles {
    public static void blinkParticles(Level level, Vec3 pos) {
        WorldParticleBuilder.create(LodestoneModParticles.BLINK_PARTICLE)
                .setLifetime(20)
                .setColorData(ColorParticleData.create(new Color(164, 29, 206, 255)).build())
                .setTransparencyData(GenericParticleData.create(1f, 1f).setEasing(Easing.SINE_IN).build())
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .setScaleData(GenericParticleData.create(0.5F, 2, 0.1F).build())
                .setSpinData(SpinParticleData.create(0, 0, 1).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }

    public static void stasisParticles(Level level, Vec3 pos) {
        WorldParticleBuilder.create(LodestoneModParticles.STASIS_BREAK_PARTICLE)
                .setLifetime(60)
                .setTransparencyData(GenericParticleData.create(0.5F,1,0).build())
                .setScaleData(GenericParticleData.create(0.1F,0.2F,0.1F).build())
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .setMotion(new Vec3(Math.random() - 0.5,Math.random() - 0.5,Math.random() - 0.5).scale(0.05))
                .spawn(level, pos.x + (Math.random() - 0.5), pos.y + (Math.random() - 0.5), pos.z + (Math.random() - 0.01));
    }
    public static void stasisBreakParticles(Level level, Vec3 pos, Vec3 motion) {
        WorldParticleBuilder.create(LodestoneModParticles.STASIS_PARTICLE)
                .setLifetime(6)
                .setTransparencyData(GenericParticleData.create(1f, 1f).setEasing(Easing.SINE_IN).build())
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .setTransparencyData(GenericParticleData.create(1,0).build())
                .setScaleData(GenericParticleData.create(0.5F).build())
                .setMotion(motion)
                .spawn(level, pos.x, pos.y, pos.z);
    }

    public static void stasisDirectionParticles(Level level, Vec3 pos) {
        WorldParticleBuilder.create(LodestoneModParticles.STASIS_DIR_PARTICLE)
                .setLifetime(5)
                .setTransparencyData(GenericParticleData.create(1f, 1f).setEasing(Easing.SINE_IN).build())
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.RANDOM_SPRITE)
                .setTransparencyData(GenericParticleData.create(1,1,0).build())
                .setScaleData(GenericParticleData.create(0.2F).build())
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)
                .spawn(level, pos.x, pos.y - Math.random()/5, pos.z);
    }

    public static void smokeParticles(Level level, Vec3 pos, @Nullable Color color) {
        if (color == null) {
            color = new Color(129, 76, 42);
        }
        WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setScaleData(GenericParticleData.create(3f, 0).build())
                .setLifetime(60)
                .setLightLevel(2)
                .setColorData(ColorParticleData.create(color).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }

    public static void rockParticles(Level level, Vec3 pos, float yv) {
        WorldParticleBuilder.create(LodestoneModParticles.ROCK_PARTICLE)
                .setNaturalLighting()
                .setLifetime(50)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.RANDOM_SPRITE)
                .setTransparencyData(GenericParticleData.create(1,1,0).build())
                .setScaleData(GenericParticleData.create(0.5F).build())
                .setMotion((Math.random()-0.5)/5,yv,(Math.random()-0.5)/5)
                .setGravityStrength(1.2F)
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)
                .spawn(level, pos.x, pos.y, pos.z);
    }
    public static void waveParticles(Level level, Vec3 pos, Vec3 motion) {
        System.out.println(pos);
        Color startingColor = new Color(124, 234, 204,255);
        Color endingColor = new Color(0, 87, 187,255);
        WorldParticleBuilder.create(LodestoneModParticles.WAVE_PARTICLE)
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setLifetime(20)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .addMotion(motion.x*4, 0.01f, motion.z*4)
                .setGravityStrength(0.04f)
                .setFrictionStrength(0.05F)
                .setNoClip(false)
                .disableCull()
                .setLightLevel(6)
                .setColorData(ColorParticleData.create(startingColor, endingColor).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }
    public static void waveParticlesBright(Level level, Vec3 pos, Vec3 motion) {
        System.out.println(pos);
        Color startingColor = new Color(124, 234, 204,255);
        Color endingColor = new Color(0, 87, 187,255);
        WorldParticleBuilder.create(LodestoneModParticles.WAVE_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setLifetime(20)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .addMotion(motion.x*4, 0.01f, motion.z*4)
                .setGravityStrength(0.04f)
                .setFrictionStrength(0.05F)
                .setNoClip(false)
                .disableCull()
                .setColorData(ColorParticleData.create(startingColor, endingColor).build())
                .spawn(level, pos.x, pos.y, pos.z);
    }
    public static void upParticles(Level level, Vec3 pos, Vec3 motion) {
        WorldParticleBuilder.create(LodestoneModParticles.UP_PARTICLE)
                .setLifetime(6)
                .setTransparencyData(GenericParticleData.create(1f, 1f).setEasing(Easing.SINE_IN).build())
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)
                .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)
                .setTransparencyData(GenericParticleData.create(1,0).build())
                .setScaleData(GenericParticleData.create(0.3F).build())
                .setMotion(motion)
                .spawn(level, pos.x, pos.y, pos.z);
    }
}
