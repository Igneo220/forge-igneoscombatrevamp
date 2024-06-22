package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class SiphonC2SPacket {
    public SiphonC2SPacket(){

    }
    public SiphonC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            player.heal(1);
            //pPlayer.setHealth(pPlayer.getHealth() + 1);

            //player.getInventory().getArmor(0).setDamageValue(boots.getDamageValue() + 25);
            //player.getInventory().getArmor(1).setDamageValue(boots.getDamageValue() + 25);
            //player.getInventory().getArmor(2).setDamageValue(boots.getDamageValue() + 25);
            //player.getInventory().getArmor(3).setDamageValue(boots.getDamageValue() + 25);
            //PhantomPainEnchantment.phantomVictim.setHealth(PhantomPainEnchantment.phantomVictim.getHealth() + 1000);
        });
        return true;
    }
}
