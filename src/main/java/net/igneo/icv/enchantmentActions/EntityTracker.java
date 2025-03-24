package net.igneo.icv.enchantmentActions;

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.EntitySyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface EntityTracker {
    ICVEntity getChild();
    void setChild(ICVEntity entity);
    default void syncClientChild(ServerPlayer player, @Nullable Entity entity, EnchantmentManager manager) {
        int ID = entity == null ? -1 : entity.getId();
        System.out.println(entity);
        System.out.println(ID);

        int slot = ICVUtils.getSlotForType(player,manager.getClass());

        ModMessages.sendToPlayer(new EntitySyncS2CPacket(ID,slot),player);
    }
}
