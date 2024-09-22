package fuzs.miniumstone.mixin;

import fuzs.miniumstone.world.item.SpecialRecipePickerItem;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlaceRecipe.class)
abstract class ServerPlaceRecipeMixin<C extends Container> {
    @Shadow
    protected Inventory inventory;
    @Unique
    private int miniumstone$stackingIndex;

    @ModifyVariable(method = "moveItemToGrid", at = @At("STORE"), ordinal = 0)
    protected int moveItemToGrid(int index, Slot slot, ItemStack stack) {
        if (slot.getItem().getItem() instanceof SpecialRecipePickerItem item && item.supportsMultipleCraftingOperations()) {
            // if the item is reused during crafting don't try to move a new item to the slot if one already exists in there
            return -1;
        } else if (stack.getItem() instanceof SpecialRecipePickerItem item && item.ignoreTagWhenMoving()) {
            // a simplified version of Inventory::findSlotMatchingUnusedItem without all the checks for item tag / damage / enchantments / display name
            for (int i = 0; i < this.inventory.items.size(); ++i) {
                ItemStack itemStack = this.inventory.items.get(i);
                if (!itemStack.isEmpty() && stack.is(itemStack.getItem())) {
                    return i;
                }
            }
            return -1;
        } else {
            return index;
        }
    }

    @ModifyVariable(method = "handleRecipeClicked", at = @At("STORE"), ordinal = 0)
    protected ItemStack handleRecipeClicked$1(ItemStack itemStack, RecipeHolder<? extends Recipe<C>> recipe, boolean placeAll) {
        // this checks if any item in the crafting grid is already at max stack size, the check will be skipped for the current item by returning an empty item
        return itemStack.getItem() instanceof SpecialRecipePickerItem item && item.supportsMultipleCraftingOperations() ? ItemStack.EMPTY : itemStack;
    }

//    @Redirect(method = "handleRecipeClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I", ordinal = 1))
//    protected int handleRecipeClicked$2(ItemStack itemStack) {
//        return itemStack.getItem() instanceof SpecialRecipePickerItem item && item.supportsMultipleCraftingOperations() ? 64 : itemStack.getMaxStackSize();
//    }

    @ModifyVariable(method = "handleRecipeClicked", at = @At("LOAD"), ordinal = 3)
    protected int handleRecipeClicked$2(int stackingIndex) {
        return this.miniumstone$stackingIndex = stackingIndex;
    }

    @ModifyVariable(method = "handleRecipeClicked", at = @At("STORE"), ordinal = 4)
    protected int handleRecipeClicked$3(int maxStackSize) {
        // finds the max stack size for all items part of the recipe, since our item is reused it should not be limited by max stack size
        ItemStack itemStack = StackedContents.fromStackingIndex(this.miniumstone$stackingIndex);
        return itemStack.getItem() instanceof SpecialRecipePickerItem item && item.supportsMultipleCraftingOperations() ? 64 : maxStackSize;
    }

    @ModifyVariable(method = "getStackSize", at = @At("STORE"), ordinal = 0)
    protected ItemStack getStackSize(ItemStack itemStack, boolean placeAll, int maxPossible, boolean recipeMatches) {
        // this method finds the smallest stack in the crafting slots so that afterward the recipe can be placed one more time than the smallest stack size
        // since our item is reused for every crafting operation it should be ignored by returning an empty stack
        return itemStack.getItem() instanceof SpecialRecipePickerItem item && item.supportsMultipleCraftingOperations() ? ItemStack.EMPTY : itemStack;
    }
}
