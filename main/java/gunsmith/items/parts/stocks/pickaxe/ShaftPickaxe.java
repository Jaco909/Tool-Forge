package gunsmith.items.parts.stocks.pickaxe;

import gunsmith.items.parts.PartMatItem;
import gunsmith.scripts.RGBToHex;
import necesse.engine.GameLog;
import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.matItem.MatItem;

import java.awt.*;
import java.util.ArrayList;

public class ShaftPickaxe extends PartMatItem {

    public ShaftPickaxe() {
        this.setItemCategory(new String[]{"pickaxeShaft"});
        this.setItemCategory(ItemCategory.craftingManager, new String[]{"pickaxeShaft"});
    }
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }

        //new GameSprite(GameTexture.fromFile("items/PickaxeShaftOutline")).initDraw().color(color).size(size).draw(x, y);
        this.getItemSprite(item, perspective).initDraw().color(color).size(size).draw(x, y);
    }
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("stocktooltip", "ShaftTip"));
        tooltips.add(Localization.translate("weapontooltip", "DamageToolTip"));

        //bonus display
        if (item.getGndData().hasKey("BonusActivate")) {
            tooltips.add(Localization.translate("bonusmodtip", "Random"));
        }
        if (item.getGndData().hasKey("StockBonus")) {
            String color = RGBToHex.RGBToHex(gunsmith.gunsmith.settingsGetter.getColor("bonusColor"));
            if (item.getGndData().getFloat("StockBonusValue") >= item.item.getDefaultItem(null,1).getGndData().getFloat("High" + item.getGndData().getString("StockBonus"))) {
                color = RGBToHex.RGBToHex(gunsmith.gunsmith.settingsGetter.getColor("bonusMaxColor"));
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
                .setInt("LowBonusRange",1)
                .setInt("HighBonusRange",2)
                .setFloat("LowBonusPower",6F)
                .setFloat("HighBonusPower",50F)
                .setFloat("LowBonusPowerMod",3F)
                .setFloat("HighBonusPowerMod",25F)
                .setString("Type","ToolShaft")
                .setString("Part","Stock"));
        return self;
    }
}