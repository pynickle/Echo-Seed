package com.euphony.echoseed.block;

import com.euphony.echoseed.EchoBlockEntityTypes;
import com.euphony.echoseed.EchoItems;
import com.euphony.echoseed.EchoTags;
import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.rules.CropLeaveResult;
import com.euphony.echoseed.rules.EchoRules;
import com.euphony.echoseed.rules.LeaveReason;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.random.RandomGenerator;

public class EchoCropBlock extends VegetationBlock implements EntityBlock {
    public static final MapCodec<EchoCropBlock> CODEC = simpleCodec(EchoCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty PRESENT = BooleanProperty.create("present");

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0),
        Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
        Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0),
        Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)
    };
    private static final int PRESENCE_PARTICLE_COLOR = 0x009295;

    public EchoCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(PRESENT, false));
    }

    @Override
    protected MapCodec<? extends VegetationBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(EchoTags.PLANTABLE_SOIL);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EchoCropBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, EchoBlockEntityTypes.ECHO_CROP, EchoCropBlockEntity::serverTick);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(PRESENT) && random.nextInt(10) == 0) {
            double height = 0.2 + state.getValue(AGE) * 0.35 + random.nextDouble() * 0.4;
            level.addParticle(
                new DustParticleOptions(PRESENCE_PARTICLE_COLOR, 1.0F),
                pos.getX() + 0.2 + random.nextDouble() * 0.6,
                pos.getY() + height,
                pos.getZ() + 0.2 + random.nextDouble() * 0.6,
                0.0,
                0.0,
                0.0
            );
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[Math.min(state.getValue(AGE), EchoRules.MATURE_AGE)];
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        CropLeaveResult result = leave(state, LeaveReason.PICK, level.getRandom());
        if (result.drops().isEmpty()) {
            return InteractionResult.PASS;
        }
        if (level instanceof ServerLevel serverLevel) {
            for (ItemStack stack : stacks(result)) {
                Block.popResource(serverLevel, pos, stack);
            }
            result.remainingAge().ifPresent(age -> {
                BlockState picked = state.setValue(AGE, age);
                serverLevel.setBlock(pos, picked, Block.UPDATE_CLIENTS);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, picked));
            });
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return stacks(leave(state, LeaveReason.BREAK, params.getLevel().getRandom()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, PRESENT);
    }

    private static CropLeaveResult leave(BlockState state, LeaveReason reason, RandomSource random) {
        return EchoConfigs.rules().leave(state.getValue(AGE), reason, rulesRandom(random));
    }

    private static RandomGenerator rulesRandom(RandomSource random) {
        return new RandomGenerator() {
            @Override
            public long nextLong() {
                return random.nextLong();
            }

            @Override
            public int nextInt(int bound) {
                return random.nextInt(bound);
            }
        };
    }

    private static List<ItemStack> stacks(CropLeaveResult result) {
        return result.drops().stream().map(EchoItems::stack).toList();
    }

    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
        BlockEntityType<A> actual,
        BlockEntityType<E> expected,
        BlockEntityTicker<? super E> ticker
    ) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }
}
