package gunsmith.items.parts.stocks.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockMachinegunVoid extends StockMachinegun {
    private final float damage;
    private final float attackSpeed;

    public StockMachinegunVoid() {
        this.damage = 5F;
        this.attackSpeed = 5F;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockMachinegunVoidTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("Damage",6F)
                .setFloat("AttackSpeed",1.71F)
                .setFloat("AmmoUseAdd",-0.10F)
                .setFloat("StockDamageUpgrade1",42F)
                .setFloat("StockDamageUpgrade10",56F)
                .setString("Name","Void")
                .setString("Type","ModularSMG")
                .setString("Part","Stock"));
        return self;
    }

    public float getDamage() {
        return this.damage;
    }
    public float getAttackSpeed() {
        return this.attackSpeed;
    }
}