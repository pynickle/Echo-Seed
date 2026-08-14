# Echo Seed

A Minecraft context for one presence-grown plant whose fruit marks a place and later returns you there.

## Language

**Echo Seed (回响种子)**:
The plantable item that becomes an Echo Crop. Compostable like a vanilla seed. Chickens and villagers do not use it.
_Avoid_: 回音种子, echo wheat

**Echo Crop (回响植株)**:
The planted block grown from an Echo Seed. Distinct from the seed item and from vanilla crops. Flowing water destroys it.
_Avoid_: plant, wheat, 作物 (unqualified)

**Echo Fruit (回响果)**:
The item harvested from a mature Echo Crop. Not compostable. Not food.
_Avoid_: echo apple, chorus fruit (vanilla)

## Presence

**Presence (存在)**:
A living, non-spectator, real player inside an Echo Crop's Presence Range. Pose, movement, and activity do not matter. Spectators, dead players, and fake players are not Present.
_Avoid_: 停留, AFK, standing still

**Presence Range (存在范围)**:
The Chebyshev neighborhood around an Echo Crop. Default extent is 4 (a 9×9×9 cube).
_Avoid_: grow radius, sphere, Euclidean radius

**Presence Value (存在值)**:
The fill of the current Growth Stage. It rises while any player is Present and falls while none are. A full bar advances the crop by one stage and resets the bar. Stages do not go backwards except when the crop is Picked.
_Avoid_: growth points, XP

**Growth Stage (生长阶段)**:
The crop's integer age: 0, 1, 2, or 3. Four parked states; three Presence-driven steps connect them. Same kind of property as a sweet berry bush's `AGE`.
_Avoid_: phase, level (unqualified), "3 stages" (that counts the arrows, not the states)

**Mature**:
An Echo Crop at Growth Stage 3. It emits light level 4 and stays there until Picked or destroyed.
_Avoid_: fully grown, max age (as a player-facing name)

**Pick (采摘)**:
Right-clicking a Mature Echo Crop to take Echo Fruit. Only Growth Stage 3 can be Picked. The plant stays and returns to Growth Stage 1. Pick yields fruit only; it never yields an Echo Seed.
_Avoid_: harvest (unqualified), break, mine

## Mark

**Mark (回响标记)**:
A player-owned location created by using an Echo Fruit. Only that player can see it: a one-player-tall translucent pillar with a faint footprint, fading over its lifetime. A player has at most one live Mark. Sneak-using an Echo Fruit dismisses the live Mark without teleporting or consuming the fruit. A Mark may point into another dimension.
_Avoid_: waypoint, lodestone, compass, banner

**Common Seed**:
An item in the common `c:seeds` tag. Combined with glowstone dust it crafts an Echo Seed.
_Avoid_: any seed (unqualified), villager plantable seeds

## Soil

**Plantable Soil**:
The blocks that accept an Echo Seed: farmland, dirt, grass, podzol, mycelium.
