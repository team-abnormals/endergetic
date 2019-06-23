package endergeticexpansion.core.registry;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import net.minecraft.tileentity.TileEntityType;

/*
 * Holds a list of all the TileEntities
 */
public class EETileEntities {

	public static TileEntityType<TileEntityCorrockCrown> CORROCK_CROWN;
	public static TileEntityType<TileEntityFrisbloomStem> FRISBLOOM_STEM;
	
	/*
	 * Poise Forest
	 */
	public static TileEntityType<TileEntityBolloomBud> BOLLOOM_BUD;
	public static TileEntityType<TileEntityPuffBugHive> PUFFBUG_HIVE;
	
}
