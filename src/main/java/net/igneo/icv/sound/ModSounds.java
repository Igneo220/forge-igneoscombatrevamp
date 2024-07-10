package net.igneo.icv.sound;

import net.igneo.icv.ICV;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ICV.MOD_ID);

    public static final RegistryObject<SoundEvent> SKEWERING_HIT = registerSoundEvents("skewering_hit");
    public static final RegistryObject<SoundEvent> PHANTOM_HURT = registerSoundEvents("phantom_hurt");
    public static final RegistryObject<SoundEvent> PHANTOM_HEAL = registerSoundEvents("phantom_heal");
    public static final RegistryObject<SoundEvent> ACRO_HIT = registerSoundEvents("acro_hit");
    public static final RegistryObject<SoundEvent> BOW_FUMBLE = registerSoundEvents("bow_fumble");
    public static final RegistryObject<SoundEvent> ACCELERATE = registerSoundEvents("accelerate");
    public static final RegistryObject<SoundEvent> HOLE_SHOT = registerSoundEvents("hole_shot");
    public static final RegistryObject<SoundEvent> HOLE_IDLE = registerSoundEvents("hole_idle");
    public static final RegistryObject<SoundEvent> KINETIC_HIT = registerSoundEvents("kinetic_hit");
    public static final RegistryObject<SoundEvent> ICE_HIT = registerSoundEvents("ice_hit");
    public static final RegistryObject<SoundEvent> BLIZZARD_START = registerSoundEvents("blizzard_start");
    public static final RegistryObject<SoundEvent> COMET_HIT = registerSoundEvents("comet_hit");
    public static final RegistryObject<SoundEvent> COMET_IDLE = registerSoundEvents("comet_idle");
    public static final RegistryObject<SoundEvent> COMET_SPAWN = registerSoundEvents("comet_spawn");
    public static final RegistryObject<SoundEvent> COUNTERWEIGHTED_MISS = registerSoundEvents("c_miss");
    public static final RegistryObject<SoundEvent> CRUSH = registerSoundEvents("crush");
    public static final RegistryObject<SoundEvent> EXTRACT = registerSoundEvents("extract");
    public static final RegistryObject<SoundEvent> GUST = registerSoundEvents("gust");
    public static final RegistryObject<SoundEvent> FLARE = registerSoundEvents("flare");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ICV.MOD_ID,name)));
    }

    public static void register(IEventBus eventbus) {
        SOUND_EVENTS.register(eventbus);
    }
}
