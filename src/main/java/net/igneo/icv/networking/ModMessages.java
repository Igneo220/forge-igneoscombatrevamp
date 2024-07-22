package net.igneo.icv.networking;

import net.igneo.icv.networking.packet.*;
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
        net.messageBuilder(DoubleJumpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DoubleJumpC2SPacket::new)
                .encoder(DoubleJumpC2SPacket::toBytes)
                .consumerMainThread(DoubleJumpC2SPacket::handle)
                .add();
        net.messageBuilder(SkyChargeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SkyChargeC2SPacket::new)
                .encoder(SkyChargeC2SPacket::toBytes)
                .consumerMainThread(SkyChargeC2SPacket::handle)
                .add();
        net.messageBuilder(ParryC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ParryC2SPacket::new)
                .encoder(ParryC2SPacket::toBytes)
                .consumerMainThread(ParryC2SPacket::handle)
                .add();
        net.messageBuilder(CometStrikeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CometStrikeC2SPacket::new)
                .encoder(CometStrikeC2SPacket::toBytes)
                .consumerMainThread(CometStrikeC2SPacket::handle)
                .add();
        net.messageBuilder(KineticC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(KineticC2SPacket::new)
                .encoder(KineticC2SPacket::toBytes)
                .consumerMainThread(KineticC2SPacket::handle)
                .add();
        net.messageBuilder(StoneCallerC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StoneCallerC2SPacket::new)
                .encoder(StoneCallerC2SPacket::toBytes)
                .consumerMainThread(StoneCallerC2SPacket::handle)
                .add();
        net.messageBuilder(CrushC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CrushC2SPacket::new)
                .encoder(CrushC2SPacket::toBytes)
                .consumerMainThread(CrushC2SPacket::handle)
                .add();
        net.messageBuilder(CrushSoundC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CrushSoundC2SPacket::new)
                .encoder(CrushSoundC2SPacket::toBytes)
                .consumerMainThread(CrushSoundC2SPacket::handle)
                .add();
        net.messageBuilder(PhantomPainC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PhantomPainC2SPacket::new)
                .encoder(PhantomPainC2SPacket::toBytes)
                .consumerMainThread(PhantomPainC2SPacket::handle)
                .add();
        net.messageBuilder(MomentumC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MomentumC2SPacket::new)
                .encoder(MomentumC2SPacket::toBytes)
                .consumerMainThread(MomentumC2SPacket::handle)
                .add();
        net.messageBuilder(TempoTheftC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TempoTheftC2SPacket::new)
                .encoder(TempoTheftC2SPacket::toBytes)
                .consumerMainThread(TempoTheftC2SPacket::handle)
                .add();
        net.messageBuilder(GustC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GustC2SPacket::new)
                .encoder(GustC2SPacket::toBytes)
                .consumerMainThread(GustC2SPacket::handle)
                .add();
        net.messageBuilder(IncaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(IncaC2SPacket::new)
                .encoder(IncaC2SPacket::toBytes)
                .consumerMainThread(IncaC2SPacket::handle)
                .add();
        net.messageBuilder(TrainDashC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TrainDashC2SPacket::new)
                .encoder(TrainDashC2SPacket::toBytes)
                .consumerMainThread(TrainDashC2SPacket::handle)
                .add();
        net.messageBuilder(CounterweightedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CounterweightedC2SPacket::new)
                .encoder(CounterweightedC2SPacket::toBytes)
                .consumerMainThread(CounterweightedC2SPacket::handle)
                .add();
        net.messageBuilder(WeightedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(WeightedC2SPacket::new)
                .encoder(WeightedC2SPacket::toBytes)
                .consumerMainThread(WeightedC2SPacket::handle)
                .add();
        net.messageBuilder(SiphonC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SiphonC2SPacket::new)
                .encoder(SiphonC2SPacket::toBytes)
                .consumerMainThread(SiphonC2SPacket::handle)
                .add();
        net.messageBuilder(WardenspineC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(WardenspineC2SPacket::new)
                .encoder(WardenspineC2SPacket::toBytes)
                .consumerMainThread(WardenspineC2SPacket::handle)
                .add();
        net.messageBuilder(JudgementC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(JudgementC2SPacket::new)
                .encoder(JudgementC2SPacket::toBytes)
                .consumerMainThread(JudgementC2SPacket::handle)
                .add();
        net.messageBuilder(JudgementHitC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(JudgementHitC2SPacket::new)
                .encoder(JudgementHitC2SPacket::toBytes)
                .consumerMainThread(JudgementHitC2SPacket::handle)
                .add();
        net.messageBuilder(FlareC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FlareC2SPacket::new)
                .encoder(FlareC2SPacket::toBytes)
                .consumerMainThread(FlareC2SPacket::handle)
                .add();
        net.messageBuilder(FlareSoundC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FlareSoundC2SPacket::new)
                .encoder(FlareSoundC2SPacket::toBytes)
                .consumerMainThread(FlareSoundC2SPacket::handle)
                .add();
        net.messageBuilder(FlareParticleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FlareParticleC2SPacket::new)
                .encoder(FlareParticleC2SPacket::toBytes)
                .consumerMainThread(FlareParticleC2SPacket::handle)
                .add();
        net.messageBuilder(FlameC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FlameC2SPacket::new)
                .encoder(FlameC2SPacket::toBytes)
                .consumerMainThread(FlameC2SPacket::handle)
                .add();
        net.messageBuilder(BlizzardC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BlizzardC2SPacket::new)
                .encoder(BlizzardC2SPacket::toBytes)
                .consumerMainThread(BlizzardC2SPacket::handle)
                .add();
        net.messageBuilder(BlizzardSoundC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BlizzardSoundC2SPacket::new)
                .encoder(BlizzardSoundC2SPacket::toBytes)
                .consumerMainThread(BlizzardSoundC2SPacket::handle)
                .add();
        net.messageBuilder(WardenScreamC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(WardenScreamC2SPacket::new)
                .encoder(WardenScreamC2SPacket::toBytes)
                .consumerMainThread(WardenScreamC2SPacket::handle)
                .add();
        net.messageBuilder(ConcussC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ConcussC2SPacket::new)
                .encoder(ConcussC2SPacket::toBytes)
                .consumerMainThread(ConcussC2SPacket::handle)
                .add();
        net.messageBuilder(ConcussHurtC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ConcussHurtC2SPacket::new)
                .encoder(ConcussHurtC2SPacket::toBytes)
                .consumerMainThread(ConcussHurtC2SPacket::handle)
                .add();
        net.messageBuilder(SmiteC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SmiteC2SPacket::new)
                .encoder(SmiteC2SPacket::toBytes)
                .consumerMainThread(SmiteC2SPacket::handle)
                .add();
        net.messageBuilder(AcrobaticC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(AcrobaticC2SPacket::new)
                .encoder(AcrobaticC2SPacket::toBytes)
                .consumerMainThread(AcrobaticC2SPacket::handle)
                .add();
        net.messageBuilder(BlockHoleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BlockHoleC2SPacket::new)
                .encoder(BlockHoleC2SPacket::toBytes)
                .consumerMainThread(BlockHoleC2SPacket::handle)
                .add();
        net.messageBuilder(RendC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RendC2SPacket::new)
                .encoder(RendC2SPacket::toBytes)
                .consumerMainThread(RendC2SPacket::handle)
                .add();
        net.messageBuilder(BlitzNBTUpdateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BlitzNBTUpdateS2CPacket::new)
                .encoder(BlitzNBTUpdateS2CPacket::toBytes)
                .consumerMainThread(BlitzNBTUpdateS2CPacket::handle)
                .add();
        net.messageBuilder(ExtractUpdateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExtractUpdateS2CPacket::new)
                .encoder(ExtractUpdateS2CPacket::toBytes)
                .consumerMainThread(ExtractUpdateS2CPacket::handle)
                .add();
        net.messageBuilder(GustS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GustS2CPacket::new)
                .encoder(GustS2CPacket::toBytes)
                .consumerMainThread(GustS2CPacket::handle)
                .add();
        net.messageBuilder(RendS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RendS2CPacket::new)
                .encoder(RendS2CPacket::toBytes)
                .consumerMainThread(RendS2CPacket::handle)
                .add();
        net.messageBuilder(PhaseUpdateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PhaseUpdateS2CPacket::new)
                .encoder(PhaseUpdateS2CPacket::toBytes)
                .consumerMainThread(PhaseUpdateS2CPacket::handle)
                .add();
        net.messageBuilder(TempoTheftS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TempoTheftS2CPacket::new)
                .encoder(TempoTheftS2CPacket::toBytes)
                .consumerMainThread(TempoTheftS2CPacket::handle)
                .add();
        net.messageBuilder(WhistlerUpdateS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(WhistlerUpdateS2CPacket::new)
                .encoder(WhistlerUpdateS2CPacket::toBytes)
                .consumerMainThread(WhistlerUpdateS2CPacket::handle)
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
