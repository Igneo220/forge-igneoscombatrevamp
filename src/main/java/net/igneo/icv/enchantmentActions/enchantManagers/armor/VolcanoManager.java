package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.TickTask;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayList;
import java.util.List;

public class VolcanoManager extends ArmorEnchantManager {
  
  private final List<Vec3> activeVolcanoes = new ArrayList<>();
  private static final int MAX_VOLCANOES = 3;
  private static final double VOLCANO_RADIUS = 5.0d;
  private static final float VOLCANO_DAMAGE = 6.0f;
  private static final int VOLCANO_DURATION = 300;
  private static final double VOLCANO_RANGE = 30.0d;
  
  public VolcanoManager(Player player) {
    super(EnchantType.BOOTS, 1, -10, true, player);
  }
  
  @Override
  public boolean canUse() {
    return true;
  }
  
  @Override
  public void onOffCoolDown(Player player) {
    activeVolcanoes.clear();
  }
  
  @Override
  public EnchantIndicator getIndicator() {
    return null;
  }
  
  @Override
  public void activate() {
    if (player.level() instanceof ServerLevel level) {
      if (activeVolcanoes.size() >= MAX_VOLCANOES) {
        return;
      }
      for (int i = 0; i < MAX_VOLCANOES; i++) {
        Vec3 targetPos = getTargetLocation();
        if (targetPos != null) {
          activeVolcanoes.add(targetPos);
          spawnVolcanicPillar(level, targetPos);
        }
      }
    }
  }
  
  private Vec3 getTargetLocation() {
    Vec3 playerPos = player.getEyePosition();
    Vec3 lookVector = player.getLookAngle().normalize().scale(VOLCANO_RANGE);
    Vec3 targetPos = playerPos.add(lookVector);
    
    BlockHitResult result = player.level().clip(
      new ClipContext(playerPos, targetPos,
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        player)
    );
    
    if (result.getType() == HitResult.Type.BLOCK) {
      Vec3 blockPos = Vec3.atCenterOf(result.getBlockPos());
      if (!player.level().getBlockState(result.getBlockPos().above()).isAir()) {
        return null;
      }
      return blockPos;
    }
    return null;
  }
  
  private void spawnVolcanicPillar(ServerLevel level, Vec3 position) {
    int tickInterval = 10;
    
    for (int tick = 0; tick < VOLCANO_DURATION; tick += tickInterval) {
      level.getServer().tell(new TickTask(level.getServer().getTickCount() + tick, () -> {
        List<Entity> nearbyEntities = level.getEntities(
          player,
          new AABB(position.subtract(VOLCANO_RADIUS, VOLCANO_RADIUS, VOLCANO_RADIUS),
            position.add(VOLCANO_RADIUS, VOLCANO_RADIUS, VOLCANO_RADIUS)),
          entity -> entity != player
        );
        
        for (Entity entity : nearbyEntities) {
          if (entity.position().distanceTo(position) <= VOLCANO_RADIUS) {
            entity.setSecondsOnFire(5);
            entity.hurt(level.damageSources().inFire(), VOLCANO_DAMAGE);
          }
        }
        
        renderRadius(level, position, (float) VOLCANO_RADIUS);
      }));
    }
  }
  
  private void renderRadius(ServerLevel level, Vec3 center, float radius) {
    ParticleOptions hitboxParticle = ParticleTypes.FLAME;
    int pointsPerRing = 32;
    int ringCount = 10;
    float heightStep = 0.5f;
    
    for (int j = 0; j < ringCount; j++) {
      float yOffset = j * heightStep;
      for (int i = 0; i < pointsPerRing; i++) {
        float angle = (float) (2 * Math.PI * i / pointsPerRing);
        float x = (float) (center.x + radius * Math.cos(angle));
        float z = (float) (center.z + radius * Math.sin(angle));
        
        level.sendParticles(
          hitboxParticle,
          x, center.y + yOffset, z,
          1, 0, 0, 0, 0
        );
      }
    }
    
    level.sendParticles(hitboxParticle, center.x, center.y, center.z, 20, 0, 0, 0, 0);
  }
}