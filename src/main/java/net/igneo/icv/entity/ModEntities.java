package net.igneo.icv.entity;

import net.igneo.icv.ICV;
import net.igneo.icv.entity.boots.soulEmber.SoulEmberEntity;
import net.igneo.icv.entity.boots.stonePillar.StonePillarEntity;
import net.igneo.icv.entity.boots.surfWave.SurfWaveEntity;
import net.igneo.icv.entity.chestplate.abyssStone.AbyssStoneEntity;
import net.igneo.icv.entity.chestplate.meteorSummoner.MeteorSummonerEntity;
import net.igneo.icv.entity.chestplate.soulOrb.SoulOrbEntity;
import net.igneo.icv.entity.chestplate.soulSpider.SoulSpiderEntity;
import net.igneo.icv.entity.helmet.blackHole.BlackHoleEntity;
import net.igneo.icv.entity.helmet.divineLightningRod.DivineLightningRodEntity;
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpike.IceSpikeEntity;
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner.IceSpikeSpawnerEntity;
import net.igneo.icv.entity.leggings.voidSpike.VoidSpikeEntity;
import net.igneo.icv.entity.leggings.wave.WaveEntity;
import net.igneo.icv.entity.weapon.FireRing.FireRingEntity;
import net.igneo.icv.entity.weapon.boostCharge.BoostChargeEntity;
import net.igneo.icv.entity.weapon.comet.CometEntity;
import net.igneo.icv.entity.weapon.ember.EmberEntity;
import net.igneo.icv.entity.weapon.snakeBite.SnakeBiteEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ICV.MOD_ID);
    
    private static <T extends net.minecraft.world.entity.Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(width, height)
                .build(name));
    }
    
    public static final RegistryObject<EntityType<CometEntity>> COMET = registerEntity("comet", CometEntity::new, 0.5f, 0.5f);
    public static final RegistryObject<EntityType<EmberEntity>> EMBER = registerEntity("ember", EmberEntity::new, 0.5f, 0.5f);
    public static final RegistryObject<EntityType<FireRingEntity>> FIRE_RING = registerEntity("fire_ring", FireRingEntity::new, 0.5f, 0.5f);
    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE = registerEntity("black_hole", BlackHoleEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<SnakeBiteEntity>> SNAKE_BITE = registerEntity("snake_bite", SnakeBiteEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<SoulOrbEntity>> SOUL_ORB = registerEntity("soul_orb", SoulOrbEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<SoulSpiderEntity>> SOUL_SPIDER = registerEntity("soul_spider", SoulSpiderEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<SurfWaveEntity>> SURF_WAVE = registerEntity("surf_wave", SurfWaveEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<StonePillarEntity>> STONE_PILLAR = registerEntity("stone_pillar", StonePillarEntity::new, 2f, 4f);
    public static final RegistryObject<EntityType<SoulEmberEntity>> SOUL_EMBER = registerEntity("soul_ember", SoulEmberEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<AbyssStoneEntity>> ABYSS_STONE = registerEntity("abyss_stone", AbyssStoneEntity::new, 2.5f, 4f);
    public static final RegistryObject<EntityType<WaveEntity>> WAVE = registerEntity("wave", WaveEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<VoidSpikeEntity>> VOID_SPIKE = registerEntity("void_spike", VoidSpikeEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<MeteorSummonerEntity>> METEOR_SUMMONER = registerEntity("meteor_summoner", MeteorSummonerEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<IceSpikeEntity>> ICE_SPIKE = registerEntity("ice_spike", IceSpikeEntity::new, 1f, 4f);
    public static final RegistryObject<EntityType<IceSpikeSpawnerEntity>> ICE_SPIKE_SPAWNER = registerEntity("ice_spike_spawner", IceSpikeSpawnerEntity::new, 0f, 0f);
    public static final RegistryObject<EntityType<DivineLightningRodEntity>> DIVINE_LIGHTNING_ROD = registerEntity("divine_lightning_rod", DivineLightningRodEntity::new, 1f, 1f);
    public static final RegistryObject<EntityType<BoostChargeEntity>> BOOST_CHARGE = registerEntity("boost_charge", BoostChargeEntity::new, 0.5f, 0.5f);
    
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}