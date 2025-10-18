package gunsmith.items.parts.barrels.glaive;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadGlaiveWood extends HeadGlaive {
    public HeadGlaiveWood() {
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadGlaiveWoodTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("attackDamage",12F)
                .setFloat("attackSpeedMod",0.83F)
                .setFloat("HeadDamageUpgrade1",62F)
                .setFloat("HeadDamageUpgrade10",100F)
                .setString("BarrelName","Wood")
                .setString("Type","ModularGlaive")
                .setString("Part","Barrel"));
        return self;
    }
}