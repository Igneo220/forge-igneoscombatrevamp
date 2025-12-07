package net.igneo.icv.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.StasisManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.sound.ModSounds;
import net.igneo.icv.sound.tickable.ParticleSound;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AscensionRune extends TextureSheetParticle {
    int renderTicks = 0;
    protected AscensionRune(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, SpriteSet spriteSet, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY+0.01, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.friction = 0.8F;
        this.xd = 0;
        this.yd = 0.4;
        this.zd = 0;
        this.quadSize = 4;
        this.lifetime = 20;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {

        this.alpha = (-(1/(float)lifetime) * age + 1);

        ++renderTicks;

        Vec3 vec3 = pRenderInfo.getPosition();
        float f = (float)(Mth.lerp((double)pPartialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)pPartialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)pPartialTicks, this.zo, this.z) - vec3.z());
        Quaternionf facing = new Quaternionf()
                .rotationXYZ((float)Math.toRadians(90), 0f, 0f);

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(pPartialTicks);

        Vector3f right    = new Vector3f(1, 0, 0).rotate(facing);
        Vector3f up       = new Vector3f(0, 1, 0).rotate(facing);
        Vector3f forward  = new Vector3f(0, 0, 1).rotate(facing);

        float roll = (float) -renderTicks /10;
        float c = (float)Math.cos(roll);
        float s = (float)Math.sin(roll);

        Vec3 rotatedRight = new Vec3(
                right.x * c + up.x * s,
                right.y * c + up.y * s,
                right.z * c + up.z * s
        );

        Vec3 rotatedUp = new Vec3(
                up.x * c - right.x * s,
                up.y * c - right.y * s,
                up.z * c - right.z * s
        );

        Matrix3f mat = new Matrix3f();
        mat.m00 = (float)rotatedRight.x; mat.m01 = (float)rotatedRight.y; mat.m02 = (float)rotatedRight.z;
        mat.m10 = (float)rotatedUp.x;    mat.m11 = (float)rotatedUp.y;    mat.m12 = (float)rotatedUp.z;
        mat.m20 = (float)forward.x;      mat.m21 = (float)forward.y;      mat.m22 = (float)forward.z;

        facing = new Quaternionf().setFromNormalized(mat);

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(facing);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(pPartialTicks);
        pBuffer.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 0xF000F0;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new AscensionRune(level, x, y, z, dx, this.sprites, dy, dz);
        }
    }
}
