package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class VolcanoManager extends ArmorEnchantManager {
  private List<BlockPos> selectedLocations = new ArrayList<>();
  private static final int MAX_LOCATIONS = 3;
  private static final long VOLCANO_DAMAGE = 10L; // NEEDS TO BE CHANGED TOO MUCH DAMAGE
  private static final double VOLCANO_RANGE = 5.0d;
  
  public VolcanoManager(Player player) {
    super(EnchantType.BOOTS, 300, -10, true, player);
  }
  
  @Override
  public void activate() {
    if (selectedLocations.size() < MAX_LOCATIONS) {
      resetCoolDown();
      HitResult hitResult = player.pick(20, 0f, false);
      
      if (hitResult.getType() == HitResult.Type.BLOCK) {
        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        BlockPos targetPos = blockHitResult.getBlockPos();
        
        selectedLocations.add(targetPos);
      }
    }
    if (selectedLocations.size() != MAX_LOCATIONS) {
      return;
    }
    active = true;
  }
  
  @Override
  public void onOffCoolDown(Player player) {
  
  }
  
  @Override
  public EnchantIndicator getIndicator() {
    return new StasisCooldownIndicator(this);
  }
  
  @Override
  public boolean canUse() {
    return !active;
  }
  
  @Override
  public void dualActivate() {
    resetCoolDown();
    if (!(player.level() instanceof ServerLevel level)) {
      return;
    }
    if (selectedLocations.size() == MAX_LOCATIONS) {
      triggerVolcanoEffect(level);
    }
    selectedLocations.clear();
    active = false;
    resetCoolDown();
  }
  
  @Override
  public boolean isDualUse() {
    return true;
  }
  
  private void triggerVolcanoEffect(ServerLevel level) {
    for (BlockPos location : selectedLocations) {
      level.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(),
        5, 0.3f, 0.3f, 0.3f, 0.05f);
      
      List<Entity> entities = ICVUtils.collectEntitiesBox(player, location.getCenter(), VOLCANO_RANGE);
      
      for (Entity entity : entities) {
        if (entity == player) {
          return;
        }
        entity.hurt(level.damageSources().inFire(), VOLCANO_DAMAGE);
      }
    }
  }
  
  private List<BlockPos> getSelectedLocations() {
    return new ArrayList<>(selectedLocations);
  }
}