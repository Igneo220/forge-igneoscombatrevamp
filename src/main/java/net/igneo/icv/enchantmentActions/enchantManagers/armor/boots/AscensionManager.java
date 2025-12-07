package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import net.igneo.icv.client.indicators.AscensionCooldownIndicator;
import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MovePlayerS2CPacket;
import net.igneo.icv.networking.packet.PushPlayerS2CPacket;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class AscensionManager extends ArmorEnchantManager {
    private boolean persist;
    private boolean primed;
    
    public AscensionManager(Player player) {
        super(EnchantType.BOOTS, 200, 10, false, player);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        player.level().playSound(null, BlockPos.containing(player.position()),ModSounds.ASCENSION_RECHARGE.get(), SoundSource.PLAYERS ,1F, 1F);
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new AscensionCooldownIndicator(this);
    }
    
    @Override
    public boolean canUse() {
        return !active;
    }
    
    @Override
    public void activate() {
        if (player.level() instanceof ServerLevel level) {
            for (ServerPlayer sPlayer: level.players()) {
                if (sPlayer.distanceTo(player) < 500) {
                    level.sendParticles(sPlayer, ModParticles.ASCENSION_RUNE.get(), true, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0);

                }
            }
            level.playSound(null,player.blockPosition(), ModSounds.ASCENSION_USE.get(), SoundSource.PLAYERS);
        }
        for (int i = 0; i < 3; ++i) {
            System.out.println(((float) 1 /(i+1))*8);
            for (Vec3 pos : ParticleShapes.renderRingList(player.level(), player.position().add(0,0.1 + (double) i /2,0),10-2*i,4-i)) {
                LodestoneParticles.upParticles(player.level(), pos,new Vec3(0,1,0));
            }
        }
        for (Vec3 pos : ParticleShapes.renderRingList(player.level(), player.position().add(0,0.1,0),8,5)) {
            LodestoneParticles.smokeParticles(player.level(), pos,new Color(30, 199, 136));
        }
        for (Entity entity : ICVUtils.collectEntitiesBox(this.player.level(), player.position(),4)) {
            if (entity instanceof Player) {
                if (entity instanceof ServerPlayer player && player != this.player) {
                    ModMessages.sendToPlayer(new PushPlayerS2CPacket(new Vec3(0, 1.2, 0)), player);
                } else if (!player.isCrouching()){
                    player.addDeltaMovement(new Vec3(0,1,0));
                }
            } else {
                entity.addDeltaMovement(new Vec3(0,1.2,0));
            }
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (active) {
            ++activeTicks;
        } else {
            activeTicks = 0;
        }
        if (activeTicks > 60 && !player.onGround() && persist) {
            persist = false;
            primed = true;
            player.setDeltaMovement(0, -1, 0);
        }
        if (primed && player.onGround()) {
            for (Entity e : ICVUtils.collectEntitiesBox(player.level(), player.position(), 3)) {
                if (e != player && e instanceof LivingEntity entity) {
                    entity.hurt(player.damageSources().playerAttack(player), entity.getDeltaMovement().length() < 0.1 ? 20 : 10);
                }
            }
            resetCoolDown();
            primed = false;
            persist = false;
            active = false;
            activeTicks = 0;
        }
    }
    
    @Override
    public void resetCoolDown() {
        super.resetCoolDown();
        System.out.println("resettin");
        
    }
}