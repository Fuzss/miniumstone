package fuzs.miniumstone.mixin;

import fuzs.miniumstone.world.item.SpecialRecipePickerItem;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StackedContents.class)
abstract class StackedContentsMixin {

    @Inject(method = "accountSimpleStack", at = @At("HEAD"), cancellable = true)
    public void accountSimpleStack(ItemStack stack, CallbackInfo callback) {
        // skip checks related to item damage / enchantments / display name when trying to find items that can be moved to the recipe grid
        if (stack.getItem() instanceof SpecialRecipePickerItem item && item.ignoreTagWhenMoving()) {
            this.accountStack(stack);
            callback.cancel();
        }
    }

    @Shadow
    public abstract void accountStack(ItemStack stack);

    @Inject(method = "accountStack(Lnet/minecraft/world/item/ItemStack;I)V", at = @At("HEAD"), cancellable = true)
    public void accountStack(ItemStack stack, int amount, CallbackInfo callback) {
        // pretend an item that can be reused during crafting is available at max amount (usually 64 times)
        if (!stack.isEmpty() && stack.getItem() instanceof SpecialRecipePickerItem item && item.supportsMultipleCraftingOperations()) {
            int stackingIndex = getStackingIndex(stack);
            this.put(stackingIndex, amount);
            callback.cancel();
        }
    }

    @Shadow
    private static int getStackingIndex(ItemStack stack) {
        throw new RuntimeException();
    }

    @Shadow
    abstract void put(int stackingIndex, int increment);
}
