package net.daemon.cottonmod;

import net.daemon.cottonmod.blocks.CottonModBlock;
import net.daemon.cottonmod.items.CottonModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CottonMod implements ModInitializer {
    public static final String MOD_ID = "cottonmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        CottonModBlock.registerBlock();
        CottonModItems.registerItems();

        ItemGroup group = FabricItemGroup.builder()
                .icon(() -> new ItemStack(CottonModItems.COTTON_ITEM))
                .displayName(Text.translatable("itemGroup.cottonmod.cotton_item_group"))
                .entries((context, entries) -> {
                    entries.add(CottonModItems.COTTON_SEED_ITEM);
                    entries.add(CottonModItems.COTTON_ITEM);
                })
                .build();
        Registry.register(Registries.ITEM_GROUP, id("cotton_item_group"), group);

        // Short grass ("grass" before 1.20.3) drops cotton seeds, same behaviour as 1.0.4.
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && Blocks.SHORT_GRASS.getLootTableKey().equals(key)) {
                tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(CottonModItems.COTTON_SEED_ITEM)));
            }
        });
    }
}
