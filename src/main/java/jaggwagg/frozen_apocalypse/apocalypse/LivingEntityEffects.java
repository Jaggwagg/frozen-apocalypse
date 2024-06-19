package jaggwagg.frozen_apocalypse.apocalypse;

import jaggwagg.frozen_apocalypse.FrozenApocalypse;
import jaggwagg.frozen_apocalypse.config.MinecraftIdentifier;
import jaggwagg.frozen_apocalypse.entity.effect.ModStatusEffects;
import jaggwagg.frozen_apocalypse.item.ThermalArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class LivingEntityEffects {
    public static void freezeLivingEntity(World world, LivingEntity livingEntity) {
        if (!LivingEntityEffects.isNearHeatSource(world, livingEntity)) {
            if (FrozenApocalypse.apocalypseLevel.canFreezeEntities() && livingEntity.getY() >= FrozenApocalypse.apocalypseLevel.getFreezingYLevel()) {
                float freezeDamage = FrozenApocalypse.apocalypseLevel.getFreezeDamage();
                int freezeDamageDelay = FrozenApocalypse.apocalypseLevel.getFreezeDamageDelay();

                if (!livingEntity.inPowderSnow) {
                    livingEntity.setInPowderSnow(true);
                }

                if (!world.isClient) {
                    livingEntity.setOnFire(false);
                }

                if (freezeDamageDelay > 0 && freezeDamage > 0.0f) {
                    if (world.getRandom().nextInt(freezeDamageDelay + 1) == 1) {
                        livingEntity.damage(world.getDamageSources().freeze(), freezeDamage);
                    }
                }
            }
        }
    }

    public static boolean isNearHeatSource(World world, LivingEntity livingEntity) {
        int lightLevel = world.getLightLevel(LightType.BLOCK, livingEntity.getBlockPos());
        int lightLevelUp = world.getLightLevel(LightType.BLOCK, livingEntity.getBlockPos().up());
        int higherLightLevel = Math.max(lightLevel, lightLevelUp);

        return higherLightLevel > FrozenApocalypse.apocalypseLevel.getMinimumHeatSourceDistance();

    }

    public static boolean shouldSkipFreezingChecks(World world, LivingEntity livingEntity) {
        return AllEffects.isSafeDimension(world) ||
                isImmuneToFreezing(livingEntity) ||
                isFreezingImmuneEntity(livingEntity) ||
                isNearHeatSource(world, livingEntity);
    }

    public static boolean isImmuneToFreezing(LivingEntity livingEntity) {
        return !FrozenApocalypse.CONFIG.isFrozenApocalypseEnabled() ||
                livingEntity.hasStatusEffect(ModStatusEffects.FROST_RESISTANCE.getStatusEffect()) ||
                isCreativeOrSpectator(livingEntity) ||
                isWearingThermalArmor(livingEntity);
    }

    public static boolean isWearingThermalArmor(LivingEntity livingEntity) {
        return ThermalArmorItem.wearingThermalArmor(livingEntity);
    }

    public static boolean isCreativeOrSpectator(LivingEntity livingEntity) {
        return livingEntity instanceof PlayerEntity playerEntity &&
                (playerEntity.isCreative() || playerEntity.isSpectator());
    }

    public static boolean isFreezingImmuneEntity(LivingEntity livingEntity) {
        String livingEntityId = String.valueOf(Registries.ENTITY_TYPE.getId(livingEntity.getType()));

        for (MinecraftIdentifier freezingImmuneEntity : FrozenApocalypse.CONFIG.getFreezingImmuneEntities()) {
            if (livingEntityId.equals(freezingImmuneEntity.getId())) {
                return true;
            }
        }

        return false;
    }
}
