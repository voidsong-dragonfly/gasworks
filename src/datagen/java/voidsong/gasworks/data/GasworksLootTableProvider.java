package voidsong.gasworks.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
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
             * Beehive oven & charcoal heap blocks, incl. fuels & ash
             */
            //Log stacks for fuel
            dropSelf(GSBlocks.OAK_LOG_PILE.get());
            dropSelf(GSBlocks.SPRUCE_LOG_PILE.get());
            dropSelf(GSBlocks.BIRCH_LOG_PILE.get());
            dropSelf(GSBlocks.JUNGLE_LOG_PILE.get());
            dropSelf(GSBlocks.ACACIA_LOG_PILE.get());
            dropSelf(GSBlocks.DARK_OAK_LOG_PILE.get());
            dropSelf(GSBlocks.CHERRY_LOG_PILE.get());
            dropSelf(GSBlocks.MANGROVE_LOG_PILE.get());
            dropSelf(GSBlocks.BAMBOO_LOG_PILE.get());
            //Resulting ash
            add(GSBlocks.PYROLYTIC_ASH.get(), LootTable.lootTable()
                .withPool(this.applyExplosionCondition(GSItems.ASH.asItem(), LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.ASH.asItem()))))
                .withPool(this.applyExplosionCondition(Items.CHARCOAL.asItem(),  LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.CHARCOAL.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.PYROLYTIC_ASH.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PyrolyticAshBlock.ASH_TYPE, AshType.CHARCOAL))))))
                .withPool(this.applyExplosionCondition(GSItems.COKE.asItem(),  LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(GSItems.COKE.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GSBlocks.PYROLYTIC_ASH.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PyrolyticAshBlock.ASH_TYPE, AshType.COKE))))))
            );
        /*
        // Add a table with a silk touch only loot table.
        add(MyBlocks.EXAMPLE_SILK_TOUCHABLE_BLOCK.get(),
            createSilkTouchOnlyTable(MyBlocks.EXAMPLE_SILK_TOUCHABLE_BLOCK.get()));*/
            // other loot table additions here
        }
    }

}
