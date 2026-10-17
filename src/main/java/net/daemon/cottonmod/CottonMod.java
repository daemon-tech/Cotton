package net.daemon.cottonmod;

import net.daemon.cottonmod.blocks.CottonModBlock;
import net.daemon.cottonmod.items.CottonModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CottonMod implements ModInitializer {
    public static final String MOD_ID = "cottonmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        CottonModBlock.registerBlock();
        CottonModItems.registerItems();

        CreativeModeTab tab = FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(CottonModItems.COTTON_ITEM))
                .title(Component.translatable("itemGroup.cottonmod.cotton_item_group"))
                .displayItems((params, output) -> {
                    output.accept(CottonModItems.COTTON_SEED_ITEM);
                    output.accept(CottonModItems.COTTON_ITEM);
                })
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("cotton_item_group"), tab);

        // Short grass drops cotton seeds like wheat seeds: 12.5%, Fortune bonus, not with shears.
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && Blocks.SHORT_GRASS.getLootTable().map(key::equals).orElse(false)) {
                HolderGetter<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
                HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
                tableBuilder.withPool(LootPool.lootPool()
                        .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, Items.SHEARS))))
                        .add(LootItem.lootTableItem(CottonModItems.COTTON_SEED_ITEM)
                                .when(LootItemRandomChanceCondition.randomChance(0.125f))
                                .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE), 2))
                                .apply(ApplyExplosionDecay.explosionDecay())));
            }
        });
    }
}
