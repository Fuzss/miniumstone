package fuzs.miniumstone.client.renderer;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import fuzs.miniumstone.MiniumStone;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public abstract class ModRenderType extends RenderType {
    /**
     * Disable depth test for rendering through blocks.
     *
     * @see RenderPipelines#LINES
     */
    public static final RenderPipeline LINES_SEE_THROUGH_RENDER_PIPELINE = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(MiniumStone.id("pipeline/lines_see_through"))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();
    /**
     * @see RenderType#LINES
     */
    public static final RenderType LINES_SEE_THROUGH = create(MiniumStone.id("lines_see_through").toString(),
            1536,
            LINES_SEE_THROUGH_RENDER_PIPELINE,
            RenderType.CompositeState.builder()
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .createCompositeState(false));

    private ModRenderType(String string, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, i, bl, bl2, runnable, runnable2);
    }
}
