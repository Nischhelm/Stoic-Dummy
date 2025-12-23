package yeelp.stoicdummy.util;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import yeelp.stoicdummy.ModConsts.DummyNBT;

public abstract class AbstractDamageInstance {

	private final String attacker, trueAttacker, source;
	private final float amountBefore, amountAfter;
	
	protected AbstractDamageInstance(DamageSource src, float amountBefore, float amountAfter) {
		this.attacker = mapIfNonNullElseGetDefault(src.getImmediateSource(), Entity::getName, "null");
		this.trueAttacker = mapIfNonNullElseGetDefault(src.getTrueSource(), Entity::getName, "null");
		this.source = src.damageType;
		this.amountBefore = amountBefore;
		this.amountAfter = amountAfter;
	}
	
	protected AbstractDamageInstance(NBTTagCompound nbt) {
		this.attacker = nbt.getString(DummyNBT.ATTACKER);
		this.trueAttacker = nbt.getString(DummyNBT.TRUE_ATTACKER);
		this.source = nbt.getString(DummyNBT.SOURCE);
		this.amountBefore = nbt.getFloat(DummyNBT.INITIAL_AMOUNT);
		this.amountAfter = nbt.getFloat(DummyNBT.FINAL_AMOUNT);
	}
	
	public String getSource() {
		return this.source;
	}
	
	public String getImmediateAttacker() {
		return this.attacker;
	}
	
	public String getTrueAttacker() {
		return this.trueAttacker;
	}
	
	public float getInitialDamage() {
		return this.amountBefore;
	}
	
	public float getFinalDamage() {
		return this.amountAfter;
	}
	
	protected static final <T, U> U mapIfNonNullElseGetDefault(T t, Function<T, U> function, U backup) {
		if(t != null) {
			return function.apply(t);
		}
		return backup;
	}
	
	public abstract NBTTagCompound writeToNBT();
	
	@Override
	public abstract String toString();
	
	public static abstract class AbstractDamageInstanceBuilder {
		private final DamageSource src;
		private final float amountBefore;
		private float amountAfter;
		
		protected AbstractDamageInstanceBuilder(DamageSource src, float amountBefore) {
			this.src = src;
			this.amountBefore = amountBefore;
		}
		
		public final void setFinalDamageTaken(float amount) {
			this.amountAfter = amount;
		}
		
		protected final DamageSource getSource() {
			return this.src;
		}
		
		protected final float getInitialDamage() {
			return this.amountBefore;
		}
		
		protected final float getFinalDamage() {
			return this.amountAfter;
		}
		
		public abstract AbstractDamageInstance build();
	}
}
