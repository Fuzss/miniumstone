package fuzs.miniumstone.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlaceRecipe.class)
abstract class ServerPlaceRecipeMixin {

    @Inject(method = "moveItemToGrid", at = @At("HEAD"), cancellable = true)
    private void moveItemToGrid(Slot slot, Holder<Item> item, int count, CallbackInfoReturnable<Integer> callback) {
        if (slot.getItem().is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG)) {
            // if the item is reused during crafting don't try to move a new item to the slot if one already exists in there
            callback.setReturnValue(-1);
        }
    }

    @ModifyVariable(
            method = "placeRecipe(Lnet/minecraft/world/item/crafting/RecipeHolder;Lnet/minecraft/world/entity/player/StackedItemContents;)V",
            at = @At("STORE")
    )
    protected ItemStack placeRecipe(ItemStack itemStack) {
        // this checks if any item in the crafting grid is already at max stack size, the check will be skipped for the current item by returning an empty item
        return itemStack.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG) ? ItemStack.EMPTY : itemStack;
    }

    @ModifyExpressionValue(
            method = "clampToMaxStackSize",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getDefaultMaxStackSize()I")
    )
    private static int clampToMaxStackSize(int maxStackSize, @Local Holder<Item> holder) {
        // finds the max stack size for all items part of the recipe, since our item is reused it should not be limited by max stack size
        return holder.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG) ? Integer.MAX_VALUE : maxStackSize;
    }

    @ModifyVariable(method = "calculateAmountToCraft", at = @At("STORE"), ordinal = 0)
    protected ItemStack calculateAmountToCraft(ItemStack itemStack) {
        // this method finds the smallest stack in the crafting slots so that afterward the recipe can be placed one more time than the smallest stack size
        // since our item is reused for every crafting operation it should be ignored by returning an empty stack
        return itemStack.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG) ? ItemStack.EMPTY : itemStack;
    }
}
