package net.daemon.cottonmod.blocks;

import net.daemon.cottonmod.CottonMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class CottonModBlock {
    public static final CottonCropBlock COTTON_CROP_BLOCK = new CottonCropBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_GREEN).nonOpaque().noCollision().ticksRandomly()
            .breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY));

    public static void registerBlock() {
        Registry.register(Registries.BLOCK, CottonMod.id("cotton_crop_block"), COTTON_CROP_BLOCK);
    }
}
