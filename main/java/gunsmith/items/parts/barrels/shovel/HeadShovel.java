package gunsmith.items.parts.barrels.shovel;

import gunsmith.items.parts.PartMatItem;
import gunsmith.scripts.RGBToHex;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;

import java.awt.*;

public class HeadShovel extends PartMatItem {

    public HeadShovel() {
        this.setItemCategory(new String[]{"parts", "shovel", "shovelHead"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"shovelHead"});
    }
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }

        new GameSprite(GameTexture.fromFile("items/ShovelHeadOutline")).initDraw().color(color).size(size).draw(x, y);
        this.getItemSprite(item, perspective).initDraw().color(color).size(size).draw(x, y);
    }
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("barreltooltip", "HeadTip"));
        tooltips.add(Localization.translate("weapontooltip", "ShovelTip"));
        //bonus display
        if (item.getGndData().hasKey("BonusActivate")) {
            tooltips.add(Localization.translate("bonusmodtip", "Random"));
        }
        if (item.getGndData().hasKey("BarrelBonus")) {
            String color = RGBToHex.RGBToHex(gunsmith.gunsmith.settingsGetter.getColor("bonusColor"));
            if (item.getGndData().getFloat("BarrelBonusValue") >= item.item.getDefaultItem(null,1).getGndData().getFloat("High" + item.getGndData().getString("BarrelBonus"))) {
                color = RGBToHex.RGBToHex(gunsmith.gunsmith.settingsGetter.getColor("bonusMaxColor"));
            }
            if (item.getGndData().getString("BodyBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                if (item.getGndData().getFloat("BarrelBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue")/10, "unit", "+","color",color));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue")/10, "unit", "","color",color));
                }
            } else {
                if (item.getGndData().getFloat("BarrelBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue"), "unit", "+","color",color));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BarrelBonus"), "value", item.getGndData().getFloat("BarrelBonusValue"), "unit", "","color",color));
                }
            }
        }
        return tooltips;
    }

    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setInt("LowBonusRange",1)
                .setInt("HighBonusRange",2)
                .setFloat("LowBonusPower",6F)
                .setFloat("HighBonusPower",50F)
                .setFloat("LowBonusPowerMod",3F)
                .setFloat("HighBonusPowerMod",25F)
                .setString("Type","ModularShovel")
                .setString("Part","Barrel"));
        return self;
    }
}