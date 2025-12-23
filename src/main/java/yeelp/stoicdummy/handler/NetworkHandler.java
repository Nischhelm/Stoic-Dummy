package yeelp.stoicdummy.handler;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.stoicdummy.ModConsts;
import yeelp.stoicdummy.network.StoicDummyStatusResponse;
import yeelp.stoicdummy.network.StoicDummyUpdateMessage;
import yeelp.stoicdummy.network.StoicDummyUpdateMessageHandler;

public final class NetworkHandler {

	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(ModConsts.MODID);
	private static int id = 0;
	
	public static void init() {
		INSTANCE.registerMessage(new StoicDummyUpdateMessageHandler(), StoicDummyUpdateMessage.class, id++, Side.SERVER);
		INSTANCE.registerMessage(new StoicDummyStatusResponse.StoicDummyStatusResponseReceiver(), StoicDummyStatusResponse.class, id++, Side.CLIENT);
	}
}
