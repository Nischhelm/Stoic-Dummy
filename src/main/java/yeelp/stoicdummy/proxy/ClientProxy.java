package yeelp.stoicdummy.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import yeelp.stoicdummy.entity.EntityStoicDummy;
import yeelp.stoicdummy.render.entity.RenderStoicDummy;

public final class ClientProxy extends Proxy {
	
	@Override
	public void preInit() {
		super.preInit();
		ModelLoader.setCustomModelResourceLocation(Proxy.dummyItem, 0, new ModelResourceLocation(Proxy.dummyItem.getRegistryName(), "inventory"));
		RenderingRegistry.registerEntityRenderingHandler(EntityStoicDummy.class, RenderStoicDummy::new);
	}
}
