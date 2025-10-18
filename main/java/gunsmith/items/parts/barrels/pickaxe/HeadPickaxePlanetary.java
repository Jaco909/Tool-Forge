package gunsmith.items.parts.barrels.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.InventoryItem;

public class HeadPickaxePlanetary extends HeadPickaxe {
    public HeadPickaxePlanetary() {
        this.rarity = Rarity.EPIC;
    }

    /*protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips();
        tooltips.add(new StringTooltips(this.getDisplayName(item), this.getRarityColor(item)));
        if (perspective != null && !perspective.getClient().characterStats.items_obtained.isItemObtained(this.getStringID())) {
            tooltips.add(Localization.translate("loottooltip", "HeadPickaxePlanetary"));
        }
        return tooltips;
    }*/

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadPickaxePlanetaryTip"));
        tooltips.add(Localization.translate("flavortooltip", "HeadPickaxePlanetary"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",11)
                .setInt("attackDamage",30)
                .setFloat("HeadDPSUpgrade1",320F)
                .setFloat("HeadDPSUpgrade10",600F)
                .setFloat("HeadDamageUpgrade1",84F)
                .setFloat("HeadDamageUpgrade10",159F)
                .setFloat("Rarity",3F)
                .setInt("toolDpsFlat",300)
                .setString("BarrelName","Planetary"));
        return self;
    }
}
