package yeelp.stoicdummy.proxy;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.stoicdummy.StoicDummy;
import yeelp.stoicdummy.entity.EntityStoicDummy;
import yeelp.stoicdummy.item.StoicDummyItem;

public class Proxy {
	
	public static StoicDummyItem dummyItem;

	@SuppressWarnings("static-method")
	public void preInit() {
		dummyItem = new StoicDummyItem();
		ForgeRegistries.ITEMS.register(dummyItem);
		EntityRegistry.registerModEntity(EntityStoicDummy.LOC, EntityStoicDummy.class, "stoicdummy.stoicdummy", 0, StoicDummy.instance, 0, 1, true);
	}
	
	public void init() {
		//empty
	}
	
	public void postInit() {
		//empty
	}
}
