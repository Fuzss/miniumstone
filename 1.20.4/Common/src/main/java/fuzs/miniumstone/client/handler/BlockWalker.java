package fuzs.miniumstone.client.handler;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BlockWalker {
    private static final List<BlockPos> NEIGHBOR_POSITIONS_CUBE = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1).filter(Predicate.not(BlockPos.ZERO::equals)).map(BlockPos::immutable).toList();
    private static final Function<Direction, List<BlockPos>> NEIGHBOR_POSITIONS_FLAT = Util.memoize(direction -> BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1).filter(t -> !t.equals(BlockPos.ZERO) && direction.getAxis().choose(t.getX(), t.getY(), t.getZ()) == 0).map(BlockPos::immutable).toList());
    private static final Function<Direction, List<BlockPos>> NEIGHBOR_POSITIONS_LINE = Util.memoize(direction -> List.of(BlockPos.ZERO.relative(direction.getOpposite())));
    private static final List<BlockPos> NEIGHBOR_POSITIONS_BUSH = BlockPos.betweenClosedStream(-2, -1, -2, 2, 1, 2).filter(t -> !BlockPos.ZERO.equals(t) && (t.getY() == 0 || t.distManhattan(BlockPos.ZERO) == 1)).map(BlockPos::immutable).toList();

    private final MiniumStoneItem.SelectionMode selectionMode;
    private final BlockPos blockPos;
    private final Direction blockDirection;
    private final BlockState blockState;
    private final List<BlockPos> neighborPositions;
    private final int maxDepth;
    private int blockTicks;
    @Nullable
    private RecipeHolder<TransmutationInWorldRecipe> recipe;
    @Nullable
    private RecipeHolder<TransmutationInWorldRecipe> reversedRecipe;
    @Nullable
    private List<BlockPos> blocks;
    @Nullable
    private VoxelShape voxelShape;

    private BlockWalker(int maxDepth, MiniumStoneItem.SelectionMode selectionMode, BlockPos blockPos, Direction blockDirection, BlockState blockState) {
        this.maxDepth = selectionMode == MiniumStoneItem.SelectionMode.LINE ? maxDepth * 2 : maxDepth;
        this.selectionMode = selectionMode;
        this.blockPos = blockPos;
        this.blockDirection = blockDirection;
        this.blockState = blockState;
        this.neighborPositions = blockState.getBlock() instanceof BushBlock ? NEIGHBOR_POSITIONS_BUSH : switch (selectionMode) {
            case CUBE -> NEIGHBOR_POSITIONS_CUBE;
            case FLAT -> NEIGHBOR_POSITIONS_FLAT.apply(blockDirection);
            case LINE -> NEIGHBOR_POSITIONS_LINE.apply(blockDirection);
        };
    }

    public static BlockWalker fromHitResult(int maxDepth, MiniumStoneItem.SelectionMode selectionMode, BlockHitResult hitResult, BlockGetter blockGetter) {
        return new BlockWalker(maxDepth, selectionMode, hitResult.getBlockPos(), hitResult.getDirection(), blockGetter.getBlockState(hitResult.getBlockPos()));
    }

    public boolean stillValid(int maxDepth, @Nullable MiniumStoneItem.SelectionMode selectionMode, @Nullable HitResult hitResult, @Nullable BlockGetter blockGetter) {
        if ((maxDepth == -1 || maxDepth == this.maxDepth) && (selectionMode == null || selectionMode == this.selectionMode)) {
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && blockGetter != null) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                return this.blockPos.equals(blockHitResult.getBlockPos()) && this.blockDirection.equals(blockHitResult.getDirection()) && this.blockState.equals(blockGetter.getBlockState(blockHitResult.getBlockPos()));
            }
        }
        return false;
    }

    public void testAllBlocks(@Nullable HitResult hitResult, BlockGetter blockGetter) {
        if (this.stillValid(-1, null, hitResult, blockGetter)) {
            if (this.blocks != null && --this.blockTicks <= 0) {
                this.blockTicks = 20;
                if (this.blocks.stream().anyMatch(t -> !this.isSame(blockGetter, t))) {
                    this.recipe = null;
                    this.reversedRecipe = null;
                    this.blocks = null;
                    this.voxelShape = null;
                }
            }
        }
    }

    public VoxelShape getVoxelShape(Level level, boolean reverse) {
        if (this.voxelShape == null) {
            return this.voxelShape = this.createVoxelShape(level, reverse);
        }
        return this.voxelShape;
    }

    private VoxelShape createVoxelShape(Level level, boolean reverse) {
        VoxelShape voxelShape = Shapes.empty();
        for (BlockPos pos : this.getBlocks(level, reverse)) {
            voxelShape = Shapes.joinUnoptimized(voxelShape, Shapes.create(new AABB(pos).inflate(0.005)), BooleanOp.OR);
        }
        return voxelShape;
    }

    public List<BlockPos> getBlocks(Level level, boolean reverse) {
        if (this.blocks == null) {
            if (this.findRecipes(level.getRecipeManager())) {
                this.blocks = this.findBlocks(level);
            } else {
                this.blocks = List.of();
            }
        }
        return reverse && this.reversedRecipe != null || !reverse && this.recipe != null ? this.blocks : List.of();
    }

    private boolean findRecipes(RecipeManager recipeManager) {
        AtomicBoolean result = new AtomicBoolean();
        List<RecipeHolder<TransmutationInWorldRecipe>> recipes = recipeManager.getAllRecipesFor(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value());
        recipes.stream().filter(recipe -> recipe.value().getBlockIngredient() == this.blockState.getBlock()).findFirst().ifPresent(recipe -> {
            this.recipe = recipe;
            result.set(true);
        });
        recipes.stream().filter(recipe -> recipe.value().isReversible() && recipe.value().getBlockResult() == this.blockState.getBlock()).findFirst().ifPresent(recipe -> {
            this.reversedRecipe = recipe;
            result.set(true);
        });
        return result.get();
    }

    private List<BlockPos> findBlocks(BlockGetter blockGetter) {

        List<BlockPos> blocks = Lists.newArrayList();
        BlockPos.breadthFirstTraversal(this.blockPos, this.maxDepth, 1024, (BlockPos pos, Consumer<BlockPos> blockPosConsumer) -> {
            for (BlockPos side : this.neighborPositions) {
                blockPosConsumer.accept(pos.offset(side));
            }
        }, (BlockPos pos) -> {
            if (this.isSame(blockGetter, pos)) blocks.add(pos);
            return true;
        });

        return blocks;
    }

    private boolean isSame(BlockGetter blockGetter, BlockPos pos) {
        return this.blockState.is(blockGetter.getBlockState(pos).getBlock());
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    @Nullable
    public Block getResult(boolean reverse) {
        return reverse && this.reversedRecipe != null ? this.reversedRecipe.value().getBlockIngredient() : !reverse && this.recipe != null ? this.recipe.value().getBlockResult() : null;
    }

    @Nullable
    public RecipeHolder<TransmutationInWorldRecipe> getRecipe(boolean reverse) {
        return reverse ? this.reversedRecipe : this.recipe;
    }
}
