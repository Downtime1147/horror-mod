package net.downtime1147.horrormod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.downtime1147.horrormod.HorrorMod;
import net.downtime1147.horrormod.entity.custom.LimbEntity;

public class LimbRenderer extends MobRenderer<LimbEntity, LimbModel<LimbEntity>> {
    public LimbRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LimbModel<>(pContext.bakeLayer(ModModelLayers.LIMB_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LimbEntity limbEntity) {
        return new ResourceLocation(HorrorMod.MOD_ID, "textures/entity/limb.png");
    }

    @Override
    public void render(LimbEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
