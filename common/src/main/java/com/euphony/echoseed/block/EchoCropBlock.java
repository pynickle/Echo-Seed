package com.euphony.echoseed.block;

import com.euphony.echoseed.EchoBlockEntityTypes;
import com.euphony.echoseed.EchoTags;
import com.euphony.echoseed.rules.EchoRules;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

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
            level.addParticle(
                new DustParticleOptions(DustColorTransitionOptions.SCULK_PARTICLE_COLOR, 1.0F),
                pos.getX() + random.nextDouble(),
                pos.getY() + 0.2 + random.nextDouble() * 0.6,
                pos.getZ() + random.nextDouble(),
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, PRESENT);
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
