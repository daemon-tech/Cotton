package net.daemon.cottonmod;

import net.daemon.cottonmod.blocks.CottonModBlock;
import net.daemon.cottonmod.items.CottonModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
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

        // Short grass ("grass" before 1.20.3) drops cotton seeds like wheat seeds.
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && Blocks.SHORT_GRASS.getLootTableKey().equals(key)) {
                // Vanilla-like: 12.5% chance, Fortune adds up to +2 per level (uniform bonus), no drop with shears
                // (the vanilla grass table already only drops seeds when not using shears; we mirror wheat seeds).
                RegistryWrapper.Impl<Enchantment> enchantments = registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
                tableBuilder.pool(LootPool.builder()
                        .conditionally(InvertedLootCondition.builder(MatchToolLootCondition.builder(
                                ItemPredicate.Builder.create().items(Items.SHEARS))))
                        .with(ItemEntry.builder(CottonModItems.COTTON_SEED_ITEM)
                                .conditionally(RandomChanceLootCondition.builder(0.125f))
                                .apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE), 2))
                                .apply(ExplosionDecayLootFunction.builder())));
            }
        });
    }
}
