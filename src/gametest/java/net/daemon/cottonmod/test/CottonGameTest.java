package net.daemon.cottonmod.test;

import java.util.List;

import net.daemon.cottonmod.blocks.CottonModBlock;
import net.daemon.cottonmod.items.CottonModItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CottonGameTest {

    /** Dry farmland (no water in range) under a crop must stay farmland, like under wheat. */
    private void dryFarmlandKeepsCrop(GameTestHelper ctx, Block crop) {
        BlockPos farm = new BlockPos(1, 1, 1);
        BlockPos up = farm.above();
        ctx.setBlock(farm, Blocks.FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE, 0));
        ctx.setBlock(up, crop.defaultBlockState().setValue(CropBlock.AGE, 7));
        ServerLevel level = ctx.getLevel();
        BlockPos absFarm = ctx.absolutePos(farm);
        for (int i = 0; i < 50; i++) {
            BlockState s = level.getBlockState(absFarm);
            if (s.is(Blocks.FARMLAND)) s.randomTick(level, absFarm, level.getRandom());
        }
        ctx.assertBlockPresent(Blocks.FARMLAND, farm);
        ctx.assertBlockPresent(crop, up);
        ctx.succeed();
    }

    @GameTest
    public void wheatDryFarmland(GameTestHelper ctx) { dryFarmlandKeepsCrop(ctx, Blocks.WHEAT); }

    @GameTest
    public void cottonDryFarmland(GameTestHelper ctx) { dryFarmlandKeepsCrop(ctx, CottonModBlock.COTTON_CROP_BLOCK); }

    /** Immature crop drops only seeds; mature drops cotton + seeds (like wheat). */
    @GameTest
    public void cottonLoot(GameTestHelper ctx) {
        ServerLevel level = ctx.getLevel();
        BlockPos pos = ctx.absolutePos(new BlockPos(1, 2, 1));
        for (int age = 0; age <= 7; age++) {
            BlockState st = CottonModBlock.COTTON_CROP_BLOCK.defaultBlockState().setValue(CropBlock.AGE, age);
            for (int n = 0; n < 20; n++) {
                List<ItemStack> drops = Block.getDrops(st, level, pos, null);
                boolean cotton = drops.stream().anyMatch(d -> d.is(CottonModItems.COTTON_ITEM));
                boolean seed = drops.stream().anyMatch(d -> d.is(CottonModItems.COTTON_SEED_ITEM));
                ctx.assertTrue(cotton == (age == 7), "cotton drop at age " + age + ": " + drops);
                ctx.assertTrue(seed, "seed always drops at age " + age + ": " + drops);
            }
        }
        ctx.succeed();
    }
}
