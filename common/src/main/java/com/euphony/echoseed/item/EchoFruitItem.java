package com.euphony.echoseed.item;

import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.mark.EchoMarkTeleporter;
import com.euphony.echoseed.mark.EchoMarks;
import com.euphony.echoseed.rules.FruitAction;
import com.euphony.echoseed.rules.MarkLocation;
import com.euphony.echoseed.rules.MarkUseResult;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EchoFruitItem extends Item {
    public static final String COOLDOWN_HINT_KEY = "echo_seed.mark.cooldown";

    public EchoFruitItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }
        MarkLocation here = new MarkLocation(
            level.dimension().identifier().toString(),
            player.getX(),
            player.getY(),
            player.getZ()
        );
        MarkUseResult result = EchoConfigs.rules().useFruit(EchoMarks.get(serverPlayer), player.isShiftKeyDown(), here);
        if (result.action() == FruitAction.TELEPORT) {
            if (result.location().isEmpty() || !EchoMarkTeleporter.teleport(serverPlayer, result.location().get())) {
                return InteractionResult.FAIL;
            }
        }
        if (result.showsCooldownHint()) {
            serverPlayer.sendOverlayMessage(Component.translatable(COOLDOWN_HINT_KEY));
            return InteractionResult.SUCCESS;
        }
        EchoMarks.set(serverPlayer, result.state());
        ItemStack stack = player.getItemInHand(hand);
        if (result.consumesFruit()) {
            stack.consume(1, player);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }
}
