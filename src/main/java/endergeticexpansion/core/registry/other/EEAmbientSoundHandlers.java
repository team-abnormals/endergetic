package endergeticexpansion.core.registry.other;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author SmellyModder(Luke Tonon)
 */
@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public class EEAmbientSoundHandlers {
	private static final List<IAmbientSoundHandler> SOUND_HANDLERS = Lists.newArrayList();
	
	static {
		registerBiomeAmbientSoundPlayer(() -> EEBiomes.POISE_FOREST.get(), SoundEvents.AMBIENT_UNDERWATER_LOOP, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE);
	}
	
	/**
	 * Registers an BiomeAmbientSoundPlayer for a specific biome
	 * For more info on the parameters visit {@link https://minecraft.gamepedia.com/Ambience}
	 * @param biome - Biome to play the ambiance in
	 * @param loopSound - The looped sound for the biome
	 * @param additionSound - The common ambient sound(s) for the biome
	 * @param moodSound - The rare/long(plays every 6000-17999 ticks) ambient sound(s) for the biome
	 */
	private static void registerBiomeAmbientSoundPlayer(Supplier<Biome> biome, SoundEvent loopSound, SoundEvent additionSound, SoundEvent moodSound) {
		SOUND_HANDLERS.add(new BiomeAmbientSoundPlayer(biome, loopSound, additionSound, moodSound));
	}
	
	@SubscribeEvent
	public static void onClientPlayerTick(ClientTickEvent event) {
		if(event.phase == Phase.START) {
			Minecraft mc = Minecraft.getInstance();
			ClientPlayerEntity player = mc.player;
			SoundHandler soundHandler = mc.getSoundHandler();
			SOUND_HANDLERS.forEach((ambientSoundHandler) -> {
				if(player != null && player.canUpdate()) {
					ambientSoundHandler.tickMainAmbience(player, soundHandler);
					ambientSoundHandler.tickAdditions(player, soundHandler);
				}
			});
		}
	}
	
	private static class BiomeAmbientSoundPlayer implements IAmbientSoundHandler {
		private final Supplier<Biome> biomeToPlayIn;
		private final SoundEvent[] soundsToPlay = new SoundEvent[3];
		private int delay = 0;
		private int ticksInBiome;
		private int ticksTillNextMood = this.generateTicksTillNextMood();
		private boolean isInBiome;
		
		public BiomeAmbientSoundPlayer(Supplier<Biome> biomeToPlayIn, SoundEvent loopSound, SoundEvent additionSound, SoundEvent moodSound) {
			this.biomeToPlayIn = biomeToPlayIn;
			this.soundsToPlay[0] = loopSound;
			this.soundsToPlay[1] = additionSound;
			this.soundsToPlay[2] = moodSound;
		}
		
		@Override
		public void tickMainAmbience(ClientPlayerEntity player, SoundHandler soundHandler) {
			boolean wasInBiome = this.isInBiome;
			boolean isInBiome = this.updateIsInBiome(player);
			
			if(!wasInBiome && isInBiome) {
				soundHandler.play(new BiomeAmbienceSound(player, this.soundsToPlay[0], this.getBiome()));
			}
		}
		
		@Override
		public void tickAdditions(ClientPlayerEntity player, SoundHandler soundHandler) {
			this.delay--;
			if(this.isInBiome(player)) {
				this.ticksInBiome++;
				
				if(this.delay <= 0 && !Minecraft.getInstance().isGamePaused()) {
					if(player.getRNG().nextFloat() < 0.01F) {
						soundHandler.play(new BiomeAmbienceAdditionSound(player, this.soundsToPlay[1]));
						this.delay = 10;
					}
					
					if(this.ticksTillNextMood <= this.ticksInBiome) {
						soundHandler.play(new BiomeAmbienceAdditionSound(player, this.soundsToPlay[2]));
						this.delay = 20;
						this.ticksTillNextMood = this.generateTicksTillNextMood();
					}
				}
			} else {
				this.ticksInBiome = 0;
			}
		}
		
		@Override
		public Biome getBiome() {
			return this.biomeToPlayIn.get();
		}
		
		private boolean updateIsInBiome(ClientPlayerEntity player) {
			this.isInBiome = this.isInBiome(player);
			return this.isInBiome;
		}
		
		private int generateTicksTillNextMood() {
			return (new Random()).nextInt(12000) + 6000;
		}
		
		class BiomeAmbienceSound extends TickableSound {
			private final ClientPlayerEntity player;
			private final Biome biome;
			private int ticksInBiome;

			public BiomeAmbienceSound(ClientPlayerEntity player, SoundEvent sound, Biome biome) {
				super(sound, SoundCategory.AMBIENT);
				this.player = player;
				this.biome = biome;
				this.repeat = true;
				this.repeatDelay = 0;
				this.volume = 1.0F;
				this.priority = true;
				this.global = true;
			}

			public void tick() {
				if(this.player.isAlive() && this.ticksInBiome >= 0) {
					BlockPos pos = this.player.getPosition();
					if(this.player.world.isAreaLoaded(pos, 1) && this.player.world.getBiome(pos) == this.biome) {
						this.ticksInBiome++;
					} else {
						this.ticksInBiome--;
					}
					
					this.ticksInBiome = Math.min(this.ticksInBiome, 40);
					this.volume = MathHelper.clamp((float) this.ticksInBiome / 40.0F, 0.0F, 1.0F);
				} else {
					this.donePlaying = true;
				}
			}
		}
		
		class BiomeAmbienceAdditionSound extends TickableSound {
			private final ClientPlayerEntity player;

			BiomeAmbienceAdditionSound(ClientPlayerEntity player, SoundEvent sound) {
				super(sound, SoundCategory.AMBIENT);
				this.player = player;
				this.repeat = false;
				this.repeatDelay = 0;
				this.volume = 1.0F;
				this.priority = true;
				this.global = true;
			}
			
			@Override
			public void tick() {
				if(!this.player.isAlive() || this.player.dimension != DimensionType.THE_END) this.donePlaying = true;
			}
		}
	}
	
	private interface IAmbientSoundHandler {
		public void tickMainAmbience(ClientPlayerEntity player, SoundHandler soundHandler);
		
		public void tickAdditions(ClientPlayerEntity player, SoundHandler soundHandler);
		
		Biome getBiome();
		
		default boolean isInBiome(ClientPlayerEntity player) {
			return player.world.getBiome(player.getPosition()) == this.getBiome();
		}
	}
}