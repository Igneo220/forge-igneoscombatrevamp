package net.igneo.icv.mixin;

import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.enchantment.RendEnchantment;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.igneo.icv.enchantment.RendEnchantment.rendHit;

@Mixin(Arrow.class)
public class ArrowMixin {/*extends AbstractArrow {



    private static long arrowtime;
    protected ArrowMixin(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }
    @Unique
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.isNoPhysics();
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
                System.out.println(entity2.getHealth());
            }
            if (level().isClientSide) {
                System.out.println(this.getOwner());
                System.out.println(Minecraft.getInstance().player);
                System.out.println(this.getOwner() == Minecraft.getInstance().player);
                if (this.getOwner() == Minecraft.getInstance().player) {
                    rendHit(entity);
                }
            }
            this.discard();
        } else if (!this.getTags().contains("phase")) {
            super.onHitEntity(pResult);
        } else {
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
            entity.hurt(damagesource, 6);
        }
    }
    @Unique
    @Override
    protected void onHit(HitResult pResult) {
        if (!this.getTags().contains("phase")) {
            super.onHit(pResult);
        }
        HitResult.Type hitresult$type = pResult.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)pResult);
            }
    }
    @Unique
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (!this.getTags().contains("phase")) {
            super.onHitBlock(pResult);
        }
        this.isNoPhysics();
    }

    @Override
    public boolean isNoPhysics() {
        if (this.getTags().contains("phase") && this.inGround) {
            return true;
        } else if (this.getTags().contains("phase")) {
            return false;
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
            this.noPhysics = false;
            isNoPhysics();
        }
        if (level().isClientSide) {
            if (this.getOwner() instanceof LocalPlayer){
                LocalPlayer player = (LocalPlayer) this.getOwner();
                if (this.getTags().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(player.getMainHandItem()).containsKey(ModEnchantments.WHISTLER.get())) {
                        if (!this.getTags().contains("whistle")) {
                            this.addTag("whistle");
                        }
                    } else if (EnchantmentHelper.getEnchantments(player.getMainHandItem()).containsKey(ModEnchantments.PHASING.get())) {
                        if (!this.getTags().contains("phase")) {
                            this.addTag("phase");
                        }
                    } else if (EnchantmentHelper.getEnchantments(player.getMainHandItem()).containsKey(ModEnchantments.REND.get())) {
                        if (!this.getTags().contains("rend")) {
                            this.addTag("rend");
                        }
                    }
                }
            }
        }
        if (this.getTags().contains("whistle")){
            //System.out.println("we do be whistling");
            if (this.getOwner() != null) {
                this.setDeltaMovement(this.getOwner().getLookAngle().scale(0.4));
            }
        }
        if (this.getTags().contains("mitosis")){
            //System.out.println("mitosis moment");
            if (!level().isClientSide) {
                double d0 = Math.abs(this.getDeltaMovement().x);
                double d1 = Math.abs(this.getDeltaMovement().y);
                double d2 = Math.abs(this.getDeltaMovement().z);
                if (System.currentTimeMillis() >= arrowtime + 250 && !this.inGround && !this.isInWater() && (d0 + d1 + d2) >= 2) {
                    System.out.println("splitting!!");
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
    }*/
}
