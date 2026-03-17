package net.igneo.icv.enchantmentActions.enchantManagers.armor.boots;

import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.igneo.icv.ICV;
import net.igneo.icv.client.animation.EnchantAnimationPlayer;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.leggings.TsunamiIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.EntityTracker;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.sound.ModSounds;
import net.igneo.icv.sound.tickable.FollowingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SurfManager extends ArmorEnchantManager implements EntityTracker {
    public SurfManager(Player player) {
        super(EnchantType.BOOTS, 450, -10, true, player);
    }
    
    public ICVEntity child = null;
    private EnchantAnimationPlayer currentAnim = null;
    private boolean riding;
    
    @Override
    public void tick() {
        super.tick();
        if (child != null) {
            player.setYBodyRot(player.getYRot());
            if (!child.isAlive()) {
                child = null;
            } else if (!riding) {
                player.startRiding(child);
                riding = true;
            }
        }
        if (currentAnim != null && !player.isPassenger()) {
            currentAnim.stop();
            animator.setAnimation(null);
            currentAnim = null;
        }
    }
    
    @Override
    public boolean canUse() {
        return child == null;
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new TsunamiIndicator(this);
    }
    
    @Override
    public void resetCoolDown() {
        super.resetCoolDown();
        riding = false;
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        if (player.level() instanceof ServerLevel level) {
            level.playSound(null, player.position().x, player.position().y, player.position().z,
                    ModSounds.SURF_COOLDOWN.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.3f, 1.0f);
        }
    }
    
    @Override
    public boolean shouldTickCooldown() {
        return child == null;
    }
    
    @Override
    public void activate() {
        player.setForcedPose(Pose.STANDING);
        if (player.level() instanceof ServerLevel) {
            child = ModEntities.SURF_WAVE.get().create(player.level());
            child.setOwner(player);
            child.setPos(player.getEyePosition());
            player.level().addFreshEntity(child);
            syncClientChild((ServerPlayer) player, child, this);
            player.level().playSound(null, player.position().x, player.position().y, player.position().z,
                    ModSounds.SURF_USE.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
            Minecraft.getInstance().getSoundManager().play(new FollowingSound(ModSounds.SURF_IDLE.get(), child));
            syncClientChild((ServerPlayer) player,child,this);
        } else if (player instanceof LocalPlayer) {
            for (Vec3 pos : ParticleShapes.renderSphereList(player.level(),player.getEyePosition(),10,10,1F)) {
                LodestoneParticles.waveParticlesBright(player.level(),pos,pos.subtract(player.position()).scale(2));
            }
            currentAnim = new EnchantAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ICV.MOD_ID, "surfanim")));
            this.animator.setAnimation(currentAnim);

        }
    }
    
    @Override
    public void onRemove() {
        if (this.child != null) this.child.discard();
    }
    
    @Override
    public ICVEntity getChild() {
        return child;
    }
    
    @Override
    public void setChild(ICVEntity entity) {
        child = entity;
    }
}
