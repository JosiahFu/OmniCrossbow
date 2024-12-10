package archives.tater.omnicrossbow;

import archives.tater.omnicrossbow.entity.BeaconLaserEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static archives.tater.omnicrossbow.entity.BeaconLaserEntity.MAX_FIRING_TICKS;

public class BeaconLaserEntityRenderer extends EntityRenderer<BeaconLaserEntity> {
    public static final Identifier BEAM_TEXTURE = BeaconBlockEntityRenderer.BEAM_TEXTURE;
    public static final int TRANSITION = 6;

    protected BeaconLaserEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    protected int getBlockLight(BeaconLaserEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(BeaconLaserEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.getFiringTicks() <= 0) return;
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90));
        var firingTime = entity.getFiringTicks() - 1 + tickDelta;
        var beamWidthScale = trapezoidalTransition(firingTime) * (1 + 0.1f * MathHelper.sin(0.5f * firingTime));
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.floorMod(5 * firingTime, 90)));
        matrices.translate(-0.5f, 0, -0.5f); // Beacon expects to start at corner
        BeaconBlockEntityRenderer.renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0f, entity.getWorld().getTime(), 0, (int) entity.getDistance(), new float[]{1f, 1f, 1f}, 0.2f * beamWidthScale, 0.25f * beamWidthScale);
        matrices.pop();
    }

    public float trapezoidalTransition(float progress) {
        if (progress <= TRANSITION) {
            return progress / TRANSITION;
        } if (progress >= MAX_FIRING_TICKS - TRANSITION) {
            return (MAX_FIRING_TICKS - progress) / TRANSITION;
        } else {
            return 1;
        }
    }

    @Override
    public Identifier getTexture(BeaconLaserEntity entity) {
        //noinspection deprecation
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
