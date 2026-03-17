package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.client.indicators.boots.BlinkCooldownIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.networking.packet.SendBlinkShaderS2CPacket;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlinkManager extends ArmorEnchantManager {
    private static final double BLINK_DISTANCE = 10.0d;
    
    public BlinkManager(Player player) {
        super(EnchantType.BOOTS, 300, -30, false, player);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        if (player.level() instanceof ServerLevel level) {
            level.playSound(null, player.position().x, player.position().y, player.position().z,
                    ModSounds.BLINK_COOLDOWN.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
        }
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new BlinkCooldownIndicator(this);
    }
    
    @Override
    public boolean canUse() {
        return true;
    }
    
    @Override
    public void activate() {
        Vec3 playerPos = player.getEyePosition();
        Vec3 lookVector = player.getLookAngle().normalize();
        Vec3 targetPos = playerPos.add(
                lookVector.x * BLINK_DISTANCE,
                (lookVector.y * BLINK_DISTANCE) - 1,
                lookVector.z * BLINK_DISTANCE
        );
        
        BlockHitResult result = player.level().clip(
                new ClipContext(
                        playerPos,
                        targetPos,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        player
                )
        );
        
        boolean hitWall = false;
        if (result.getType() != BlockHitResult.Type.MISS) {
            Vec3 hitPos = result.getLocation();
            targetPos = hitPos.subtract(
                    lookVector.x * 0.5f,
                    lookVector.y * 2.5f,
                    lookVector.z * 0.5f
            );
        }
        
        player.teleportTo(
                targetPos.x,
                targetPos.y,
                targetPos.z
        );
        if (player.level() instanceof ServerLevel level) {
            
            ParticleShapes.renderLine(level, playerPos, targetPos, ParticleTypes.PORTAL, 10);
            
            LodestoneParticles.blinkParticles(level, targetPos.add(player.getLookAngle().scale(8)));
            level.playSound(null, targetPos.x, targetPos.y, targetPos.z,
                    ModSounds.BLINK_USE_WALL.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
            level.playSound(null, playerPos.x, playerPos.y, playerPos.z,
                    net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT,
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
            
            
            targetPos = new Vec3(targetPos.x, targetPos.y - 0.5, targetPos.z);
            ICVUtils.sendPacketInRange(level, targetPos, 200, new SendBlinkShaderS2CPacket(targetPos));
        }
        LodestoneParticles.blinkParticles(player.level(), playerPos);
        
        //for (Vec3 pos : ParticleShapes.renderRingList(player.level(),targetPos,10,1.5F)) {
        //  System.out.println(pos);
        //  LodestoneParticles.blinkParticles(player.level(), pos);
        //}
    }
    
    
}