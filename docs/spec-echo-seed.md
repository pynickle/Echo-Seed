# Echo Seed

A Fabric + NeoForge mod for Minecraft 26.1.2: one Presence-grown plant whose Echo Fruit marks a place and later returns you there.

## Problem Statement

The player wants a plant that notices they are nearby, grows only from that attention, and whose fruit is a personal, one-at-a-time way to leave a place and come back. Vanilla crops grow from time and bone meal. Chorus fruit and ender pearls teleport, but they are not a planted, homely loop. The player does not want new dimensions, combat, weight, extra plants, or changes to mining, movement, or fighting.

## Solution

Add one plantable Echo Seed (回响种子) that becomes an Echo Crop (回响植株). The crop advances through four Growth Stages only while a real player is inside its Presence Range. A Mature crop can be Picked like a sweet berry bush for Echo Fruit (回响果). Using an Echo Fruit either creates a self-only Mark (回响标记) or teleports back to the live Mark and consumes the fruit.

## User Stories

1. As a player, I want to craft an Echo Seed from any Common Seed plus glowstone dust, so that the plant is cheap to start with materials I already farm.
2. As a player, I want wheat, beetroot, melon, pumpkin, torchflower seeds, and pitcher pods to all count as Common Seeds, so that I do not memorize a custom list.
3. As a player using other crop mods, I want their seeds to work in the recipe when they join `#c:seeds`, so that pack makers do not need a special integration.
4. As a player, I want to plant an Echo Seed on farmland, dirt, grass, podzol, or mycelium, so that I can put it by my door without hoeing first.
5. As a player, I want the crop to stay if farmland is trampled back to dirt, so that walking on my path does not pop the plant.
6. As a player, I do not want bone meal or the passage of time alone to grow the crop, so that it stays a presence plant.
7. As a player, I want walking, flying, swimming, or riding inside the range to count as Presence, so that I do not have to stand still to tend it.
8. As a player, I want a default Presence Range of Chebyshev distance 4 (a 9×9×9 cube), so that “four blocks” matches how I count space in Minecraft.
9. As a spectator, I do not want my camera to grow crops, so that spectating a farm is not farming.
10. As a dead player waiting to respawn, I do not want my corpse to keep growing the crop, so that death is not free AFK growth.
11. As a server operator, I do not want fake players to count as Present, so that automation cannot replace a person.
12. As a creative-mode player, I want to count as Present, so that I can test growth without switching modes.
13. As a player sharing a base, I want any real nearby player to supply Presence, so that a housemate can tend the plant.
14. As a player sharing a base, I do not want two people to stack Presence, so that a crowd is not a fertilizer.
15. As a player, I want to see four Growth Stages (0, 1, 2, 3), so that I can read the plant the way I read a sweet berry bush.
16. As a player, I want each stage to take about two minutes of Presence at default speed, so that the first Mature takes about six minutes.
17. As a player who walks past the farm, I do not want the crop to jump a whole stage in seconds, so that traffic is not an instant harvest.
18. As a player who leaves mid-stage, I want only the current Presence Value to decay, so that the plant does not shrink a whole stage.
19. As a player looking at a Mature crop, I do not want it to wither after I walk away, so that a finished plant stays harvestable.
20. As a player exploring far away, I want Presence Value to freeze while the chunk is unloaded, so that progress is not invented or destroyed offline.
21. As a player standing in range, I want sparse dark-teal particles at the crop, so that I know I am feeding it before the model changes.
22. As a player, I want a soft sound when a Growth Stage advances, so that a change I am not staring at is still noticeable.
23. As a player, I do not want a HUD or action-bar spam while growing, so that living next to the plant is quiet.
24. As a player at night, I want a Mature crop to emit light level 4, so that I can see which plant is ready without it becoming a lamp.
25. As a player, I want light to go out when the crop leaves Mature, so that glow means “Picked me”.
26. As a player, I want to Pick only at Growth Stage 3 by right-clicking, so that a half-grown plant is not harvested by accident.
27. As a player, I want a Pick to drop 2–4 Echo Fruit and no Echo Seed, so that one bush is not a seed factory.
28. As a player, I want a Pick to return the crop to Growth Stage 1, so that the next cycle is about four minutes, not a full replant.
29. As a player, I want right-click on stages 0–2 to do nothing, so that I do not waste a click expecting berries-at-age-2 behaviour.
30. As a player relocating a plant, I want breaking a Mature crop to drop 2–4 Echo Fruit plus one Echo Seed, so that I can move it.
31. As a player who breaks an immature crop, I want exactly one Echo Seed back, so that a mis-click is recoverable.
32. As a player, I want flowing water to destroy the crop and use the same drops as breaking it, so that a flood behaves like other plants I know.
33. As a player, I want pistons and explosions to destroy the crop with the same drops as breaking it, so that machines and creepers are consistent.
34. As a player walking through the plant, I do not want collision or damage, so that a doorway plant does not fight me.
35. As a player, I want an Echo Seed to compost like a vanilla seed, so that extras are not trash.
36. As a player, I do not want Echo Fruit to compost, so that a teleport item is not fertilizer.
37. As a player, I do not want chickens to eat Echo Seeds, so that a bird cannot delete my teleport line.
38. As a player, I do not want villagers to pick up or plant Echo Seeds, so that a village does not farm Marks.
39. As a player in creative, I want Echo Seeds in the Natural Blocks tab and Echo Fruit in Ingredients, so that I can find them next to similar vanilla items.
40. As a player with no live Mark, I want using an Echo Fruit to create a Mark at my feet without consuming the fruit, so that the first use is a bookmark.
41. As a player with a live Mark, I want using an Echo Fruit to teleport to that Mark, clear it, and consume one fruit, so that return is a one-shot.
42. As a player holding two Echo Fruit, I want both to share the same per-player Mark, so that I cannot run two Marks at once.
43. As a player who drops and picks up the fruit, I want the Mark to stay, so that inventory shuffling is not a cancel.
44. As a player who marked the wrong spot, I want sneak-use to dismiss the Mark without teleporting or consuming, so that I can re-mark.
45. As a player with no live Mark, I want sneak-use to still create a Mark, so that sneak is not a special plant action.
46. As a player, I want only myself to see the Mark: a one-player-tall translucent teal pillar and a faint footprint that fade over 45 seconds, so that it is personal and temporary.
47. As a player, I want the Mark to store dimension and position, so that I can return from the Nether to an Overworld home.
48. As a player in another dimension, I want the 45-second timer to run in real server time, so that leaving the dimension is not a pause cheat.
49. As a player whose Mark expired, I want the next use to create a new Mark, so that an old fruit is not stuck.
50. As a player teleporting into a solid block, I want to be nudged into air above, so that I am not permanently stuck.
51. As a player whose Mark sits in lava or the void, I want the teleport to still happen, so that the fruit is not a safety tool.
52. As a player teleporting, I want the destination chunk to load as part of the teleport, so that I do not land in an unloaded hole.
53. As a player who just teleported, I want an 8-second cooldown before another teleport, so that fruit cannot be chain-warped.
54. As a player on cooldown, I want use to do nothing and show an action-bar hint, so that I know why it failed and do not lose the fruit.
55. As a player placing or cancelling a Mark, I do not want those actions to start cooldown, so that fixing a bad Mark is free.
56. As a player teleporting, I want a short dark-teal particle gather/scatter and a soft echo sound, so that the move is readable.
57. As a player cancelling a Mark, I want the pillar to vanish with no teleport effects, so that cancel is not confused with travel.
58. As a player, I do not want teleport to deal damage, weakness, or fire, so that the fruit does not change combat.
59. As a server operator, I want growth speed, Mark duration, cooldown, and Presence Range in a server-owned config file, so that clients cannot lie about the rules.
60. As a server operator, I want that file to load even when YACL is absent, so that dedicated servers do not depend on a GUI mod.
61. As a player with YACL and a config screen entry, I want to edit those four values, so that I can tune feel in singleplayer.
62. As a player without YACL, I want the same JSON defaults to apply, so that the mod still works.
63. As a player on a dedicated server, I want the client to show the server's values in YACL, so that the GUI is not a lie.
64. As a player, I want sculk/echo colours (dark teal, black-green, faint glow) on seed, crop, fruit, and Mark, so that the set reads as one thing.
65. As a player, I want the four crop models and both item textures to look finished, so that a tiny content mod still feels like Minecraft.
66. As a pack maker, I want Fabric and NeoForge jars for 26.1.2 from one project, so that I do not maintain two mods.
67. As a developer, I want the template id and packages renamed to `echo_seed` / `com.euphony.echoseed`, so that the shipped mod is not Example Mod.

