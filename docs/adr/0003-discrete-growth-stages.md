# Echo Crops grow in discrete stages

Growth is a vanilla-style sequence of Growth Stages, not a single hidden bar that flips the plant to Mature at one threshold.

A staged blockstate matches how players already read crops, gives the presence mechanic visible steps, and keeps Mature as “the last stage” rather than a separate flag.

The clock is a Presence Value bar on the current Growth Stage, not random ticks. Completing the bar advances one stage. Leaving decays only the current bar; the stage itself never decreases except when the crop is Picked.
