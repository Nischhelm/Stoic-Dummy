package yeelp.stoicdummy.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

public final class DummyCategory {
	
	@Name("Allow Armor Damage")
	@Comment("If set to true, armor the Stoic Dummy wears will be damaged when being attacked.")
	public boolean damageArmor = true;

}
