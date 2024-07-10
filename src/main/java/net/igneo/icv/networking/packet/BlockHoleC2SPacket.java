package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class BlockHoleC2SPacket {
    public BlockHoleC2SPacket(){

    }
    public BlockHoleC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                level.playSound(null, player.blockPosition(), ModSounds.HOLE_SHOT.get(), SoundSource.PLAYERS, 0.5F, 0.1F);
                ModEntities.BLACK_HOLE.get().spawn(level, player.blockPosition().atY((int) player.getEyeY()), MobSpawnType.MOB_SUMMONED).setOwner(player);
                enchVar.setHoleCooldown(System.currentTimeMillis());
            });
        });
        return true;
    }
}
