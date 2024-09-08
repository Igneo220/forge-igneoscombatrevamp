package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AccelerateS2CPacket {

    private final double x;
    private final double y;
    private final double z;
    public AccelerateS2CPacket(Vec3 lookDirection){
        this.x = lookDirection.x;
        this.y = lookDirection.y;
        this.z = lookDirection.z;
    }
    public AccelerateS2CPacket(FriendlyByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Vec3 enemyLook = new Vec3(x,y,z);;
            Minecraft.getInstance().player.setDeltaMovement(enemyLook);
            if (y == 1) {
                Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    enchVar.setConcussed(true);
                });
            }
        });
        return true;
    }
}
