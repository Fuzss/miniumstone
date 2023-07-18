package fuzs.miniumstone.client.shape;

import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class ShapelessShape {
    // all blocks in 3x3x3 cube around the block
    private static final List<BlockPos> NEIGHBOR_POSITIONS = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
            .filter(Predicate.not(BlockPos.ZERO::equals))
            .map(BlockPos::immutable)
            .toList();
    // all blocks in 5x5 square around block's Y-level, plus blocks directly above & below
    private static final List<BlockPos> NEIGHBOR_POSITIONS_PLANT = BlockPos.betweenClosedStream(-2, -1, -2, 2, 1, 2)
            .filter(t -> !BlockPos.ZERO.equals(t) && (t.getY() == 0 || t.distManhattan(BlockPos.ZERO) == 0))
            .map(BlockPos::immutable)
            .toList();

    public List<BlockPos> getBlocks(ShapeContext context) {
        HashSet<BlockPos> known = new HashSet<>();
//        walk(context, known, context.matcher() == BlockMatcher.CROP_LIKE);

        List<BlockPos> list = new ArrayList<>(known);
        list.sort(Comparator.comparingInt(t -> t.distManhattan(context.pos())));

        if (list.size() > context.maxBlocks()) {
            list.subList(context.maxBlocks(), list.size()).clear();
        }

        return list;
    }

    private void walk(ShapeContext context, HashSet<BlockPos> known, boolean cropLike) {
        Set<BlockPos> traversed = new HashSet<>();
        Deque<BlockPos> openSet = new ArrayDeque<>();
        openSet.add(context.pos());
        traversed.add(context.pos());

        while (!openSet.isEmpty()) {
            BlockPos ptr = openSet.pop();

            if (context.check(ptr) && known.add(ptr)) {
                if (known.size() >= context.maxBlocks()) {
                    return;
                }

                for (BlockPos side : cropLike ? NEIGHBOR_POSITIONS_PLANT : NEIGHBOR_POSITIONS) {
                    BlockPos offset = ptr.offset(side);

                    if (traversed.add(offset)) {
                        openSet.add(offset);
                    }
                }
            }
        }
    }
}
