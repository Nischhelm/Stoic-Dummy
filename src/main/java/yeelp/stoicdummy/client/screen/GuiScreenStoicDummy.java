package yeelp.stoicdummy.client.screen;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.Container;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.stoicdummy.inventory.ContainerStoicDummy;
import yeelp.stoicdummy.util.StringUtils;

@SideOnly(Side.CLIENT)
public final class GuiScreenStoicDummy extends GuiContainer {
	
	private GuiTextField inputPotion, inputAmp;
	private GuiButton ampUp, ampDown, addPotion;
	private List<GuiRadioButton> creatureAttributeButtons;
	private List<GuiRemovePotionButton> potionRemovalButtons;
	private final ContainerStoicDummy dummy;
	
	private enum CreatureAttributeDisplay {
		DEFAULT(EnumCreatureAttribute.UNDEFINED),
		UNDEAD(EnumCreatureAttribute.UNDEAD),
		ARTHROPOD(EnumCreatureAttribute.ARTHROPOD),
		ILLAGER(EnumCreatureAttribute.ILLAGER);
		
		private final EnumCreatureAttribute mapsTo;
		
		private CreatureAttributeDisplay(EnumCreatureAttribute attribute) {
			this.mapsTo = attribute;
		}
		
		EnumCreatureAttribute getCreatureAttribute() {
			return this.mapsTo;
		}
	}

	public GuiScreenStoicDummy(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.dummy = (ContainerStoicDummy) this.inventorySlots;
		this.potionRemovalButtons = Lists.newArrayList();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int inputPotionY = y - 10; 
		this.inputPotion = new GuiTextField(0, this.fontRenderer, x + 48, inputPotionY, 52, 12);
		this.inputAmp = new GuiTextField(1, this.fontRenderer, this.inputPotion.x + this.inputPotion.width + 6, inputPotionY, 28, 12);
		this.inputAmp.setValidator((s) -> s.chars().allMatch((c) -> Character.isDigit((char) c)));
		
		int buttonId = 0;
		int ampY = (this.inputAmp.height + 2)/2;
		int ampX = this.inputAmp.x + this.inputAmp.width + 1;
		this.ampUp = new GuiButton(buttonId++, ampX, inputPotionY - 1, 10, ampY, "^");
		this.ampDown = new GuiButton(buttonId++, ampX, inputPotionY + ampY - 1, 10, ampY, "V");
		this.addPotion = new GuiButton(buttonId++, ampX + this.ampUp.width, inputPotionY - 1, ampY * 2, ampY * 2, "+");
		this.addPotion.enabled = false;
		this.addButton(this.addPotion);
		this.addButton(this.ampUp);
		this.addButton(this.ampDown);
		this.updateGUI();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_RETURN && (this.inputPotion.isFocused() || this.inputAmp.isFocused())) {
			this.tryAddPotion();
		}
		else if (keyCode == Keyboard.KEY_TAB) {
			if(this.inputPotion.isFocused()) {
				this.inputPotion.setFocused(false);
				this.inputAmp.setFocused(true);
			}
			else if (this.inputAmp.isFocused()) {
				this.inputAmp.setFocused(false);
				this.inputPotion.setFocused(true);
			}
		}
		else if(this.inputPotion.textboxKeyTyped(typedChar, keyCode)) {
			this.dummy.updatePotionToAdd(this.inputPotion.getText());
		}
		else if (this.inputAmp.textboxKeyTyped(typedChar, keyCode)) {
			int newAmp = this.inputAmp.getText().isEmpty() ? 0 : Integer.parseInt(this.inputAmp.getText());
			this.updateAmplifier(newAmp);
		}
		else {
			super.keyTyped(typedChar, keyCode);			
		}
		this.addPotion.enabled = this.dummy.canAddPotionEffect();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.inputPotion.mouseClicked(mouseX, mouseY, mouseButton);
		this.inputAmp.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int buttonClicked = button.id;
		if(buttonClicked == this.ampUp.id) {
			this.updateAmplifier(this.dummy.getAmplifier() + 1);
		}
		else if (buttonClicked == this.ampDown.id) {
			this.updateAmplifier(this.dummy.getAmplifier() - 1);
		}
		else if (buttonClicked == this.addPotion.id) {
			this.tryAddPotion();
		}
		else if(button instanceof GuiRemovePotionButton) {
			this.dummy.removePotionEffect(((GuiRemovePotionButton) button).getPotionEffect().getPotion());
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.inputPotion.drawTextBox();
		this.inputAmp.drawTextBox();
		this.updateGUI();
		this.potionRemovalButtons.forEach((button) -> {
			PotionEffect effect = button.getPotionEffect();
			this.drawString(this.fontRenderer, String.format("%s %s", new TextComponentTranslation(effect.getPotion().getName()).getFormattedText(), StringUtils.convertToRomanNumerals(effect.getAmplifier() + 1)), this.inputPotion.x + GuiRemovePotionButton.BUTTON_WIDTH + 5, button.y + button.height/3, 0xffffffff);
		});
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		return;
	}
	
	private boolean updateGUI() {
		this.potionRemovalButtons.forEach(this.buttonList::remove);
		this.potionRemovalButtons.clear();
		int x = this.inputPotion.x + 3;
		int y = this.inputPotion.y + this.inputPotion.height + 15;
		int id = 100;
		for(PotionEffect effect : this.dummy.getPotionEffects()) {
			if(effect == null) {
				return false;
			}
			GuiRemovePotionButton button = new GuiRemovePotionButton(effect, id++, x, y);
			y += GuiRemovePotionButton.BUTTON_WIDTH + 2;
			this.potionRemovalButtons.add(button);
			this.addButton(button);	
		}
		return true;
	}
	
	private void updateAmplifier(int amp) {
		int cappedAmp = amp > 127 ? 127 : (amp < 0 ? 0 : amp);
		this.inputAmp.setText(String.valueOf(cappedAmp));
		this.dummy.updateAmplifierSelected(cappedAmp);
	}
	
	private void tryAddPotion() {
		if(this.dummy.canAddPotionEffect()) {
			this.dummy.addPotionEffect();
			this.inputPotion.setText("");
			this.inputAmp.setText("0");
			this.addPotion.enabled = false;
		}
	}
	
	private static final class GuiRemovePotionButton extends GuiButton {
		private final PotionEffect effect;
		static final int BUTTON_WIDTH = 15;
		public GuiRemovePotionButton(PotionEffect effect, int id, int x, int y) {
			super(id, x, y, BUTTON_WIDTH, BUTTON_WIDTH, "X");
			this.effect = effect;
		}
		
		PotionEffect getPotionEffect() {
			return this.effect;
		}
	}
		
	private static final class GuiRadioButton extends GuiButton {

		private boolean set = false;
		private final CreatureAttributeDisplay display;
		public GuiRadioButton(CreatureAttributeDisplay display, int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			this.display = display;
		}
		
		void set() {
			this.set = true;
		}
		
		void unset() {
			this.set = false;
		}
		
	}

}
