package net.igneo.icv.enchantmentActions.enchantManagers.armor.helmet;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.client.indicators.boots.StasisCooldownIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GravityWellManager extends ArmorEnchantManager {
    private Direction surfaceDirection;
    private Vec3 position;
    private static final double PUSH_STRENGTH = 1.5d;
    private static final double EFFECT_RADIUS = 5.0d;
    
    public GravityWellManager(Player player) {
        super(EnchantType.HELMET, 300, 10, false, player);
    }
    
    @Override
    public void onOffCoolDown(Player player) {
        // Method left empty as in original
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new StasisCooldownIndicator(this);
    }
    
    @Override
    public void activate() {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        
        HitResult hitResult = player.pick(5, 0f, false);
        
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            getBlockFacingDirection(blockHitResult);
        }
    }
    
    private void getBlockFacingDirection(BlockHitResult blockHitResult) {
        surfaceDirection = blockHitResult.getDirection();
        position = blockHitResult.getLocation();
        
        applyGravityEffect(surfaceDirection);
    }
    
    private void applyGravityEffect(Direction direction) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        
        List<Entity> entities = ICVUtils.collectEntitiesBox(level, position, 7.5f);
        Vec3 pushVector = getPushVector(direction);
        
        for (Entity entity : entities) {
            double distance = entity.position().distanceTo(position);
            double scaledStrength = PUSH_STRENGTH * (1.0 - (distance / EFFECT_RADIUS));
            
            if (scaledStrength <= 0) {
                continue;
            }
            
            Vec3 scaledPush = pushVector.scale(scaledStrength);
            entity.setDeltaMovement(entity.getDeltaMovement().add(scaledPush));
            
            if (entity instanceof LivingEntity && direction == Direction.UP) {
                entity.fallDistance = 0.0F;
            }
        }
    }
    
    private Vec3 getPushVector(Direction direction) {
        return switch (direction) {
            case UP -> new Vec3(0, PUSH_STRENGTH * 0.2f, 0);
            case DOWN -> new Vec3(0, -PUSH_STRENGTH, 0);
            case NORTH -> new Vec3(0, 0.2f, -PUSH_STRENGTH * 2);
            case SOUTH -> new Vec3(0, 0.2f, PUSH_STRENGTH * 2);
            case EAST -> new Vec3(PUSH_STRENGTH * 2, 0.2f, 0);
            case WEST -> new Vec3(-PUSH_STRENGTH * 2, 0.2f, 0);
        };
    }
    
    @Override
    public boolean canUse() {
        return stableCheck();
    }
}