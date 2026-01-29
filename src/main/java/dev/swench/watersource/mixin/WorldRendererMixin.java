package dev.swench.watersource.mixin;

import dev.swench.watersource.Config;
import dev.swench.watersource.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (!Config.waterEnabled && !Config.lavaEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        Vec3d cameraPos = camera.getPos();
        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        BlockPos playerPos = client.player.getBlockPos();
        int maxRadius = Math.max(Config.waterRadius, Config.lavaRadius);

        for (int x = -maxRadius; x <= maxRadius; x++) {
            for (int y = -maxRadius; y <= maxRadius; y++) {
                for (int z = -maxRadius; z <= maxRadius; z++) {
                    int distSq = x*x + y*y + z*z;
                    BlockPos pos = playerPos.add(x, y, z);
                    FluidState fluidState = client.world.getFluidState(pos);

                    if (fluidState.isEmpty()) continue;

                    if (Config.waterEnabled && distSq <= Config.waterRadius * Config.waterRadius) {
                        if (fluidState.getFluid() == Fluids.WATER || fluidState.getFluid() == Fluids.FLOWING_WATER) {
                            if (fluidState.isStill() && fluidState.getLevel() == 8) {
                                RenderUtils.drawBox(matrices, pos, Config.waterColor.getValue(), Config.waterFill, 100 - Config.waterAlpha);
                            }
                        }
                    }

                    if (Config.lavaEnabled && distSq <= Config.lavaRadius * Config.lavaRadius) {
                        if (fluidState.getFluid() == Fluids.LAVA || fluidState.getFluid() == Fluids.FLOWING_LAVA) {
                            if (fluidState.isStill() && fluidState.getLevel() == 8) {
                                RenderUtils.drawBox(matrices, pos, Config.lavaColor.getValue(), Config.lavaFill, 100 - Config.lavaAlpha);
                            }
                        }
                    }
                }
            }
        }

        matrices.pop();
    }
}
