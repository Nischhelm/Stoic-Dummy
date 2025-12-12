package yeelp.stoicdummy.entity;

import java.util.EnumMap;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import yeelp.stoicdummy.ModConsts;
import yeelp.stoicdummy.config.ModConfig;

public final class EntityStoicDummy extends EntityLivingBase {

	public static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "stoicdummy");
	private final EnumHandSide hand;
	private final DummyInventory inventory;
	
	private EntityStoicDummy(World worldIn, EnumHandSide hand) {
		super(worldIn);
		this.hand = hand;
		this.inventory = new DummyInventory();
	}
	
	public EntityStoicDummy(World worldIn, EntityPlayer placer) {
		this(worldIn, placer.getPrimaryHand());
	}
	
	public EntityStoicDummy(World worldIn) {
		this(worldIn, EnumHandSide.RIGHT);
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return this.inventory.getArmorInventory();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return this.inventory.get(slotIn);
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return this.hand;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		this.inventory.put(slotIn, stack);
	}
	
	@Override
	protected void damageArmor(float damage) {
		if(!ModConfig.dummy.damageArmor) {
			return;
		}
		int damageDealt = (int) Math.max(1.0f, damage / 4.0f);
		this.getArmorInventoryList().forEach((s) -> s.damageItem(damageDealt, this));
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);
		//heal after damage dealt to not kill
		this.heal(this.getMaxHealth());
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if(player.isSneaking() && player.getHeldItemMainhand().isEmpty()) {
			this.dropEquipment(false, 0);
			return true;
		}
		return false;
	}
	
	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
		this.inventory.values().forEach((item) -> this.entityDropItem(item, 0.0f));
		this.inventory.clear();
	}

	private final class DummyInventory extends EnumMap<EntityEquipmentSlot, ItemStack> {
		DummyInventory() {
			super(EntityEquipmentSlot.class);
			for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
				this.put(slot, ItemStack.EMPTY);
			}
		}
		
		Iterable<ItemStack> getArmorInventory() {
			return this.entrySet().stream().filter((e) -> e.getKey().getSlotType() == EntityEquipmentSlot.Type.ARMOR).map(Entry::getValue).collect(Collectors.toList());
		}
		
		@Override
		public ItemStack put(EntityEquipmentSlot key, ItemStack value) {
			return super.put(key, Objects.requireNonNull(value));
		}
		
		@Override
		public void clear() {
			this.replaceAll((slot, stack) -> ItemStack.EMPTY);
		}
	}
}
