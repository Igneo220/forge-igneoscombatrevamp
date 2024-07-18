package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class ParryC2SPacket {
    public ParryC2SPacket(){

    }
    public ParryC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(PlayerEnchantmentActions::setParryTime);
        });
        return true;
    }
}
