package com.euphony.echoseed.client;

import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.rules.EchoConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * YACL types live only in this class. Callers must not load it unless YACL is present.
 */
public final class EchoYaclScreen {
    private EchoYaclScreen() {
    }

    public static Screen create(Screen parent) {
        Draft draft = Draft.from(EchoConfigs.active());
        boolean editable = localEditsApply();
        return YetAnotherConfigLib.createBuilder()
            .title(Component.translatable("echo_seed.config.title"))
            .category(ConfigCategory.createBuilder()
                .name(Component.translatable("echo_seed.config.category.gameplay"))
                .option(doubleOption(
                    "growth_speed",
                    EchoConfig.DEFAULTS.growthSpeed(),
                    () -> draft.growthSpeed,
                    value -> draft.growthSpeed = value,
                    editable
                ))
                .option(doubleOption(
                    "mark_duration_seconds",
                    EchoConfig.DEFAULTS.markDurationSeconds(),
                    () -> draft.markDurationSeconds,
                    value -> draft.markDurationSeconds = value,
                    editable
                ))
                .option(doubleOption(
                    "cooldown_seconds",
                    EchoConfig.DEFAULTS.cooldownSeconds(),
                    () -> draft.cooldownSeconds,
                    value -> draft.cooldownSeconds = value,
                    editable
                ))
                .option(Option.<Integer>createBuilder()
                    .name(Component.translatable("echo_seed.config.presence_range"))
                    .description(OptionDescription.of(Component.translatable("echo_seed.config.presence_range.desc")))
                    .binding(
                        EchoConfig.DEFAULTS.presenceRange(),
                        () -> draft.presenceRange,
                        value -> draft.presenceRange = value
                    )
                    .controller(IntegerFieldControllerBuilder::create)
                    .available(editable)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("echo_seed.config.show_mark_duration"))
                    .description(OptionDescription.of(Component.translatable("echo_seed.config.show_mark_duration.desc")))
                    .binding(
                        EchoConfig.DEFAULTS.showMarkDuration(),
                        () -> draft.showMarkDuration,
                        value -> draft.showMarkDuration = value
                    )
                    .controller(BooleanControllerBuilder::create)
                    .available(editable)
                    .build())
                .build())
            .save(() -> save(draft, editable))
            .build()
            .generateScreen(parent);
    }

    private static Option<Double> doubleOption(
        String key,
        double defaultValue,
        Supplier<Double> getter,
        Consumer<Double> setter,
        boolean editable
    ) {
        return Option.<Double>createBuilder()
            .name(Component.translatable("echo_seed.config." + key))
            .description(OptionDescription.of(Component.translatable("echo_seed.config." + key + ".desc")))
            .binding(defaultValue, getter, setter)
            .controller(DoubleFieldControllerBuilder::create)
            .available(editable)
            .build();
    }

    private static void save(Draft draft, boolean editable) {
        if (!editable) {
            return;
        }
        EchoConfigs.apply(EchoConfig.sanitize(
            draft.growthSpeed,
            draft.markDurationSeconds,
            draft.cooldownSeconds,
            draft.presenceRange,
            draft.showMarkDuration
        ));
        EchoConfigs.save();
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getSingleplayerServer() != null) {
            EchoConfigs.syncAll(minecraft.getSingleplayerServer().getPlayerList().getPlayers());
        }
    }

    private static boolean localEditsApply() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.getSingleplayerServer() != null || minecraft.level == null;
    }

    private static final class Draft {
        private double growthSpeed;
        private double markDurationSeconds;
        private double cooldownSeconds;
        private int presenceRange;
        private boolean showMarkDuration;

        private static Draft from(EchoConfig config) {
            Draft draft = new Draft();
            draft.growthSpeed = config.growthSpeed();
            draft.markDurationSeconds = config.markDurationSeconds();
            draft.cooldownSeconds = config.cooldownSeconds();
            draft.presenceRange = config.presenceRange();
            draft.showMarkDuration = config.showMarkDuration();
            return draft;
        }
    }
}
