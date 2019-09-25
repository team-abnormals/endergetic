package endergeticexpansion.common.world;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraft.world.server.ServerWorld;

public class EndergeticEndDimension extends EndDimension {

	public EndergeticEndDimension(World worldIn, DimensionType typeIn) {
		super(worldIn, typeIn);
		CompoundNBT compoundnbt = worldIn.getWorldInfo().getDimensionData(typeIn);
		this.dragonFightManager = worldIn instanceof ServerWorld ? new EndergeticDragonFightManager((ServerWorld)worldIn, compoundnbt.getCompound("DragonFight"), this) : null;
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		EndGenerationSettings endgenerationsettings = ChunkGeneratorType.FLOATING_ISLANDS.createSettings();
		endgenerationsettings.setDefaultBlock(Blocks.END_STONE.getDefaultState());
		endgenerationsettings.setDefaultFluid(Blocks.AIR.getDefaultState());
		endgenerationsettings.setSpawnPos(this.getSpawnCoordinate());
		return ChunkGeneratorType.FLOATING_ISLANDS.create(this.world, EndOverrideHandler.ENDERGETIC_END.create(BiomeProviderType.THE_END.createSettings().setSeed(this.world.getSeed())), endgenerationsettings);
	}
	
}
