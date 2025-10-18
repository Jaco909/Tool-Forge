package gunsmith.items.parts.barrels.glaive;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadGlaiveRunestone extends HeadGlaive {
    public HeadGlaiveRunestone() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadGlaiveRunestoneTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("attackDamage",18F)
                .setFloat("HeadDamageUpgrade1",76F)
                .setFloat("HeadDamageUpgrade10",100F)
                .setString("BarrelName","Runestone")
                .setString("Type","ModularGlaive")
                .setString("Part","Barrel"));
        return self;
    }
}