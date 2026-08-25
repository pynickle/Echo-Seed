# Echo Seed

**Echo Seed** is a tiny vanilla-feeling plant for Fabric and NeoForge. It grows only while you are nearby. Its fruit remembers a place — then brings you back.

No new dimension. No waypoint network. No combat changes.

## Presence

Craft an **Echo Seed** from any common seed (`#c:seeds`) and glowstone dust. Seeds from other crop mods work when they join that tag. Plant it on farmland, dirt, grass, podzol, or mycelium — you do not need a hoe.

The **Echo Crop** ignores bone meal and the passage of time. It grows while a real, living player is inside a 9×9×9 cube around it. Walking, flying, swimming, and riding all count. Spectators, dead players, and fake players do not. A housemate can tend it; a crowd is not fertilizer.

Four stages, like a sweet berry bush. About two minutes of presence per stage; the first harvest is around six minutes. Leave mid-stage and only the current progress fades. A finished plant stays ready.

When it glows (light 4), right-click to pick **2–4 Echo Fruit**. The plant stays and returns to stage 1. Break a mature plant to take the fruit and the seed with you.

## Echo Fruit

Not food. Not compost.

- **Use** with no Mark — leave a personal Mark at your feet. The fruit is not consumed.
- **Use** with a live Mark — teleport there (including other dimensions), clear the Mark, consume one fruit.
- **Sneak-use** with a live Mark — dismiss it. No teleport, no consume.

You have at most one Mark. Only you can see it: a fading teal pillar, **45 seconds**. After a successful teleport, wait **8 seconds**.

Plant it by the door. Mark the threshold. Step into a cave. Use the fruit to come home.

## Config

[YetAnotherConfigLib](https://modrinth.com/mod/yacl) is optional. The server still owns `config/echo_seed.json`:

- Growth speed (default `1.0` ≈ 120 seconds per stage)
- Mark duration (default 45 seconds)
- Teleport cooldown (default 8 seconds)
- Presence range (default 4 → 9×9×9)
- Show remaining Mark time on the action bar (default off)
