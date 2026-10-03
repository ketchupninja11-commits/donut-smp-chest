package com.storagefinder.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {
    public boolean storageEnabled = true;
    public boolean spawnerEnabled = true;
    public boolean chests = true;
    public boolean shulkers = true;
    public boolean droppers = true;
    public boolean barrels = false;
    public boolean hoppers = false;
    public int scanRadius = 48;
    public int scanIntervalTicks = 10;
    public int maxHighlights = 5000;
    public float lineWidth = 2.0f;
    public int chestColor = 0xFFFFC107;
    public int shulkerColor = 0xFFE040FB;
    public int dropperColor = 0xFF42A5F5;
    public int barrelColor = 0xFF8D6E63;
    public int hopperColor = 0xFFB0BEC5;
    public int spawnerColor = 0xFF00E676;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Path path() {
        return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("storagefinder.json");
    }

    public static Config load() {
        try {
            Path p = path();
            if (Files.exists(p)) return GSON.fromJson(Files.readString(p), Config.class);
        } catch (Exception ignored) {}
        Config c = new Config(); c.save(); return c;
    }

    public void save() {
        try {
            Files.createDirectories(path().getParent());
            Files.writeString(path(), GSON.toJson(this));
        } catch (IOException ignored) {}
    }

    public int colorFor(BlockState state) {
        if (state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST)) return chestColor;
        if (state.getBlock() instanceof ShulkerBoxBlock) return shulkerColor;
        if (state.is(Blocks.DROPPER)) return dropperColor;
        if (state.is(Blocks.BARREL)) return barrelColor;
        if (state.is(Blocks.HOPPER)) return hopperColor;
        return chestColor;
    }
}
