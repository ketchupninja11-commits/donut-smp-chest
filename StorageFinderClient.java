package com.storagefinder.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.core.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class StorageFinderClient implements ClientModInitializer {
    public static final String MOD_ID = "storagefinder";
    public static final Config CONFIG = Config.load();
    public static final KeyMapping OPEN_GUI = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.storagefinder.open_gui", GLFW.GLFW_KEY_C, KeyMapping.Category.MISC));

    private static final List<BlockPos> STORAGE = new ArrayList<>();
    private static final List<BlockPos> SPAWNERS = new ArrayList<>();
    private static int ticks;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(StorageFinderClient::tick);
        LevelRenderEvents.END_MAIN.register(StorageFinderClient::render);
    }

    private static void tick(Minecraft client) {
        if (OPEN_GUI.consumeClick()) client.setScreen(new StorageFinderScreen());
        if (client.level == null || client.player == null) return;
        if (++ticks >= CONFIG.scanIntervalTicks) {
            ticks = 0;
            scan(client);
        }
    }

    private static void scan(Minecraft client) {
        STORAGE.clear();
        SPAWNERS.clear();
        int r = CONFIG.scanRadius;
        BlockPos origin = client.player.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if (x*x + y*y + z*z > r*r) continue;
                    pos.set(origin.getX()+x, origin.getY()+y, origin.getZ()+z);
                    BlockState state = client.level.getBlockState(pos);
                    if (CONFIG.storageEnabled && isStorage(state)) STORAGE.add(pos.immutable());
                    if (CONFIG.spawnerEnabled && state.is(Blocks.SPAWNER)) SPAWNERS.add(pos.immutable());
                    if (STORAGE.size() + SPAWNERS.size() >= CONFIG.maxHighlights) return;
                }
            }
        }
    }

    private static boolean isStorage(BlockState state) {
        if (CONFIG.chests && (state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST))) return true;
        if (CONFIG.shulkers && isShulker(state)) return true;
        if (CONFIG.droppers && state.is(Blocks.DROPPER)) return true;
        if (CONFIG.barrels && state.is(Blocks.BARREL)) return true;
        if (CONFIG.hoppers && state.is(Blocks.HOPPER)) return true;
        return false;
    }

    private static boolean isShulker(BlockState state) {
        return state.getBlock() instanceof ShulkerBoxBlock;
    }

    private static void render(net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext context) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) return;
        Camera camera = client.gameRenderer.getMainCamera();
        MultiBufferSource buffers = context.bufferSource();
        var pose = context.poseStack();
        var consumer = buffers.getBuffer(RenderTypes.lines());
        var cameraPos = camera.position();
        float width = CONFIG.lineWidth;

        for (BlockPos pos : STORAGE) {
            int color = CONFIG.colorFor(client.level.getBlockState(pos));
            renderBox(pose, consumer, pos, cameraPos.x(), cameraPos.y(), cameraPos.z(), color, width);
        }
        if (CONFIG.spawnerEnabled) {
            for (BlockPos pos : SPAWNERS) {
                renderBox(pose, consumer, pos, cameraPos.x(), cameraPos.y(), cameraPos.z(), CONFIG.spawnerColor, width);
            }
        }
    }

    private static void renderBox(com.mojang.blaze3d.vertex.PoseStack pose,
                                  com.mojang.blaze3d.vertex.VertexConsumer consumer,
                                  BlockPos pos, double cx, double cy, double cz,
                                  int color, float width) {
        double x = pos.getX() - cx;
        double y = pos.getY() - cy;
        double z = pos.getZ() - cz;
        ShapeRenderer.renderShape(pose, consumer,
                net.minecraft.world.phys.shapes.Shapes.block(), x, y, z, color, width);
    }

    public static List<BlockPos> storagePositions() { return STORAGE; }
    public static List<BlockPos> spawnerPositions() { return SPAWNERS; }
}
