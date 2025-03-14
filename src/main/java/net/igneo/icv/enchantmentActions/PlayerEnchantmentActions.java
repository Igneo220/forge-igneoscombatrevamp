package net.igneo.icv.enchantmentActions;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public class PlayerEnchantmentActions {

    /*
            0=boots
            1=leggings
            2=chestplate
            3=helmet
            4=mainhand
            5=offhand
     */
    private EnchantmentManager[] managers = new EnchantmentManager[6];

    public EnchantmentManager getManager(int slot) {
        return managers[slot];
    }
    public EnchantmentManager[] getManagers() {
        return managers;
    }
    public boolean animated = false;

    public void setManager(EnchantmentManager manager, int slot) {
        if (manager == null || EnchantType.applicableSlot(manager.getType(),slot)) {
            if (this.managers[slot] != null) {
                this.managers[slot].onRemove();
            }
            this.managers[slot] = manager;
            if (manager != null) {
                manager.onEquip();
            }
            if (slot < 4 && FMLEnvironment.dist.isClient()) {
                if (manager instanceof ArmorEnchantManager aManager) {
                    indicators[slot] = aManager.getIndicator();
                } else {
                    indicators[slot] = null;
                }
            }
        } else {
            if (this.managers[slot] != null) {
                this.managers[slot].onRemove();
            }
            this.managers[slot] = null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public EnchantIndicator[] indicators = new EnchantIndicator[4];



    //--------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    
    private boolean concussed = false;
    public boolean getConcussed() {
        return this.concussed;
    }
    public void setConcussed(boolean value) {
        this.concussed = value;
    }
    //enchant check
    private int helmetID = 0;
    public int getHelmetID() {
        return this.helmetID;
    }
    public void setHelmetID(int newint) {
        this.helmetID = newint;
    }
    private int chestID = 0;
    public int getChestID() {
        return this.chestID;
    }
    public void setChestID(int newint) {
        this.chestID = newint;
    }
    private int legID = 0;
    public int getLegID() {
        return this.legID;
    }
    public void setLegID(int newint) {
        this.legID = newint;
    }
    private int bootID = 0;
    public int getBootID() {
        return this.bootID;
    }
    public void setBootID(int newint) {
        this.bootID = newint;
    }

    //armor check
    private NonNullList<String> playerTrims = NonNullList.withSize(4, "NONE");
    public NonNullList<String> getPlayerTrims() {
        return this.playerTrims;
    }
    public void setPlayerTrims(String string, int index) {
        this.playerTrims.set(index,string);
    }
    private NonNullList<String> playerEnchantments = NonNullList.withSize(4, "NONE");
    public NonNullList<String> getPlayerEnchantments() {
        return this.playerEnchantments;
    }
    public void setPlayerEnchantments(String string, int index) {
        this.playerEnchantments.set(index,string);
    }

    //trim variables
    private long vexTimer;
    public long getVexTimer() {
        return this.vexTimer;
    }
    public void setVexTimer(long inLong) {
        this.vexTimer = inLong;
    }
    private int wayBuff;
    public int getWayBuff() {
        return this.wayBuff;
    }
    public void setWayBuff(int newBuff) {
        this.wayBuff = newBuff;
    }

    private int wildBuff;
    public int getWildBuff() {
        return this.wildBuff;
    }
    public void setWildBuff(int newBuff) {
        this.wildBuff = newBuff;
    }
    private int silenceBuff;
    public int getSilenceBuff() {
        return this.silenceBuff;
    }
    public void setSilenceBuff(int newBuff) {
        this.silenceBuff = newBuff;
    }
    private int snoutBuff;
    public int getSnoutBuff() {
        return this.snoutBuff;
    }
    public void setSnoutBuff(int newBuff) {
        this.snoutBuff = newBuff;
    }
    private int hostBuff;
    public int getHostBuff() {
        return this.hostBuff;
    }
    public void setHostBuff(int newBuff) {
        this.hostBuff = newBuff;
    }
    private int duneBuff;
    public int getDuneBuff() {
        return this.duneBuff;
    }
    public void setDuneBuff(int newBuff) {
        this.duneBuff = newBuff;
    }
    private int shaperBuff;
    public int getShaperBuff() {
        return this.shaperBuff;
    }
    public void setShaperBuff(int newBuff) {
        this.shaperBuff = newBuff;
    }
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



    //black hole enchantment work
    private long holeCooldown;

    public long getHoleCooldown() {
        return this.holeCooldown;
    }
    public void setHoleCooldown(long newCooldown) {
        this.holeCooldown = newCooldown;
    }



    //Kinetic enchantment work
    private double kinX;
    private double kinZ;

    public double getKinX() {
        return this.kinX;
    }
    public void setKinX(double X) {
        this.kinX = X;
    }
    public double getKinZ() {
        return this.kinZ;
    }
    public void setKinZ(double Z) {
        this.kinZ = Z;
    }



    //parry enchantment work
    private long parryTime;
    public long getParryTime() {
        return this.parryTime;
    }
    public void setParryTime() {
        this.parryTime = System.currentTimeMillis();
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


    //stone caller work
    private long stoneTime = 0;
    private int stoneX;
    private int stoneY;
    private int stoneZ;
    private int stoneLookX;
    private int stoneLookZ;
    private int stoneCeiling;
    public long getStoneTime() {
        return this.stoneTime;
    }
    public void setStoneTime(long newTime) {
        this.stoneTime = newTime;
    }
    public int getStoneX() {
        return this.stoneX;
    }
    public void setStoneX(int newint) {
        this.stoneX = newint;
    }
    public int getStoneY() {
        return this.stoneY;
    }
    public void setStoneY(int newint) {
        this.stoneY = newint;
    }
    public int getStoneZ() {
        return this.stoneZ;
    }
    public void setStoneZ(int newint) {
        this.stoneZ = newint;
    }
    public int getStoneLookX() {
        return this.stoneLookX;
    }
    public void setStoneLookX(int newint) {
        this.stoneLookX = newint;
    }
    public int getStoneLookZ() {
        return this.stoneLookZ;
    }
    public void setStoneLookZ(int newint) {
        this.stoneLookZ = newint;
    }
    public int getStoneCeiling() {
        return this.stoneCeiling;
    }
    public void setStoneCeiling(int newInt) {
        this.stoneCeiling = newInt;
    }



    //general work
    public void copyFrom(PlayerEnchantmentActions source) {

        this.acrobatBonus = source.acrobatBonus;

        this.blitzBoostCount = source.blitzBoostCount;
        this.blitzTime = source.blitzTime;

        this.holeCooldown = source.holeCooldown;

        this.parryTime = source.parryTime;

        this.phantomVictim = source.phantomVictim;
        this.phantomDelay = source.phantomDelay;
        this.phantomHurt = source.phantomHurt;

        this.stoneTime = source.stoneTime;
        this.stoneX = source.stoneX;
        this.stoneY = source.stoneY;
        this.stoneZ = source.stoneZ;
        this.stoneLookX = source.stoneLookX;
        this.stoneLookZ = source.stoneLookZ;
        this.stoneCeiling = source.stoneCeiling;
    }
    public void saveNBTData(CompoundTag nbt) {

        nbt.putBoolean("acrobatBonus",acrobatBonus);

        nbt.putInt("blitzBoostCount", blitzBoostCount);
        nbt.putLong("blitzTime", blitzTime);

        nbt.putLong("holeCooldown",holeCooldown);

        nbt.putDouble("kinX",kinX);
        nbt.putDouble("kinZ",kinZ);

        nbt.putLong("parryTime",parryTime);

        nbt.putLong("phantomDelay",phantomDelay);
        nbt.putInt("phantomHurt",phantomHurt);

        nbt.putLong("stoneTime",stoneTime);
        nbt.putInt("stoneX",stoneX);
        nbt.putInt("stoneY",stoneY);
        nbt.putInt("stoneZ",stoneZ);
        nbt.putInt("stoneLookX",stoneLookX);
        nbt.putInt("stoneLookZ",stoneLookZ);
        nbt.putInt("stoneCeiling",stoneCeiling);
    }

    public void loadNBTData(CompoundTag nbt) {

        acrobatBonus = nbt.getBoolean("acrobatBonus");

        blitzBoostCount = nbt.getInt("blitzBoostCount");
        blitzTime = nbt.getLong("blitzTime");

        holeCooldown = nbt.getLong("holeCooldown");

        kinX = nbt.getLong("kinX");
        kinZ = nbt.getLong("kinZ");

        parryTime = nbt.getLong("parryTime");

        phantomDelay = nbt.getLong("phantomDelay");
        phantomHurt = nbt.getInt("phantomHurt");

        stoneTime = nbt.getLong("stoneTime");
        stoneX = nbt.getInt("stoneX");
        stoneY = nbt.getInt("stoneY");
        stoneZ = nbt.getInt("stoneZ");
        stoneLookX = nbt.getInt("stoneLookX");
        stoneLookZ = nbt.getInt("stoneLookZ");
        stoneCeiling = nbt.getInt("stoneCeiling");
    }



}
