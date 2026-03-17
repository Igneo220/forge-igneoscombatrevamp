package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RiftRipperManager extends ArmorEnchantManager {
    private Vec3 oldPlayerPosition;
    private Vec3 newPlayerPosition;
    private static final double TELEPORT_RADIUS = 5.0d;
  
  public RiftRipperManager(Player player) {
    super(EnchantType.HELMET, 300, -10, true, player);
  }
  
  @Override
  public void onOffCoolDown(Player player) {
  
  }
  
  @Override
  public EnchantIndicator getIndicator() {
    return new StasisCooldownIndicator(this);
  }
  
  @Override
  public void activate() {
    System.out.println("running activate");
    oldPlayerPosition = player.position();
    active = true;
  }
  
  @Override
  public boolean canUse() {
    return !active;
  }
  
  @Override
  public void dualActivate() {
        System.out.println(oldPlayerPosition);
        newPlayerPosition = player.position();
        resetCoolDown();
        newPlayerPosition = player.position();

        player.setPos(oldPlayerPosition);

        List<Entity> entities = ICVUtils.collectEntitiesBox(player.level(), oldPlayerPosition, TELEPORT_RADIUS);

        for (Entity entity : entities) {
            entity.setPos(newPlayerPosition);
        }

        active = false;
        resetCoolDown();
  }
  
  @Override
  public boolean isDualUse() {
    return true;
  }
}
