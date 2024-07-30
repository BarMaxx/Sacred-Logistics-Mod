package ru.barmaxx.sacredlogistics.entities

import com.mojang.blaze3d.Blaze3D
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Quaternion
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.resources.ResourceLocation
import ru.barmaxx.sacredlogistics.SacredLogistics
import ru.barmaxx.sacredlogistics.registry.SacredBlocks
import ru.hollowhorizon.hc.client.utils.rl
import ru.hollowhorizon.hc.common.effects.ParticleEmitterInfo
import ru.hollowhorizon.hc.common.effects.ParticleHelper

class MeteoriteRenderer(p_174008_: EntityRendererProvider.Context) : EntityRenderer<MeteoriteEntity>(p_174008_) {
    override fun getTextureLocation(p0: MeteoriteEntity): ResourceLocation {
        return TextureManager.INTENTIONAL_MISSING_TEXTURE
    }

    override fun render(
        entity: MeteoriteEntity,
        p_114486_: Float,
        partialTick: Float,
        stack: PoseStack,
        source: MultiBufferSource,
        light: Int,
    ) {
        stack.pushPose()
        val time = Blaze3D.getTime().toFloat()
        val scale = 5f
        stack.scale(scale, scale, scale)
        stack.mulPose(
            Quaternion.fromXYZ(
                time * 2.3f,
                time,
                time * 0.3f
            )
        )
        stack.translate(-0.5, -0.5, -0.5)
        Minecraft.getInstance().blockRenderer
            .renderSingleBlock(
                SacredBlocks.METEORITE_ORE.get().defaultBlockState(),
                stack,
                source,
                light,
                OverlayTexture.NO_OVERLAY
            )
        if(entity.tickCount % 50 == 0) ParticleHelper.addParticle(entity.level, ParticleEmitterInfo("${SacredLogistics.MODID}:meteorite".rl).apply {
            scale(0.10f*scale)
            bindOnEntity(entity)
        })
        stack.popPose()
    }

    override fun shouldRender(
        p_114491_: MeteoriteEntity,
        p_114492_: Frustum,
        p_114493_: Double,
        p_114494_: Double,
        p_114495_: Double
    ) = true
}