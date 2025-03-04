package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import com.lowdragmc.photon.client.fx.*;
import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class CometStrikeManager extends WeaponEnchantManager{
    private BlockPos cometSpawn;
    private BlockEffect effect;
    public static final UUID COMET_SPEED_MODIFIER_UUID = UUID.fromString("8a23719c-852d-47fc-bb41-8527955288d4");
    public CometStrikeManager(Player player) {
        super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
    }

    @Override
    public void onEquip() {
        super.onEquip();
        applyPassive();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        removePassive();
    }

    @Override
    public void applyPassive() {
        super.applyPassive();
        System.out.println("applying buff");
        player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(
                new AttributeModifier(COMET_SPEED_MODIFIER_UUID,
                        "Comet strike speed boost",
                        0.02F,
                        AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void removePassive() {
        player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(COMET_SPEED_MODIFIER_UUID);
    }

    public void spawnComet() {
        if (player.level() instanceof ServerLevel level) {
            if (cometSpawn == null) {
                System.out.println("grabbing new location");
                int x = 0;
                int z = 0;
                double i = 0.4;
                if (player.getLookAngle().x > i) {
                    x = 2;
                } else if (player.getLookAngle().x < -i) {
                    x = -2;
                }
                if (player.getLookAngle().z > i) {
                    z = 2;
                } else if (player.getLookAngle().z < -i) {
                    z = -2;
                }
                cometSpawn = new BlockPos(player.getBlockX() + x, player.getBlockY(), player.getBlockZ() + z);
                FX fx = FXHelper.getFX(new ResourceLocation(ICV.MOD_ID,"comet_land"));
                if (fx != null) {
                    effect = new BlockEffect(fx,player.level(),cometSpawn);
                    effect.setAllowMulti(true);
                    effect.start();
                }
            }
            if (activeTicks > 30) {
                ModEntities.COMET.get().spawn(level, cometSpawn, MobSpawnType.COMMAND);
                //level.sendParticles(ParticleTypes.END_ROD, cometSpawn.getX() + 0.5, cometSpawn.getY() + 0.5, cometSpawn.getZ() + 0.5, 15, 0, 0, 0, 0.1);
                level.playSound(null, cometSpawn, ModSounds.COMET_SPAWN.get(), SoundSource.PLAYERS, 0.5F,1);
                active = false;
                cometSpawn = null;
                if (effect != null) effect.setForcedDeath(true);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (activeTicks > 10) {
            spawnComet();
        }
    }
}
