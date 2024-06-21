package jaggwagg.frozen_apocalypse.config;

public class ApocalypseLevel {
    private final Integer apocalypseLevel;
    private final int startingDay;
    private final int worldUpdateSpeed;
    private final float sunSize;
    private final boolean canMobsSurviveDaylight;
    private final boolean canMobsSpawnDaylight;
    private final boolean canAllBiomesSnow;
    private final boolean freezeEntities;
    private final int freezingYLevel;
    private final int minimumHeatSourceDistance;
    private final int freezeDamageDelay;
    private final float freezeDamage;
    private final boolean leavesDecay;
    private final boolean grassToFrostedGrass;
    private final boolean waterToIce;
    private final boolean placeSnow;
    private final boolean placeIcicles;
    private final boolean frostedGrassToDeadGrass;
    private final boolean leavesToDeadLeaves;
    private final boolean iceToPackedIce;
    private final boolean lavaToObsidian;
    private final boolean placeSnowBlock;
    private final boolean grassToPermafrost;
    private final boolean packedIceToBlueIce;

    private ApocalypseLevel(Builder builder) {
        this.apocalypseLevel = builder.apocalypseLevel;
        this.startingDay = builder.startingDay;
        this.worldUpdateSpeed = builder.worldUpdateSpeed;
        this.sunSize = builder.sunSize;
        this.canMobsSurviveDaylight = builder.canMobsSurviveDaylight;
        this.canMobsSpawnDaylight = builder.canMobsSpawnDaylight;
        this.canAllBiomesSnow = builder.canAllBiomesSnow;
        this.freezeEntities = builder.freezeEntities;
        this.freezingYLevel = builder.freezingYLevel;
        this.freezeDamage = builder.freezeDamage;
        this.freezeDamageDelay = builder.freezeDamageDelay;
        this.minimumHeatSourceDistance = builder.minimumHeatSourceDistance;
        this.leavesDecay = builder.leavesDecay;
        this.grassToFrostedGrass = builder.grassToFrostedGrass;
        this.waterToIce = builder.waterToIce;
        this.placeSnow = builder.placeSnow;
        this.placeIcicles = builder.placeIcicles;
        this.frostedGrassToDeadGrass = builder.frostedGrassToDeadGrass;
        this.leavesToDeadLeaves = builder.leavesToDeadLeaves;
        this.iceToPackedIce = builder.iceToPackedIce;
        this.lavaToObsidian = builder.lavaToObsidian;
        this.placeSnowBlock = builder.placeSnowBlock;
        this.grassToPermafrost = builder.grassToPermafrost;
        this.packedIceToBlueIce = builder.packedIceToBlueIce;
    }

    public Integer getApocalypseLevel() {
        return this.apocalypseLevel;
    }

    public int getStartingDay() {
        return this.startingDay;
    }

    public int getWorldUpdateSpeed() {
        return this.worldUpdateSpeed;
    }

    public float getSunSize() {
        return this.sunSize;
    }

    public boolean canMobsSurviveDaylight() {
        return this.canMobsSurviveDaylight;
    }

    public boolean canMobsSpawnDaylight() {
        return this.canMobsSpawnDaylight;
    }

    public boolean canAllBiomesSnow() {
        return this.canAllBiomesSnow;
    }

    public boolean canFreezeEntities() {
        return this.freezeEntities;
    }

    public int getFreezingYLevel() {
        return this.freezingYLevel;
    }

    public int getMinimumHeatSourceDistance() {
        return this.minimumHeatSourceDistance;
    }

    public int getFreezeDamageDelay() {
        return this.freezeDamageDelay;
    }

    public float getFreezeDamage() {
        return this.freezeDamage;
    }

    public boolean canGrassTurnToFrostedGrass() {
        return this.grassToFrostedGrass;
    }

    public boolean canWaterTurnToIce() {
        return this.waterToIce;
    }

    public boolean canPlaceSnow() {
        return this.placeSnow;
    }

    public boolean canPlaceIcicles() {
        return this.placeIcicles;
    }

