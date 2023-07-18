package fuzs.miniumstone.world.item;

/**
 * Add small interface for improving the recipe picker move behavior for items that may contain data in their tag and / or are not used up during crafting.
 */
public interface SpecialRecipePickerItem {

    /**
     * @return <code>true</code> when this item should be moved by the recipe picker regardless of item tag / damage / enchantments / display name
     */
    default boolean ignoreTagWhenMoving() {
        return false;
    }

    /**
     * @return <code>true</code> if this item is not used during a crafting operation and remains in the crafting interface (maybe it takes durability damage instead)
     */
    default boolean supportsMultipleCraftingOperations() {
        return false;
    }
}