## Implementation Decisions

- Target Minecraft 26.1.2, Fabric and NeoForge, Java 25, existing Architectury Loom split (shared common, thin loader entrypoints). No Architectury API dependency unless a later ticket proves a loader gap that cannot live behind a tiny platform helper.
- Mod id `echo_seed`, display name Echo Seed / 回响种子, maven group / root package `com.euphony.echoseed`. License on disk is MIT (ENC_Euphony); metadata should match the repo license, not the template Unlicense leftover.
- Shared game rules live in common: items, crop block, Presence/growth policy, Mark policy, loot policy, config load, networking for Mark visibility and config sync. Loader modules only register entrypoints, optional YACL/Mod Menu hooks, and platform `isModLoaded`.
- Crafting: shapeless Common Seed (`#c:seeds`) + glowstone dust → one Echo Seed. Do not invent a private seeds tag.
- Plantable Soil is a block tag containing farmland, dirt, grass, podzol, mycelium. The crop must survive farmland converting to dirt.
- Growth Stages are an integer age 0–3 on the crop, same kind of property as a sweet berry bush. Stage 3 is Mature and is the only light-emitting, Pickable stage (light 4).
- Growth clock is a Presence Value bar on the current stage (ADR-0003), not random ticks and not `randomTickSpeed`. Default fill is 2 minutes of Presence per stage (first Mature ≈ 6 minutes). Config “growth speed” is a multiplier on that duration. Decay is half the fill rate and only eats the current bar. Stages never decrement except Pick → 1. Unloaded chunks freeze the clock (ADR-0001, range-only Presence).
- Presence: Chebyshev distance ≤ configured range (default 4) from the crop. A Present player is living, not spectator, and a real connected player. Creative counts. Multiple players do not add (ADR-0001).
- Pick is berry-style (ADR-0004): right-click Mature only; 2–4 Echo Fruit, no seed; age set to 1. Break / water / piston / explosion use break loot: immature → 1 Echo Seed; Mature → 2–4 Echo Fruit + 1 Echo Seed. No Fortune or Silk Touch special case. No collision, no contact damage.
- Echo Seed is compostable at vanilla-seed odds. Echo Fruit is not compostable and is not food. Do not add either item to chicken food or villager pickup/plant tags.
- Mark is per-player, not per-item (ADR-0002). No live Mark + use → create Mark, do not consume. Live Mark + use → teleport, clear Mark, consume one fruit. Sneak + live Mark → clear Mark, no teleport, no consume. One Mark per player. Duration default 45s of server real time, including other dimensions. Cooldown default 8s after a successful teleport only.
- Teleport stores dimension + position. Cross-dimension is allowed. Destination chunks load as part of teleport. If the destination is inside a block, search upward for air. Lava/void are not special-cased. No damage, no status effects.
- Mark rendering is client-only for the owning player: one-player-tall translucent pillar, faint footprint, fade over remaining lifetime, no collision. Other clients must not spawn it.
- Config is server-owned JSON (ADR-0005). Keys: growth speed, Mark duration, cooldown, Presence Range. Sync to clients for GUI display. YACL is optional (`suggests` / NeoForge optional). YACL types must not load unless the mod is present. JSON still loads without YACL. Do not copy Better-Client’s “no YACL ⇒ ignore file” behaviour.
- Visual theme is sculk/echo. Texture production may use Imagine, ImageMagick, scripts, or vanilla references; the acceptance bar is “looks like a finished Minecraft plant,” not a specific toolchain.
- Do not change vanilla mining, movement, or combat. No new dimension, no extra plants, no weight system.

