package net.igneo.icv.entity;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.custom.CometEntity;
import net.igneo.icv.entity.custom.FireEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ICV.MOD_ID);

    public static final RegistryObject<EntityType<CometEntity>> COMET =
            ENTITY_TYPES.register("comet",() -> EntityType.Builder.<CometEntity>of(CometEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("comet"));
    public static final RegistryObject<EntityType<FireEntity>> FIRE =
            ENTITY_TYPES.register("fire",() -> EntityType.Builder.<FireEntity>of(FireEntity::new, MobCategory.MISC)
                    .sized(1f,1f).build("comet"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
    /*
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static final RegistryObject<EntityType<CometEntity>> COMET;
    public static final RegistryObject<EntityType<FireEntity>> FIRE;
    public static final RegistryObject<EntityType<IcicleEntity>> ICICLE;
    public static final RegistryObject<EntityType<BoltEntity>> BOLT;
    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE;

    public ModEntities() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "icv");
        COMET = ENTITY_TYPES.register("comet", () -> {
            return Builder.of(CometEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).build("comet");
        });
        FIRE = ENTITY_TYPES.register("fire", () -> {
            return Builder.<FireEntity>of(FireEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("fire");
        });
        ICICLE = ENTITY_TYPES.register("icicle", () -> {
            return Builder.of(IcicleEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("ice");
        });
        BOLT = ENTITY_TYPES.register("bolt", () -> {
            return Builder.of(BoltEntity::new, MobCategory.MISC).sized(1.0F, 0.5F).build("bolt");
        });
        BLACK_HOLE = ENTITY_TYPES.register("black_hole", () -> {
            return Builder.of(BlackHoleEntity::new, MobCategory.MISC).sized(0.9F, 0.9F).build("black_hole");
        });
    }*/
}