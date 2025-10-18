package gunsmith.items.parts.barrels.glaive;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadGlaiveIvy extends HeadGlaive {
    public HeadGlaiveIvy() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadGlaiveIvyTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("attackDamage",27F)
                .setFloat("HeadDamageUpgrade1",70F)
                .setFloat("HeadDamageUpgrade10",100F)
                .setString("BarrelName","Ivy")
                .setString("Type","ModularGlaive")
                .setString("Part","Barrel"));
        return self;
    }
}