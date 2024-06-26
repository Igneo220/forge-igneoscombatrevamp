package net.igneo.icv.enchantmentActions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class PlayerEnchantmentActions {


    //acrobatic enchantment work
    private boolean acrobatBonus;
    public boolean getAcrobatBonus() {
        return this.acrobatBonus;
    }
    public void setAcrobatBonus(boolean newValue) {
        this.acrobatBonus = newValue;
    }


    //blitz enchantment work

    private int blitzBoostCount;
    private long blitzTime;

    public int getBlitzBoostCount() {
        return this.blitzBoostCount;
    }
    public void addBlitzBoostCount() {
        this.blitzBoostCount = blitzBoostCount + 1;
        if (this.blitzBoostCount > 20) {
            this.blitzBoostCount = 20;
        }
    }
    public void resetBoostCount() {
        this.blitzBoostCount = 0;
    }

    public long getBlitzTime() {
        return this.blitzTime;
    }
    public void setBlitzTime(long time) {
        this.blitzTime = time;
    }



    //phantom pain enchantment work
    private int phantomHurt;
    private long phantomDelay;
    private LivingEntity phantomVictim;

    public int getPhantomHurt() {
        return this.phantomHurt;
    }
    public void addPhantomHurt(int hurt) {
        this.phantomHurt = this.phantomHurt + hurt;
    }
    public void resetPhantomHurt() {
        this.phantomHurt = 0;
    }
    public long getPhantomDelay() {
        return this.phantomDelay;
    }
    public void setPhantomDelay(Long newDelay) {
        this.phantomDelay = newDelay;
    }
    public LivingEntity getPhantomVictim() {
        return this.phantomVictim;
    }
    public void setPhantomVictim(LivingEntity victim) {
        this.phantomVictim = victim;
    }
    public void deletePhantomVictim() {
        this.phantomVictim = null;
    }



    //general work
    public void copyFrom(PlayerEnchantmentActions source) {

        this.acrobatBonus = source.acrobatBonus;

        this.blitzBoostCount = source.blitzBoostCount;
        this.blitzTime = source.blitzTime;

        this.phantomVictim = source.phantomVictim;
        this.phantomDelay = source.phantomDelay;
        this.phantomHurt = source.phantomHurt;
    }
    public void saveNBTData(CompoundTag nbt) {

        nbt.putInt("blitzBoostCount", blitzBoostCount);
        nbt.putLong("blitzTime", blitzTime);

    }

    public void loadNBTData(CompoundTag nbt) {

        blitzBoostCount = nbt.getInt("blitzBoostCount");
        blitzTime = nbt.getLong("blitzTime");

    }
}
