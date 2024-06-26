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

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ICV.MOD_ID,name)));
    }

    public static void register(IEventBus eventbus) {
        SOUND_EVENTS.register(eventbus);
    }
}
