package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.GustS2CPacket;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class ViperManager extends WeaponEnchantManager implements EntityTracker {

  ICVEntity child;
  boolean nullCheck = false;

  public ViperManager(Player player) {
    super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
  }
  
  @Override
  public void activate() {
    super.activate();
    if (player.level() instanceof ServerLevel level) {
      child = ModEntities.SNAKE_BITE.get().create(player.level());
      child.setOwner(player);
      child.setPos(player.getEyePosition().subtract(0,1,0));
      child.setDeltaMovement(ICVUtils.getFlatDirection(player.getYRot(),1,0));
      level.addFreshEntity(child);
      syncClientChild((ServerPlayer) player,child,this);
      nullCheck = true;
    }
  }

  @Override
  public float getDamageBonus() {
    if (target == null) {
      return 0;
    } else if (target instanceof LivingEntity entity) {
      if (!entity.getActiveEffects().isEmpty()) {
        for (MobEffectInstance instance : entity.getActiveEffects()) {
          MobEffect effect = instance.getEffect();
          if (!effect.isBeneficial()) {
            int duration = instance.getDuration() - 20;
            entity.removeEffect(effect);
            if (duration > 0) {
              entity.addEffect(new MobEffectInstance(effect,duration,instance.getAmplifier(),instance.isAmbient(),instance.isVisible()));
            }
          }
        }
        return 4;
      }
    }
    return 0;
  }

  @Override
  public boolean canUse() {
    return super.canUse() && child == null;
  }

  @Override
  public ICVEntity getChild() {
    return child;
  }

  @Override
  public void setChild(ICVEntity entity) {
    child = entity;
  }

  @Override
  public void tick() {
    super.tick();
    if (nullCheck && player instanceof ServerPlayer serverPlayer && child == null) {
      syncClientChild(serverPlayer,null,this);
      nullCheck = false;
    }
  }
}