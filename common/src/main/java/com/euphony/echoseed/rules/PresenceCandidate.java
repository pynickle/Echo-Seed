package com.euphony.echoseed.rules;

public record PresenceCandidate(
    int blockX,
    int blockY,
    int blockZ,
    boolean living,
    boolean spectator,
    boolean realConnectedPlayer
) {
}
