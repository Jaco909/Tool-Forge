package gunsmith.items.parts;

import gunsmith.packets.SaveModifierPacket;
import necesse.engine.GameLog;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.*;
import necesse.inventory.item.Item;
import necesse.inventory.item.matItem.MatItem;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.IngredientCounter;
import necesse.inventory.recipe.IngredientUser;
import necesse.level.maps.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

///////////////////////////////////////
/// List of Valid Attributes
///
/// DamageUpgrade# tiers between 1 and 5 are automatically calculated
///
/// HeadDamageUpgrade1  - Tier 1 damage value, float, tools/melee
/// HeadDamageUpgrade10  - Tier 5 damage value, float
/// HeadDamageUpgrade#  - Tier # damage value, float
/// BodyDamageUpgrade1  - Tier 1 damage value, float, unused
/// BodyDamageUpgrade5  - Tier 5 damage value, float
/// BodyDamageUpgrade#  - Tier # damage value, float
/// StockDamageUpgrade1 - Tier 1 damage value, float, guns
/// StockDamageUpgrade10 - Tier 5 damage value, float
/// StockDamageUpgrade# - Tier # damage value, float
///
/// Rarity              - Name color, float
/// attackDamage        - Flat damage value, float
/// DamageMod           - Damage multiplier, float
/// ArmorPen            - Armor penetration, float
/// ArmorPenMod         - Armor penetration multiplier, float
/// CritChance          - Crit chance, float
/// attackSpeed         - Base attack speed, Int
/// attackSpeedMod      - Attack speed multiplier, float
/// RangeAdd            - Attack range base value, int
/// RangeMod            - Attack range multiplier, float
/// Knockback           - Knockback base value, int
/// KnockbackMod        - Knockback multiplier, float
/// MovementMod         - Attack movespeed reduction, float, 1 = 0%
/// LifeCost            - Life cost base value, float
/// LifeCostMod         - Life cost multiplier, float
/// LifeSteal           - Life steal base value, float
/// LifeStealMod        - Life steal multiplier, float
/// ResilienceAdd       - Extra resilience, float
/// ResilienceAddMod    - Extra resilience multiplier, float
/// ManaCost            - Mana cost base value, float, multiplied by 5% of current attack damage before buffs
/// ManaCostDamageMod   - Override 5% damage cost multiplier, float
/// ManaCostMod         - Mana cost multiplier, float
///
/// AmmoUseAdd          - Ammo consumption rate, float, 1 = 0%, base 0.5
/// Pellets             - Bullets per shot, int, default 4
/// SpreadMod           - Bullet spread multiplier, float
/// ProjectileSpeed     - Projectile speed base, float
/// ProjectileSpeedMod  - Projectile speed multiplier, float
/// ShotAmount          - Burstfire tool shot amount per burst, int
/// ShotTime            - Time between each shot in burst, int
/// ReloadTime          - Time between bursts, int
///
/// toolTier            - Tool damage tier, float
/// toolDpsFlat         - Tool damage base value, int
/// toolDpsMod          - Tool damage multiplier, float
/// HeadDPSUpgrade1     - Tier 1 tool damage value, float
/// HeadDPSUpgrade10     - Tier 5 tool damage value, float
/// HeadDPSUpgrade#     - Tier # tool damage value, float
/// extraRange          - Tool damage range, int
/// extraRangeMod       - Tool damage range multiplier
///
/// ShowAttack          - Makes glaives display spin particles
/// AttackDisplay       - Defines spin particle for glaives. Options: Slime,Fire,Cryo
///////////////////////////////////////