## Testing Decisions

A good test asserts an outward rule (what the player or world observes) from a given situation. It does not assert field names, tick counts inside a renderer, or which class stored the Mark.

There are no tests in the template today. Prefer a single deep rules module that both the Minecraft adapters and the tests call, rather than spinning up a client for every rule.

**Seam (the one we will test):** a pure Echo rules module with no Minecraft types in its API. It answers:

- Given crop age, Presence Value, configured durations, whether anyone is Present, and a time delta — what are the new age, Presence Value, and whether the stage just advanced?
- Given Mark state (none / live at a location / remaining time / cooldown remaining), sneak, and “use fruit” — what is the new Mark state, and do we consume the fruit / teleport / show cooldown hint?
- Given age and the reason the crop left the world (Pick, break, water, piston, explosion) — what items drop, and if the block remains, what age is it?

Minecraft code (block entity tick, item use, loot tables, particles, YACL) is an adapter around that module. GameTests or in-game checks are extra confidence for planting, lighting, and rendering, not the place the rules are specified.

If this seam is wrong, say so before `/to-tickets`. The user asked to publish immediately; change the seam in a comment on this issue if needed.

## Out of Scope

- New dimensions, biomes, structures, or boss content
- Additional plants, trees, or food
- Combat, armour weight, mining speed, or movement changes
- Safe-teleport / anti-lava protection
- Fortune / Silk Touch extra behaviour
- Villager or chicken integrations
- Partial Pick at Growth Stage 2
- Age going backwards from decay
- Offline / unloaded decay simulation
- Bundling YACL inside the jar
- Ports off 26.1.2 in this spec

## Further Notes

Glossary: root `CONTEXT.md`. Decisions: `docs/adr/0001`–`0005`.

Grilling settled the rules; this issue is the parent spec. Split with `/to-tickets` next. Do not implement the whole mod in one window.

Default numbers: Presence Range 4, 120 seconds Presence per stage, decay at half fill rate, Mark 45 seconds, teleport cooldown 8 seconds, Mature light 4, Pick 2–4 fruit.
