package yeelp.stoicdummy.util;

import java.util.LinkedList;
import java.util.Queue;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import yeelp.stoicdummy.ModConsts.DummyNBT;
import yeelp.stoicdummy.SDLogger;
import yeelp.stoicdummy.config.ModConfig;

public final class DamageHistory extends LinkedList<AbstractDamageInstance> {

	private final Queue<AbstractDamageInstance> delegate;
	
	public DamageHistory() {
		this.delegate = Lists.newLinkedList();
	}

	@Override
	public boolean add(AbstractDamageInstance e) {
		boolean result = this.delegate.add(e);
		if(this.size() > ModConfig.dummy.historyLength) {
			this.poll();
		}
		return result;
	}
	
	public NBTTagList writeToNBT() {
		NBTTagList lst = new NBTTagList();
		this.forEach((instance) -> lst.appendTag(instance.writeToNBT()));
		return lst;
	}
	
	public void readFromNBT(NBTTagList lst) {
		this.clear();
		lst.forEach((nbt) -> {
			NBTTagCompound compound = (NBTTagCompound) nbt;
			byte type = compound.getByte(DummyNBT.TYPE);
			if(type == SimpleDamageInstance.ID) {
				this.add(new SimpleDamageInstance(compound));				
			}
			else {
				SDLogger.err("Invalid damage instance ID! {}", type);				
			}
		});
	}
}
