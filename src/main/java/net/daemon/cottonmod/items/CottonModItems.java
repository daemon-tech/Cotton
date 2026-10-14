package net.daemon.cottonmod.items;

import net.daemon.cottonmod.CottonMod;
import net.daemon.cottonmod.blocks.CottonModBlock;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CottonModItems {
    public static final Item COTTON_ITEM = new Item(new Item.Settings());
    public static final Item COTTON_SEED_ITEM = new AliasedBlockItem(CottonModBlock.COTTON_CROP_BLOCK, new Item.Settings());

    public static void registerItems() {
        Registry.register(Registries.ITEM, CottonMod.id("cotton_seed_item"), COTTON_SEED_ITEM);
        Registry.register(Registries.ITEM, CottonMod.id("cotton_item"), COTTON_ITEM);
    }
}
