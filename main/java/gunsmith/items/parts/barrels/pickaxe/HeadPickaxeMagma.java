package gunsmith.items.parts.barrels.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadPickaxeMagma extends HeadPickaxe {
    public HeadPickaxeMagma() {
        this.rarity = Rarity.RARE;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadPickaxeMagmaTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",10)
                .setInt("attackDamage",34)
                .setFloat("HeadDPSUpgrade1",225F)
                .setFloat("HeadDPSUpgrade10",548F)
                .setFloat("HeadDamageUpgrade1",84F)
                .setFloat("HeadDamageUpgrade10",161F)
                .setInt("toolDpsFlat",205)
                .setFloat("Rarity",3F)
                .setString("BarrelName","Magma"));
        return self;
    }
}
