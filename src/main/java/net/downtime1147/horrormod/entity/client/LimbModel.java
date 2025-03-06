package net.downtime1147.horrormod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.downtime1147.horrormod.entity.animations.ModAnimationDefinitions;
import net.downtime1147.horrormod.entity.custom.LimbEntity;


public class LimbModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart limb;
	private final ModelPart root;
	private final ModelPart base_lower;
	private final ModelPart base_upper;
	private final ModelPart tip_lower;
	private final ModelPart tip_upper;

	public LimbModel(ModelPart root) {
		this.limb = root.getChild("limb");
		this.root = this.limb.getChild("root");
		this.base_lower = this.root.getChild("base_lower");
		this.base_upper = this.base_lower.getChild("base_upper");
		this.tip_lower = this.base_upper.getChild("tip_lower");
		this.tip_upper = this.tip_lower.getChild("tip_upper");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition limb = partdefinition.addOrReplaceChild("limb", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition root = limb.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -17.0F, -5.0F, 10.0F, 17.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition base_lower = root.addOrReplaceChild("base_lower", CubeListBuilder.create().texOffs(0, 28).addBox(-4.0F, -13.0F, -4.0F, 8.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 0.0F));

		PartDefinition base_upper = base_lower.addOrReplaceChild("base_upper", CubeListBuilder.create().texOffs(33, 28).addBox(-3.0F, -14.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition tip_lower = base_upper.addOrReplaceChild("tip_lower", CubeListBuilder.create().texOffs(41, 0).addBox(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 0.0F));

		PartDefinition tip_upper = tip_lower.addOrReplaceChild("tip_upper", CubeListBuilder.create().texOffs(33, 49).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root.getAllParts().forEach(ModelPart::resetPose);

		this.animate(((LimbEntity) entity).idleAnimationState, ModAnimationDefinitions.idle, ageInTicks, 1f);
		this.animate(((LimbEntity) entity).retractAnimationState, ModAnimationDefinitions.retract, ageInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		limb.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return limb;
	}
}