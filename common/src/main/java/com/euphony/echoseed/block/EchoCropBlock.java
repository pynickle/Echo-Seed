package com.euphony.echoseed.block;

import com.euphony.echoseed.EchoTags;
import com.euphony.echoseed.rules.EchoRules;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EchoCropBlock extends VegetationBlock {
    public static final MapCodec<EchoCropBlock> CODEC = simpleCodec(EchoCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0),
        Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
        Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0),
        Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)
    };

    public EchoCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[Math.min(state.getValue(AGE), EchoRules.MATURE_AGE)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
