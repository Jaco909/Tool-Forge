package gunsmith.items.parts.stocks.smg;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class StockMachinegunPristine extends StockMachinegun {
    private final float damage;
    private final float attackSpeed;

    public StockMachinegunPristine() {
        this.damage = 10F;
        this.attackSpeed = 5F;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockMachinegunPristineTip"));
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("StockItem",this.getStringID())
                .setFloat("Damage",13F)
                .setFloat("AttackSpeed",1.94F)
                .setFloat("StockDamageUpgrade1",42F)
                .setFloat("StockDamageUpgrade10",56F)
                .setString("Name","Pristine")
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