package com.teamabnormals.endergetic.common.network;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.teamabnormals.endergetic.common.item.BoofloVestItem;
import com.teamabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.endergetic.core.registry.EESoundEvents;
import com.teamabnormals.endergetic.core.registry.other.tags.EEEntityTypeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class C2SInflateBoofloVestMessage {
	private static final String POISE_BUBBLE_ID = "endergetic:short_poise_bubble";
	public static final float HORIZONTAL_BOOST_FORCE = 4.0F;
	public static final float VERTICAL_BOOST_FORCE = 0.75F;
	private static final int DEFAULT_DELAY = 7;
	private static final int DELAY_INCREASE_THRESHOLD = 5;
	private static final int DELAY_MULTIPLIER = 5;

	public static void serialize(C2SInflateBoofloVestMessage message, FriendlyByteBuf buffer) {
	}

	public static C2SInflateBoofloVestMessage deserialize(FriendlyByteBuf buffer) {
		return new C2SInflateBoofloVestMessage();
	}

	public static boolean handle(C2SInflateBoofloVestMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null && !player.isOnGround() && !player.isSpectator()) {
					ItemStack stack = player.getInventory().armor.get(2);
					if (stack.getItem() == EEItems.BOOFLO_VEST.get() && BoofloVestItem.canBoof(stack, player)) {
						CompoundTag tag = stack.getOrCreateTag();
						tag.putBoolean(BoofloVestItem.BOOFED_TAG, true);
						tag.putInt(BoofloVestItem.TICKS_BOOFED_TAG, 0);

						int increment = tag.getInt(BoofloVestItem.TIMES_BOOFED_TAG) + 1;
						tag.putInt(BoofloVestItem.TIMES_BOOFED_TAG, increment);
						player.getCooldowns().addCooldown(EEItems.BOOFLO_VEST.get(), increment < DELAY_INCREASE_THRESHOLD ? DEFAULT_DELAY : DELAY_MULTIPLIER * increment);

						Entity ridingEntity = player.getVehicle();
						for (Entity entity : player.level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(2.0D), entity -> entity != player && entity != ridingEntity && !entity.getType().is(EEEntityTypeTags.BOOF_BLOCK_RESISTANT))) {
							EntityMotionHelper.knockbackEntity(entity, HORIZONTAL_BOOST_FORCE, VERTICAL_BOOST_FORCE, false, false);
						}

						if (ridingEntity != null) {
							EntityMotionHelper.knockbackEntity(ridingEntity, HORIZONTAL_BOOST_FORCE, VERTICAL_BOOST_FORCE, true, false);
						}

						double posX = player.getX();
						double posY = player.getY();
						double posZ = player.getZ();
						RandomSource rand = player.getRandom();
						for (int i = 0; i < 8; i++) {
							double x = posX + makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
							double y = posY + (rand.nextFloat() * 0.05F) + 1.25F;
							double z = posZ + makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
							NetworkUtil.spawnParticle(POISE_BUBBLE_ID, x, y, z, makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F);
						}

						player.level.playSound(null, posX, posY, posZ, EESoundEvents.BOOFLO_VEST_INFLATE.get(), SoundSource.PLAYERS, 1.0F, Mth.clamp(1.3F - (increment * 0.15F), 0.25F, 1.0F));
					}
				}
			});
			return true;
		}
		return false;
	}

	private static double makeNegativeRandomly(double value, RandomSource rand) {
		return rand.nextBoolean() ? -value : value;
	}
}
