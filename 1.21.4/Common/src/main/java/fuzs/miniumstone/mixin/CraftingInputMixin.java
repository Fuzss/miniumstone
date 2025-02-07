package fuzs.miniumstone.mixin;

import fuzs.miniumstone.world.item.crafting.PlayerRecipeInput;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.CraftingInput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CraftingInput.class)
abstract class CraftingInputMixin implements PlayerRecipeInput {
    @Nullable
    @Unique
    private Player miniumstone$player;

    @Override
    public void miniumstone$setPlayer(Player player) {
        this.miniumstone$player = player;
    }

    @Nullable
    @Override
    public Player miniumstone$getPlayer() {
        return this.miniumstone$player;
    }
}
