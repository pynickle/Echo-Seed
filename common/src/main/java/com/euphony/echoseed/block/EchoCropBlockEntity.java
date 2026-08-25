package com.euphony.echoseed.block;

import com.euphony.echoseed.EchoBlockEntityTypes;
import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.rules.EchoRules;
import com.euphony.echoseed.rules.GrowthResult;
import com.euphony.echoseed.rules.GrowthState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EchoCropBlockEntity extends BlockEntity {
    private static final String PRESENCE_VALUE_KEY = "PresenceValue";

    private long presenceValueMillis;

    public EchoCropBlockEntity(BlockPos pos, BlockState state) {
        super(EchoBlockEntityTypes.ECHO_CROP, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EchoCropBlockEntity entity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        boolean present = hasPresentPlayer(serverLevel, pos);
        int age = state.getValue(EchoCropBlock.AGE);
        GrowthResult result = EchoConfigs.rules().grow(
                new GrowthState(age, entity.presenceValueMillis),
                present,
                EchoRules.MILLIS_PER_GAME_TICK
        );
        GrowthState next = result.state();
        if (entity.presenceValueMillis != next.presenceValueMillis()) {
            entity.presenceValueMillis = next.presenceValueMillis();
            entity.setChanged();
        }
        BlockState updated = state;
        if (next.age() != age) {
            updated = updated.setValue(EchoCropBlock.AGE, next.age());
        }
        if (state.getValue(EchoCropBlock.PRESENT) != present) {
            updated = updated.setValue(EchoCropBlock.PRESENT, present);
        }
        if (updated != state) {
            serverLevel.setBlock(pos, updated, Block.UPDATE_CLIENTS);
        }
        if (result.stageAdvanced()) {
            serverLevel.levelEvent(LevelEvent.SOUND_CHORUS_GROW, pos, 0);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(updated));
        }
    }

    static boolean hasPresentPlayer(ServerLevel level, BlockPos crop) {
        for (ServerPlayer player : level.players()) {
            if (EchoRules.isEligiblePresent(player.isAlive(), player.isSpectator(), isRealConnectedPlayer(player))
                    && EchoRules.isInsidePresenceRange(
                    crop.getX(),
                    crop.getY(),
                    crop.getZ(),
                    player.getBlockX(),
                    player.getBlockY(),
                    player.getBlockZ(),
                    EchoConfigs.rules().presenceRange()
            )) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRealConnectedPlayer(ServerPlayer player) {
        return player.connection != null && !player.getClass().getName().contains("FakePlayer");
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong(PRESENCE_VALUE_KEY, this.presenceValueMillis);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.presenceValueMillis = input.getLongOr(PRESENCE_VALUE_KEY, 0L);
    }
}
