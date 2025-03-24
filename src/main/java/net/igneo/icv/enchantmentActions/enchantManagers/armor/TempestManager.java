package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import com.lowdragmc.photon.client.fx.BlockEffect;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.igneo.icv.ICV;
import net.igneo.icv.client.animation.EnchantAnimationPlayer;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TempestManager extends ArmorEnchantManager{
    public TempestManager(Player player) {
        super(EnchantType.LEGGINGS, 300, -10, false, player);
    }

    @Override
    public void activate() {
        if (player.level().isClientSide) {
            player.setDeltaMovement(ICVUtils.getFlatInputDirection(player.getYRot(),1.5F,0.5));
        }
    }

    @Override
    public void onOffCoolDown(Player player) {

    }

    @Override
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }

    @Override
    public void resetCoolDown() {
        addCoolDown((maxCoolDown/3));
    }

    @Override
    public boolean isOffCoolDown() {
        return getCoolDown() <= maxCoolDown - (maxCoolDown/3);
    }

    @Override
    public boolean canUse() {
        return true;
    }
}