///////////////////////////////////////
/// List of Valid Modifiers
///
/// All values are float. All mod values are divided by 100 unless noted otherwise. All values can be negative for a debuff (except BonusMoveSpeed because I need to fix the tooltip).
///
/// BonusDamage             - +# attack damage
/// BonusDamageMod          - +#% attack damage multiplier
/// BonusArmorPen           - +# armor penetration
/// BonusArmorPenMod        - +#% armor penetration multiplier
/// BonusCritChance         - +#% crit chance
/// BonusCritDamage         - BROKEN
/// BonusKnockback          - +# knockback
/// BonusKnockbackMod       - +#% knockback multiplier
/// BonusMoveSpeed          - -#% attack move speed reduction
/// BonusAttackRange        - +# attack range
/// BonusAttackRangeMod     - +#% attack range multiplier
/// BonusAttackSpeed        - -# flat attack speed
/// BonusAttackSpeedMod     - +% attack speed multiplier
/// BonusLifeCost           - +# flat life cost
/// BonusLifeCostMod        - +#% life cost multiplier
/// BonusLifeSteal          - +# flat life steal on hit
/// BonusLifeStealMod       - +#% life steal on hit multiplier
/// BonusManaCost           - +# flat mana cost
/// BonusManaCostMod        - +#% mana cost multiplier
/// BonusResilienceAdd      - +# flat resilience gain
/// BonusResilienceMod      - +#% resilience gain multiplier
/// BonusAmmoUseAdd         - +# ammo consumption chance
/// BonusPellets            - +# pellets per shot
/// BonusSpreadMod          - +#% spread per shot
/// BonusVelocity           - +#% flat bullet velocity
/// BonusVelocityMod        - +#% bullet velocity
/// BonusPower              - +# flat tool mining power
/// BonusPowerMod           - +#% tool mining power multiplier
/// BonusRange              - +# flat tool range
/// BonusRangeMod           - +#% tool range multiplier
/// BonusToolTier           - Tool damage tier, NO, WHY IS THIS HERE, NEVER USE THIS, BAD, WHY DID I ADD THIS, NO!!!
///////////////////////////////////////

public class PartMatItem extends MatItem {
    public PartMatItem() {
        super(1, Rarity.NORMAL);
    }

