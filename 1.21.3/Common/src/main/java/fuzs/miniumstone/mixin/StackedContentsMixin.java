package fuzs.miniumstone.mixin;

import fuzs.miniumstone.init.ModRegistry;
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
    public void accountSimpleStack(ItemStack itemStack, CallbackInfo callback) {
        // skip checks related to item components, such as damage, enchantments, and display name
        // when trying to find items that can be moved to the recipe grid
        if (itemStack.is(ModRegistry.RECIPES_IGNORE_COMPONENTS_ITEM_TAG)) {
            this.accountStack(itemStack);
            callback.cancel();
        }
    }

    @Shadow
    public abstract void accountStack(ItemStack stack);

    @Inject(method = "accountStack(Lnet/minecraft/world/item/ItemStack;I)V", at = @At("HEAD"), cancellable = true)
    public void accountStack(ItemStack itemStack, int amount, CallbackInfo callback) {
        // pretend an item that can be reused during crafting is available at max amount (usually 64 times)
        if (itemStack.is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG)) {
            int stackingIndex = getStackingIndex(itemStack);
            this.put(stackingIndex, Integer.MAX_VALUE);
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
