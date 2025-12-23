package yeelp.stoicdummy.network;

import net.minecraft.nbt.NBTTagCompound;

public class StoicDummyEmptyMessage extends StoicDummyMessageContents {

	@Override
	NBTTagCompound writeMessageContents() {
		return new NBTTagCompound();
	}

}
