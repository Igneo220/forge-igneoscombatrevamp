package net.igneo.icv.shader.shader;

import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.StasisManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet.BlackHoleManager;
import net.igneo.icv.entity.helmet.blackHole.BlackHoleEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.postprocess.DynamicShaderFxInstance;

import java.util.function.BiConsumer;

public class BlackHoleFx extends DynamicShaderFxInstance {
    public Vector3f center;
    public BlackHoleEntity blackHole;

    public BlackHoleFx(BlackHoleEntity blackHole) {
        this.blackHole = blackHole;
        this.center = new Vector3f((float) blackHole.position().x, (float) blackHole.position().y, (float) blackHole.position().z);
    }

    @Override
    public void update(double deltaTime) {
        System.out.println(center);
        if (blackHole == null || !blackHole.isAlive()) {
            this.remove();
        }
        center = new Vector3f((float) blackHole.position().x, (float) blackHole.position().y, (float) blackHole.position().z);
        super.update(deltaTime);
    }

    @Override
    public void writeDataToBuffer(BiConsumer<Integer, Float> writer) {
        writer.accept(0, center.x());
        writer.accept(1, center.y());
        writer.accept(2, center.z());
    }
}