package gunsmith.items.tools;

import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.projectileToolItem.gunProjectileToolItem.GunProjectileToolItem;
import necesse.inventory.lootTable.presets.GunWeaponsLootTable;
import necesse.level.maps.Level;

public class ModularGun extends GunProjectileToolItem {
    public ModularGun() {
        super(NORMAL_AMMO_TYPES, 1000, null);
        this.stackSize = 1;
        this.canBeUsedForRaids = false;
        this.useForRaidsOnlyIfObtained = false;
        this.addGlobalIngredient(new String[]{"bulletuser"});
    }
    public int getAvailableAmmoBurst(ItemAttackerMob attackerMob, int bullets) {
        return bullets;
    }
    public InventoryItem superOnAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem", "BodyMachinegunBasic")).getDefaultItem(null, 1);
        if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Burst")) {
            return super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        } else if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Ripper")) {
            return super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        } else if (bodyItem.getGndData().getString("BodyName").equalsIgnoreCase("Economic")) {
            return super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        } else {
            return null;
        }
    }
}
