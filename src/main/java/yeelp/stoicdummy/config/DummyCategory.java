package yeelp.stoicdummy.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public final class DummyCategory {
	
	@Name("Allow Armor Damage")
	@Comment("If set to true, armor the Stoic Dummy wears will be damaged when being attacked.")
	public boolean damageArmor = true;
	
	@Name("History Length")
	@Comment("The Stoic Dummy will remember this damage information for the N most recent times it took damage. N is this value defined here.")
	@RangeInt(min = 1)
	public int historyLength = 5;

}
