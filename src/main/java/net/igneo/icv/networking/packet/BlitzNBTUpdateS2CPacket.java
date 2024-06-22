package net.igneo.icv.networking.packet;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlitzNBTUpdateS2CPacket {
    private final int boostCount;
    private final long boostTime;
    public BlitzNBTUpdateS2CPacket(int boostCount, long boostTime){
        this.boostCount = boostCount;
        this.boostTime = boostTime;
    }
    public BlitzNBTUpdateS2CPacket(FriendlyByteBuf buf) {
        this.boostCount = buf.readInt();
        this.boostTime = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(boostCount);
        buf.writeLong(boostTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                //event.player.sendSystemMessage(Component.literal("it do be workin"));
                enchVar.addBlitzBoostCount();
                enchVar.setBlitzTime(System.currentTimeMillis());
            });
        });
        return true;
    }
}
