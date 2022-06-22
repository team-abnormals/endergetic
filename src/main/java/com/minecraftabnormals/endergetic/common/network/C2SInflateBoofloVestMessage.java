package com.minecraftabnormals.endergetic.common.network;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.minecraftabnormals.endergetic.common.items.BoofloVestItem;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public final class C2SInflateBoofloVestMessage {
	private static final String POISE_BUBBLE_ID = "endergetic:short_poise_bubble";
	public static final float HORIZONTAL_BOOST_FORCE = 4.0F;
	public static final float VERTICAL_BOOST_FORCE = 0.75F;
	private static final int DEFAULT_DELAY = 7;
	private static final int DELAY_INCREASE_THRESHOLD = 5;
	private static final int DELAY_MULTIPLIER = 5;

	public static void serialize(C2SInflateBoofloVestMessage message, PacketBuffer buffer) {
	}

	public static C2SInflateBoofloVestMessage deserialize(PacketBuffer buffer) {
		return new C2SInflateBoofloVestMessage();
	}

	public static boolean handle(C2SInflateBoofloVestMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				PlayerEntity player = context.getSender();
				if (player != null && !player.isOnGround() && !player.isSpectator()) {
					ItemStack stack = player.inventory.armor.get(2);
					if (stack.getItem() == EEItems.BOOFLO_VEST.get() && BoofloVestItem.canBoof(stack, player)) {
						CompoundNBT tag = stack.getOrCreateTag();
						tag.putBoolean(BoofloVestItem.BOOFED_TAG, true);
						tag.putInt(BoofloVestItem.TICKS_BOOFED_TAG, 0);

						int increment = tag.getInt(BoofloVestItem.TIMES_BOOFED_TAG) + 1;
						tag.putInt(BoofloVestItem.TIMES_BOOFED_TAG, increment);
						player.getCooldowns().addCooldown(EEItems.BOOFLO_VEST.get(), increment < DELAY_INCREASE_THRESHOLD ? DEFAULT_DELAY : DELAY_MULTIPLIER * increment);

						Entity ridingEntity = player.getVehicle();
						for (Entity entity : player.level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(2.0D), entity -> entity != player && entity != ridingEntity && !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType()))) {
							EntityMotionHelper.knockbackEntity(entity, HORIZONTAL_BOOST_FORCE, VERTICAL_BOOST_FORCE, false, false);
						}

						if (ridingEntity != null) {
							EntityMotionHelper.knockbackEntity(ridingEntity, HORIZONTAL_BOOST_FORCE, VERTICAL_BOOST_FORCE, true, false);
						}

						double posX = player.getX();
						double posY = player.getY();
						double posZ = player.getZ();
						Random rand = player.getRandom();
						for (int i = 0; i < 8; i++) {
							double x = posX + MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
							double y = posY + (rand.nextFloat() * 0.05F) + 1.25F;
							double z = posZ + MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
							NetworkUtil.spawnParticle(POISE_BUBBLE_ID, x, y, z, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F);
						}

						player.level.playSound(null, posX, posY, posZ, EESounds.BOOFLO_VEST_INFLATE.get(), SoundCategory.PLAYERS, 1.0F, MathHelper.clamp(1.3F - (increment * 0.15F), 0.25F, 1.0F));
					}
				}
			});
			return true;
		}
		return false;
	}
}
