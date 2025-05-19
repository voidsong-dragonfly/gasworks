package voidsong.gasworks.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import voidsong.gasworks.Gasworks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Gasworks.MOD_ID)
public class CachedRecipeList<R extends Recipe<?>> {
    public static final int INVALID_RELOAD_COUNT = -1;
    private static int reloadCount = 0;

    private final Supplier<RecipeType<R>> type;
    private Map<ResourceLocation, RecipeHolder<R>> recipes;
    private List<RecipeHolder<R>> recipeHolders;
    private boolean cachedDataIsClient;
    private int cachedAtReloadCount = INVALID_RELOAD_COUNT;

    public CachedRecipeList(Supplier<RecipeType<R>> type) {
        this.type = type;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onTagsUpdated(TagsUpdatedEvent ev) {
        ++reloadCount;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRecipeUpdatedClient(RecipesUpdatedEvent ev) {
        ++reloadCount;
    }

    public List<RecipeHolder<R>> getRecipes(@Nonnull Level level) {
        updateCache(level.getRecipeManager(), level.isClientSide());
        return Objects.requireNonNull(recipeHolders);
    }

    public R getById(@Nonnull Level level, ResourceLocation name) {
        var holder = holderById(level, name);
        return holder!=null?holder.value(): null;
    }

    public RecipeHolder<R> holderById(@Nonnull Level level, ResourceLocation name) {
        updateCache(level.getRecipeManager(), level.isClientSide());
        return recipes.get(name);
    }

    private void updateCache(RecipeManager manager, boolean isClient) {
        if(recipes!=null&&cachedAtReloadCount==reloadCount&&(!cachedDataIsClient||isClient))
            return;
        this.recipes = manager.getRecipes().stream()
            .filter(iRecipe -> iRecipe.value().getType()==type.get())
            .collect(Collectors.toMap(RecipeHolder::id, rh -> (RecipeHolder<R>)rh));
        this.recipeHolders = List.copyOf(this.recipes.values());
        this.cachedDataIsClient = isClient;
        this.cachedAtReloadCount = reloadCount;
    }
}
