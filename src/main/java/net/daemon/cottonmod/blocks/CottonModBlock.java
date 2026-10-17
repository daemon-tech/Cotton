package net.daemon.cottonmod.blocks;

import net.daemon.cottonmod.CottonMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CottonModBlock {
    public static final ResourceKey<Block> COTTON_CROP_KEY = ResourceKey.create(Registries.BLOCK, CottonMod.id("cotton_crop_block"));

    public static final CottonCropBlock COTTON_CROP_BLOCK = new CottonCropBlock(BlockBehaviour.Properties.of()
            .setId(COTTON_CROP_KEY)
            .mapColor(MapColor.PLANT).noCollision().randomTicks()
            .instabreak().sound(SoundType.CROP).pushReaction(PushReaction.POPPED));

    public static void registerBlock() {
        Registry.register(BuiltInRegistries.BLOCK, COTTON_CROP_KEY, COTTON_CROP_BLOCK);
    }
}
