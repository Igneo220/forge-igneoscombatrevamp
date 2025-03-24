package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlinkManager extends ArmorEnchantManager {
  private static final double BLINK_DISTANCE = 10.0d;
  
  public BlinkManager(Player player) {
    super(EnchantType.BOOTS, 300, 10, false, player);
  }
  
  @Override
  public void onOffCoolDown(Player player) {
  }
  
  @Override
  public EnchantIndicator getIndicator() {
    return null;
  }
  
  @Override
  public boolean canUse() {
    return stableCheck();
  }
  
  @Override
  public void activate() {
    Vec3 playerPos = player.getEyePosition();
    Vec3 lookVector = player.getLookAngle().normalize();
    Vec3 targetPos = playerPos.add(
      lookVector.x * BLINK_DISTANCE,
      lookVector.y * BLINK_DISTANCE,
      lookVector.z * BLINK_DISTANCE
    );
    
    BlockHitResult result = player.level().clip(
      new ClipContext(
        playerPos,
        targetPos,
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        player
      )
    );
    
    if (result.getType() != BlockHitResult.Type.MISS) {
      Vec3 hitPos = result.getLocation();
      targetPos = hitPos.subtract(
        lookVector.x * 0.5f,
        lookVector.y * 2.5f,
        lookVector.z * 0.5f
      );
    }
    
    player.teleportTo(
      targetPos.x,
      targetPos.y - player.getEyeHeight(),
      targetPos.z
    );
    
    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
      net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT,
      net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
  }
}