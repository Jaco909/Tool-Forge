package gunsmith.items.parts.barrels.glaive;

import gunsmith.items.parts.barrels.pickaxe.HeadPickaxe;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadGlaiveGold extends HeadGlaive {
    public HeadGlaiveGold() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadGlaiveGoldTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("attackDamage",18F)
                .setFloat("HeadDamageUpgrade1",70F)
                .setFloat("HeadDamageUpgrade10",100F)
                .setString("BarrelName","Gold")
                .setString("Type","ModularGlaive")
                .setString("Part","Barrel"));
        return self;
    }
}