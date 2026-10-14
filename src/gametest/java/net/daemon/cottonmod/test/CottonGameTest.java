package net.daemon.cottonmod.test;

import net.daemon.cottonmod.blocks.CottonModBlock;
import net.daemon.cottonmod.items.CottonModItems;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class CottonGameTest implements FabricGameTest {

    /** Dry farmland (no water in range) under a crop must stay farmland, like under wheat. */
    private void dryFarmlandKeepsCrop(TestContext ctx, Block crop) {
        BlockPos farm = new BlockPos(1, 1, 1);
        BlockPos up = farm.up();
        ctx.setBlockState(farm, Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 0));
        ctx.setBlockState(up, crop.getDefaultState().with(CropBlock.AGE, 7));
        ServerWorld world = ctx.getWorld();
        BlockPos absFarm = ctx.getAbsolutePos(farm);
        for (int i = 0; i < 50; i++) {
            BlockState s = world.getBlockState(absFarm);
            if (s.isOf(Blocks.FARMLAND)) s.randomTick(world, absFarm, world.random);
        }
        ctx.expectBlock(Blocks.FARMLAND, farm);
        ctx.expectBlock(crop, up);
        ctx.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void wheatDryFarmland(TestContext ctx) { dryFarmlandKeepsCrop(ctx, Blocks.WHEAT); }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void cottonDryFarmland(TestContext ctx) { dryFarmlandKeepsCrop(ctx, CottonModBlock.COTTON_CROP_BLOCK); }

    /** Immature crop drops only seeds; mature drops cotton + seeds (like wheat). */
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void cottonLoot(TestContext ctx) {
        ServerWorld world = ctx.getWorld();
        BlockPos pos = ctx.getAbsolutePos(new BlockPos(1, 2, 1));
        for (int age = 0; age <= 7; age++) {
            BlockState st = CottonModBlock.COTTON_CROP_BLOCK.getDefaultState().with(CropBlock.AGE, age);
            for (int n = 0; n < 20; n++) {
                var drops = Block.getDroppedStacks(st, world, pos, null);
                boolean cotton = drops.stream().anyMatch(d -> d.isOf(CottonModItems.COTTON_ITEM));
                boolean seed = drops.stream().anyMatch(d -> d.isOf(CottonModItems.COTTON_SEED_ITEM));
                ctx.assertTrue(cotton == (age == 7), "cotton drop at age " + age + ": " + drops);
                ctx.assertTrue(seed, "seed always drops at age " + age + ": " + drops);
            }
        }
        ctx.complete();
    }
}
