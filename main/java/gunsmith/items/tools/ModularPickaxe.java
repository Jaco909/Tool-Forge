package gunsmith.items.tools;

import gunsmith.gunsmith;
import gunsmith.packets.RequestItemPacket;
import gunsmith.scripts.RGBToHex;
import necesse.engine.GameLog;
import necesse.engine.GlobalData;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LineHitbox;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.particle.Particle;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.itemAttack.ItemAttackDrawOptions;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.inventory.item.*;
import necesse.inventory.item.toolItem.pickaxeToolItem.CustomPickaxeToolItem;
import necesse.inventory.lootTable.presets.ToolsLootTable;
import necesse.level.gameObject.RockOreObject;
import necesse.level.maps.Level;

import gunsmith.scripts.ObjectRadiusSearch;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class ModularPickaxe extends CustomPickaxeToolItem implements ItemInteractAction {
    public ModularPickaxe() {
        super(0,0,0,0,50,80,500, null);
        this.damageType = DamageTypeRegistry.MELEE;
        this.showAttackAllDirections = true;
        this.attackXOffset = 0;
        this.attackYOffset = 9;
        this.setItemCategory(new String[]{"TF", "pickaxe"});
    }
    public boolean animDrawBehindHand(InventoryItem item) {
        return true;
    }
    public InventoryItem defaultBarrel(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","HeadPickaxeWood")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BarrelBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BarrelBonus","BarrelBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultBody(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyPickaxeBasic")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BodyBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BodyBonus","BodyBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultStock(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","ShaftPickaxeWood")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("StockBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"StockBonus","StockBonusValue");
        }
        return defaultItem;
    }

    //////////////////////////////////////////////
    ///SETUP VISUALS
    //////////////////////////////////////////////
    protected ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        return ModularBase.getDisplayNameTooltips(item,perspective,blackboard,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }

    //tooltips
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips();
        Control statsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewStats").toString());
        Control partsKey = Control.getControl(gunsmith.settingsGetter.getSelection("viewParts").toString());

        if (!partsKey.isDown()) {
            if (!statsKey.isDown()) {
                tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
            }
        }
        if (!item.getGndData().hasKey("StockItem")) {
            //recipe view
            return tooltips;
        } else {
            if (partsKey.isDown() && partsKey != statsKey) {
                tooltips.add(Localization.translate("itemtooltip", "Head") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "HeadPickaxeWood")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Brace") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyPickaxeBasic")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Shaft") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "ShaftPickaxeWood")+"Name"));
            } else if (statsKey.isDown() || (statsKey.isDown() && statsKey == partsKey) || (partsKey.isDown() && statsKey == partsKey)) {
                if (statsKey == partsKey) {
                    tooltips.add(Localization.translate("itemtooltip", "Head") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "HeadPickaxeWood")+"Name"));
                    tooltips.add(Localization.translate("itemtooltip", "Brace") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyPickaxeBasic")+"Name"));
                    tooltips.add(Localization.translate("itemtooltip", "Shaft") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "ShaftPickaxeWood")+"Name"));
                }
                tooltips.add(Localization.translate("barreltooltip", "HeadTip"));
                if (item.getGndData().hasKey("BarrelBonus")) {
                    String color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusColor"));
                    if (item.getGndData().getFloat("BarrelBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("BarrelBonus"))) {
                        color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusMaxColor"));
                    }
                    if (item.getGndData().getString("BarrelBonus").equalsIgnoreCase("BonusAttackSpeed")) {
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
                tooltips.add(Localization.translate("barreltooltip", item.getGndData().getString("BarrelItem", "HeadPickaxeIron")+"Tip"));
                tooltips.add(Localization.translate("bodytooltip", "BraceTip"));
                if (item.getGndData().hasKey("BodyBonus")) {
                    String color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusColor"));
                    if (item.getGndData().getFloat("BodyBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("BodyBonus"))) {
                        color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusMaxColor"));
                    }
                    if (item.getGndData().getString("BodyBonus").equalsIgnoreCase("BonusAttackSpeed")) {
                        if (item.getGndData().getFloat("BodyBonusValue") > 0) {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue")/10, "unit", "+","color",color));
                        } else {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue")/10, "unit", "","color",color));
                        }
                    } else {
                        if (item.getGndData().getFloat("BodyBonusValue") > 0) {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue"), "unit", "+","color",color));
                        } else {
                            tooltips.add(Localization.translate("bonusmodtip", item.getGndData().getString("BodyBonus"), "value", item.getGndData().getFloat("BodyBonusValue"), "unit", "","color",color));
                        }
                    }
                }
                tooltips.add(Localization.translate("bodytooltip", item.getGndData().getString("BodyItem", "BodyPickaxeBasic")+"Tip"),400);
                tooltips.add(Localization.translate("stocktooltip", "ShaftTip"));
                if (item.getGndData().hasKey("StockBonus")) {
                    String color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusColor"));
                    if (item.getGndData().getFloat("StockBonusValue") >= defaultStock(item).getGndData().getFloat("High" + item.getGndData().getString("StockBonus"))) {
                        color = RGBToHex.RGBToHex(gunsmith.settingsGetter.getColor("bonusMaxColor"));
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
                tooltips.add(Localization.translate("stocktooltip", item.getGndData().getString("StockItem", "ShaftPickaxeWood")+"Tip"));
            } else {
                if (item.getGndData().getString("BodyItem","BodyPickaxeBasic").equalsIgnoreCase("BodyPickaxeAccelerating")) {
                    tooltips.add(Localization.translate("itemtooltip", "pickaxeAccelerating"));
                }
                if (item.getGndData().getString("BodyItem","BodyPickaxeBasic").equalsIgnoreCase("BodyPickaxeDowsing")) {
                    tooltips.add(Localization.translate("itemtooltip", "pickaxeDowsing"));
                }
                if (partsKey == statsKey) {
                    tooltips.add(Localization.translate("itemtooltip", "holdparts", "shift", statsKey.id));
                } else {
                    tooltips.add(Localization.translate("itemtooltip", "holdShift", "shift", statsKey.id));
                    tooltips.add(Localization.translate("itemtooltip", "holdCtrl", "ctrl", partsKey.id));
                }
            }
            return tooltips;
        }
    }
    public void addStatTooltips(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, ItemAttackerMob perspective, boolean forceAdd) {
        this.addToolTierTip(list, currentItem, lastItem, perspective, true);
        this.addToolDPSTip(list, currentItem, lastItem, perspective, true);
        this.addMobDamageTip(list, currentItem, lastItem, perspective);
        this.addAttackSpeedTip(list, currentItem, lastItem, perspective);
        this.addAddedRangeTip(list, currentItem, lastItem, perspective, forceAdd);
    }
    public void addToolTierTip(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
        float tier = this.getToolTier(currentItem, perspective);
        float lastTier = lastItem == null ? tier : this.getToolTier(lastItem, perspective);
        if (tier != lastTier || forceAdd) {
            DoubleItemStatTip tip = new LocalMessageDoubleItemStatTip("itemtooltip", "toolDamageTier", "value", (double)tier, 1);
            if (lastItem != null) {
                tip.setCompareValue((double)lastTier);
            }

            list.add(30, tip);
        }
    }
    public int getToolDps(InventoryItem item, Mob persepctive) {
        int toolDps = this.getFlatToolDps(item);
        int buffModifierFlat = persepctive == null ? 0 : (Integer)persepctive.buffManager.getModifier(BuffModifiers.TOOL_DAMAGE_FLAT);
        float buffModifier = persepctive == null ? 1.0F : (Float)persepctive.buffManager.getModifier(BuffModifiers.TOOL_DAMAGE);
        return Math.round((float)(toolDps + buffModifierFlat) * buffModifier * (Float)this.getEnchantment(item).applyModifierLimited(ToolItemModifiers.TOOL_DAMAGE, (Float)ToolItemModifiers.TOOL_DAMAGE.defaultBuffManagerValue));
    }
    public void addToolDPSTip(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, ItemAttackerMob perspective, boolean forceAdd) {
        int dps = this.getToolDps(currentItem, perspective);
        int lastDps = lastItem == null ? -1 : this.getToolDps(lastItem, perspective);
        if (dps > 0 || lastDps > 0 || forceAdd) {
            DoubleItemStatTip tip = new LocalMessageDoubleItemStatTip("itemtooltip", "tooldmg", "value", (double)dps, 0);
            if (lastItem != null) {
                tip.setCompareValue((double)lastDps);
            }

            list.add(50, tip);
        }

        if (GlobalData.debugActive()) {
            String debugToolString = this.getToolHitDamageString(currentItem, perspective);
            StringItemStatTip debugToolTip = new StringItemStatTip(debugToolString) {
                public GameMessage toMessage(Color betterColor, Color worseColor, Color neutralColor, boolean showDifference) {
                    return new StaticMessage("Tool hit damage: " + this.getReplaceValue(betterColor, worseColor, neutralColor, showDifference));
                }
            };
            if (lastItem != null) {
                String lastDebugToolString = this.getToolHitDamageString(lastItem, perspective);
                debugToolTip.setCompareValue(lastDebugToolString, dps == lastDps ? null : dps > lastDps);
            }

            list.add(40, debugToolTip);
        }

    }
    public void addMobDamageTip(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective) {
        float mobDamage = this.getFlatAttackDamage(currentItem).damage;
        float lastMobDamage = lastItem == null ? 0 : this.getFlatAttackDamage(lastItem).damage;
        if (mobDamage != 0 || lastMobDamage != 0) {
            DoubleItemStatTip tip = new LocalMessageDoubleItemStatTip("itemtooltip", "toolMobDamage", "value", Math.round(mobDamage), 0);
            tip.setValueToString(GameMath::removeDecimalIfZero);
            if (lastItem != null) {
                tip.setCompareValue(Math.round(lastMobDamage));
            }

            list.add(51, tip);
        }
    }
    public void addAddedRangeTip(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
        int addedRange = this.getAddedRange(currentItem);
        float bonusMod = 1;
        if (defaultBarrel(currentItem).getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusRangeMod")) {
            bonusMod = bonusMod + defaultBarrel(currentItem).getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (defaultBody(currentItem).getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusRangeMod")) {
            bonusMod = bonusMod + defaultBody(currentItem).getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (defaultStock(currentItem).getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusRangeMod")) {
            bonusMod = bonusMod + defaultStock(currentItem).getGndData().getFloat("StockBonusValue",0)/100;
        }
        float rangeMod = bonusMod + defaultBarrel(currentItem).getGndData().getFloat("extraRangeMod", 0) + defaultBody(currentItem).getGndData().getFloat("extraRangeMod", 0) + defaultStock(currentItem).getGndData().getFloat("extraRangeMod", 0);
        int lastAddedRange = lastItem == null ? 0 : this.getAddedRange(lastItem);
        if (addedRange != 0 || lastAddedRange != 0 || bonusMod > 1 || forceAdd) {
            DoubleItemStatTip tip;
            if (addedRange != 0) {
                if (rangeMod == 1) {
                    tip = new LocalMessageDoubleItemStatTip("itemtooltip", "tooladdrange", "value", (double) addedRange, 0);
                } else {
                    tip = new LocalMessageDoubleItemStatTip("itemtooltip", "tooladdrange", "value", (double) addedRange * (3.5 * rangeMod), 0);
                }
            } else {
                tip = new LocalMessageDoubleItemStatTip("itemtooltip", "tooladdrange", "value", (double) 3.5 * rangeMod, 0);
            }
            tip.setValueToString((value) -> value >= (double)0.0F ? "+" + GameMath.removeDecimalIfZero(value) : GameMath.removeDecimalIfZero(value));
            if (lastItem != null) {
                tip.setCompareValue((double)lastAddedRange);
            }

            list.add(250, tip);
        }

    }
    public void draw(InventoryItem item, PlayerMob perspective, int x, int y, boolean inInventory) {
        super.draw(item, perspective, x, y, inInventory);
    }
    public void drawIcon(InventoryItem item, PlayerMob perspective, int x, int y, int size, Color color) {
        if (color != null) {
            color = MergeFunction.MULTIPLY.merge(color, this.getDrawColor(item, perspective));
        } else {
            color = this.getDrawColor(item, perspective);
        }
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BarrelItem", "HeadPickaxeIron"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("StockItem", "ShaftPickaxeIron"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BodyItem", "BodyPickaxeBasic"))).initDraw().color(color).size(size).depth(-1F).draw(x, y);

        //This is where we generate the attack texture
        //This is the ONLY spot where OpenGL doesn't throw a fit about generating new textures
        float bonusSize = 0;
        if (ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item))) != 1){
            bonusSize = ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item)))-1;
        }
        File texture;
        if (bonusSize != 0) {
            int bonusSizeText = Math.round((bonusSize + 1) * 100);
            texture = new File(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadPickaxeIron") + item.getGndData().getString("BodyItem", "BodyPickaxeBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeIron") + bonusSizeText + ".png");
        } else {
            texture = new File(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadPickaxeIron") + item.getGndData().getString("BodyItem", "BodyPickaxeBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeIron") + ".png");
        }
        if (!texture.exists()) {
            GameTexture baseWorld = GameTexture.fromFile("player/weapons/pickaxe/ModularPickaxeBase.png", true);
            GameTexture barrelMatTex = GameTexture.fromFile("player/weapons/pickaxe/"+item.getGndData().getString("BarrelItem", "HeadPickaxeIron")+".png", true);
            GameTexture bodyMatTex = GameTexture.fromFile("player/weapons/pickaxe/"+item.getGndData().getString("BodyItem", "BodyPickaxeBasic")+".png", true);
            GameTexture stockMatTex = GameTexture.fromFile("player/weapons/pickaxe/"+item.getGndData().getString("StockItem", "ShaftPickaxeIron")+".png", true);
            GameTexture attackTex = new GameTexture("debug",baseWorld.getWidth(),baseWorld.getHeight());
            attackTex.merge(barrelMatTex, 0, 0, MergeFunction.NORMAL);
            attackTex.merge(stockMatTex, 0, 0, MergeFunction.NORMAL);
            attackTex.merge(bodyMatTex, 0, 0, MergeFunction.NORMAL);

            //resize for attack size
            //MELEE ONLY
            int sizeBonusFlat = defaultBarrel(item).getGndData().getInt("RangeAdd",0) + defaultBody(item).getGndData().getInt("RangeAdd",0) + defaultStock(item).getGndData().getInt("RangeAdd",0);
            float sizeBonusMod = 1 + defaultBarrel(item).getGndData().getFloat("RangeMod",0F) + defaultBody(item).getGndData().getFloat("RangeMod",0F) + defaultStock(item).getGndData().getFloat("RangeMod",0F);
            if (ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item))) != 1){
                bonusSize = ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item)))-1;
            }

            float sizeMod = ((this.attackRange.defaultValue + sizeBonusFlat)*sizeBonusMod)/this.attackRange.defaultValue;
            float sizeModBonus = ((this.attackRange.defaultValue + sizeBonusFlat)*(sizeBonusMod+bonusSize))/this.attackRange.defaultValue;

            //anything but 3 works well
            //0, 1, or 4
            GameTexture finalized = attackTex.resize(Math.round(baseWorld.getWidth()*sizeMod),Math.round(baseWorld.getHeight()*sizeMod),1,1);
            GameTexture finalizedBonus = attackTex.resize(Math.round(baseWorld.getWidth()*sizeModBonus),Math.round(baseWorld.getHeight()*sizeModBonus),1,1);
            attackTex.delete();
            finalized.makeFinal();
            finalizedBonus.makeFinal();
            finalized.saveTextureImage(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadPickaxeIron") + item.getGndData().getString("BodyItem", "BodyPickaxeBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeIron"));
            if (bonusSize != 0) {
                int bonusSizeText = Math.round((bonusSize + 1) * 100);
                finalizedBonus.saveTextureImage(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadPickaxeIron") + item.getGndData().getString("BodyItem", "BodyPickaxeBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeIron") + bonusSizeText);
            }
        }
    }
    public GameSprite getAttackSprite(InventoryItem item, PlayerMob player) {
        File texture;
        float bonusSize = 0;
        if (ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item))) != 1){
            bonusSize = ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item)))-1;
        }
        if (bonusSize != 0) {
            int bonusSizeText = Math.round((bonusSize + 1) * 100);
            texture = new File(gunsmith.modDirectory + "/attackTextures/"+item.getGndData().getString("BarrelItem", "HeadPickaxeIron")+item.getGndData().getString("BodyItem", "BodyPickaxeBasic")+item.getGndData().getString("StockItem", "ShaftPickaxeIron")+bonusSizeText+".png");
        } else {
            texture = new File(gunsmith.modDirectory + "/attackTextures/"+item.getGndData().getString("BarrelItem", "HeadPickaxeIron")+item.getGndData().getString("BodyItem", "BodyPickaxeBasic")+item.getGndData().getString("StockItem", "ShaftPickaxeIron")+".png");
        }
        GameTexture attTex = null;
        try {
            attTex = GameTexture.fromFileRawOutside(texture.getPath(), true);
        } catch (FileNotFoundException e) {
            if (!texture.exists()) {
                player.getClient().network.sendPacket(new RequestItemPacket(player.getPlayerSlot(), item));
            }
            return new GameSprite(GameTexture.fromFile("player/weapons/demonicpickaxe.png"));
        }
        if (!texture.exists()) {
            player.getClient().network.sendPacket(new RequestItemPacket(player.getPlayerSlot(),item));
            return new GameSprite(GameTexture.fromFile("player/weapons/demonicpickaxe.png"));
        } else {
            return new GameSprite(attTex);
        }
    }

    public GameSprite getWorldItemSprite(InventoryItem item, PlayerMob perspective) {
        return this.getAttackSprite(item, perspective);
    }

    //////////////////////////////////////////////
    ///SETUP STATS
    //////////////////////////////////////////////
    public float getToolTier(InventoryItem item, Mob perspective) {
        return ModularBase.getToolTier(item,perspective,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    public int getFlatToolDps(InventoryItem item) {
        return ModularBase.getFlatToolDps(item,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    public int getAddedRange(InventoryItem item) {
        return ModularBase.getAddedRange(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this);
    }
    public int getMiningRange(InventoryItem item, ItemAttackerMob attackerMob) {
        return ModularBase.getMiningRange(item,defaultBarrel(item),defaultBody(item),defaultStock(item),attackerMob,this.getAddedRange(item));
    }
    public float getAttackMovementMod(InventoryItem item) {
        return ModularBase.getAttackMovementMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    public int getFlatAttackAnimTime(InventoryItem item) {
        return ModularBase.getFlatAttackAnimTime(item,defaultBarrel(item),defaultBody(item),defaultStock(item));
    }
    public float getAttackSpeedModifier(InventoryItem item, ItemAttackerMob attackerMob) {
        return ModularBase.getAttackSpeedModifier(item,attackerMob,defaultBarrel(item),defaultBody(item),defaultStock(item),this.damageType,this.getEnchantment(item));
    }
    public GameDamage getFlatAttackDamage(InventoryItem item) {
        return ModularBase.getFlatAttackDamage(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.getDamageType(item));
    }
    public int getFlatAttackRange(InventoryItem item) {
        return ModularBase.getFlatAttackRange(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item)));
    }
    public int getKnockback(InventoryItem item, Attacker attacker) {
        return ModularBase.getKnockback(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.knockback,attacker,this.getEnchantment(item));
    }
    public float getFlatManaCost(InventoryItem item) {
        return ModularBase.getFlatManaCost(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this);
    }
    public float getResilienceGain(InventoryItem item) {
        return ModularBase.getResilienceGain(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this, this.resilienceGain);
    }
    public int getFlatLifeCost(InventoryItem item) {
        return ModularBase.getFlatLifeCost(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this,this.lifeCost.getValue(this.getUpgradeTier(item)));
    }
    public int getLifeSteal(InventoryItem item) {
        return ModularBase.getLifeSteal(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this,this.lifeSteal.getValue(this.getUpgradeTier(item)));
    }

    //////////////////////////////////////////////
    ///PICKAXE SPECIAL
    //////////////////////////////////////////////
    public void showAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int animAttack, int seed, GNDItemMap mapContent) {
        if (level.isClient()) {
            SoundManager.playSound(GameResources.swing2, SoundEffect.effect(attackerMob));
        }
        if (this.getFlatLifeCost(item) != 0) {
            attackerMob.setHealth(attackerMob.getHealth()-this.getFlatLifeCost(item),attackerMob);
            attackerMob.sendHealthPacket(false);
        }
        this.consumeMana(attackerMob,item);

    }
    public InventoryItem onAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        super.onAttack(level, x, y, attackerMob, attackHeight, item, slot, animAttack, seed, mapContent);
        return item;
    }
    public void hitMob(InventoryItem item, ToolItemMobAbilityEvent event, Level level, Mob target, Mob attacker) {
        if (this.getLifeSteal(item) > 0) {
            attacker.setHealth(attacker.getHealth()+this.getLifeSteal(item));
            attacker.sendHealthPacket(false);
        }

        if (event.totalHits == 0 && target.canGiveResilience(attacker)) {
            attacker.addResilience(this.getResilienceGain(item));
        }

        target.isServerHit(this.getAttackDamage(item), target.x - attacker.x, target.y - attacker.y, (float)this.getKnockback(item, attacker), attacker);

        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","HeadPickaxeIron")).getDefaultItem(null,1);
        if (barrelItem.getGndData().getString("BarrelName","HeadPickaxeIron").equalsIgnoreCase("Magma")) {
            if (level.isServer()) {
                if (target != null) {
                    ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("MagmaPickDebuff"), target, 10.0F, attacker);
                    target.addBuff(ab, true);
                }

            }
        }
    }

    public boolean canLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, InventoryItem item) {
        InventoryItem bodyItem = defaultBody(item);
        if (attackerMob.isPlayer) {
            if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Dowsing")) {
                return attackerMob.buffManager.getStacks(BuffRegistry.getBuff("DowsingPickaxeCooldown")) < 5;
            } else if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Accelerating")) {
                return attackerMob.buffManager.getStacks(BuffRegistry.getBuff("AccelerationPickaxeStacks")) > 0;
            }
        }
        return false;
    }

    public InventoryItem onLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int seed, GNDItemMap mapContent) {
        InventoryItem bodyItem = defaultBody(item);
        if (attackerMob.isPlayer) {
            if (level.isClient()) {
                if (bodyItem.getGndData().getString("BodyName","Basic").equalsIgnoreCase("Dowsing")) {
                    DowsingAbility(level, attackerMob);
                } else if (bodyItem.getGndData().getString("BodyName","Basic").equalsIgnoreCase("Accelerating")) {
                    AcceleratingAbility(level, attackerMob);
                }
            }
        }
        return item;
    }

    public void AcceleratingAbility(Level level, ItemAttackerMob attackerMob) {
        ActiveBuff ab = new ActiveBuff("AccelerationPickaxeBuff", attackerMob, 10F, null);
        ab.getGndData().setInt("stacks",attackerMob.buffManager.getBuff(BuffRegistry.getBuff("AccelerationPickaxeStacks")).getStacks());
        attackerMob.buffManager.addBuff(ab,true);
        SoundManager.playSound(gunsmith.PickaxeAccelerate, SoundEffect.effect(attackerMob).volume(1f).pitch(1.1F));
        //attackerMob.buffManager.getBuff(BuffRegistry.getBuff("AccelerationPickaxeBuff")).setStacks(attackerMob.buffManager.getBuff(BuffRegistry.getBuff("AccelerationPickaxeStacks")).getStacks(),10,attackerMob);
        attackerMob.buffManager.removeBuff(BuffRegistry.getBuff("AccelerationPickaxeStacks"),true);
        for (int i = 0; i < 4; i++) {
            if (attackerMob.isVisible()) {
                Mob owner = attackerMob;
                AtomicReference<Float> currentAngle = new AtomicReference(GameRandom.globalRandom.nextFloat() * 360.0F);
                float distance = 20.0F;
                owner.getLevel().entityManager.addParticle(owner.x + GameMath.sin((Float)currentAngle.get()) * distance, owner.y + GameMath.cos((Float)currentAngle.get()) * distance * 0.75F, Particle.GType.CRITICAL).color(new Color(255, 146, 85)).height(0.0F).moves((pos, delta, lifeTime, timeAlive, lifePercent) -> {
                    float angle = (Float)currentAngle.accumulateAndGet(delta * 150.0F / 250.0F, Float::sum);
                    float distY = distance * 0.75F;
                    pos.x = owner.x + GameMath.sin(angle) * distance;
                    pos.y = owner.y + GameMath.cos(angle) * distY * 0.75F;
                }).lifeTime(1000).sizeFades(16, 24);
            }
        }
    }

    public float getItemCooldownPercent(InventoryItem item, PlayerMob perspective) {
        return 0F;
    }

    public ItemControllerInteract getControllerInteract(Level level, PlayerMob player, InventoryItem item, boolean beforeObjectInteract, int interactDir, LinkedList<Rectangle> mobInteractBoxes, LinkedList<Rectangle> tileInteractBoxes) {
        Point2D.Float controllerAimDir = player.getControllerAimDir();
        Point levelPos = this.getControllerAttackLevelPos(level, controllerAimDir.x, controllerAimDir.y, player, item);
        return new ItemControllerInteract(levelPos.x, levelPos.y) {
            public DrawOptions getDrawOptions(GameCamera camera) {
                return null;
            }

            public void onCurrentlyFocused(GameCamera camera) {
            }
        };
    }

    public void DowsingAbility(Level level, ItemAttackerMob attackerMob) {
        ActiveBuff ab = new ActiveBuff("DowsingPickaxeCooldown", attackerMob, 10F, attackerMob);
        attackerMob.buffManager.addBuff(ab, true);
        Point orePoint = ObjectRadiusSearch.ObjectRadiusSearchPoint(new Point(attackerMob.getTileX(), attackerMob.getTileY()), 30, level);
        if (orePoint != null) {
            RockOreObject oreObject = (RockOreObject) level.objectLayer.getObject(0,orePoint.x, orePoint.y);
            Color oreColor = new Color(255, 255, 255);

            ///pixel searches are expensive, so do presets first
            if (oreObject.droppedOre.equalsIgnoreCase("copperore")) {
                oreColor = new Color(227, 125, 82);
            } else if (oreObject.droppedOre.equalsIgnoreCase("ironore")) {
                oreColor = new Color(188, 155, 132);
            } else if (oreObject.droppedOre.equalsIgnoreCase("goldore")) {
                oreColor = new Color(241, 215, 88);
            } else if (oreObject.droppedOre.equalsIgnoreCase("ivyore")) {
                oreColor = new Color(160, 237, 109);
            } else if (oreObject.droppedOre.equalsIgnoreCase("myceliumore")) {
                oreColor = new Color(241, 139, 56);
            } else if (oreObject.droppedOre.equalsIgnoreCase("glacialore")) {
                oreColor = new Color(97, 231, 243);
            } else if (oreObject.droppedOre.equalsIgnoreCase("nightsteelore")) {
                oreColor = new Color(61, 76, 126);
            } else if (oreObject.droppedOre.equalsIgnoreCase("spideriteore")) {
                oreColor = new Color(159, 227, 113);
            } else if (oreObject.droppedOre.equalsIgnoreCase("tungstenore")) {
                oreColor = new Color(55, 67, 94);
            } else if (oreObject.droppedOre.equalsIgnoreCase("alchemyshard")) {
                oreColor = new Color(255, 94, 156);
            } else if (oreObject.droppedOre.equalsIgnoreCase("amberore")) {
                oreColor = new Color(237, 172, 99);
            } else if (oreObject.droppedOre.equalsIgnoreCase("ancientfossilore")) {
                oreColor = new Color(163, 115, 60);
            } else if (oreObject.droppedOre.equalsIgnoreCase("frostshardore")) {
                oreColor = new Color(128, 255, 244);
            } else if (oreObject.droppedOre.equalsIgnoreCase("lifequartzore")) {
                oreColor = new Color(255, 182, 182);
            } else if (oreObject.droppedOre.equalsIgnoreCase("quartzore")) {
                oreColor = new Color(255, 249, 209);
            } else if (oreObject.droppedOre.equalsIgnoreCase("slimeum")) {
                oreColor = new Color(6, 196, 6);
            } else if (oreObject.droppedOre.equalsIgnoreCase("upgradeshard")) {
                oreColor = new Color(28, 92, 230);
            } else {
                ///mod ore support
                ///pulls texture, pulls first solid color
                ///slow on older machines
                GameTexture oreTex = GameTexture.fromFile("objects/" + oreObject.droppedOre);
                if (oreTex != null) {
                    for (int i = 0; i < oreTex.getHeight(); i++) {
                        for (int j = 0; j < oreTex.getWidth(); j++) {
                            if (oreTex.getAlpha(i, j) == 255) {
                                oreColor = new Color(oreTex.getRed(i, j), oreTex.getGreen(i, j), oreTex.getBlue(i, j));
                            }
                        }
                    }

                }
            }
            double dx = orePoint.x - attackerMob.getTileX();
            double dy = orePoint.y - attackerMob.getTileY();
            float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
            SoundManager.playSound(gunsmith.PickaxeDowsing, SoundEffect.effect(attackerMob).volume(0.5f).pitch(0.8F + (float) attackerMob.buffManager.getStacks(BuffRegistry.getBuff("DowsingPickaxeCooldown")) / 10));
            for (int i = 0; i < 8; i++) {
                level.entityManager.addParticle(attackerMob.x + (float) (GameRandom.globalRandom.nextGaussian() * (double) 6.0F), attackerMob.y + (float) (GameRandom.globalRandom.nextGaussian() * (double) 8.0F), Particle.GType.IMPORTANT_COSMETIC).movesConstantAngle(angle + GameRandom.globalRandom.getFloatBetween(-15, 15), 30 + GameRandom.globalRandom.getIntBetween(0, 10)).sizeFades(1, 10).color(oreColor).height(16.0F).lifeTime(700).givesLight(30).size((options, lifeTime, timeAlive, lifePercent) -> options.size(10)).fadesAlphaTime(1, 500);
                SoundManager.playSound(GameResources.cling, SoundEffect.effect(attackerMob).volume(0.6f).pitch(GameRandom.globalRandom.getFloatBetween(2.1f, 3.5f)));
            }
            for (int i = 0; i < 5; i++) {
                level.entityManager.addParticle(attackerMob.x + (float) (GameRandom.globalRandom.nextGaussian() * (double) 6.0F), attackerMob.y + (float) (GameRandom.globalRandom.nextGaussian() * (double) 8.0F), Particle.GType.IMPORTANT_COSMETIC).movesConstantAngle(angle + GameRandom.globalRandom.getFloatBetween(-10, 10), 45 + GameRandom.globalRandom.getIntBetween(0, 10)).color(oreColor).height(16.0F).lifeTime(850 + GameRandom.globalRandom.getIntBetween(0, 100)).givesLight(85).size((options, lifeTime, timeAlive, lifePercent) -> options.size(12)).sizeFades(1, 10).fadesAlphaTime(1, 500);
            }
            for (int i = 0; i < 3; i++) {
                level.entityManager.addParticle(attackerMob.x + (float) (GameRandom.globalRandom.nextGaussian() * (double) 6.0F), attackerMob.y + (float) (GameRandom.globalRandom.nextGaussian() * (double) 8.0F), Particle.GType.IMPORTANT_COSMETIC).movesConstantAngle(angle + GameRandom.globalRandom.getFloatBetween(-5, 5), 60 + GameRandom.globalRandom.getIntBetween(0, 10)).color(oreColor).height(16.0F).lifeTime(1000 + GameRandom.globalRandom.getIntBetween(0, 100)).givesLight(100).size((options, lifeTime, timeAlive, lifePercent) -> options.size(14)).sizeFades(1, 10).fadesAlphaTime(1, 500);
            }
        } else {
            SoundManager.playSound(GameResources.electricExplosion, SoundEffect.effect(attackerMob).volume(0.7f).pitch(GameRandom.globalRandom.getFloatBetween(0.9f, 1f)));
        }
    }
    //////////////////////////////////////////////
    ///HITBOX CHANGE
    //////////////////////////////////////////////
    public ItemAttackDrawOptions setupItemSpriteAttackDrawOptions(ItemAttackDrawOptions options, InventoryItem item, PlayerMob player, int mobDir, float attackDirX, float attackDirY, float attackProgress, Color itemColor) {
        ItemAttackDrawOptions.AttackItemSprite itemSprite = options.itemSprite(this.getAttackSprite(item, player));
        itemSprite.itemRotatePoint(this.attackXOffset, this.attackYOffset);
        itemSprite.itemRawCoords();
        if (itemColor != null) {
            itemSprite.itemColor(itemColor);
        }

        return itemSprite.itemEnd();
    }
    public float getHitboxSwingAngle(InventoryItem item, int dir) {
        return 90.0F;
    }
    public float getHitboxSwingAngleOffset(InventoryItem item, int dir, float swingAngle) {
        return 0.0F;
    }
    public Function<Float, Float> getSwingDirection(InventoryItem item, AttackAnimMob mob) {
        int attackDir = mob.getDir();
        float animSwingAngle = this.getHitboxSwingAngle(item, attackDir);
        float animSwingAngleOffset = this.getHitboxSwingAngleOffset(item, attackDir, animSwingAngle);
        Function<Float, Float> angleGetter;
        if (attackDir == 0) {
            if (this.getAnimInverted(item)) {
                angleGetter = (progress) -> -progress * animSwingAngle - animSwingAngleOffset;
            } else {
                angleGetter = (progress) -> 180.0F + progress*2 * animSwingAngle + animSwingAngleOffset;
            }
        } else if (attackDir == 1) {
            if (this.getAnimInverted(item)) {
                angleGetter = (progress) -> 90.0F - progress * animSwingAngle - animSwingAngleOffset;
            } else {
                angleGetter = (progress) -> 270.0F + progress * animSwingAngle + animSwingAngleOffset;
            }
        } else if (attackDir == 2) {
            if (this.getAnimInverted(item)) {
                angleGetter = (progress) -> 180.0F - progress * animSwingAngle - animSwingAngleOffset;
            } else {
                angleGetter = (progress) -> progress*2 * animSwingAngle + animSwingAngleOffset;
            }
        } else if (this.getAnimInverted(item)) {
            angleGetter = (progress) -> 90.0F + progress * animSwingAngle + animSwingAngleOffset;
        } else {
            angleGetter = (progress) -> 270.0F - progress * animSwingAngle - animSwingAngleOffset;
        }

        return angleGetter;
    }
    public ArrayList<Shape> getHitboxes(InventoryItem item, AttackAnimMob mob, int aimX, int aimY, ToolItemMobAbilityEvent event, boolean forDebug) {
        ArrayList<Shape> out = new ArrayList();
        int attackRange = Math.round(this.getFlatAttackRange(item)*0.85F);
        float lastProgress = event.lastHitboxProgress;
        float nextProgress = mob.getAttackAnimProgress();
        float circumference = (float)(Math.PI * (double)attackRange);
        float percPerWidth = Math.max(10.0F, this.width) / circumference;
        Point2D.Float base = new Point2D.Float(mob.x, mob.y);
        int attackDir = mob.getDir();
        if (attackDir == 0) {
            base.x += 8.0F;
        } else if (attackDir == 2) {
            base.x -= 8.0F;
        }

        for(float progress = lastProgress; progress <= nextProgress; progress += percPerWidth) {
            float angle = (Float)this.getSwingDirection(item, mob).apply(progress);
            Point2D.Float dir = GameMath.getAngleDir(angle);
            Line2D.Float attackLine = new Line2D.Float(base.x, base.y, dir.x * (float)attackRange + mob.x, dir.y * (float)attackRange + mob.y);
            if (this.width > 0.0F) {
                out.add(new LineHitbox(attackLine, this.width));
            } else {
                out.add(attackLine);
            }

            if (!forDebug) {
                event.lastHitboxProgress = progress;
            }
        }

        return out;
    }

}
