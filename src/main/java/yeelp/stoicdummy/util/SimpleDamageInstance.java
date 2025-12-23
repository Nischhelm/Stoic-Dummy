package yeelp.stoicdummy.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import yeelp.stoicdummy.ModConsts.DummyNBT;
import yeelp.stoicdummy.ModConsts.TranslationKeys;
import yeelp.stoicdummy.util.Translations.Translator;

public final class SimpleDamageInstance extends AbstractDamageInstance {

	public static final byte ID = (byte) 1;
	private static final Translator TRANSLATOR = Translations.INSTANCE.getTranslator(TranslationKeys.HISTORY_ROOT);
	private static final ITextComponent ATTACKER = TRANSLATOR.getComponent(TranslationKeys.ATTACKER);
	private static final ITextComponent TRUE_ATTACKER = TRANSLATOR.getComponent(TranslationKeys.TRUE_ATTACKER);
	private static final ITextComponent SOURCE = TRANSLATOR.getComponent(TranslationKeys.SOURCE);
	private static final ITextComponent DAMAGE_DEALT = TRANSLATOR.getComponent(TranslationKeys.DAMAGE_DEALT);
	private static final ITextComponent DAMAGE_TAKEN = TRANSLATOR.getComponent(TranslationKeys.DAMAGE_TAKEN);
	
	private SimpleDamageInstance(DamageSource src, float initialDamage, float finalDamage) {
		super(src, initialDamage, finalDamage);
	}
	
	public SimpleDamageInstance(NBTTagCompound nbt) {
		super(nbt);
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s %s, %s %d, %s %d", this.getAttackerString(), SOURCE.getFormattedText(), this.getSource(), DAMAGE_DEALT.getFormattedText(), this.getInitialDamage(), DAMAGE_TAKEN.getFormattedText(), this.getFinalDamage());
	}
	
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setByte(DummyNBT.TYPE, ID);
		compound.setString(DummyNBT.ATTACKER, this.getImmediateAttacker());
		compound.setString(DummyNBT.TRUE_ATTACKER, this.getTrueAttacker());
		compound.setString(DummyNBT.SOURCE, this.getSource());
		compound.setFloat(DummyNBT.INITIAL_AMOUNT, this.getInitialDamage());
		compound.setFloat(DummyNBT.FINAL_AMOUNT, this.getFinalDamage());
		return compound;
	}
	
	private String getAttackerString() {
		if(this.getImmediateAttacker().equals(this.getTrueAttacker())) {
			return String.format("%s %s", ATTACKER.getFormattedText(), this.getImmediateAttacker());
		}
		return String.format("%s %s, %s %s", ATTACKER.getFormattedText(), this.getImmediateAttacker(), TRUE_ATTACKER.getFormattedText(), this.getTrueAttacker());
	}
	
	public static final class SimpleDamageInstanceBuilder extends AbstractDamageInstanceBuilder {
		public SimpleDamageInstanceBuilder(DamageSource src, float initialDamage) {
			super(src, initialDamage);
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		public AbstractDamageInstance build() {
			return new SimpleDamageInstance(this.getSource(), this.getInitialDamage(), this.getFinalDamage());
		}
	}
}
