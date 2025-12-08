package yeelp.stoicdummy.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import yeelp.stoicdummy.ModConsts;

@Config(modid = ModConsts.MODID)
public final class ModConfig {

	@Name("Enable Debug mode")
	@Comment("Set to true to enable debug mode, where StoicDummy will add additional information to the console.")
	public static boolean debug = false;
}
