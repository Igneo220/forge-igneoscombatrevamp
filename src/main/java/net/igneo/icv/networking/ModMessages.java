package net.igneo.icv.networking;

import net.igneo.icv.networking.packet.AcrobaticC2SPacket;
import net.igneo.icv.networking.packet.BlitzNBTUpdateS2CPacket;
import net.igneo.icv.networking.packet.BlizzardC2SPacket;
import net.igneo.icv.networking.packet.BlockHoleC2SPacket;
import net.igneo.icv.networking.packet.CometStrikeC2SPacket;
import net.igneo.icv.networking.packet.ConcussC2SPacket;
import net.igneo.icv.networking.packet.CounterweightedC2SPacket;
import net.igneo.icv.networking.packet.CrushC2SPacket;
import net.igneo.icv.networking.packet.FlameC2SPacket;
import net.igneo.icv.networking.packet.FlareC2SPacket;
import net.igneo.icv.networking.packet.GustC2SPacket;
import net.igneo.icv.networking.packet.IncaC2SPacket;
import net.igneo.icv.networking.packet.JudgementC2SPacket;
import net.igneo.icv.networking.packet.MomentumC2SPacket;
import net.igneo.icv.networking.packet.PhantomPainC2SPacket;
import net.igneo.icv.networking.packet.RendC2SPacket;
import net.igneo.icv.networking.packet.SiphonC2SPacket;
import net.igneo.icv.networking.packet.SmiteC2SPacket;
import net.igneo.icv.networking.packet.TempoTheftC2SPacket;
import net.igneo.icv.networking.packet.TrainDashC2SPacket;
import net.igneo.icv.networking.packet.WardenScreamC2SPacket;
import net.igneo.icv.networking.packet.WardenspineC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
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
        net.messageBuilder(CometStrikeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(CometStrikeC2SPacket::new).encoder(CometStrikeC2SPacket::toBytes).consumerMainThread(CometStrikeC2SPacket::handle).add();
        //net.messageBuilder(StoneCallC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(StoneCallC2SPacket::new).encoder(StoneCallC2SPacket::toBytes).consumerMainThread(StoneCallC2SPacket::handle).add();
        //net.messageBuilder(StoneBreakC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(StoneBreakC2SPacket::new).encoder(StoneBreakC2SPacket::toBytes).consumerMainThread(StoneBreakC2SPacket::handle).add();
        net.messageBuilder(CrushC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(CrushC2SPacket::new).encoder(CrushC2SPacket::toBytes).consumerMainThread(CrushC2SPacket::handle).add();
        net.messageBuilder(PhantomPainC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(PhantomPainC2SPacket::new).encoder(PhantomPainC2SPacket::toBytes).consumerMainThread(PhantomPainC2SPacket::handle).add();
        net.messageBuilder(MomentumC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(MomentumC2SPacket::new).encoder(MomentumC2SPacket::toBytes).consumerMainThread(MomentumC2SPacket::handle).add();
        net.messageBuilder(TempoTheftC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(TempoTheftC2SPacket::new).encoder(TempoTheftC2SPacket::toBytes).consumerMainThread(TempoTheftC2SPacket::handle).add();
        net.messageBuilder(GustC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(GustC2SPacket::new).encoder(GustC2SPacket::toBytes).consumerMainThread(GustC2SPacket::handle).add();
        net.messageBuilder(IncaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(IncaC2SPacket::new).encoder(IncaC2SPacket::toBytes).consumerMainThread(IncaC2SPacket::handle).add();
        net.messageBuilder(TrainDashC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(TrainDashC2SPacket::new).encoder(TrainDashC2SPacket::toBytes).consumerMainThread(TrainDashC2SPacket::handle).add();
        net.messageBuilder(CounterweightedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(CounterweightedC2SPacket::new).encoder(CounterweightedC2SPacket::toBytes).consumerMainThread(CounterweightedC2SPacket::handle).add();
        net.messageBuilder(SiphonC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(SiphonC2SPacket::new).encoder(SiphonC2SPacket::toBytes).consumerMainThread(SiphonC2SPacket::handle).add();
        net.messageBuilder(WardenspineC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(WardenspineC2SPacket::new).encoder(WardenspineC2SPacket::toBytes).consumerMainThread(WardenspineC2SPacket::handle).add();
        net.messageBuilder(JudgementC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(JudgementC2SPacket::new).encoder(JudgementC2SPacket::toBytes).consumerMainThread(JudgementC2SPacket::handle).add();
        net.messageBuilder(FlareC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(FlareC2SPacket::new).encoder(FlareC2SPacket::toBytes).consumerMainThread(FlareC2SPacket::handle).add();
        net.messageBuilder(FlameC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(FlameC2SPacket::new).encoder(FlameC2SPacket::toBytes).consumerMainThread(FlameC2SPacket::handle).add();
        net.messageBuilder(BlizzardC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(BlizzardC2SPacket::new).encoder(BlizzardC2SPacket::toBytes).consumerMainThread(BlizzardC2SPacket::handle).add();
        net.messageBuilder(WardenScreamC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(WardenScreamC2SPacket::new).encoder(WardenScreamC2SPacket::toBytes).consumerMainThread(WardenScreamC2SPacket::handle).add();
        net.messageBuilder(ConcussC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ConcussC2SPacket::new).encoder(ConcussC2SPacket::toBytes).consumerMainThread(ConcussC2SPacket::handle).add();
        net.messageBuilder(SmiteC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(SmiteC2SPacket::new).encoder(SmiteC2SPacket::toBytes).consumerMainThread(SmiteC2SPacket::handle).add();
        net.messageBuilder(AcrobaticC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(AcrobaticC2SPacket::new).encoder(AcrobaticC2SPacket::toBytes).consumerMainThread(AcrobaticC2SPacket::handle).add();
        net.messageBuilder(BlockHoleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(BlockHoleC2SPacket::new).encoder(BlockHoleC2SPacket::toBytes).consumerMainThread(BlockHoleC2SPacket::handle).add();
        net.messageBuilder(RendC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(RendC2SPacket::new).encoder(RendC2SPacket::toBytes).consumerMainThread(RendC2SPacket::handle).add();
        net.messageBuilder(BlitzNBTUpdateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(BlitzNBTUpdateS2CPacket::new).encoder(BlitzNBTUpdateS2CPacket::toBytes).consumerMainThread(BlitzNBTUpdateS2CPacket::handle).add();
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