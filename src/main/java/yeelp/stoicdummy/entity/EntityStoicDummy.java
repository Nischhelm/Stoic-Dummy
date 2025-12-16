package yeelp.stoicdummy.entity;

import java.util.EnumMap;
import java.util.Objects;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import yeelp.stoicdummy.ModConsts;
import yeelp.stoicdummy.ModConsts.DummyNBT;
import yeelp.stoicdummy.SDLogger;
import yeelp.stoicdummy.config.ModConfig;
import yeelp.stoicdummy.proxy.Proxy;

public final class EntityStoicDummy extends EntityLivingBase implements IEntityAdditionalSpawnData {
	public static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "stoicdummy");
	private static final int KILL_IN_VOID_THRESHOLD = -150;
	private EnumHandSide hand;
	private final DummyInventory inventory;
	private int rotationTarget = 0;

	private EntityStoicDummy(World worldIn, EnumHandSide hand, int rotation) {
		super(worldIn);
		this.hand = hand;
		this.rotationYaw = rotation;
		this.prevRotationYaw = rotation;
		this.rotationTarget = rotation;
		this.inventory = new DummyInventory();
	}

	public EntityStoicDummy(World worldIn, EntityPlayer placer, int rotation) {
		this(worldIn, placer.getPrimaryHand(), rotation);
	}

	public EntityStoicDummy(World worldIn) {
		this(worldIn, EnumHandSide.RIGHT, 0);
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
	
	private void setRotation(float rotation) {
		this.rotationTarget = (int) rotation;
		this.rotationYaw = rotation;
		this.rotationYawHead = this.rotationYaw;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationYawHead = this.rotationYaw;
		this.renderYawOffset = this.rotationYaw;
		this.prevRenderYawOffset = this.rotationYaw;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean(DummyNBT.HAND, this.hand == EnumHandSide.RIGHT);
		compound.setInteger(DummyNBT.ROTATION, this.rotationTarget);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.hand = compound.getBoolean(DummyNBT.HAND) ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
		this.setRotation(compound.getInteger(DummyNBT.ROTATION));
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
	public void onEntityUpdate() {
		super.onEntityUpdate();
		this.setRotation(this.rotationTarget);
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);
		// heal after damage dealt to not kill
		this.heal(this.getMaxHealth());
		if(damageSrc == DamageSource.OUT_OF_WORLD && this.posY <= KILL_IN_VOID_THRESHOLD) {
			this.setDead();
		}
		SDLogger.debug("Pos: {}, {}, {}. Rotation: {}, {}, {}", this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationYawHead, this.prevRotationYaw);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if(player.world.isRemote) {
			return false;
		}
		if(player.isSneaking() && player.getHeldItemMainhand().isEmpty() && !this.isDead) {
			this.dropEquipment(false, 0);
			this.entityDropItem(new ItemStack(Proxy.dummyItem), 0.5f);
			this.setDead();
			return true;
		}
		return false;
	}

	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
		this.inventory.values().forEach((item) -> this.entityDropItem(item, 0.0f));
		this.inventory.clear();
	}

	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
		return;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeBoolean(this.rotationTarget != 0);
		buffer.writeShortLE(this.rotationTarget);
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		if(additionalData.readBoolean()) {
			this.setRotation(additionalData.readShortLE());
		}
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
