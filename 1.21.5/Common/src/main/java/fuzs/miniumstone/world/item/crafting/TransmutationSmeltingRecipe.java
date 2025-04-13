package fuzs.miniumstone.world.item.crafting;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.puzzleslib.impl.core.proxy.ProxyImpl;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class TransmutationSmeltingRecipe extends CustomRecipe {
    private final Ingredient miniumStone = Ingredient.of(ModRegistry.MINIUM_STONE_ITEM.value());

    public TransmutationSmeltingRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        if (!hasExactlyOne(container, this.miniumStone)) return false;
        if (!hasExactlyOne(container, getFuelIngredient(level.registryAccess()))) return false;
        ItemStack itemStack = this.findAllMatchingItem(container, level.registryAccess());
        if (itemStack == ItemStack.EMPTY) {
            return false;
        } else {
            SingleRecipeInput recipeContainer = new SingleRecipeInput(itemStack);
            return ((ServerLevel) level).recipeAccess()
                    .getRecipeFor(RecipeType.SMELTING, recipeContainer, level)
                    .map(RecipeHolder::value)
                    .filter(recipe -> recipe.matches(recipeContainer, level))
                    .isPresent();
        }
    }

    static Ingredient getFuelIngredient(HolderGetter.Provider registries) {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        return Ingredient.of(items.getOrThrow(ItemTags.COALS));
    }

    private static boolean hasExactlyOne(CraftingInput container, Ingredient ingredient) {
        boolean hasIngredient = false;
        for (int i = 0; i < container.size(); i++) {
            if (ingredient.test(container.getItem(i))) {
                if (hasIngredient) {
                    return false;
                } else {
                    hasIngredient = true;
                }
            }
        }
        return hasIngredient;
    }

    private ItemStack findAllMatchingItem(CraftingInput container, HolderGetter.Provider registries) {
        ItemStack toSmelt = ItemStack.EMPTY;
        for (int i = 0; i < container.size(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty() && !this.miniumStone.test(itemStack) &&
                    !getFuelIngredient(registries).test(itemStack)) {
                if (toSmelt == ItemStack.EMPTY) {
                    toSmelt = itemStack.copyWithCount(1);
                } else if (ItemStack.isSameItem(toSmelt, itemStack)) {
                    toSmelt.grow(1);
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        return toSmelt;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider registryAccess) {
        ItemStack itemStack = this.findAllMatchingItem(container, registryAccess);
        if (itemStack != ItemStack.EMPTY) {
            SingleRecipeInput recipeContainer = new SingleRecipeInput(itemStack);
            MinecraftServer minecraftServer = ProxyImpl.get().getMinecraftServer();
            Level level = minecraftServer.getAllLevels().iterator().next();
            ItemStack resultStack = minecraftServer.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeContainer, level)
                    .map(RecipeHolder::value)
                    .map(recipe -> recipe.assemble(recipeContainer, registryAccess))
                    .orElse(ItemStack.EMPTY);
            if (!resultStack.isEmpty()) resultStack.setCount(itemStack.getCount());
            return resultStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        return MiniumStoneHelper.damageMiniumStoneClearRest(container);
    }

    @Override
    public RecipeSerializer<? extends TransmutationSmeltingRecipe> getSerializer() {
        return ModRegistry.TRANSMUTATION_SMELTING_RECIPE_SERIALIZER.value();
    }
}
