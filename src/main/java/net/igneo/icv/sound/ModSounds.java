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
    
    public static final RegistryObject<SoundEvent> COMET_HIT = registerSoundEvents("comet_hit");
    public static final RegistryObject<SoundEvent> COMET_SPAWN = registerSoundEvents("comet_spawn");
    public static final RegistryObject<SoundEvent> GUST = registerSoundEvents("gust");
    public static final RegistryObject<SoundEvent> PARRY = registerSoundEvents("parry");
    
    
    public static final RegistryObject<SoundEvent> BLINK_COOLDOWN = registerSoundEvents("blink_cooldown");
    public static final RegistryObject<SoundEvent> BLINK_USE = registerSoundEvents("blink_use");
    public static final RegistryObject<SoundEvent> BLINK_USE_WALL = registerSoundEvents("blink_use_wall");


    public static final RegistryObject<SoundEvent> SURF_IDLE = registerSoundEvents("surf_idle");
    public static final RegistryObject<SoundEvent> SURF_USE = registerSoundEvents("surf_use");
    public static final RegistryObject<SoundEvent> SURF_COOLDOWN = registerSoundEvents("surf_cooldown");
    public static final RegistryObject<SoundEvent> SURF_PICKUP = registerSoundEvents("surf_pickup");

    public static final RegistryObject<SoundEvent> STONE_CALLER_ACTIVATE = registerSoundEvents("stone_caller_activate");
    public static final RegistryObject<SoundEvent> STONE_CALLER_BREAK = registerSoundEvents("stone_caller_break");
    public static final RegistryObject<SoundEvent> STONE_CALLER_DESPAWN = registerSoundEvents("stone_caller_despawn");
    public static final RegistryObject<SoundEvent> STONE_CALLER_HIT = registerSoundEvents("stone_caller_hit");
    public static final RegistryObject<SoundEvent> STONE_CALLER_RECHARGE = registerSoundEvents("stone_caller_recharge");

    public static final RegistryObject<SoundEvent> STASIS_ACTIVATE = registerSoundEvents("stasis_activate");
    public static final RegistryObject<SoundEvent> STASIS_BREAK = registerSoundEvents("stasis_break");
    public static final RegistryObject<SoundEvent> STASIS_IDLE = registerSoundEvents("stasis_idle");
    public static final RegistryObject<SoundEvent> STASIS_RECHARGE = registerSoundEvents("stasis_recharge");
    public static final RegistryObject<SoundEvent> STASIS_HIT = registerSoundEvents("stasis_hit");

    public static final RegistryObject<SoundEvent> ASCENSION_USE = registerSoundEvents("ascension_use");
    public static final RegistryObject<SoundEvent> ASCENSION_RECHARGE = registerSoundEvents("ascension_recharge");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ICV.MOD_ID, name)));
    }
    
    public static void register(IEventBus eventbus) {
        SOUND_EVENTS.register(eventbus);
    }
}
