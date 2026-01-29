package dev.swench.watersource.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void drawBox(MatrixStack matrixStack, BlockPos pos, int color, boolean fill, int alphaPercent) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) alphaPercent / 100.0F;

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.disableDepthTest();

        Box box = new Box(pos);
        
        if (fill) {
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            drawFilledBox(matrixStack, bufferBuilder, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
            tessellator.draw();
        }

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        drawBoxOutlineAsQuads(matrix, bufferBuilder, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        tessellator.draw();
        
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static void drawBoxOutlineAsQuads(Matrix4f matrix, BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        double lineWidth = 0.008;
        
        drawLineAsQuad(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, lineWidth, r, g, b, a);
        
        drawLineAsQuad(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, lineWidth, r, g, b, a);
        
        drawLineAsQuad(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, lineWidth, r, g, b, a);
        drawLineAsQuad(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, lineWidth, r, g, b, a);
    }
    
    private static void drawLineAsQuad(BufferBuilder buffer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, double width, float r, float g, float b, float a) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        if (length == 0) return;
        
        dx /= length;
        dy /= length;
        dz /= length;
        
        double px, py, pz;
        if (Math.abs(dy) > 0.9) {
            px = 1; py = 0; pz = 0;
        } else {
            px = 0; py = 1; pz = 0;
        }
        
        double cx = dy * pz - dz * py;
        double cy = dz * px - dx * pz;
        double cz = dx * py - dy * px;
        double clen = Math.sqrt(cx * cx + cy * cy + cz * cz);
        cx = (cx / clen) * width;
        cy = (cy / clen) * width;
        cz = (cz / clen) * width;
        
        buffer.vertex(matrix, (float)(x1 - cx), (float)(y1 - cy), (float)(z1 - cz)).color(r, g, b, a).next();
        buffer.vertex(matrix, (float)(x1 + cx), (float)(y1 + cy), (float)(z1 + cz)).color(r, g, b, a).next();
        buffer.vertex(matrix, (float)(x2 + cx), (float)(y2 + cy), (float)(z2 + cz)).color(r, g, b, a).next();
        buffer.vertex(matrix, (float)(x2 - cx), (float)(y2 - cy), (float)(z2 - cz)).color(r, g, b, a).next();
    }

    private static void drawFilledBox(MatrixStack matrixStack, BufferBuilder bufferBuilder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(r, g, b, a).next();
    }
}