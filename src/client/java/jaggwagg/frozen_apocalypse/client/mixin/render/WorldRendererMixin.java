package jaggwagg.frozen_apocalypse.client.mixin.render;

import jaggwagg.frozen_apocalypse.FrozenApocalypse;
import jaggwagg.frozen_apocalypse.client.FrozenApocalypseClient;
import jaggwagg.frozen_apocalypse.config.ApocalypseLevel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Unique
    private Matrix4f sunMatrixCopy;

    @ModifyVariable(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
                    ordinal = 0),
            ordinal = 1)
    private Matrix4f scaleSun(Matrix4f in) {
        Matrix4f copy = new Matrix4f(in);
        sunMatrixCopy = new Matrix4f(copy);

        if (FrozenApocalypse.CONFIG.isFrozenApocalypseEnabled() && FrozenApocalypse.CONFIG.isSunSizeChangesEnabled()) {
            for (ApocalypseLevel apocalypseLevel : FrozenApocalypse.CONFIG.getApocalypseLevels()) {
                if (FrozenApocalypseClient.frozenApocalypseLevelClient == apocalypseLevel.getApocalypseLevel()) {
                    return copy.scale(apocalypseLevel.getSunSize(), 1.0f, apocalypseLevel.getSunSize());
                }
            }
        }

        return copy;
    }

    @ModifyVariable(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
                    ordinal = 1),
            ordinal = 1)
    private Matrix4f fixMoonAndStars(Matrix4f in) {
        Matrix4f copy = new Matrix4f(sunMatrixCopy);

        sunMatrixCopy = null;

        return copy;
    }
}
