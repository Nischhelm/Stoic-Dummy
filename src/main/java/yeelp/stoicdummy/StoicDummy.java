package yeelp.stoicdummy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.stoicdummy.entity.EntityStoicDummy;
import yeelp.stoicdummy.proxy.Proxy;

@Mod(modid = ModConsts.MODID, name = ModConsts.NAME, version = ModConsts.VERSION)
public final class StoicDummy {

	@Instance
	public static StoicDummy instance;
	
	@SidedProxy(clientSide = ModConsts.CLIENT_PROXY, serverSide = ModConsts.SERVER_PROXY)
	public static Proxy proxy;
	
	@SuppressWarnings("static-method")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SDLogger.init(event.getModLog());
		SDLogger.info("Stoic Dummy is version: {}", ModConsts.VERSION);
		SDLogger.debug("debug mode enabled");
		proxy.preInit();
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void init(@SuppressWarnings("unused") FMLInitializationEvent event) {
		proxy.init();
	}
	
	@SuppressWarnings("static-method")
	@EventHandler
	public void postInit(@SuppressWarnings("unused") FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
