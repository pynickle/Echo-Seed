package com.euphony.echoseed.mark;

import com.euphony.echoseed.rules.MarkLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public final class EchoMarkTeleporter {
    private static final DustParticleOptions SCULK_DUST =
            new DustParticleOptions(DustColorTransitionOptions.SCULK_PARTICLE_COLOR, 1.0F);

    private EchoMarkTeleporter() {
    }

    public static boolean teleport(ServerPlayer player, MarkLocation destination) {
        ServerLevel target = resolve(player, destination.dimension());
        if (target == null) {
            return false;
        }
        ServerLevel origin = player.level();
        Vec3 from = player.position();
        BlockPos blockPos = BlockPos.containing(destination.x(), destination.y(), destination.z());
        ChunkPos chunk = ChunkPos.containing(blockPos);
        target.getChunkSource().addTicketWithRadius(TicketType.PORTAL, chunk, 2);
        target.getChunkAt(blockPos);
        double x = destination.x();
        double y = nudgeUp(target, player, x, destination.y(), destination.z());
        double z = destination.z();
        Vec3 to = new Vec3(x, y, z);
        if (player.isPassenger()) {
            player.stopRiding();
        }
        ServerPlayer teleported = player.teleport(new TeleportTransition(
                target,
                to,
                Vec3.ZERO,
                player.getYRot(),
                player.getXRot(),
                Set.of(),
                TeleportTransition.DO_NOTHING
        ));
        if (teleported == null) {
            return false;
        }
        teleported.resetFallDistance();
        effects(origin, from);
        effects(teleported.level(), teleported.position());
        teleported.gameEvent(GameEvent.TELEPORT);
        return true;
    }

    private static ServerLevel resolve(ServerPlayer player, String dimension) {
        ResourceKey<Level> key = ResourceKey.create(player.level().dimension().registryKey(), Identifier.parse(dimension));
        return player.level().getServer().getLevel(key);
    }

    private static double nudgeUp(ServerLevel level, ServerPlayer player, double x, double y, double z) {
        double maxY = level.getMaxY() - player.getBbHeight();
        double current = y;
        while (current < maxY && !level.noCollision(box(player, x, current, z))) {
            current += 1.0;
        }
        return current;
    }

    private static AABB box(ServerPlayer player, double x, double y, double z) {
        return player.getDimensions(player.getPose()).makeBoundingBox(x, y, z);
    }

    private static void effects(ServerLevel level, Vec3 pos) {
        double x = pos.x;
        double y = pos.y + 0.9;
        double z = pos.z;
        level.sendParticles(SCULK_DUST, x, y, z, 16, 0.12, 0.25, 0.12, 0.0);
        level.sendParticles(SCULK_DUST, x, y, z, 16, 0.55, 0.9, 0.55, 0.04);
        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.55F, 0.75F);
    }
}
