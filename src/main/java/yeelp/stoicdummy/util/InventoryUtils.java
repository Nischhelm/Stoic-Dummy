package yeelp.stoicdummy.util;

import java.util.Arrays;
import java.util.Optional;

import net.minecraft.inventory.EntityEquipmentSlot;

public final class InventoryUtils {

	private InventoryUtils() {
		throw new RuntimeException("Class is not to be instantiated.");
	}
	
	public static Optional<EntityEquipmentSlot> getSlotFromSlotIndex(int index) {
		return Arrays.stream(EntityEquipmentSlot.values()).filter((slot) -> slot.getSlotIndex() == index).findFirst();
	}
}
