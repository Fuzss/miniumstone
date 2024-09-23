package fuzs.miniumstone.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlaceRecipe.class)
abstract class ServerPlaceRecipeMixin<I extends RecipeInput, R extends Recipe<I>> {
    @Shadow
    protected Inventory inventory;

    @ModifyVariable(method = "moveItemToGrid", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
    protected int moveItemToGrid(int index, Slot slot, ItemStack itemStack) {
        if (slot.getItem().is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG)) {
            // if the item is reused during crafting don't try to move a new item to the slot if one already exists in there
            return -1;
        } else if (itemStack.is(ModRegistry.RECIPES_IGNORE_COMPONENTS_ITEM_TAG)) {
            // a simplified version of Inventory::findSlotMatchingUnusedItem without all the checks for item tag / damage / enchantments / display name
            for (int i = 0; i < this.inventory.items.size(); ++i) {
                ItemStack inventoryStack = this.inventory.items.get(i);
                if (!inventoryStack.isEmpty() && itemStack.is(inventoryStack.getItem())) {
                    return i;
                }
            }
            return -1;
        } else {
            return index;
        }
    }

    @ModifyVariable(method = "handleRecipeClicked", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    protected ItemStack handleRecipeClicked$1(ItemStack itemStack, RecipeHolder<R> recipe, boolean placeAll) {
        // this checks if any item in the crafting grid is already at max stack size, the check will be skipped for the current item by returning an empty item
        return itemStack.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG) ? ItemStack.EMPTY : itemStack;
    }

    @WrapOperation(
            method = "handleRecipeClicked",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I", ordinal = 1)
    )
    protected int handleRecipeClicked$3(ItemStack itemStack, Operation<Integer> operation) {
        // finds the max stack size for all items part of the recipe, since our item is reused it should not be limited by max stack size
        return itemStack.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG) ? Integer.MAX_VALUE : operation.call(
                itemStack);
    }

    @ModifyVariable(method = "getStackSize", at = @At("STORE"), ordinal = 0)
    protected ItemStack getStackSize(ItemStack itemStack, boolean placeAll, int maxPossible, boolean recipeMatches) {
        // this method finds the smallest stack in the crafting slots so that afterward the recipe can be placed one more time than the smallest stack size
        // since our item is reused for every crafting operation it should be ignored by returning an empty stack
        return itemStack.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG) ? ItemStack.EMPTY : itemStack;
    }
}
