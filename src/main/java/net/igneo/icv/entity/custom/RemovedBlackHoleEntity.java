package net.igneo.icv.entity.custom;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.BlackHoleManager;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class RemovedBlackHoleEntity extends Fireball {
    public Vec3 trajectory;
    private long blackHoleTime = 0;
    private int holeDelay = 0;
    public RemovedBlackHoleEntity(EntityType<? extends Fireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        blackHoleTime = 0;
        this.setPos(this.getX(),this.getY() - 0.3,this.getZ());
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        if (level().isClientSide && FMLEnvironment.dist.isClient()) {
            if (this.getOwner() == null) {
                this.setOwner(level().getNearestPlayer(this,4));
            }
        }
        if (trajectory == null && !level().isClientSide && this.getOwner() != null) {
            trajectory = this.getOwner().getLookAngle().scale(0.15);
            this.setDeltaMovement(trajectory);
        }
        if (blackHoleTime == 0) {
            trajectory = null;
            if (!level().isClientSide) {
                level().playSound(null, this.blockPosition(), ModSounds.HOLE_IDLE.get(), SoundSource.PLAYERS, 20F, 1F);
            }
            blackHoleTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() >= blackHoleTime + 14000) {
            if (this.getOwner() != null && this.getOwner() instanceof Player player) {
                player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    if (enchVar.getManager(3) instanceof BlackHoleManager manager) {
                        manager.child = null;
                    }
                });
            }
            this.discard();
        }
        if (!level().isClientSide) {
            ServerLevel level = (ServerLevel) level();
            if (System.currentTimeMillis() > blackHoleTime + holeDelay) {
                level.sendParticles(ModParticles.BLACK_HOLE_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.01);
                holeDelay += 100;
            }
            for (Entity entity : level.getAllEntities()) {
                Vec3 pushVec = (((this.position().subtract(entity.position())).scale((10.1 - entity.distanceTo(this)) * 0.1)).scale(0.035));
                if (entity.distanceTo(this) < 2 && entity != this && entity != this.getOwner() && !(entity instanceof RemovedBlackHoleEntity)) {
                    if (entity instanceof LivingEntity || entity instanceof ServerPlayer) {
                        LivingEntity entity1 = (LivingEntity) entity;
                        entity1.hurt(this.damageSources().cramming(), 3);
                    }
                    entity.addDeltaMovement((this.position().subtract(entity.position())).scale(0.14));
                } else if (entity.distanceTo(this) < 10 && entity != this && entity != this.getOwner() && !(entity instanceof RemovedBlackHoleEntity)) {
                    entity.addDeltaMovement(pushVec);
                }
            }
        } /*else if (level().isClientSide){
                LocalPlayer player = Minecraft.getInstance().player;
                Vec3 pushVec = (((this.position().subtract(player.position())).scale((10.1 - player.distanceTo(this)) * 0.1)).scale(0.04));
                if (player.distanceTo(this) < 1 && Minecraft.getInstance().player != this.getOwner()) {
                    player.addDeltaMovement((this.position().subtract(player.position())).scale(0.14).scale(0.8));
                } else if (player.distanceTo(this) < 10 && Minecraft.getInstance().player != this.getOwner()) {
                    player.addDeltaMovement(pushVec.scale(0.7));
                }
                //&& uniPlayer != this.getOwner()
        }*/
        super.tick();
    }
}
