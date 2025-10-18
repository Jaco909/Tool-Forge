package gunsmith.items.parts.barrels.pickaxe;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

public class HeadPickaxeCrystal extends HeadPickaxe {
    public HeadPickaxeCrystal() {
        this.rarity = Rarity.RARE;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadPickaxeCrystalTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("toolTier",10)
                .setInt("attackDamage",24)
                .setFloat("HeadDPSUpgrade1",290F)
                .setFloat("HeadDPSUpgrade10",588F)
                .setFloat("HeadDamageUpgrade1",84F)
                .setFloat("HeadDamageUpgrade10",145F)
                .setFloat("Rarity",3F)
                .setInt("toolDpsFlat",275)
                .setString("BarrelName","Crystal"));
        return self;
    }
}
