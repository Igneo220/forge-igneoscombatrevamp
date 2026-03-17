package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.helmet.blackHole.BlackHoleEntity;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.shader.postProcessors.BlackHolePostProcessor;
import net.igneo.icv.shader.postProcessors.StasisPostProcessor;
import net.igneo.icv.shader.shader.BlackHoleFx;
import net.igneo.icv.shader.shader.StasisFx;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.UUID;
import java.util.function.Supplier;

public class SendBlackHoleShaderS2CPacket {

    private final int id;

    public SendBlackHoleShaderS2CPacket(int id) {
        this.id = id;
    }

    public SendBlackHoleShaderS2CPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
    }
    
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlackHoleFx fx = new BlackHoleFx((BlackHoleEntity) Minecraft.getInstance().level.getEntity(id));
            BlackHolePostProcessor.INSTANCE.addFxInstance(fx);

            System.out.println("ye it happenin");
            //Minecraft.getInstance().level.addParticle(ModParticles.STASIS_RUNE.get(),center.x,center.y,center.z,0,0,0);
        });
        return true;
    }
}
