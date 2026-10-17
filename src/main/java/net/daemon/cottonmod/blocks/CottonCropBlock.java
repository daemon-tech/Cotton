package net.daemon.cottonmod.blocks;

import net.daemon.cottonmod.items.CottonModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CottonCropBlock extends CropBlock {
    public CottonCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CottonModItems.COTTON_SEED_ITEM;
    }
}
