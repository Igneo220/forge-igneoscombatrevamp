package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CascadeManager extends WeaponEnchantManager {
  private long airTimeStart = 0;
  
  public CascadeManager(Player player) {
    super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
  }
  
  @Override
  public boolean stableCheck() {
    return true;
  }
  
  @Override
  public void onAttack(Entity entity) {
    super.onAttack(entity);
    float fallBoost = (float) Math.max(0.75f, Math.log10(player.fallDistance));
    
    if (player.fallDistance > 0.0f && !player.onGround()) {
      player.setDeltaMovement(player.getDeltaMovement().x, fallBoost, player.getDeltaMovement().z);
    }
    
    System.out.println(player.fallDistance * 1.2);
    
    if (player.fallDistance * 1.2 >= 10) {
      player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
        SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1.5f, 0.5f);
    }
  }
  
  @Override
  public void activate() {
    super.activate();
    if (player.onGround()) {
      return;
    }
    
    airTimeStart = System.currentTimeMillis();
    long airTime = System.currentTimeMillis() - airTimeStart;
    float accelerationFactor = Math.min(1.0f + (airTime / 1000.0f), 1.0f);
    
    Vec3 slamVelocity = new Vec3(0, -1 * accelerationFactor, 0);
    player.addDeltaMovement(slamVelocity);
    player.hurtMarked = true;
  }
  
  @Override
  public void tick() {
    super.tick();
    if (!player.onGround() || airTimeStart == 0) {
      return;
    }
    
    airTimeStart = 0;
    if (player.fallDistance <= 3) {
      return;
    }
    
    float damage = Math.min(6.0f, player.fallDistance * 0.5f);
    float radius = Math.min(10, player.fallDistance * 1.2f);
    
    renderRadius((ServerLevel) player.level(), player.position(), radius);
    List<Entity> nearbyEntities = player.level().getEntities(player, player.getBoundingBox().inflate(radius));
    
    for (Entity entity : nearbyEntities) {
      Vec3 push = entity.position().subtract(player.position()).normalize().scale(2);
      entity.hurt(entity.damageSources().playerAttack(player), damage);
      entity.addDeltaMovement(push);
      entity.hurtMarked = true;
    }
    player.fallDistance = 0;
  }
  
  private void renderRadius(ServerLevel level, Vec3 center, double radius) {
    ParticleOptions hitboxParticle = ParticleTypes.ELECTRIC_SPARK;
    
    int pointsPerRing = 32 * (int) player.fallDistance;
    
    for (int i = 0; i < pointsPerRing; i++) {
      double angle = 2 * Math.PI * i / pointsPerRing;
      double x = center.x + radius * Math.cos(angle);
      double z = center.z + radius * Math.sin(angle);
      
      level.sendParticles(
        hitboxParticle,
        x, center.y, z,
        1, 0, 0, 0, 0
      );
    }
  }
}