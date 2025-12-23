package yeelp.stoicdummy.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.stoicdummy.entity.EntityStoicDummy;

public class StoicDummyUpdateMessage implements IMessage {

	private MessageType type;
	private int dummyId;
	private StoicDummyMessageContents contents;
	
	public StoicDummyUpdateMessage() {
		//empty. public because FML needs it
	}
	
	StoicDummyUpdateMessage(MessageType type, EntityStoicDummy dummy, StoicDummyMessageContents contents) {
		this.type = type;
		this.dummyId = dummy.getEntityId();
		this.contents = contents;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		this.type = MessageType.decodeType(pbuf.readByte());
		this.dummyId = pbuf.readInt();
		try {
			this.contents = this.type.decodeMessageContents(pbuf.readCompoundTag());			
		}
		catch(IOException e) {
			throw new RuntimeException("Unable to read NBTCompoundTag", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pbuf = new PacketBuffer(buf);
		pbuf.writeByte(this.type.encodeType());
		pbuf.writeInt(this.dummyId);
		pbuf.writeCompoundTag(this.contents.writeMessageContents());
	}
	
	MessageType getMessageType() {
		return this.type;
	}
	
	int getDummyID() {
		return this.dummyId;
	}
	
	StoicDummyMessageContents getMessageContents() {
		return this.contents;
	}
}
