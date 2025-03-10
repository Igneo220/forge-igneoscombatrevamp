package net.igneo.icv.mixin;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.PhaseUpdateS2CPacket;
import net.igneo.icv.networking.packet.RendS2CPacket;
import net.igneo.icv.networking.packet.WhistlerUpdateS2CPacket;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@Mixin(value = Arrow.class,priority = 999999999)
public class ArrowMixin extends AbstractArrow {
    @Unique
    private List<LivingEntity> phaseList = new ArrayList<LivingEntity>();

    @Unique
    private long inblock = 0;

    @Unique
    private long arrowtime = 0;
    protected ArrowMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (this.getTags().contains("rend")) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            DamageSource damagesource;
            if (entity1 == null) {
                damagesource = this.damageSources().arrow(this, this);
            } else {
                damagesource = this.damageSources().arrow(this, entity1);
                if (entity1 instanceof LivingEntity) {
                    ((LivingEntity)entity1).setLastHurtMob(entity);
                }
            }
            entity.hurt(damagesource, 0);
            if (entity instanceof LivingEntity) {
                LivingEntity entity2 = (LivingEntity) entity;
                if (this.getOwner() instanceof ServerPlayer) {
                    ModMessages.sendToPlayer(new RendS2CPacket(pResult.getEntity().getId()),(ServerPlayer) this.getOwner());
                }
                if (entity.level() instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel) entity.level();
                    level.playSound(null,this.blockPosition(), ModSounds.REND_HIT.get(), SoundSource.PLAYERS);
                    level.sendParticles(ModParticles.REND_HIT_PARTICLE.get(),this.getX(),this.getY(),this.getZ(),5,0,0,0,1);
                }
            }
            this.discard();
        } else if (this.getTags().contains("phase")) {

        } else if (this.getTags().contains("scatter")) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            DamageSource damagesource;
            if (entity1 == null) {
                damagesource = this.damageSources().arrow(this, this);
            } else {
                damagesource = this.damageSources().arrow(this, entity1);
                if (entity1 instanceof LivingEntity) {
                    ((LivingEntity) entity1).setLastHurtMob(entity);
                }
            }
            entity.hurt(damagesource, 2.5F);
            this.discard();
        } else if (this.getTags().contains("whistle")) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            DamageSource damagesource;
            if (entity1 == null) {
                damagesource = this.damageSources().arrow(this, this);
            } else {
                damagesource = this.damageSources().arrow(this, entity1);
                if (entity1 instanceof LivingEntity) {
                    ((LivingEntity) entity1).setLastHurtMob(entity);
                }
            }
            entity.hurt(damagesource, 15F);
            this.discard();
        } else {
            super.onHitEntity(pResult);
            this.discard();
        }
    }

    @Unique
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (!this.getTags().contains("phase")) {
            super.onHitBlock(pResult);
        } else {
            //this.addDeltaMovement(this.getDeltaMovement().scale(0.3));
            inblock = System.currentTimeMillis();
            this.noPhysics = true;
            isNoPhysics();
        }
    }

    @Override
    public boolean isNoPhysics() {
        if (this.getTags().contains("phase")) {
            return true;
        } else {
            return super.isNoPhysics();
        }
    }

    @Override
    public boolean isFree(double pX, double pY, double pZ) {
        if (!this.getTags().contains("phase")) {
            return super.isFree(pX, pY, pZ);
        } else {
            return true;
        }
    }

    @Inject(method = "tick",at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (this.getTags().contains("phase")) {
            this.setNoPhysics(true);
            this.noPhysics = true;
        }
        if(this.level() instanceof ServerLevel) {
            ServerLevel level = (ServerLevel) this.level();
            if (arrowtime == 0) {
                arrowtime = System.currentTimeMillis();
                if (this.getTags().contains("phase")) {
                    for (ServerPlayer player : level.players()) {
                        ModMessages.sendToPlayer(new PhaseUpdateS2CPacket(this.getId()), player);
                    }
                } else if (this.getTags().contains("whistle")) {
                    for (ServerPlayer player : level.players()) {
                        ModMessages.sendToPlayer(new WhistlerUpdateS2CPacket(this.getId()), player);
                    }
                }
            } else if (System.currentTimeMillis() >= arrowtime + 5000) {
                this.discard();
            }
            if (this.getTags().contains("phase")) {
                for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getDeltaMovement().x/2,0,this.getDeltaMovement().z/2))) {
                    boolean cancel = false;
                    if (!phaseList.isEmpty()) {
                        for (LivingEntity phaseEntity : phaseList) {
                            if (entity == phaseEntity || entity == this.getOwner()) {
                                cancel = true;
                            }
                        }
                    }
                    System.out.println(this.getOwner());
                    if (!cancel && entity != this.getOwner()) {
                        level.playSound(null, this.blockPosition(), ModSounds.PHASE.get(), SoundSource.PLAYERS, 2, (float) 0.3 + (float) abs(Math.random() + 0.5));
                        Entity entity1 = this.getOwner();
                        DamageSource damagesource;
                        if (entity1 == null) {
                            damagesource = this.damageSources().arrow(this, this);
                        } else {
                            damagesource = this.damageSources().arrow(this, entity1);
                            if (entity1 instanceof LivingEntity) {
                                ((LivingEntity) entity1).setLastHurtMob(entity);
                            }
                        }
                        if (entity != entity1) {

                            entity.hurt(damagesource, 10);
                            phaseList.add(entity);
                        }
                    }
                }
                //level.playSound(null,this.blockPosition(),ModSounds.PHASE.get(),SoundSource.PLAYERS);
                if (inblock != 0 && System.currentTimeMillis() > inblock + 50) {
                    level.playSound(null, this.blockPosition(), ModSounds.PHASE.get(), SoundSource.PLAYERS, 2, (float) 0.3 + (float) abs(Math.random() + 0.5));
                    inblock = 0;
                }
                level.sendParticles(ModParticles.PHASE_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                this.noPhysics = true;
                isNoPhysics();
            }
        }
        if (this.getTags().contains("whistle")){
            if (this.getOwner() != null) {
                this.setDeltaMovement(this.getOwner().getLookAngle().scale(0.6));
            }
        }
        if (this.getTags().contains("mitosis")){
            if (!level().isClientSide) {
                double d0 = Math.abs(this.getDeltaMovement().x);
                double d1 = Math.abs(this.getDeltaMovement().y);
                double d2 = Math.abs(this.getDeltaMovement().z);
                if (System.currentTimeMillis() >= arrowtime + 100 && !this.inGround && !this.isInWater() && (d0 + d1 + d2) >= 2) {
                    if (this.level() instanceof ServerLevel) {
                        ServerLevel level = (ServerLevel) this.level();
                        level.playSound(null,this.blockPosition(), ModSounds.MITOSIS.get(),SoundSource.PLAYERS);
                    }
                    arrowtime = System.currentTimeMillis();
                    Projectile mitosisArrow;
                    mitosisArrow = createArrow(level(),(LivingEntity) this.getOwner());
                    mitosisArrow.setPos(this.position());
                    mitosisArrow.setDeltaMovement(this.getDeltaMovement());
                    level().addFreshEntity(mitosisArrow);
                }
                double random = Math.random();
                if (random > 0.6) {
                    this.addDeltaMovement(new Vec3(0.01, 0, 0));
                } else if (random < 0.3) {
                    this.addDeltaMovement(new Vec3(0, 0.01, 0));
                } else {
                    this.addDeltaMovement(new Vec3(0, 0, 0.01));
                }
            }
        }
    }
    @Shadow
    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Unique
    private static AbstractArrow createArrow(Level pLevel, LivingEntity pLivingEntity) {
        ArrowItem arrowitem = (ArrowItem)(Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel,ItemStack.EMPTY, pLivingEntity);

        if (pLivingEntity instanceof Player) {
            abstractarrow.setCritArrow(true);
        }

        abstractarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        abstractarrow.setBaseDamage(1.5);

        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);

        return abstractarrow;
    }
}
