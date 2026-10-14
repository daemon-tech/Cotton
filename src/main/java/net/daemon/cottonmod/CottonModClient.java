package net.daemon.cottonmod;

import net.daemon.cottonmod.blocks.CottonModBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class CottonModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CottonModBlock.COTTON_CROP_BLOCK, RenderLayer.getCutout());
    }
}
