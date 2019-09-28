package endergeticexpansion.core.registry;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.boof.TileEntityBoof;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EETileEntities {
	public static final List<TileEntityType<?>> tileEntities = Lists.newArrayList();
	
	public static TileEntityType<TileEntityCorrockCrown> CORROCK_CROWN = registerTileEntity("corrock_crown", TileEntityCorrockCrown::new, EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING, EEBlocks.CORROCK_CROWN_OVERWORLD_WALL, EEBlocks.CORROCK_CROWN_NETHER_STANDING, EEBlocks.CORROCK_CROWN_NETHER_WALL, EEBlocks.CORROCK_CROWN_END_STANDING, EEBlocks.CORROCK_CROWN_END_WALL);
	public static TileEntityType<TileEntityFrisbloomStem> FRISBLOOM_STEM = registerTileEntity("frisbloom_stem", TileEntityFrisbloomStem::new, EEBlocks.FRISBLOOM_STEM);
	
	/*
	 * Poise Forest
	 */
	public static TileEntityType<TileEntityBolloomBud> BOLLOOM_BUD = registerTileEntity("bolloom_bud", TileEntityBolloomBud::new, EEBlocks.BOLLOOM_BUD);
	public static TileEntityType<TileEntityPuffBugHive> PUFFBUG_HIVE = registerTileEntity("puffbug_hive", TileEntityPuffBugHive::new, EEBlocks.PUFFBUG_HIVE);
	public static TileEntityType<TileEntityBoof> BOOF_BLOCK = registerTileEntity("boof_block", TileEntityBoof::new, EEBlocks.BOOF_BLOCK);
	public static TileEntityType<TileEntityDispensedBoof> BOOF_BLOCK_DISPENSED = registerTileEntity("boof_dispensed_block", TileEntityDispensedBoof::new, EEBlocks.BOOF_BLOCK_DISPENSED);
	
	@SuppressWarnings("unchecked")
	private static <T extends TileEntity> TileEntityType<T> registerTileEntity(String name, Supplier<? extends T> factoryIn, Block... validBlocks) {
		TileEntityType<? extends T> builder = TileEntityType.Builder.create(factoryIn, validBlocks).build(null);
		builder.setRegistryName(EndergeticExpansion.MOD_ID, name);
		tileEntities.add(builder);
		return (TileEntityType<T>) builder;
	}
	
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
		for(TileEntityType<?> type : tileEntities) {
			event.getRegistry().register(type);
		}
	}
}