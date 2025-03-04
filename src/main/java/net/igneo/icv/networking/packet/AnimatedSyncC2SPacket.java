package net.igneo.icv.networking.packet;

import java.util.function.Supplier;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class AnimatedSyncC2SPacket {
    private boolean state;
    public AnimatedSyncC2SPacket(boolean state) {
        this.state = state;
    }

    public AnimatedSyncC2SPacket(FriendlyByteBuf buf) {
        state = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(state);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = (NetworkEvent.Context)supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                enchVar.animated = state;
            });
        });
        return true;
    }
}