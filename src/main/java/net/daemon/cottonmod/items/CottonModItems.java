package net.daemon.cottonmod.items;

import net.daemon.cottonmod.CottonMod;
import net.daemon.cottonmod.blocks.CottonModBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class CottonModItems {
    private static ResourceKey<Item> key(String name) {
        return ResourceKey.create(Registries.ITEM, CottonMod.id(name));
    }

    public static final Item COTTON_ITEM = new Item(new Item.Properties().setId(key("cotton_item")));
    public static final Item COTTON_SEED_ITEM = new BlockItem(CottonModBlock.COTTON_CROP_BLOCK,
            new Item.Properties().setId(key("cotton_seed_item")).useItemDescriptionPrefix()
                    .compostable(ContextIntProviders.COMPOSTABLE_LOW));

    public static void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, key("cotton_seed_item"), COTTON_SEED_ITEM);
        Registry.register(BuiltInRegistries.ITEM, key("cotton_item"), COTTON_ITEM);
    }
}
