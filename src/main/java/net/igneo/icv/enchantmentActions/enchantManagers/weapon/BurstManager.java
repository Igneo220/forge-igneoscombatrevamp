package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class BurstManager extends WeaponEnchantManager {
  public static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("3e176df4-23ac-4811-8dea-d461bb401352");
  private int burstBoostCount;
  private int burstTimer;
  
  public BurstManager(Player player) {
    super(EnchantType.WEAPON, player, new ResourceLocation(ICV.MOD_ID, "dual_handed_slash_cross"));
  }
  
  @Override
  public void onAttack(Entity target) {
    super.onAttack(target);
    player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
      if (player.fallDistance <= 0) {
        ++burstBoostCount;
      } else {
        ++burstBoostCount;
        ++burstBoostCount;
        ++burstBoostCount;
      }
      burstTimer = 0;
      if (target.level() instanceof ServerLevel level) {
        level.sendParticles(ModParticles.ATTACK_SPEED_PARTICLE.get(),
                player.getX(),
                player.getY() + 1.5f,
                player.getZ(),
                5, Math.random(), Math.random(), Math.random(), 0.5f);
        level.playSound(null, player.blockPosition(),
                SoundEvents.ARROW_HIT_PLAYER,
                SoundSource.PLAYERS,
                0.5f, (float) 0.3 + (float) burstBoostCount * 0.1f);
        player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
        player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Attack Speed Boost Burst", burstBoostCount * 0.1f, AttributeModifier.Operation.ADDITION));
        //ModMessages.sendToPlayer(new BurstNBTUpdateS2CPacket(enchVar.getBurstBoostCount(), enchVar.getBurstTime()), (ServerPlayer) player);
      }
    });
  }
  
  @Override
  public void activate() {
    super.activate();
    
    
    if (player.level() instanceof ServerLevel level) {
      level.playSound(null, player.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS, 0.5f, 1.2f);
      level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0f, player.getZ(),
        10, 0.5f, 0.5f, 0.5f, 0.1f);
      
      double radius = 2.0d;
      float pushStrength = 2.5f;
      
      
      float BurstBoostDecay = (float) Math.log(burstBoostCount) ;
      
      renderRadius(level, player.position(), radius * BurstBoostDecay);
      
      List<Entity> nearbyEntities = level.getEntities(player,
        player.getBoundingBox().inflate(radius * BurstBoostDecay), entity -> entity != player);
      
      for (Entity entity : nearbyEntities) {
        Vec3 direction = new Vec3(entity.getX() - player.getX(),
          entity.getY() - player.getY(),
          entity.getZ() - player.getZ()).normalize();
        
        Vec3 pushVelocity = direction.scale(pushStrength * BurstBoostDecay);
        entity.addDeltaMovement(pushVelocity);
        entity.hurtMarked = true;
      }
    }

    burstBoostCount = 0;
    burstTimer = 0;
  }
  
  private void renderRadius(ServerLevel level, Vec3 center, double radius) {
    ParticleOptions hitboxParticle = ParticleTypes.ELECTRIC_SPARK;
    
    int pointsPerRing = 32;
    double yOffset = 0.05f;
    
    for (int i = 0; i < pointsPerRing; i++) {
      double angle = 2 * Math.PI * i / pointsPerRing;
      double x = center.x + radius * Math.cos(angle);
      double z = center.z + radius * Math.sin(angle);
      
      level.sendParticles(
        hitboxParticle,
        x, center.y + yOffset, z,
        1, 0, 0, 0, 0
      );
    }
  }

  @Override
  public void tick() {
    super.tick();

    if (burstBoostCount > 0) {
      if (burstTimer < 60) {
        ++burstTimer;
      } else {
        burstTimer = 0;
        burstBoostCount = 0;
        if (player.level() instanceof ServerLevel level) {
          player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
          level.playSound(null, player.blockPosition(), SoundEvents.CREEPER_DEATH, SoundSource.PLAYERS, 4F, 0.2F);
        }
      }
    }
  }
}