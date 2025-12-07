package net.igneo.icv.networking;

import net.igneo.icv.networking.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    
    public ModMessages() {
    }
    
    private static int id() {
        return packetId++;
    }
    
    public static void register() {
        SimpleChannel net = ChannelBuilder.named(new ResourceLocation("icv", "messages")).networkProtocolVersion(() -> {
            return "1.0";
        }).clientAcceptedVersions((s) -> {
            return true;
        }).serverAcceptedVersions((s) -> {
            return true;
        }).simpleChannel();
        INSTANCE = net;
        net.messageBuilder(EnchantUseC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EnchantUseC2SPacket::new)
                .encoder(EnchantUseC2SPacket::toBytes)
                .consumerMainThread(EnchantUseC2SPacket::handle)
                .add();
        net.messageBuilder(InputSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(InputSyncC2SPacket::new)
                .encoder(InputSyncC2SPacket::toBytes)
                .consumerMainThread(InputSyncC2SPacket::handle)
                .add();
        net.messageBuilder(AnimatedSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(AnimatedSyncC2SPacket::new)
                .encoder(AnimatedSyncC2SPacket::toBytes)
                .consumerMainThread(AnimatedSyncC2SPacket::handle)
                .add();
        net.messageBuilder(EquipmentUpdateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EquipmentUpdateS2CPacket::new)
                .encoder(EquipmentUpdateS2CPacket::toBytes)
                .consumerMainThread(EquipmentUpdateS2CPacket::handle)
                .add();
        net.messageBuilder(EntitySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EntitySyncS2CPacket::new)
                .encoder(EntitySyncS2CPacket::toBytes)
                .consumerMainThread(EntitySyncS2CPacket::handle)
                .add();
        net.messageBuilder(EnchantAttackS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnchantAttackS2CPacket::new)
                .encoder(EnchantAttackS2CPacket::toBytes)
                .consumerMainThread(EnchantAttackS2CPacket::handle)
                .add();
        net.messageBuilder(EnchantHitS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnchantHitS2CPacket::new)
                .encoder(EnchantHitS2CPacket::toBytes)
                .consumerMainThread(EnchantHitS2CPacket::handle)
                .add();
        net.messageBuilder(MovePlayerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MovePlayerS2CPacket::new)
                .encoder(MovePlayerS2CPacket::toBytes)
                .consumerMainThread(MovePlayerS2CPacket::handle)
                .add();
        net.messageBuilder(PushPlayerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PushPlayerS2CPacket::new)
                .encoder(PushPlayerS2CPacket::toBytes)
                .consumerMainThread(PushPlayerS2CPacket::handle)
                .add();
        net.messageBuilder(SendBlinkShaderS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SendBlinkShaderS2CPacket::new)
                .encoder(SendBlinkShaderS2CPacket::toBytes)
                .consumerMainThread(SendBlinkShaderS2CPacket::handle)
                .add();
        net.messageBuilder(SendStasisShaderS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SendStasisShaderS2CPacket::new)
                .encoder(SendStasisShaderS2CPacket::toBytes)
                .consumerMainThread(SendStasisShaderS2CPacket::handle)
                .add();
    }
    
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> {
            return player;
        }), message);
    }
}
