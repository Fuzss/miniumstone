package fuzs.miniumstone.mixin;

import fuzs.miniumstone.world.item.crafting.PlayerRecipeInput;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ResultSlot.class)
abstract class ResultSlotMixin extends Slot {

    public ResultSlotMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @ModifyVariable(method = "onTake", at = @At("STORE"))
    public CraftingInput onTake(CraftingInput craftingInput, Player player, ItemStack itemStack) {
        ((PlayerRecipeInput) craftingInput).miniumstone$setPlayer(player);
        return craftingInput;
    }
}
