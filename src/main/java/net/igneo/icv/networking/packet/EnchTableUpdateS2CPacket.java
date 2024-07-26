package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.event.ModEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnchTableUpdateS2CPacket {
    private final int shift;
    public EnchTableUpdateS2CPacket(int newshift) {
        this.shift = newshift;
    }
    public EnchTableUpdateS2CPacket(FriendlyByteBuf buf) {
        this.shift = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(shift);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ModEvents.enchShift = shift;
        });
        return true;
    }
}
