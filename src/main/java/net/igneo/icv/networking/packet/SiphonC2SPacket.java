package net.igneo.icv.networking.packet;

import net.igneo.icv.particle.ModParticles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.SoundType;
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

            level.sendParticles(ModParticles.SIPHON_PARTICLE.get(),player.getX(),player.getEyeY(),player.getZ(),5,0,0,0,1);
            level.playSound(null,player.blockPosition(), SoundType.ANVIL.getPlaceSound(), SoundSource.PLAYERS,1,1.3F);

            player.heal(1);
            player.setHealth(player.getHealth() + 1);

            player.getInventory().getArmor(0).setDamageValue(player.getInventory().getArmor(0).getDamageValue() + 25);
            player.getInventory().getArmor(1).setDamageValue(player.getInventory().getArmor(1).getDamageValue() + 25);
            player.getInventory().getArmor(2).setDamageValue(player.getInventory().getArmor(2).getDamageValue() + 25);
            player.getInventory().getArmor(3).setDamageValue(player.getInventory().getArmor(3).getDamageValue() + 25);
        });
        return true;
    }
}
