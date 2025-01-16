package net.igneo.icv.enchantmentActions.enchantManagers.armor;

import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.blackHole.BlackHoleEntity;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlackHoleSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BlackHoleManager extends ArmorEnchantManager {
    public BlackHoleManager(Player player) {
        super(EnchantType.HELMET,900,-30,true,player);
    }
    public static void onKeyInputEvent() {

    }

    public BlackHoleEntity child = null;

    @Override
    public void tick() {
        super.tick();
        if (child != null && !child.isAlive()) {
            child = null;
        }
    }

    @Override
    public boolean canUse() {
        return child == null;
    }

    @Override
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }

    @Override
    public void onOffCoolDown(Player player) {
        FX fx = FXHelper.getFX(new ResourceLocation(ICV.MOD_ID,"blackholehalo"));
        EntityEffect effect = new EntityEffect(fx,player.level(),player);
        effect.start();
    }

    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }

    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel) {
            child = ModEntities.BLACK_HOLE.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition());
            child.setDeltaMovement(player.getLookAngle().scale(0.4));
            player.level().addFreshEntity(child);
            ModMessages.sendToPlayer(new BlackHoleSyncS2CPacket(child.getId()), (ServerPlayer) player);
        }
    }

    @Override
    public void dualActivate() {
        if (child != null && player.level() instanceof ServerLevel) {
            Vec3 pushVec = new Vec3((player.getX() - child.getX()), ((player.getEyeY() - 0.5) - child.getY()), (player.getZ() - child.getZ()));
            if (child.getDeltaMovement().length() < 0.3) {
                child.addDeltaMovement(pushVec.normalize().scale(0.1));
            } else {
                child.setDeltaMovement(pushVec.normalize().scale(0.3));
            }
        }
    }
}
