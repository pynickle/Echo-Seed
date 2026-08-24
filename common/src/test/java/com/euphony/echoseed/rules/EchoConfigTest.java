package com.euphony.echoseed.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EchoConfigTest {
    private final MarkLocation feet = new MarkLocation("minecraft:overworld", 10.5, 64.0, -3.25);

    @Test
    void missingJsonUsesDefaults() {
        EchoConfig config = EchoConfig.parse("");

        assertEquals(EchoConfig.DEFAULTS, config);
        assertEquals(1.0, config.growthSpeed());
        assertEquals(45.0, config.markDurationSeconds());
        assertEquals(8.0, config.cooldownSeconds());
        assertEquals(4, config.presenceRange());
    }

    @Test
    void missingFileUsesDefaults(@TempDir Path dir) {
        Path file = dir.resolve("echo_seed.json");

        assertEquals(EchoConfig.DEFAULTS, EchoConfig.read(file));
    }

    @Test
    void malformedJsonUsesDefaults() {
        assertEquals(EchoConfig.DEFAULTS, EchoConfig.parse("{not json"));
    }

    @Test
    void jsonWithTheFourKeysLoads() {
        EchoConfig config = EchoConfig.parse(
            """
            {
              "growth_speed": 2.0,
              "mark_duration_seconds": 10.0,
              "cooldown_seconds": 3.0,
              "presence_range": 2
            }
            """
        );

        assertEquals(2.0, config.growthSpeed());
        assertEquals(10.0, config.markDurationSeconds());
        assertEquals(3.0, config.cooldownSeconds());
        assertEquals(2, config.presenceRange());
    }

    @Test
    void missingKeysUseDefaults() {
        EchoConfig config = EchoConfig.parse("{\"growth_speed\": 0.5}");

        assertEquals(0.5, config.growthSpeed());
        assertEquals(45.0, config.markDurationSeconds());
        assertEquals(8.0, config.cooldownSeconds());
        assertEquals(4, config.presenceRange());
    }

    @Test
    void writtenFileIsTheSameJsonADedicatedServerWouldRead(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("echo_seed.json");
        EchoConfig original = new EchoConfig(0.5, 30.0, 12.0, 6);

        EchoConfig.write(file, original);

        String json = Files.readString(file);
        assertTrue(json.contains("\"growth_speed\""));
        assertTrue(json.contains("\"mark_duration_seconds\""));
        assertTrue(json.contains("\"cooldown_seconds\""));
        assertTrue(json.contains("\"presence_range\""));
        assertEquals(original, EchoConfig.read(file));
    }

    @Test
    void defaultsKeepRangeFourTwoMinutesPerStageMark45AndCooldown8() {
        EchoRules rules = EchoRules.from(EchoConfig.DEFAULTS);

        GrowthResult grown = rules.grow(GrowthState.planted(), true, 120_000L);
        assertEquals(1, grown.state().age());
        assertEquals(0L, grown.state().presenceValueMillis());

        PresenceCandidate atEdge = new PresenceCandidate(14, 64, -3, true, false, true);
        PresenceCandidate justOutside = new PresenceCandidate(15, 64, -3, true, false, true);
        assertTrue(rules.anyonePresent(10, 64, -3, List.of(atEdge)));
        assertFalse(rules.anyonePresent(10, 64, -3, List.of(justOutside)));

        MarkUseResult created = rules.useFruit(MarkState.none(), false, feet);
        assertEquals(45_000L, created.state().live().orElseThrow().remainingMillis());

        MarkUseResult teleported = rules.useFruit(created.state(), false, feet);
        assertEquals(8_000L, teleported.state().cooldownRemainingMillis());
    }

    @Test
    void growthSpeedTwoHalvesTimeToTheNextStage() {
        EchoRules rules = EchoRules.from(EchoConfig.parse("{\"growth_speed\": 2.0}"));

        GrowthResult atSixty = rules.grow(GrowthState.planted(), true, 60_000L);
        assertEquals(1, atSixty.state().age());
        assertEquals(0L, atSixty.state().presenceValueMillis());

        GrowthResult shortOfSixty = rules.grow(GrowthState.planted(), true, 59_000L);
        assertEquals(0, shortOfSixty.state().age());
        assertEquals(59_000L, shortOfSixty.state().presenceValueMillis());
    }

    @Test
    void presenceRangeTwoChangesWhoIsPresent() {
        EchoRules rules = EchoRules.from(EchoConfig.parse("{\"presence_range\": 2}"));
        PresenceCandidate twoAway = new PresenceCandidate(12, 64, -3, true, false, true);
        PresenceCandidate threeAway = new PresenceCandidate(13, 64, -3, true, false, true);

        assertTrue(rules.anyonePresent(10, 64, -3, List.of(twoAway)));
        assertFalse(rules.anyonePresent(10, 64, -3, List.of(threeAway)));
    }

    @Test
    void markDurationAndCooldownChangeFruitUse() {
        EchoRules rules = EchoRules.from(EchoConfig.parse(
            """
            {
              "mark_duration_seconds": 10.0,
              "cooldown_seconds": 2.0
            }
            """
        ));

        MarkUseResult created = rules.useFruit(MarkState.none(), false, feet);
        assertEquals(10_000L, created.state().live().orElseThrow().remainingMillis());

        MarkUseResult teleported = rules.useFruit(created.state(), false, feet);
        assertEquals(FruitAction.TELEPORT, teleported.action());
        assertEquals(2_000L, teleported.state().cooldownRemainingMillis());

        MarkUseResult blocked = rules.useFruit(teleported.state(), false, feet);
        assertEquals(FruitAction.BLOCKED_BY_COOLDOWN, blocked.action());
        assertEquals(Optional.empty(), blocked.location());
    }

    @Test
    void invalidNumbersFallBackToDefaults() {
        EchoConfig config = EchoConfig.parse(
            """
            {
              "growth_speed": 0,
              "mark_duration_seconds": -5,
              "cooldown_seconds": -1,
              "presence_range": -3
            }
            """
        );

        assertEquals(EchoConfig.DEFAULTS, config);
    }
}
