package com.storagefinder.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class StorageFinderScreen extends Screen {
    private int left;
    private int top;

    public StorageFinderScreen() {
        super(Component.literal("StorageFinder Settings"));
    }

    @Override
    protected void init() {
        left = Math.max(20, (width - 720) / 2);
        top = Math.max(18, (height - 420) / 2);
        int x1 = left + 25;
        int x2 = left + 370;
        int y = top + 72;

        addRenderableWidget(button(x1, y, 315, "StorageFinder: " + on(CONFIG.storageEnabled), b -> { CONFIG.storageEnabled = !CONFIG.storageEnabled; b.setMessage(label("StorageFinder: ", CONFIG.storageEnabled)); CONFIG.save(); }));
        addRenderableWidget(button(x2, y, 315, "SpawnerFinder: " + on(CONFIG.spawnerEnabled), b -> { CONFIG.spawnerEnabled = !CONFIG.spawnerEnabled; b.setMessage(label("SpawnerFinder: ", CONFIG.spawnerEnabled)); CONFIG.save(); }));
        y += 38;
        addRenderableWidget(button(x1, y, 150, "Chests: " + on(CONFIG.chests), b -> { CONFIG.chests=!CONFIG.chests; b.setMessage(label("Chests: ", CONFIG.chests)); CONFIG.save(); }));
        addRenderableWidget(button(x1+165, y, 150, "Shulkers: " + on(CONFIG.shulkers), b -> { CONFIG.shulkers=!CONFIG.shulkers; b.setMessage(label("Shulkers: ", CONFIG.shulkers)); CONFIG.save(); }));
        addRenderableWidget(button(x2, y, 150, "Droppers: " + on(CONFIG.droppers), b -> { CONFIG.droppers=!CONFIG.droppers; b.setMessage(label("Droppers: ", CONFIG.droppers)); CONFIG.save(); }));
        addRenderableWidget(button(x2+165, y, 150, "Barrels: " + on(CONFIG.barrels), b -> { CONFIG.barrels=!CONFIG.barrels; b.setMessage(label("Barrels: ", CONFIG.barrels)); CONFIG.save(); }));
        y += 48;
        addRenderableWidget(button(x1, y, 315, "Scan radius: " + CONFIG.scanRadius, b -> { CONFIG.scanRadius = next(CONFIG.scanRadius, 16, 128, 16); b.setMessage(Component.literal("Scan radius: " + CONFIG.scanRadius)); CONFIG.save(); }));
        addRenderableWidget(button(x2, y, 315, "Scan interval: " + CONFIG.scanIntervalTicks + " ticks", b -> { CONFIG.scanIntervalTicks = next(CONFIG.scanIntervalTicks, 2, 40, 2); b.setMessage(Component.literal("Scan interval: " + CONFIG.scanIntervalTicks + " ticks")); CONFIG.save(); }));
        y += 38;
        addRenderableWidget(button(x1, y, 315, "Line width: " + String.format("%.1f", CONFIG.lineWidth), b -> { CONFIG.lineWidth = CONFIG.lineWidth >= 5 ? 1 : CONFIG.lineWidth + .5f; b.setMessage(Component.literal("Line width: " + String.format("%.1f", CONFIG.lineWidth))); CONFIG.save(); }));
        addRenderableWidget(button(x2, y, 315, "Max highlights: " + CONFIG.maxHighlights, b -> { CONFIG.maxHighlights = next(CONFIG.maxHighlights, 1000, 10000, 1000); b.setMessage(Component.literal("Max highlights: " + CONFIG.maxHighlights)); CONFIG.save(); }));
        y += 38;
        addRenderableWidget(button(x1, y, 315, "Chest color: " + hex(CONFIG.chestColor), b -> { CONFIG.chestColor = cycleColor(CONFIG.chestColor); b.setMessage(Component.literal("Chest color: " + hex(CONFIG.chestColor))); CONFIG.save(); }));
        addRenderableWidget(button(x2, y, 315, "Shulker color: " + hex(CONFIG.shulkerColor), b -> { CONFIG.shulkerColor = cycleColor(CONFIG.shulkerColor); b.setMessage(Component.literal("Shulker color: " + hex(CONFIG.shulkerColor))); CONFIG.save(); }));
        y += 38;
        addRenderableWidget(button(x1, y, 315, "Dropper color: " + hex(CONFIG.dropperColor), b -> { CONFIG.dropperColor = cycleColor(CONFIG.dropperColor); b.setMessage(Component.literal("Dropper color: " + hex(CONFIG.dropperColor))); CONFIG.save(); }));
        addRenderableWidget(button(x2, y, 315, "Spawner color: " + hex(CONFIG.spawnerColor), b -> { CONFIG.spawnerColor = cycleColor(CONFIG.spawnerColor); b.setMessage(Component.literal("Spawner color: " + hex(CONFIG.spawnerColor))); CONFIG.save(); }));
        y += 52;
        addRenderableWidget(Button.builder(Component.literal("Reset Defaults"), b -> { reset(); rebuildWidgets(); }).bounds(x1, y, 150, 28).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose()).bounds(x2+165, y, 150, 28).build());
    }

    private Button button(int x, int y, int w, String text, Button.OnPress press) {
        return Button.builder(Component.literal(text), press).bounds(x, y, w, 30).build();
    }
    private static Component label(String s, boolean v) { return Component.literal(s + on(v)); }
    private static String on(boolean v) { return v ? "ON" : "OFF"; }
    private static int next(int v, int min, int max, int step) { int n=v+step; return n>max?min:n; }
    private static String hex(int c) { return String.format("#%06X", c & 0xFFFFFF); }
    private static int cycleColor(int c) {
        int[] colors = {0xFFFFC107,0xFFFF5252,0xFF69F0AE,0xFF40C4FF,0xFFE040FB,0xFFFFFFFF,0xFFFF6D00};
        for (int i=0;i<colors.length;i++) if ((c&0xFFFFFF)==(colors[i]&0xFFFFFF)) return colors[(i+1)%colors.length];
        return colors[0];
    }
    private static void reset() { StorageFinderClient.CONFIG.storageEnabled=true; StorageFinderClient.CONFIG.spawnerEnabled=true; StorageFinderClient.CONFIG.chests=true; StorageFinderClient.CONFIG.shulkers=true; StorageFinderClient.CONFIG.droppers=true; StorageFinderClient.CONFIG.barrels=false; StorageFinderClient.CONFIG.hoppers=false; StorageFinderClient.CONFIG.scanRadius=48; StorageFinderClient.CONFIG.scanIntervalTicks=10; StorageFinderClient.CONFIG.maxHighlights=5000; StorageFinderClient.CONFIG.lineWidth=2; StorageFinderClient.CONFIG.chestColor=0xFFFFC107; StorageFinderClient.CONFIG.shulkerColor=0xFFE040FB; StorageFinderClient.CONFIG.dropperColor=0xFF42A5F5; StorageFinderClient.CONFIG.spawnerColor=0xFF00E676; StorageFinderClient.CONFIG.save(); }

    @Override public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        extractBackground(g, mouseX, mouseY, delta);
        g.fill(left, top, left+720, top+420, 0xF0101018);
        g.fill(left, top, left+720, top+4, 0xFF40C4FF);
        g.drawCenteredString(font, "StorageFinder", width/2, top+20, 0xFFFFFFFF);
        g.drawCenteredString(font, "StorageFinder + SpawnerFinder", width/2, top+38, 0xFF9EADB8);
        g.drawString(font, "MODULES", left+25, top+58, 0xFF40C4FF);
        
    }

    @Override public boolean isPauseScreen() { return false; }
}
