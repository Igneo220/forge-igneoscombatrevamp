package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.GustS2CPacket;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class GustManager extends WeaponEnchantManager {
  public static long gustDelay = 0;
  private static final long GUST_COOLDOWN = 2000;
  
  public GustManager(Player player) {
    super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
  }
  
  @Override
  public void activate() {
    super.activate();
    if (System.currentTimeMillis() < gustDelay + GUST_COOLDOWN) {
      return;
    }
    
    if (!(player.level() instanceof ServerLevel level)) {
      return;
    }
    level.playSound(null, player.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS, 0.5f, 1.2f);
    level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0f, player.getZ(),
      10, 0.5f, 0.5f, 0.5f, 0.1f);
    
    double radius = 5.0d;
    double halfAngleDegrees = 45.0d;
    double cosThreshold = Math.cos(Math.toRadians(halfAngleDegrees));
    
    Vec3 playerLook = player.getLookAngle().normalize();
    
    renderConeHitbox(level, player, playerLook, radius, halfAngleDegrees);
    
    List<Entity> nearbyEntities = level.getEntities(player,
      player.getBoundingBox().inflate(radius), entity -> entity != player);
    
    for (Entity entity : nearbyEntities) {
      Vec3 toEntity = new Vec3(entity.getX() - player.getX(),
        entity.getY() - player.getY(),
        entity.getZ() - player.getZ());
      double distance = toEntity.length();
      if (distance >= radius) {
        continue;
      }
      Vec3 toEntityNormalized = toEntity.normalize();
      double dot = playerLook.dot(toEntityNormalized);
      if (dot <= cosThreshold) {
        continue;
      }
      Vec3 launchVelocity = new Vec3(entity.getDeltaMovement().x, 0.8f, entity.getDeltaMovement().z);
      entity.setDeltaMovement(launchVelocity);
      entity.hurtMarked = true;
      
      if (entity instanceof ServerPlayer serverPlayer) {
        ModMessages.sendToPlayer(new GustS2CPacket(), serverPlayer);
      }
      
      level.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(),
        5, 0.3f, 0.3f, 0.3f, 0.05f);
      level.playSound(null, entity.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }
  }
  
  private void renderConeHitbox(ServerLevel level, Player player, Vec3 direction, double radius, double halfAngleDegrees) {
    Vec3 playerPos = player.position().add(0, 1, 0);
    ParticleOptions hitboxParticle = ParticleTypes.END_ROD;
    
    int particlesPerRing = 16;
    int numRings = 5;
    
    for (int ring = 1; ring <= numRings; ring++) {
      double ringRadius = (radius * ring) / numRings;
      double ringDistance = ringRadius;
      
      double circleRadius = Math.tan(Math.toRadians(halfAngleDegrees)) * ringDistance;
      
      for (int i = 0; i < particlesPerRing; i++) {
        double angle = 2 * Math.PI * i / particlesPerRing;
        
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = direction.cross(up).normalize();
        if (right.length() < 0.001) {
          right = new Vec3(1, 0, 0);
        }
        Vec3 newUp = right.cross(direction).normalize();
        
        Vec3 offset = right.scale(Math.cos(angle) * circleRadius)
          .add(newUp.scale(Math.sin(angle) * circleRadius));
        
        Vec3 particlePos = playerPos.add(direction.scale(ringDistance)).add(offset);
        
        level.sendParticles(
          hitboxParticle,
          particlePos.x, particlePos.y, particlePos.z,
          1, 0, 0, 0, 0
        );
      }
    }
  }

  @Override
  public float getDamageBonus() {
    if (target == null) {
      return 0;
    }
    return target.onGround() ? 0 : 3;
  }
}