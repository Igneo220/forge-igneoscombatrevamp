package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class StoneCallerC2SPacket {
    private final int next;
    private final int x;
    private final int y;
    private final int z;
    public StoneCallerC2SPacket(int adding,int x1,int y1,int z1){
        this.next = adding;
        this.x = x1;
        this.y = y1;
        this.z = z1;
    }
    public StoneCallerC2SPacket(FriendlyByteBuf buf) {
        this.next = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(next);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            Thread HurtEntities = new Thread(() -> {
                for (Entity entity : level.getAllEntities()) {
                    if (entity.getBoundingBox().intersects(player.getEyePosition(),player.getEyePosition().add(player.getLookAngle().scale(20))) && entity != player && entity instanceof LivingEntity) {
                        entity.hurt(player.damageSources().fallingStalactite(player),5);
                    }
                }
            });
            HurtEntities.start();

            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if(enchVar.getStoneTime() == 0) {
                    enchVar.setStoneTime(System.currentTimeMillis());
                    enchVar.setStoneX(x);
                    enchVar.setStoneY(y);
                    enchVar.setStoneZ(z);
                    player.setDeltaMovement(0, 1, 0);
                    if (enchVar.getStoneCeiling() == 0) {
                        for (int j = 0; j <= 4; ++j) {
                            if (level.getBlockState(new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + j, enchVar.getStoneZ())).is(BlockTags.REPLACEABLE)) {
                                enchVar.setStoneCeiling(j+1);
                            } else {
                                break;
                            }
                        }
                    }
                    int d0 = 0;
                    int d1 = 0;
                    if (player.getLookAngle().x < -0.4) {
                        d0 = -1;
                    } else if (player.getLookAngle().x > 0.4) {
                        d0 = 1;
                    }
                    if (player.getLookAngle().z < -0.4) {
                        d1 = -1;
                    } else if (player.getLookAngle().z > 0.4) {
                        d1 = 1;
                    }
                    enchVar.setStoneLookX(0);
                    enchVar.setStoneLookZ(0);
                    int step = 0;
                    for (int j = 0; j < 25; ++j) {
                        if (level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*d0),enchVar.getStoneY() + step,enchVar.getStoneZ() + (j*d1))).is(BlockTags.REPLACEABLE)) {
                            boolean findingFloor = true;
                            while (findingFloor) {
                                if (level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * d0), enchVar.getStoneY() + step - 1, enchVar.getStoneZ() + (j * d1))).is(BlockTags.REPLACEABLE)) {
                                    --step;
                                } else {
                                    findingFloor = false;
                                }
                            }
                            if (step > -20) {
                                enchVar.setStoneLookX(j * d0);
                                enchVar.setStoneLookZ(j * d1);
                            } else {
                                break;
                            }
                        } else {
                            if (level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*d0),enchVar.getStoneY() + step + 1,enchVar.getStoneZ() + (j*d1))).is(BlockTags.REPLACEABLE)){
                                enchVar.setStoneLookX(j*d0);
                                enchVar.setStoneLookZ(j*d1);
                                ++step;
                            } else {
                                break;
                            }
                        }
                    }
                }
                int i = Math.abs(enchVar.getStoneLookX());
                if (Math.abs(enchVar.getStoneLookZ()) > Math.abs(enchVar.getStoneLookX())) {
                    i = Math.abs(enchVar.getStoneLookZ());
                }
                int m0 = 1;
                int m1 = 1;
                if (enchVar.getStoneLookX() < 0) {
                    m0 = -1;
                } else if (enchVar.getStoneLookX() == 0) {
                    m0 = 0;
                }
                if (enchVar.getStoneLookZ() < 0) {
                    m1 = -1;
                } else if (enchVar.getStoneLookZ() == 0) {
                    m1 = 0;
                }
                int ceilingOffset = 0;
                int offset = 0;
                for (int j = 0; j <= i; ++j) {
                    if (level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + next + offset, enchVar.getStoneZ() + (j*m1))).is(BlockTags.REPLACEABLE)) {
                        boolean findFloor = true;
                        while (findFloor) {
                            if (!level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + offset - 1, enchVar.getStoneZ() + (j * m1))).is(BlockTags.REPLACEABLE)) {
                                if (!level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset -1, enchVar.getStoneZ() + (j*m1))).getBlock().equals(Blocks.DRIPSTONE_BLOCK) && !level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset - 1, enchVar.getStoneZ() + (j*m1))).getBlock().equals(Blocks.POINTED_DRIPSTONE)) {
                                    findFloor = false;
                                } else {
                                    --offset;
                                }
                            } else {
                                --offset;
                            }
                        }
                        for (int p = 0; p <= enchVar.getStoneCeiling(); ++p) {
                            if (!level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset + p, enchVar.getStoneZ() + (j*m1))).is(BlockTags.REPLACEABLE)) {
                                if (!level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset + p, enchVar.getStoneZ() + (j*m1))).getBlock().equals(Blocks.DRIPSTONE_BLOCK) && !level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset + p, enchVar.getStoneZ() + (j*m1))).getBlock().equals(Blocks.POINTED_DRIPSTONE)) {
                                    ceilingOffset = enchVar.getStoneCeiling() - p;
                                    break;
                                }
                            }
                            if (p == enchVar.getStoneCeiling()) {
                                ceilingOffset = 0;
                            }
                        }
                        System.out.println("step: " + next + " ceiling: " + (enchVar.getStoneCeiling() - ceilingOffset));
                        if (next < enchVar.getStoneCeiling() - 2 - ceilingOffset) {
                            level.playSound(null, new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + next + offset, enchVar.getStoneZ()), SoundType.DEEPSLATE_BRICKS.getBreakSound(), SoundSource.PLAYERS, 10F, 0.1F);
                            level.setBlock(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + next+ offset, enchVar.getStoneZ() + (j * m1)), Blocks.DRIPSTONE_BLOCK.defaultBlockState(), 2);
                        } else if (next < enchVar.getStoneCeiling() - ceilingOffset){
                            level.playSound(null, new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + next + offset, enchVar.getStoneZ()), SoundType.DEEPSLATE_BRICKS.getBreakSound(), SoundSource.PLAYERS, 10F, 1.1F);
                            level.setBlock(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + next+ offset, enchVar.getStoneZ() + (j * m1)), Blocks.POINTED_DRIPSTONE.defaultBlockState(), 2);
                        }
                    } else if (level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + next + offset + 1, enchVar.getStoneZ() + (j*m1))).is(BlockTags.REPLACEABLE)) {
                        ++offset;
                        for (int p = 0; p <= enchVar.getStoneCeiling(); ++p) {
                            if (!level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset + p, enchVar.getStoneZ() + (j*m1))).is(BlockTags.REPLACEABLE)) {
                                if (!level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset + p, enchVar.getStoneZ() + (j*m1))).getBlock().equals(Blocks.DRIPSTONE_BLOCK) && !level.getBlockState(new BlockPos(enchVar.getStoneX() + (j*m0), enchVar.getStoneY() + offset + p, enchVar.getStoneZ() + (j*m1))).getBlock().equals(Blocks.POINTED_DRIPSTONE)) {
                                    ceilingOffset = enchVar.getStoneCeiling() - p;
                                    break;
                                }
                            }
                            if (p == enchVar.getStoneCeiling()) {
                                ceilingOffset = 0;
                            }
                        }
                        if (next < enchVar.getStoneCeiling() - 2 - ceilingOffset) {
                            level.playSound(null, new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + next + offset, enchVar.getStoneZ()), SoundType.DEEPSLATE_BRICKS.getBreakSound(), SoundSource.PLAYERS, 10F, 0.1F);
                            level.setBlock(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + next+ offset, enchVar.getStoneZ() + (j * m1)), Blocks.DRIPSTONE_BLOCK.defaultBlockState(), 2);
                        } else if (next < enchVar.getStoneCeiling() - ceilingOffset){
                            level.playSound(null, new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + next + offset, enchVar.getStoneZ()), SoundType.DEEPSLATE_BRICKS.getBreakSound(), SoundSource.PLAYERS, 10F, 1.1F);
                            level.setBlock(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + next+ offset, enchVar.getStoneZ() + (j * m1)), Blocks.POINTED_DRIPSTONE.defaultBlockState(), 2);
                        }
                    }
                }
            });
        });
        return true;
    }
}
