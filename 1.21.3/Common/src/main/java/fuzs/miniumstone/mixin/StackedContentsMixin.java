package fuzs.miniumstone.mixin;

import fuzs.miniumstone.init.ModRegistry;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StackedContents.class)
abstract class StackedContentsMixin<T> {
    @Shadow
    @Final
    public Reference2IntOpenHashMap<T> amounts;

    @Inject(method = "put", at = @At("HEAD"), cancellable = true)
    void put(T item, int amount, CallbackInfo callback) {
        // pretend an item that can be reused during crafting is available at max amount (usually 64 times)
        if (item instanceof Holder<?> holder &&
                ((Holder<Item>) holder).is(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG)) {
            this.amounts.put(item, Integer.MAX_VALUE);
            callback.cancel();
        }
    }
}