    public boolean canFrostedGrassTurnToDeadGrass() {
        return this.frostedGrassToDeadGrass;
    }

    public boolean canLeavesTurnToDeadLeaves() {
        return this.leavesToDeadLeaves;
    }

    public boolean canIceTurnToPackedIce() {
        return this.iceToPackedIce;
    }

    public boolean canLavaTurnToObsidian() {
        return this.lavaToObsidian;
    }

    public boolean canPlaceSnowBlock() {
        return this.placeSnowBlock;
    }

    public boolean canGrassTurnToPermafrost() {
        return this.grassToPermafrost;
    }

    public boolean canLeavesDecay() {
        return this.leavesDecay;
    }

    public boolean canPackedIceTurnToBlueIce() {
        return this.packedIceToBlueIce;
    }

    public static class Builder {
        private final Integer apocalypseLevel;
        private final int startingDay;
        private final int worldUpdateSpeed;
        private final float sunSize;
        private boolean canMobsSurviveDaylight;
        private boolean canMobsSpawnDaylight;
        private boolean canAllBiomesSnow;
        private boolean freezeEntities;
        private int freezingYLevel;
        private float freezeDamage;
        private int freezeDamageDelay;
        private int minimumHeatSourceDistance;
        private boolean leavesDecay;
        private boolean grassToFrostedGrass;
        private boolean waterToIce;
        private boolean placeSnow;
        private boolean placeIcicles;
        private boolean frostedGrassToDeadGrass;
        private boolean leavesToDeadLeaves;
        private boolean iceToPackedIce;
        private boolean lavaToObsidian;
        private boolean placeSnowBlock;
        private boolean grassToPermafrost;
        private boolean packedIceToBlueIce;

        public Builder(Integer apocalypseLevel, int startingDay, int worldUpdateSpeed, float sunSize) {
            this.apocalypseLevel = apocalypseLevel;
            this.startingDay = startingDay;
            this.worldUpdateSpeed = worldUpdateSpeed;
            this.sunSize = sunSize;
        }

        public ApocalypseLevel build() {
            return new ApocalypseLevel(this);
        }

        public Builder freezeEntities(int freezingYLevel, int minimumHeatSourceDistance, int freezeDamageDelay, float freezeDamage) {
            this.freezeEntities = true;
            this.freezingYLevel = freezingYLevel;
            this.minimumHeatSourceDistance = minimumHeatSourceDistance;
            this.freezeDamageDelay = freezeDamageDelay;
            this.freezeDamage = freezeDamage;
            return this;
        }

        public Builder mobsCanSurviveDaylight() {
            this.canMobsSurviveDaylight = true;
            return this;
        }

        public Builder mobsCanSpawnDaylight() {
            this.canMobsSpawnDaylight = true;
            return this;
        }

        public Builder allBiomesSnow() {
            this.canAllBiomesSnow = true;
            return this;
        }

        public Builder leavesDecay() {
            this.leavesDecay = true;
            return this;
        }

        public Builder grassToFrostedGrass() {
            this.grassToFrostedGrass = true;
            return this;
        }

        public Builder waterToIce() {
            this.waterToIce = true;
            return this;
        }

        public Builder placeSnow() {
            this.placeSnow = true;
            return this;
        }

        public Builder placeIcicles() {
            this.placeIcicles = true;
            return this;
        }

        public Builder frostedGrassToDeadGrass() {
            this.frostedGrassToDeadGrass = true;
            return this;
        }

        public Builder leavesToDeadLeaves() {
            this.leavesToDeadLeaves = true;
            return this;
        }

        public Builder iceToPackedIce() {
            this.iceToPackedIce = true;
            return this;
        }

        public Builder lavaToObsidian() {
            this.lavaToObsidian = true;
            return this;
        }

        public Builder placeSnowBlock() {
            this.placeSnowBlock = true;
            return this;
        }

        public Builder grassToPermafrost() {
            this.grassToPermafrost = true;
            return this;
        }

        public Builder packedIceToBlueIce() {
            this.packedIceToBlueIce = true;
            return this;
        }
    }
}
