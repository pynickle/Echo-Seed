# Echo Seed / 回响种子

A Fabric + NeoForge mod for Minecraft 26.1.2: one Presence-grown plant whose Echo Fruit marks a place and later returns you there.

## Project structure

- `common/` — shared code, resources, mixins, Access Widener, and the Echo rules module
- `fabric/` — Fabric entrypoints and metadata
- `neoforge/` — NeoForge entrypoints and metadata
- `gradle.properties` — versions and mod coordinates (`echo_seed`, `com.euphony.echoseed`)

## Requirements

- JDK 25
- Gradle Wrapper (`./gradlew` or `gradlew.bat`)

## Getting started

```bash
./gradlew build
./gradlew :common:test
```

Use your IDE's generated run configs, or Gradle tasks, to launch Fabric or NeoForge during development.

> [!NOTE]
> This project targets Minecraft `26.1.2`, Fabric Loader `0.19.3`, Fabric API `0.155.2+26.1.2`, and NeoForge `26.1.2.95`. License is MIT.

## Access wideners

This project keeps a single Access Widener in `common/src/main/resources/${mod_id}.accesswidener` and wires both loaders to that file.

- Fabric copies it into the jar (`processResources`) and injects it with Loom (`loom.injectAccessWidener`). `fabric.mod.json` declares the same file as `"accessWidener"`.
- NeoForge reads the same file through `loom.accessWidenerPath` and converts it to `META-INF/accesstransformer.cfg` when building the remapped jar (`loom.neoForge.convertAccessWideners`). There is no hand-written Access Transformer source file.

## Notes

- Shared initialization starts in `common/src/main/java/com/euphony/echoseed/EchoSeed.java`
- Growth, Mark, and drop rules live in `common/src/main/java/com/euphony/echoseed/rules/`
- Fabric metadata lives in `fabric/src/main/resources/fabric.mod.json`
- NeoForge metadata lives in `neoforge/src/main/resources/META-INF/neoforge.mods.toml`
