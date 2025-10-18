package gunsmith.items.parts.barrels.shotgun;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;

import java.awt.*;

public class BarrelShotgunCrystalized extends BarrelShotgun {

    public BarrelShotgunCrystalized() {
        this.rarity = Rarity.EPIC;
    }

    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "BarrelShotgunCrystalizedTip"));
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setString("BarrelItem",this.getStringID())
                .setFloat("SpreadMod",-0.3F)
                .setFloat("RangeMod",0.25F)
                .setFloat("ProjectileSpeedMod",0.5F)
                .setFloat("KnockbackMod",0.0001F)
                .setString("Name","Crystalized"));
        return self;
    }
}