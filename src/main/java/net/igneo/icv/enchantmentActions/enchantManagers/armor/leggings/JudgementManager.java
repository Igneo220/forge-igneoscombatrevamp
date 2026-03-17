package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.igneo.icv.ICV;
import net.igneo.icv.client.animation.EnchantAnimationPlayer;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.leggings.JudgementCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class JudgementManager extends ArmorEnchantManager {
    public boolean kicking = false;
    private int kickTicks = 0;
    private Vec3 kickPos;
    private Entity judged;
    
    public JudgementManager(Player player) {
        super(EnchantType.LEGGINGS, 100, -10, false, player);
    }
    
    @Override
    public void activate() {
        active = true;
        System.out.println("activating");
        if (player.level() instanceof ClientLevel) {
            this.animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_jump"))));
        }
        kickPos = this.player.position();
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new JudgementCooldownIndicator(this);
    }
    
    @Override
    public boolean canUse() {
        return true;
    }

    private void preHit() {
        judged.hurt(player.damageSources().playerAttack(player),5);
        LodestoneParticles.judgementParticles(player.level(),judged.getEyePosition());
        player.level().playSound(null, player.blockPosition(), ModSounds.PARRY.get(), SoundSource.PLAYERS);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (active) {
            //Windup and cancel check
            if (activeTicks == 24) {
                launch();
            } else if (activeTicks < 24) {
                if (activeTicks == 12) {
                    telegraph();
                }
                if (kickPos != null) player.setPos(kickPos);
            } else {
                if (!player.onGround() || kicking) {
                    if (player.level().isClientSide) {
                        if (!animator.isActive()) {
                            animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_idle"))));
                        }
                    }
                    hitCheck();
                } else if (activeTicks > 30) {
                    miss();
                }
            }
        }
    }

    private void telegraph() {
        double yaw = Math.toRadians(player.getYRot());
        double angleEdit = 10;
        double particleScale = 0.7;
        double x = -Math.sin(yaw+angleEdit);
        double z = Math.cos(yaw+angleEdit);

        player.level().playSound(null, player.blockPosition(), ModSounds.JUDGEMENT_USE.get(), SoundSource.PLAYERS);

        Vec3 particleVec = new Vec3(x,0,z).scale(particleScale).reverse();
        LodestoneParticles.rockParticles(player.level(), player.position().add(particleVec),0.2f);
        LodestoneParticles.smokeParticles(player.level(),player.position().add(particleVec), null);
        x = -Math.sin(yaw-angleEdit);
        z = Math.cos(yaw-angleEdit);
        particleVec = new Vec3(x,0,z).scale(particleScale).reverse();
        LodestoneParticles.rockParticles(player.level(), player.position().add(particleVec),0.2f);
        LodestoneParticles.smokeParticles(player.level(),player.position().add(particleVec), null);
    }

    private void hitCheck() {
        if (activeTicks > 24 && !kicking) {
            search();
        } else {
            if (kickTicks < 10) {
                ++kickTicks;
                player.setPos(kickPos.add(0,0.1,0));
            } else {
                trueHit();
            }
        }
    }

    private void trueHit() {
        double yaw = Math.toRadians(player.getYRot());
        double x = -Math.sin(yaw);
        double y = 0.5;
        double z = Math.cos(yaw);
        double scale = 1.5;

        player.level().playSound(null, player.blockPosition(), ModSounds.JUDGEMENT_HIT.get(), SoundSource.PLAYERS);

        Vec3 flatDirection = new Vec3(x * scale, y, z * scale);
        judged.setDeltaMovement(flatDirection);
        if (player.level() instanceof ServerLevel level) {
            ICVUtils.explode(judged.getEyePosition(),level);
        }
        for (Entity subject : ICVUtils.collectEntitiesBox(player.level(),player.position(),4)) {
            if (subject instanceof LivingEntity entity && entity != player) {
                entity.hurt(player.damageSources().playerAttack(player),10);
            }
        }
        scale = -0.5;
        flatDirection = new Vec3(x * scale, y, z * scale);
        player.setDeltaMovement(flatDirection);
        activeTicks = 0;
        active = false;
        kicking = false;
        kickPos = null;
        kickTicks = 0;
    }

    private void search() {
        System.out.println("searching");
        kickPos = this.player.position();
        LodestoneParticles.smokeParticles(player.level(),player.position(), new Color(52, 211, 175));
        for (Entity entity : player.level().getEntities(null, player.getBoundingBox().inflate(1.2))) {
            if (entity != player && !(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrb)) {
                kicking = true;
                judged = entity;
                preHit();
                if (player.level().isClientSide) {
                    animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_hit"))));
                }
            }
        }
    }

    private void miss() {
        System.out.println("we missed");
        if (player.level().isClientSide) {
            animator.setAnimation(new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "judgement_miss"))));
        }
        double yaw = Math.toRadians(player.getYRot());
        double x = -Math.sin(yaw);
        double y = 0;
        double z = Math.cos(yaw);
        double scale = 2.5;

        Vec3 flatDirection = new Vec3(x * scale, y, z * scale);
        player.setDeltaMovement(flatDirection);
        activeTicks = 0;
        active = false;
        kicking = false;
        kickPos = null;
        kickTicks = 0;
    }

    private void launch() {
        double yaw = Math.toRadians(player.getYRot());
        double x = -Math.sin(yaw);
        double y = 0.35;
        double z = Math.cos(yaw);
        double scale = 3.5;

        player.level().playSound(null, player.blockPosition(), ModSounds.JUDGEMENT_LAUNCH.get(), SoundSource.PLAYERS);
        Vec3 flatDirection = new Vec3(x * scale, y, z * scale);
        player.setDeltaMovement(flatDirection);
    }
}
