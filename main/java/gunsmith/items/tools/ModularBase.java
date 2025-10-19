package gunsmith.items.tools;

import necesse.engine.GameLog;
import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItem;
import necesse.engine.network.gameNetworkData.GNDItemGameDamage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.gameDamageType.DamageType;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.gfx.GameColor;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.enchants.ToolDamageEnchantment;
import necesse.inventory.enchants.ToolItemEnchantment;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import necesse.inventory.item.upgradeUtils.IntUpgradeValue;

import java.awt.*;

public class ModularBase {

    //tool name
    public static ListGameTooltips getDisplayNameTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem) {
        ListGameTooltips tooltips = new ListGameTooltips();
        if (!item.getGndData().hasKey("StockItem")) {
            //recipe display
            tooltips.add(new StringTooltips(item.item.getStringID() + " Template", new Color(255,255,255)));
            tooltips.add(new StringTooltips("I see you cheating :)", new Color(255, 0, 0)));
        } else {
            float rarity = barrelItem.getGndData().getFloat("Rarity",0) + bodyItem.getGndData().getFloat("Rarity",0) + stockItem.getGndData().getFloat("Rarity",0);

            GameColor tiercolor = GameColor.ITEM_COMMON;
            if (rarity >= 6F) {
                tiercolor = GameColor.ITEM_QUEST;
            } else if (rarity >= 5F) {
                tiercolor = GameColor.ITEM_LEGENDARY;
            } else if (rarity >= 4F) {
                tiercolor = GameColor.ITEM_RARE;
            } else if (rarity >= 3F) {
                tiercolor = GameColor.ITEM_EPIC;
            } else if (rarity >= 2F) {
                tiercolor = GameColor.ITEM_UNCOMMON;
            }
            if (item.getGndData().hasKey("name")) {
                tooltips.add(new StringTooltips(item.getGndData().getString("name","Wack"), tiercolor));

            } else {
                if (item.item instanceof ModularGun) {
                    tooltips.add(new StringTooltips(Localization.translate("item", item.getGndData().getString("BodyItem") + "Name") + " " + Localization.translate("item", item.getGndData().getString("StockItem") + "Name") + " " + Localization.translate("item", item.item.getStringID()), tiercolor));
                } else {
                    tooltips.add(new StringTooltips(Localization.translate("item", item.getGndData().getString("BodyItem") + "Name") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem") + "Name") + " " + Localization.translate("item", item.item.getStringID()), tiercolor));
                }
            }
            int upgradeLevel = item.item.getUpgradeLevel(item);
            if (upgradeLevel > 0) {
                int tier = upgradeLevel / 100;
                String tierString;
                if ((float)tier == (float)upgradeLevel / 100.0F) {
                    tierString = String.valueOf(tier);
                } else {
                    int extra = upgradeLevel - tier * 100;
                    tierString = tier + " +" + extra + "%";
                }

                tooltips.add(new StringTooltips(Localization.translate("item", "tier", "tiernumber", tierString), new Color(133, 49, 168)));
            }
        }
        return tooltips;
    }

    //attack move speed
    public static float getAttackMovementMod(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem) {
        float bonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusMoveSpeed")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusMoveSpeed")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusMoveSpeed")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        if (0.5F+bonus+(barrelItem.getGndData().getFloat("MovementMod",0F)+bodyItem.getGndData().getFloat("MovementMod",0F)+stockItem.getGndData().getFloat("MovementMod",0F)) >= 1) {
            return 1;
        } else {
            return 0.5F+bonus+(barrelItem.getGndData().getFloat("MovementMod",0F)+bodyItem.getGndData().getFloat("MovementMod",0F)+stockItem.getGndData().getFloat("MovementMod",0F));
        }
    }

    //attack speed
    public static int getFlatAttackAnimTime(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem) {
        float bonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackSpeed")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackSpeed")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackSpeed")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }

        int acceleration = item.getGndData().getInt("accelerationSwings",0);
        return Math.round(barrelItem.getGndData().getInt("attackSpeed", 0) + bodyItem.getGndData().getInt("attackSpeed", 0) + stockItem.getGndData().getInt("attackSpeed", 0) - bonus + (acceleration*100));
    }
    public static float getAttackSpeedModifier(InventoryItem item, ItemAttackerMob attackerMob, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, DamageType type,ToolDamageEnchantment enchantment) {
        float bonus = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackSpeedMod")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackSpeedMod")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackSpeedMod")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float speedMod = bonus + barrelItem.getGndData().getFloat("attackSpeedMod", 0) + bodyItem.getGndData().getFloat("attackSpeedMod", 0) + stockItem.getGndData().getFloat("attackSpeedMod", 0);
        return type.calculateTotalAttackSpeedModifier(attackerMob, (Float)enchantment.applyModifierUnlimited(ToolItemModifiers.ATTACK_SPEED, (Float)ToolItemModifiers.ATTACK_SPEED.defaultBuffValue))*speedMod;
    }
    public static float getAttackSpeedModifier(InventoryItem item, ItemAttackerMob attackerMob, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, DamageType type,ToolItemEnchantment enchantment) {
        float bonus = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackSpeedMod")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackSpeedMod")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackSpeedMod")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float speedMod = bonus + barrelItem.getGndData().getFloat("attackSpeedMod", 0) + bodyItem.getGndData().getFloat("attackSpeedMod", 0) + stockItem.getGndData().getFloat("attackSpeedMod", 0);
        return type.calculateTotalAttackSpeedModifier(attackerMob, (Float)enchantment.applyModifierUnlimited(ToolItemModifiers.ATTACK_SPEED, (Float)ToolItemModifiers.ATTACK_SPEED.defaultBuffValue))*speedMod;
    }
    //attack damage
    public static GameDamage getFlatAttackDamage(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, DamageType type) {
        float damageBonus = 0;
        float damageBonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusDamage")) {
            damageBonus = damageBonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusDamage")) {
            damageBonus = damageBonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusDamage")) {
            damageBonus = damageBonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusDamageMod")) {
            damageBonusMod = damageBonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusDamageMod")) {
            damageBonusMod = damageBonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusDamageMod")) {
            damageBonusMod = damageBonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float armorPenBonus = 0;
        float armorPenBonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusArmorPen")) {
            armorPenBonus = armorPenBonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusArmorPen")) {
            armorPenBonus = armorPenBonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusArmorPen")) {
            armorPenBonus = armorPenBonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusArmorPenMod")) {
            armorPenBonusMod = armorPenBonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusArmorPenMod")) {
            armorPenBonusMod = armorPenBonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusArmorPenMod")) {
            armorPenBonusMod = armorPenBonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float critChanceBonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusCritChance")) {
            critChanceBonus = critChanceBonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusCritChance")) {
            critChanceBonus = critChanceBonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusCritChance")) {
            critChanceBonus = critChanceBonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float armorPen = armorPenBonus + barrelItem.getGndData().getFloat("ArmorPen", 0F) + bodyItem.getGndData().getFloat("ArmorPen", 0F) + stockItem.getGndData().getFloat("ArmorPen", 0F);
        if (armorPenBonusMod > 0) {
            armorPen = armorPen * armorPenBonusMod;
        }
        float critChance = critChanceBonus + barrelItem.getGndData().getFloat("CritChance", 0F) + bodyItem.getGndData().getFloat("CritChance", 0F) + stockItem.getGndData().getFloat("CritChance", 0F);
        float damage =  damageBonus + barrelItem.getGndData().getFloat("attackDamage", 0F) + bodyItem.getGndData().getFloat("attackDamage", 0F) + stockItem.getGndData().getFloat("attackDamage", 0F);
        float damageModBase = 1 + barrelItem.getGndData().getFloat("DamageMod", 0F) + bodyItem.getGndData().getFloat("DamageMod", 0F) + stockItem.getGndData().getFloat("DamageMod", 0F);
        if (barrelItem.getGndData().hasKey("HeadDamageUpgrade" + item.item.getUpgradeLevel(item)) || barrelItem.getGndData().hasKey("HeadDamageUpgrade1") || barrelItem.getGndData().hasKey("HeadDamageUpgrade10")) {
            if (item.item.getUpgradeTier(item) > 0) {
                if ((barrelItem.getGndData().hasKey("HeadDamageUpgrade" + item.item.getUpgradeLevel(item) / 100))) {
                    return new GameDamage(type, (((barrelItem.getGndData().getFloat("HeadDamageUpgrade" + item.item.getUpgradeLevel(item) / 100, 16F) + damage-barrelItem.getGndData().getFloat("attackDamage", 0F)) * damageModBase))*damageBonusMod, armorPen, critChance);
                } else {
                    float upgMod = (barrelItem.getGndData().getFloat("HeadDamageUpgrade10", 66F) - barrelItem.getGndData().getFloat("HeadDamageUpgrade1", 50F))/9;
                    return new GameDamage(type, ((((barrelItem.getGndData().getFloat("HeadDamageUpgrade1", 50F) + (upgMod * (item.item.getUpgradeTier(item) - 1))) + damage-barrelItem.getGndData().getFloat("attackDamage", 0F)) * damageModBase))*damageBonusMod, armorPen, critChance);
                }
            } else {
                return new GameDamage(type, ((damage * damageModBase)*damageBonusMod), armorPen, critChance);
            }
        } else if (bodyItem.getGndData().hasKey("BodyDamageUpgrade" + item.item.getUpgradeLevel(item)) || bodyItem.getGndData().hasKey("BodyDamageUpgrade1") || bodyItem.getGndData().hasKey("BodyDamageUpgrade10")) {
            if (item.item.getUpgradeTier(item) > 0) {
                if ((bodyItem.getGndData().hasKey("BodyDamageUpgrade" + item.item.getUpgradeLevel(item)/100))) {
                    return new GameDamage(type, (((bodyItem.getGndData().getFloat("BodyDamageUpgrade" + item.item.getUpgradeLevel(item)/100, 16F) + damage-bodyItem.getGndData().getFloat("attackDamage", 0F)) * damageModBase))*damageBonusMod, armorPen, critChance);
                } else {
                    float upgMod = (bodyItem.getGndData().getFloat("BodyDamageUpgrade5",66F)-bodyItem.getGndData().getFloat("BodyDamageUpgrade1",50F))/9;
                    return new GameDamage(type, ((((bodyItem.getGndData().getFloat("BodyDamageUpgrade1", 50F)+(upgMod*(item.item.getUpgradeTier(item)-1))) + damage-bodyItem.getGndData().getFloat("attackDamage", 0F)) * damageModBase))*damageBonusMod, armorPen, critChance);
                }
            } else {
                return new GameDamage(type, ((damage * damageModBase)*damageBonusMod), armorPen, critChance);
            }
        } else {
            if (item.item.getUpgradeTier(item) > 0) {
                if ((stockItem.getGndData().hasKey("StockDamageUpgrade" + item.item.getUpgradeLevel(item)/100))) {
                    return new GameDamage(type, (((stockItem.getGndData().getFloat("StockDamageUpgrade" + item.item.getUpgradeLevel(item)/100, 16F) + damage-stockItem.getGndData().getFloat("attackDamage", 0F)) * damageModBase))*damageBonusMod, armorPen, critChance);
                } else {
                    float upgMod = (stockItem.getGndData().getFloat("StockDamageUpgrade10",66F)-stockItem.getGndData().getFloat("StockDamageUpgrade1",50F))/9;
                    return new GameDamage(type, ((((stockItem.getGndData().getFloat("StockDamageUpgrade1", 50F)+(upgMod*(item.item.getUpgradeTier(item)-1))) + damage-stockItem.getGndData().getFloat("attackDamage", 0F)) * damageModBase))*damageBonusMod, armorPen, critChance);
                }
            } else {
                return new GameDamage(type, ((damage * damageModBase)*damageBonusMod), armorPen, critChance);
            }
        }
    }

    //attack range
    /*public static int getAttackRange(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, int flatAttackRange, ToolDamageEnchantment enchantment) {
        float bonus = 0;
        float bonusMod = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackRange")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackRange")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackRange")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        int attackRange = flatAttackRange + Math.round(bonus) + barrelItem.getGndData().getInt("RangeAdd",0) + bodyItem.getGndData().getInt("RangeAdd",0) + stockItem.getGndData().getInt("RangeAdd",0);
        float attackRangeMod = bonusMod + barrelItem.getGndData().getFloat("RangeMod",0) + bodyItem.getGndData().getFloat("RangeMod",0) + stockItem.getGndData().getFloat("RangeMod",0);
        return attackRangeMod > 0 ? Math.round((float)attackRange * (Float)enchantment.applyModifierLimited(ToolItemModifiers.RANGE, (Float)ToolItemModifiers.RANGE.defaultBuffManagerValue) * attackRangeMod) : Math.round((float)attackRange * (Float)enchantment.applyModifierLimited(ToolItemModifiers.RANGE, (Float)ToolItemModifiers.RANGE.defaultBuffManagerValue));
    }*/
    public static int getFlatAttackRange(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, int flatAttackRange) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackRange")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackRange")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackRange")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        int attackRange = flatAttackRange + Math.round(bonus) + barrelItem.getGndData().getInt("RangeAdd",0) + bodyItem.getGndData().getInt("RangeAdd",0) + stockItem.getGndData().getInt("RangeAdd",0);
        float attackRangeMod = bonusMod + barrelItem.getGndData().getFloat("RangeMod",0) + bodyItem.getGndData().getFloat("RangeMod",0) + stockItem.getGndData().getFloat("RangeMod",0);
        return Math.round(attackRange * attackRangeMod);
    }
    public static float getFlatAttackRangeOnlyBonusMod(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, int flatAttackRange) {
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAttackRangeMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        return bonusMod;
    }

    //knockback
    public static int getKnockback(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, IntUpgradeValue knockbacks, Attacker attacker, ToolDamageEnchantment enchantment) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusKnockback")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusKnockback")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusKnockback")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusKnockbackMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusKnockbackMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusKnockbackMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }


        int knockback = Math.round(bonus) + knockbacks.getValue(0) + barrelItem.getGndData().getInt("Knockback",0) + bodyItem.getGndData().getInt("Knockback",0) + stockItem.getGndData().getInt("Knockback",0);
        float knockbackMod = bonusMod + barrelItem.getGndData().getFloat("KnockbackMod",0) + bodyItem.getGndData().getFloat("KnockbackMod",0) + stockItem.getGndData().getFloat("KnockbackMod",0);
        Mob attackOwner = attacker != null ? attacker.getAttackOwner() : null;
        if (attackOwner != null) {
            knockbackMod = knockbackMod + (Float)attackOwner.buffManager.getModifier(BuffModifiers.KNOCKBACK_OUT);
        }

        return knockbackMod > 1 ? Math.round((float)knockback * (Float)enchantment.applyModifierLimited(ToolItemModifiers.KNOCKBACK, (Float)ToolItemModifiers.KNOCKBACK.defaultBuffManagerValue) * knockbackMod) : Math.round((float)knockback * (Float)enchantment.applyModifierLimited(ToolItemModifiers.KNOCKBACK, (Float)ToolItemModifiers.KNOCKBACK.defaultBuffManagerValue));
    }
    public static int getKnockback(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, IntUpgradeValue knockbacks, Attacker attacker, ToolItemEnchantment enchantment) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusKnockback")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusKnockback")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusKnockback")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusKnockbackMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusKnockbackMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusKnockbackMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }


        int knockback = Math.round(bonus) + knockbacks.getValue(0) + barrelItem.getGndData().getInt("Knockback",0) + bodyItem.getGndData().getInt("Knockback",0) + stockItem.getGndData().getInt("Knockback",0);
        float knockbackMod = bonusMod + barrelItem.getGndData().getFloat("KnockbackMod",0) + bodyItem.getGndData().getFloat("KnockbackMod",0) + stockItem.getGndData().getFloat("KnockbackMod",0);
        Mob attackOwner = attacker != null ? attacker.getAttackOwner() : null;
        if (attackOwner != null) {
            knockbackMod = (Float)attackOwner.buffManager.getModifier(BuffModifiers.KNOCKBACK_OUT);
        }

        return knockbackMod > 1 ? Math.round((float)knockback * (Float)enchantment.applyModifierLimited(ToolItemModifiers.KNOCKBACK, (Float)ToolItemModifiers.KNOCKBACK.defaultBuffManagerValue) * knockbackMod) : Math.round((float)knockback * (Float)enchantment.applyModifierLimited(ToolItemModifiers.KNOCKBACK, (Float)ToolItemModifiers.KNOCKBACK.defaultBuffManagerValue));
    }

    //Tool Damage
    public static int getFlatToolDps(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusPower")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusPower")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusPower")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusPowerMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusPowerMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusPowerMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float toolDamage =  bonus + bodyItem.getGndData().getFloat("toolDpsFlat", 0F) + stockItem.getGndData().getFloat("toolDpsFlat", 0F);
        float toolDamageMod = bonusMod + barrelItem.getGndData().getFloat("toolDpsMod", 0F) + bodyItem.getGndData().getFloat("toolDpsMod", 0F) + stockItem.getGndData().getFloat("toolDpsMod", 0F);

        if (item.item.getUpgradeTier(item) > 0) {
            if ((barrelItem.getGndData().hasKey("HeadDPSUpgrade" + item.item.getUpgradeLevel(item)/100))) {
                return Math.round((barrelItem.getGndData().getFloat("HeadDPSUpgrade" + item.item.getUpgradeLevel(item)/100, 16F) + toolDamage * toolDamageMod));
            } else {
                float upgMod = (stockItem.getGndData().getFloat("HeadDPSUpgrade10",588F)-stockItem.getGndData().getFloat("HeadDPSUpgrade1",210F))/9;
                return Math.round(((barrelItem.getGndData().getFloat("HeadDPSUpgrade1" + item.item.getUpgradeLevel(item)/100, 210F)+(upgMod*(item.item.getUpgradeTier(item)-1)))  + toolDamage * toolDamageMod));
            }
        } else {
            return Math.round(((barrelItem.getGndData().getInt("toolDpsFlat", 0) + toolDamage) * toolDamageMod));
        }
    }

    //tool range
    public static int getAddedRange(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, ToolItem toolItem) {
        float bonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusRange")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusRange")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusRange")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }

        float range = bonus+(barrelItem.getGndData().getInt("extraRange", 0) + bodyItem.getGndData().getInt("extraRange", 0) + stockItem.getGndData().getInt("extraRange", 0));
        return Math.round(range);
    }

    //tool range multiplier
    public static int getMiningRange(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, ItemAttackerMob attackerMob, int range) {
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusRangeMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusRangeMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusRangeMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float rangeMod = bonusMod + barrelItem.getGndData().getFloat("extraRangeMod", 0) + bodyItem.getGndData().getFloat("extraRangeMod", 0) + stockItem.getGndData().getFloat("extraRangeMod", 0);
        return rangeMod > 1 ? (int)(((3.5F + (float)range)*rangeMod + (attackerMob == null ? 0.0F : (Float)attackerMob.buffManager.getModifier(BuffModifiers.MINING_RANGE))) * 32.0F) : (int)((3.5F + (float)range + (attackerMob == null ? 0.0F : (Float)attackerMob.buffManager.getModifier(BuffModifiers.MINING_RANGE))) * 32.0F);
    }

    //tool tier
    public static float getToolTier(InventoryItem item, Mob perspective, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem) {
        //NO DON'T FUCK WITH TOOL TIERS WHY AM I EVEN ADDING THIS
        //YOU WILL SCREW UP THE NORMAL GAME PROGRESSION
        //EVEN FUTURE ME READING WILL KNOW NOT TO USE THIS
        float bonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusToolTier")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusToolTier")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusToolTier")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float tier = bonus + barrelItem.getGndData().getFloat("toolTier", 0F) + bodyItem.getGndData().getFloat("toolTier", 0F) + stockItem.getGndData().getFloat("toolTier", 0F);
        if (item.item.getUpgradeLevel(item)/100 > 0) {
            return tier > 10 ? tier + (float)(perspective == null ? 0 : (Integer)perspective.buffManager.getModifier(BuffModifiers.TOOL_TIER)) : 10 + (float)(perspective == null ? 0 : (Integer)perspective.buffManager.getModifier(BuffModifiers.TOOL_TIER));
        } else {
            return tier > 0 ? tier + (float)(perspective == null ? 0 : (Integer)perspective.buffManager.getModifier(BuffModifiers.TOOL_TIER)) : 0 + (float)(perspective == null ? 0 : (Integer)perspective.buffManager.getModifier(BuffModifiers.TOOL_TIER));
        }
    }

    //mana cost
    public static float getFlatManaCost(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, ToolItem toolItem) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusManaCost")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusManaCost")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusManaCost")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusManaCostMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusManaCostMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusManaCostMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float cost = bonus+(barrelItem.getGndData().getFloat("ManaCost", 0.0F) + bodyItem.getGndData().getFloat("ManaCost", 0.0F) + stockItem.getGndData().getFloat("ManaCost", 0.0F));
        float costMod = bonusMod+(barrelItem.getGndData().getFloat("ManaCostMod", 0.0F) + bodyItem.getGndData().getFloat("ManaCostMod", 0.0F) + stockItem.getGndData().getFloat("ManaCostMod", 0.0F));
        float costDamageMod = 0.05F;
        if (barrelItem.getGndData().hasKey("ManaCostDamageMod") || bodyItem.getGndData().hasKey("ManaCostDamageMod") || stockItem.getGndData().hasKey("ManaCostDamageMod")) {
            costDamageMod = barrelItem.getGndData().getFloat("ManaCostDamageMod",0.0F) + bodyItem.getGndData().getFloat("ManaCostDamageMod",0.0F) + stockItem.getGndData().getFloat("ManaCostDamageMod",0.0F);
        }
        if (cost > 0) {
            if (costDamageMod > 0) {
                return costMod > 1 ? cost * costMod + (toolItem.getFlatAttackDamage(item).damage * costDamageMod) : cost + (toolItem.getFlatAttackDamage(item).damage * costDamageMod);
            } else {
                return costMod > 1 ? cost * costMod : cost;
            }
        } else {
            return 0;
        }
    }

    //life cost
    public static int getFlatLifeCost(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, ToolItem toolItem, int lifeCost) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusLifeCost")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusLifeCost")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusLifeCost")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusLifeCostMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusLifeCostMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusLifeCostMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float cost = bonus+(barrelItem.getGndData().getFloat("LifeCost", 0.0F) + bodyItem.getGndData().getFloat("LifeCost", 0.0F) + stockItem.getGndData().getFloat("LifeCost", 0.0F));
        float costMod = bonusMod+(barrelItem.getGndData().getFloat("LifeCostMod", 0.0F) + bodyItem.getGndData().getFloat("LifeCostMod", 0.0F) + stockItem.getGndData().getFloat("LifeCostMod", 0.0F));
        return costMod > 1 ? Math.round((cost+lifeCost)*costMod) : Math.round(cost+lifeCost);
    }

    //life steal
    public static int getLifeSteal(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, ToolItem toolItem, int lifeSteal) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusLifeSteal")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusLifeSteal")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusLifeSteal")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusLifeStealMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusLifeStealMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusLifeStealMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }
        float cost = bonus+(barrelItem.getGndData().getFloat("LifeSteal", 3.0F) + bodyItem.getGndData().getFloat("LifeSteal", 0.0F) + stockItem.getGndData().getFloat("LifeSteal", 0.0F));
        float costMod = bonusMod+(barrelItem.getGndData().getFloat("LifeStealMod", 0.0F) + bodyItem.getGndData().getFloat("LifeStealMod", 0.0F) + stockItem.getGndData().getFloat("LifeStealMod", 0.0F));
        return costMod > 1 ? Math.round((cost+lifeSteal)*costMod) : Math.round(cost+lifeSteal);
    }

    //resilience gain
    public static float getResilienceGain(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, ToolItem toolItem, FloatUpgradeValue resilienceGain) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusResilienceAdd")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusResilienceAdd")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusResilienceAdd")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusResilienceMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusResilienceMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusResilienceMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float add = bonus+(barrelItem.getGndData().getFloat("ResilienceAdd", 0.0F) + bodyItem.getGndData().getFloat("ResilienceAdd", 0.0F) + stockItem.getGndData().getFloat("ResilienceAdd", 0.0F));
        float mod = bonusMod+(barrelItem.getGndData().getFloat("ResilienceMod", 0.0F) + bodyItem.getGndData().getFloat("ResilienceMod", 0.0F) + stockItem.getGndData().getFloat("ResilienceMod", 0.0F));
        return mod > 1 ? (add+resilienceGain.getValue((float)toolItem.getUpgradeLevel(item)) + (Float)toolItem.getEnchantment(item).applyModifierLimited(ToolItemModifiers.RESILIENCE_GAIN, (Float)ToolItemModifiers.RESILIENCE_GAIN.defaultBuffManagerValue)) * mod : add+resilienceGain.getValue((float)toolItem.getUpgradeLevel(item)) + (Float)toolItem.getEnchantment(item).applyModifierLimited(ToolItemModifiers.RESILIENCE_GAIN, (Float)ToolItemModifiers.RESILIENCE_GAIN.defaultBuffManagerValue);
    }

    //ammo consumption
    public static float getAmmoConsumeChance(ItemAttackerMob attackerMob, InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, float ammoConsumeChance) {
        float bonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusAmmoUseAdd")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusAmmoUseAdd")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusAmmoUseAdd")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float playerMod = attackerMob == null ? 1.0F : (Float)attackerMob.buffManager.getModifier(BuffModifiers.BULLET_USAGE);
        if (item.getGndData().getInt("shotsRemaining",0) > 0) {
            //burst fire mod ammo consumption removal
            return 0;
        } else {
            return (ammoConsumeChance + bonus + barrelItem.getGndData().getFloat("AmmoUseAdd", 0.0F) + bodyItem.getGndData().getFloat("AmmoUseAdd", 0.0F) + stockItem.getGndData().getFloat("AmmoUseAdd", 0.0F)) * playerMod;
        }
    }

    //ranged spread
    public static float getSpread(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, float baseSpread) {
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusSpreadMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusSpreadMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusSpreadMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        float mod = bonusMod + barrelItem.getGndData().getFloat("SpreadMod", 0.0F) + bodyItem.getGndData().getFloat("SpreadMod", 0.0F) + stockItem.getGndData().getFloat("SpreadMod", 0.0F);
        return mod*baseSpread;
    }

    //projectiles per shot
    public static int getPellets(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem) {
        float bonus = 0;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusPellets")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusPellets")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusPellets")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }

        int base = Math.round(bonus) + (barrelItem.getGndData().getInt("Pellets", 0) + bodyItem.getGndData().getInt("Pellets", 0) + stockItem.getGndData().getInt("Pellets", 0));
        return base > 0 ? base : 4;
    }

    //projectile speed
    public static int getFlatVelocity(InventoryItem item, InventoryItem barrelItem, InventoryItem bodyItem, InventoryItem stockItem, IntUpgradeValue velocity, ToolItem toolItem) {
        float bonus = 0;
        float bonusMod = 1;
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusVelocity")) {
            bonus = bonus + barrelItem.getGndData().getFloat("BarrelBonusValue",0);
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusVelocity")) {
            bonus = bonus + bodyItem.getGndData().getFloat("BodyBonusValue",0);
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusVelocity")) {
            bonus = bonus + stockItem.getGndData().getFloat("StockBonusValue",0);
        }
        if (barrelItem.getGndData().getString("BarrelBonus","null").equalsIgnoreCase("BonusVelocityMod")) {
            bonusMod = bonusMod + barrelItem.getGndData().getFloat("BarrelBonusValue",0)/100;
        }
        if (bodyItem.getGndData().getString("BodyBonus","null").equalsIgnoreCase("BonusVelocityMod")) {
            bonusMod = bonusMod + bodyItem.getGndData().getFloat("BodyBonusValue",0)/100;
        }
        if (stockItem.getGndData().getString("StockBonus","null").equalsIgnoreCase("BonusVelocityMod")) {
            bonusMod = bonusMod + stockItem.getGndData().getFloat("StockBonusValue",0)/100;
        }

        int base = Math.round(bonus) + velocity.getValue(toolItem.getUpgradeTier(item)) +(barrelItem.getGndData().getInt("ProjectileSpeed", 0) + bodyItem.getGndData().getInt("ProjectileSpeed", 0) + stockItem.getGndData().getInt("ProjectileSpeed", 0));
        float baseMod = bonusMod +(barrelItem.getGndData().getFloat("ProjectileSpeedMod", 0.0F) + bodyItem.getGndData().getFloat("ProjectileSpeedMod", 0.0F) + stockItem.getGndData().getFloat("ProjectileSpeedMod", 0.0F));

        return Math.round(base*baseMod);
    }
}
