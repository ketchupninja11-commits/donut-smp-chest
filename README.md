# StorageFinder — Fabric 26.1.2

Client-side Minecraft Fabric mod for Minecraft 26.1.2.

## Modules

### StorageFinder
Highlights:
- Chests
- Trapped chests
- Shulker boxes
- Droppers
- Optional barrels
- Optional hoppers

### SpawnerFinder
Highlights mob spawner blocks.

## GUI

Press **C** to open the StorageFinder configuration screen.

Settings include module toggles, block-type toggles, scan radius, scan interval,
maximum highlights, outline width, and per-type colors. Settings are saved to
`config/storagefinder.json`.

## Build with GitHub Actions

1. Create a repository and upload the contents of this folder.
2. Open the **Actions** tab.
3. Run **Build StorageFinder** (or push to `main`/`master`).
4. Open the completed workflow run.
5. Download the **StorageFinder-Fabric-26.1.2** artifact.
6. Put the resulting `.jar` in your Minecraft `mods` folder.

The workflow provisions Java 25 and Gradle 9.1 automatically, so Java/Gradle do
not need to be installed on your PC for the GitHub build.

## Compatibility

- Minecraft: 26.1.2
- Fabric Loader: 0.19.2+
- Fabric API: 0.155.3+26.1.2+
- Java: 25+

Minecraft 26.1+ uses the unobfuscated Fabric Loom plugin and Java 25.
