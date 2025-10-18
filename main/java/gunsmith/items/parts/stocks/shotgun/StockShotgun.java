package gunsmith.items.parts.stocks.shotgun;

import gunsmith.items.parts.PartMatItem;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

import java.awt.*;

public class StockShotgun extends PartMatItem {

    public StockShotgun() {
        this.setItemCategory(new String[]{"shotgunStock"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"shotgunStock"});
    }
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }

        new GameSprite(GameTexture.fromFile("items/ShotgunStockOutline")).initDraw().color(color).size(size).draw(x, y);
        this.getItemSprite(item, perspective).initDraw().color(color).size(size).draw(x, y);
    }
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "StockTip"));
        tooltips.add(Localization.translate("weapontooltip", "ShotgunTip"));
        //bonus display
        if (item.getGndData().hasKey("BonusActivate")) {
            tooltips.add(Localization.translate("bonusmodtip", "Random"));
        }
        if (item.getGndData().hasKey("StockBonus")) {
            String color = "§#07BFE8";
            if (item.getGndData().getFloat("StockBonusValue") >= item.item.getDefaultItem(null,1).getGndData().getFloat("High" + item.getGndData().getString("StockBonus"))) {
                color = "§#E655E6";
            }
            if (item.getGndData().getString("StockBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                if (item.getGndData().getFloat("StockBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue")/10, "unit", "+","color",color));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue")/10, "unit", "","color",color));
                }
            } else {
                if (item.getGndData().getFloat("StockBonusValue") > 0) {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue"), "unit", "+","color",color));
                } else {
                    tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("StockBonus"), "value", item.getGndData().getFloat("StockBonusValue"), "unit", "","color",color));
                }
            }
        }
        return tooltips;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = super.getDefaultItem(player,amount);
        self.setGndData(self.getGndData()
                .setFloat("LowBonusPellets",1F)
                .setFloat("HighBonusPellets",2F)
                .setFloat("LowBonusVelocityMod",7F)
                .setFloat("HighBonusVelocityMod",30F)
                .setFloat("LowBonusAmmoUseAdd",-5)
                .setFloat("HighBonusAmmoUseAdd",-20)
                .setFloat("LowBonusSpreadMod",-8F)
                .setFloat("HighBonusSpreadMod",-33F)
                .setString("Type","ModularShotgun")
                .setString("Part","Stock"));
        return self;
    }
}