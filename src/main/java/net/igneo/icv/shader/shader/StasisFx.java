package net.igneo.icv.shader.shader;

import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.StasisManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.postprocess.DynamicShaderFxInstance;

import java.util.function.BiConsumer;

public class StasisFx extends DynamicShaderFxInstance {
    public Vector3f center;
    public Vector3f color;
    public Player player;

    public StasisFx(Vector3f center, Vector3f color, Player player) {
        this.center = center;
        this.color = color;
        this.player = player;
    }

    @Override
    public void update(double deltaTime) {
        StasisManager manager = (StasisManager) ICVUtils.getManagerForType(this.player, StasisManager.class);
        if (manager == null || !manager.active) {
            this.remove();
        }
        super.update(deltaTime);
    }

    @Override
    public void writeDataToBuffer(BiConsumer<Integer, Float> writer) {
        writer.accept(0, center.x());
        writer.accept(1, center.y());
        writer.accept(2, center.z());
        writer.accept(3, color.x());
        writer.accept(4, color.y());
        writer.accept(5, color.z());
    }
}