    public boolean noBonus(InventoryItem item){
        return !item.getGndData().hasKey("BarrelBonus") && !item.getGndData().hasKey("BodyBonus") && !item.getGndData().hasKey("StockBonus");
    }
    public void countIngredientAmount(Level level, PlayerMob player, Inventory inventory, int inventorySlot, InventoryItem item, String purpose, IngredientCounter handler) {
        if (noBonus(item)) {
            super.countIngredientAmount(level, player, inventory, inventorySlot, item, purpose, handler);
        }
    }
    public int removeInventoryAmount(Level level, PlayerMob player, final InventoryItem item, Inventory inventory, int inventorySlot, Ingredient ingredient, int amount, Collection<InventoryItemsRemoved> collect) {
        return noBonus(item) ? super.removeInventoryAmount(level, player, item, inventory, inventorySlot, ingredient, amount, collect) : 0;
    }
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        //chose bonus modifier
        if (item.getGndData().getBoolean("BonusActivate") && !item.getGndData().hasKey("BarrelBonus") && !item.getGndData().hasKey("BodyBonus") && !item.getGndData().hasKey("StockBonus")) {
            if (perspective.getInv().streamInventorySlots(false,false,false,true).anyMatch(inventorySlot -> inventorySlot.getItem() == item)) {

                //generate bonus type
                item.getGndData().addAll(randomModifier(item.item.getDefaultItem(null,1).getGndData()));

                //generate bonus values
                if (item.getGndData().getBoolean("isCrafted")) {
                    //crafted modifiers get lower modifier rolls
                    if (item.getGndData().hasKey("BarrelBonus")) {
                        item.getGndData().setFloat("BarrelBonusValue", Math.round(GameRandom.globalRandom.getFloatBetween(item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("BarrelBonus")), item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("BarrelBonus")) * GameRandom.globalRandom.getFloatBetween(1.20F, 1.40F)) * 10.0F) / 10.0F);
                    } else if (item.getGndData().hasKey("BodyBonus")) {
                        item.getGndData().setFloat("BodyBonusValue", Math.round(GameRandom.globalRandom.getFloatBetween(item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("BodyBonus")), item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("BodyBonus")) * GameRandom.globalRandom.getFloatBetween(1.20F, 1.40F)) * 10.0F) / 10.0F);
                    } else if (item.getGndData().hasKey("StockBonus")) {
                        item.getGndData().setFloat("StockBonusValue", Math.round(GameRandom.globalRandom.getFloatBetween(item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("StockBonus")), item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("StockBonus")) * GameRandom.globalRandom.getFloatBetween(1.20F, 1.40F)) * 10.0F) / 10.0F);
                    }
                } else {
                    //loot items get higher modifier rolls
                    if (item.getGndData().hasKey("BarrelBonus")) {
                        item.getGndData().setFloat("BarrelBonusValue", Math.round(GameRandom.globalRandom.getFloatBetween(item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("BarrelBonus")) * GameRandom.globalRandom.getFloatBetween(1.20F, 1.35F), item.item.getDefaultItem(null, 1).getGndData().getFloat("High" + item.getGndData().getString("BarrelBonus"))) * 10.0F) / 10.0F);
                    } else if (item.getGndData().hasKey("BodyBonus")) {
                        item.getGndData().setFloat("BodyBonusValue", Math.round(GameRandom.globalRandom.getFloatBetween(item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("BodyBonus")) * GameRandom.globalRandom.getFloatBetween(1.20F, 1.35F), item.item.getDefaultItem(null, 1).getGndData().getFloat("High" + item.getGndData().getString("BodyBonus"))) * 10.0F) / 10.0F);
                    } else if (item.getGndData().hasKey("StockBonus")) {
                        item.getGndData().setFloat("StockBonusValue", Math.round(GameRandom.globalRandom.getFloatBetween(item.item.getDefaultItem(null, 1).getGndData().getFloat("Low" + item.getGndData().getString("StockBonus")) * GameRandom.globalRandom.getFloatBetween(1.20F, 1.35F), item.item.getDefaultItem(null, 1).getGndData().getFloat("High" + item.getGndData().getString("StockBonus"))) * 10.0F) / 10.0F);
                    }
                }

                //send GNDdata packet for server sync
                Iterator slots = perspective.getInv().streamInventorySlots(false,false,false,true).iterator();
                while (slots.hasNext()) {
                    InventorySlot slot = (InventorySlot) slots.next();
                    if (slot.getItem() == item) {
                        item.getGndData().clearItem("BonusActivate");
                        perspective.getClient().network.sendPacket(new SaveModifierPacket(slot.slot,perspective,new PlayerInventorySlot(perspective.getInv().main,slot.slot)));
                        break;
                    }
                }
            }
        }
        return tooltips;
    }

    public static GNDItemMap randomModifier(GNDItemMap GNData){
        //Find all keys in default item that start with 'Bonus'
        ArrayList<String> bonusKeys = new ArrayList<String>();
        for (String keyName : GNData.getKeyStringSet()) {
            if (keyName.startsWith("LowBonus")) {
                bonusKeys.add(keyName.substring(3));
            }
        }


        //Choose one and set it as the bonus modifier
        if (!bonusKeys.isEmpty()) {
            if (GNData.getString("Part").equalsIgnoreCase("Barrel")) {
                GNData.clearAll();
                GNData.setString("BarrelBonus", bonusKeys.get(GameRandom.globalRandom.getIntBetween(0, bonusKeys.size()-1)));
            } else if (GNData.getString("Part").equalsIgnoreCase("Body")) {
                GNData.clearAll();
                GNData.setString("BodyBonus", bonusKeys.get(GameRandom.globalRandom.getIntBetween(0, bonusKeys.size()-1)));
            } else if (GNData.getString("Part").equalsIgnoreCase("Stock")) {
                GNData.clearAll();
                GNData.setString("StockBonus", bonusKeys.get(GameRandom.globalRandom.getIntBetween(0, bonusKeys.size()-1)));
            }
        }
        return GNData;
    }
    public InventoryItem getDefaultItem(PlayerMob player, int amount) {
        InventoryItem self = new InventoryItem(this, amount);
        self.setGndData(self.getGndData()
                //.setFloat("LowBonusDamage",18F)
                //.setFloat("HighBonusDamage",30F)
                .setFloat("LowBonusDamageMod",5F)
                .setFloat("HighBonusDamageMod",30F)
                .setFloat("LowBonusArmorPen",1F)
                .setFloat("HighBonusArmorPen",5F)
                .setFloat("LowBonusCritChance",3F)
                .setFloat("HighBonusCritChance",15F)
                .setFloat("LowBonusKnockback",20F)
                .setFloat("HighBonusKnockback",120F)
                .setFloat("LowBonusMoveSpeed",4F)
                .setFloat("HighBonusMoveSpeed",20F)
                .setFloat("LowBonusAttackRangeMod",5F)
                .setFloat("HighBonusAttackRangeMod",28F)
                .setFloat("LowBonusAttackSpeedMod",5F)
                .setFloat("HighBonusAttackSpeedMod",20F));
        return self;
    }
}
