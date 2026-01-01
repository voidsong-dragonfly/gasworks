package voidsong.gasworks.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredBlock;
import voidsong.gasworks.common.block.CandelabraBlock;
import voidsong.gasworks.common.block.CompostBlock;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.block.SillBlock;
import voidsong.gasworks.common.block.properties.AshType;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.common.registry.GSItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GasworksLootTableProvider extends LootTableProvider {
    public GasworksLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Set.of(), List.of(), provider);
    }

    @Override
    @Nonnull
    public List<SubProviderEntry> getTables() {
        return ImmutableList.of(
            new SubProviderEntry(GasworksBlockLootSubProvider::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(WritableRegistry<LootTable> registry, @Nonnull ValidationContext tracker, @Nonnull ProblemReporter.Collector collector) {
        registry.forEach(table -> table.validate(tracker));
    }

    private static class GasworksBlockLootSubProvider extends BlockLootSubProvider {
        private GasworksBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
        }

        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        @Override
        @Nonnull
        protected Iterable<Block> getKnownBlocks() {
            return GSBlocks.BLOCKS.getEntries()
                .stream()
                .map(e -> (Block) e.value())
                .toList();
        }

        // Actually add our loot tables.
        @Override
        protected void generate() {
            /*
             * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
             */
            // Log stacks for fuel
            dropSelf(GSBlocks.OAK_LOG_PILE.get());
            dropSelf(GSBlocks.SPRUCE_LOG_PILE.get());
            dropSelf(GSBlocks.BIRCH_LOG_PILE.get());
            dropSelf(GSBlocks.JUNGLE_LOG_PILE.get());
            dropSelf(GSBlocks.ACACIA_LOG_PILE.get());
            dropSelf(GSBlocks.DARK_OAK_LOG_PILE.get());
            dropSelf(GSBlocks.CHERRY_LOG_PILE.get());
            dropSelf(GSBlocks.MANGROVE_LOG_PILE.get());
            dropSelf(GSBlocks.BAMBOO_LOG_PILE.get());
            // Coal stacks for fuel
            add(GSBlocks.COAL_PILE.get(), createSingleItemTableWithSilkTouch(GSBlocks.COAL_PILE.get(), Items.COAL, ConstantValue.exactly(8)));
            add(GSBlocks.CHARCOAL_PILE.get(), createSingleItemTableWithSilkTouch(GSBlocks.CHARCOAL_PILE.get(), Items.CHARCOAL, ConstantValue.exactly(8)));
            // Brick piles for firing
            add(GSBlocks.UNFIRED_BRICK_CLAMP.get(), createSingleItemTableWithSilkTouch(GSBlocks.UNFIRED_BRICK_CLAMP.get(), Items.CLAY_BALL, ConstantValue.exactly(4)));
            add(GSBlocks.FIRED_BRICK_CLAMP.get(), createSingleItemTableWithSilkTouch(GSBlocks.FIRED_BRICK_CLAMP.get(), Items.BRICK, ConstantValue.exactly(4)));
            add(GSBlocks.UNFIRED_FIREBRICK_CLAMP.get(), createSingleItemTableWithSilkTouch(GSBlocks.UNFIRED_FIREBRICK_CLAMP.get(), GSItems.FIRECLAY_BALL, ConstantValue.exactly(4)));
            add(GSBlocks.FIRED_FIREBRICK_CLAMP.get(), createSingleItemTableWithSilkTouch(GSBlocks.FIRED_FIREBRICK_CLAMP.get(), GSItems.FIREBRICK, ConstantValue.exactly(4)));
            // Resulting ash
            add(GSBlocks.PYROLYTIC_ASH.get(), LootTable.lootTable()
                .withPool(this.applyExplosionCondition(GSItems.ASH.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.ASH.asItem()))
                    .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))))
                .withPool(this.applyExplosionCondition(Items.CHARCOAL.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.CHARCOAL.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.PYROLYTIC_ASH.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PyrolyticAshBlock.ASH_TYPE, AshType.CHARCOAL))))))
                .withPool(this.applyExplosionCondition(GSItems.COKE.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(5)))
                    .add(LootItem.lootTableItem(GSItems.COKE.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.PYROLYTIC_ASH.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PyrolyticAshBlock.ASH_TYPE, AshType.COKE))))))
            );
            // Compost piles for fertilizer
            add(GSBlocks.COMPOST_PILE.get(), LootTable.lootTable()
                .withPool(this.applyExplosionCondition(GSItems.COMPOST_PILE.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.COMPOST_PILE.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.COMPOST_PILE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CompostBlock.AGE, 0))))))
                .withPool(this.applyExplosionCondition(GSItems.COMPOST_PILE.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.COMPOST_PILE.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.COMPOST_PILE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CompostBlock.AGE, 1))))))
                .withPool(this.applyExplosionCondition(GSItems.COMPOST_PILE.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.COMPOST_PILE.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.COMPOST_PILE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CompostBlock.AGE, 2))))))
                .withPool(this.applyExplosionCondition(GSItems.COMPOST_PILE.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.COMPOST_PILE.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.COMPOST_PILE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CompostBlock.AGE, 3))))))
                .withPool(this.applyExplosionCondition(GSItems.COMPOST.asItem(),  LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.COMPOST.asItem())
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6)))
                    .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.COMPOST_PILE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CompostBlock.AGE, 4))))));
            /*
             * Building blocks, including various 'functional' blocks
             */
            // Fireclay blocks of various types
            add(GSBlocks.FIRECLAY.get(), createSingleItemTableWithSilkTouch(GSBlocks.FIRECLAY.get(), GSItems.FIRECLAY_BALL, ConstantValue.exactly(4)));
            dropSelf(GSBlocks.FIREBRICKS.get());
            dropSelf(GSBlocks.FIREBRICK_STAIRS.get());
            dropSelf(GSBlocks.FIREBRICK_SLAB.get());
            dropSelf(GSBlocks.FIREBRICK_WALL.get());
            for(DeferredBlock<SillBlock> block : GSBlocks.FIREBRICK_SILLS)
                dropSelf(block.get());
            for(DeferredBlock<HorizontalDirectionalBlock> block : GSBlocks.FIREBRICK_QUOINS)
                dropSelf(block.get());
            // Normal brick quoins & specialty blocks
            for(DeferredBlock<SillBlock> block : GSBlocks.BRICK_SILLS)
                dropSelf(block.get());
            for(DeferredBlock<HorizontalDirectionalBlock> block : GSBlocks.BRICK_QUOINS)
                dropSelf(block.get());
            // Framed glass
            dropWhenSilkTouch(GSBlocks.FRAMED_GLASS.get());
            dropWhenSilkTouch(GSBlocks.FRAMED_GLASS_PANE.get());
            for(DeferredBlock<StainedGlassBlock> block : GSBlocks.STAINED_FRAMED_GLASS)
                dropWhenSilkTouch(block.get());
            for(DeferredBlock<StainedGlassPaneBlock> block : GSBlocks.STAINED_FRAMED_GLASS_PANES)
                dropWhenSilkTouch(block.get());
            // Candelabras
            dropSelf(GSBlocks.CANDELABRA.get());
            for(Pair<DyeColor, DeferredBlock<CandelabraBlock>> pair : GSBlocks.CANDELABRAS)
                dropSelf(pair.getSecond().get());
        }
    }

}
