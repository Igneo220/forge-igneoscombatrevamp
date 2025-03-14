package net.igneo.icv.enchantmentActions.enchantManagers.weapon;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.PushPlayerS2CPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class TungstenCoreManager extends WeaponEnchantManager{
  public static final UUID KNOCKBACK_MODIFIER_UUID = UUID.fromString("f53860da-d668-41db-bad4-e9fb3fbefe41");
  
  public TungstenCoreManager(Player player) {
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
    player.getAttributes().getInstance(Attributes.ATTACK_KNOCKBACK).addTransientModifier(
      new AttributeModifier(KNOCKBACK_MODIFIER_UUID,
        "Tungsten Core Knockback boost",
        2f,
        AttributeModifier.Operation.ADDITION)
    );
  }
  
  @Override
  public void removePassive() {
    super.removePassive();
    player.getAttributes().getInstance(Attributes.ATTACK_KNOCKBACK).removeModifier(KNOCKBACK_MODIFIER_UUID);
  }
  
  @Override
  public void activate() {
    super.activate();
    
    if (player.level() instanceof ServerLevel level) {
      float radius = 3.0f;
      float pushStrength = 3.5f;
      
      renderRadius(level, player.position(), radius);
      
      List<Entity> nearbyEntities = level.getEntities(player,
        player.getBoundingBox().inflate(radius), entity -> entity != player);
      
      for (Entity entity : nearbyEntities) {
        Vec3 direction = entity.position().subtract(player.position()).normalize();
        
        Vec3 pushVelocity = direction.scale(pushStrength);
        entity.addDeltaMovement(pushVelocity);
        entity.hurtMarked = true;
        
        if (entity instanceof ServerPlayer serverPlayer) {
         ModMessages.sendToPlayer(new PushPlayerS2CPacket(pushVelocity), serverPlayer);
        }
      }
    }
  }
  
  private void renderRadius(ServerLevel level, Vec3 center, float radius) {
    ParticleOptions hitboxParticle = ParticleTypes.ELECTRIC_SPARK;
    
    int pointsPerRing = 32;
    float yOffset = 0.05f;
    
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
}