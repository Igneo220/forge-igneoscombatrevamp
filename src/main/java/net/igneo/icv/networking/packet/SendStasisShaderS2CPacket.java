package net.igneo.icv.networking.packet;

import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.shader.postProcessors.StasisPostProcessor;
import net.igneo.icv.shader.shader.StasisFx;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Supplier;

public class SendStasisShaderS2CPacket {

    private final double x;
    private final double y;
    private final double z;
    private final UUID id;

    public SendStasisShaderS2CPacket(Vec3 pos, UUID id) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        this.id = id;
    }

    public SendStasisShaderS2CPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.id = buf.readUUID();
    }
    
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeUUID(id);
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Vec3 pos = new Vec3(x, y, z);
            Vector3f center = pos.toVector3f();
            Vector3f color = new Vector3f(0.988F, 0.827F, 0.011F);
            StasisFx fx = new StasisFx(center, color, Minecraft.getInstance().level.getPlayerByUUID(id));
            StasisPostProcessor.INSTANCE.addFxInstance(fx);

            Minecraft.getInstance().level.addParticle(ModParticles.STASIS_RUNE.get(),center.x,center.y,center.z,0,0,0);
        });
        return true;
    }
}
