package gunsmith.patches;

import necesse.engine.GameLog;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.modifiers.Modifier;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.gameDamageType.DamageType;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = DamageType.class, name = "getTypeCritDamageModifier", arguments = {Attacker.class})
public class CritDamageModifierPatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static float onExit(@Advice.This DamageType type, @Advice.Argument(0) Attacker attacker, @Advice.Return(readOnly = false) float out) {
        Modifier<Float> modifier = type.getBuffCritDamageModifier();
        InventoryItem item = null;
        if (attacker.getAttackOwner().isPlayer) {
            item = attacker.getFirstPlayerOwner().getSelectedItem();
        }

        float bonus = 0;
        if (item != null) {
            if (item.getGndData().getString("BarrelBonus", "null").equalsIgnoreCase("BonusCritDamage")) {
                bonus = bonus + item.getGndData().getFloat("BarrelBonusValue", 0) / 100;
            }
            if (item.getGndData().getString("BodyBonus", "null").equalsIgnoreCase("BonusCritDamage")) {
                bonus = bonus + item.getGndData().getFloat("BodyBonusValue", 0) / 100;
            }
            if (item.getGndData().getString("StockBonus", "null").equalsIgnoreCase("BonusCritDamage")) {
                bonus = bonus + item.getGndData().getFloat("StockBonusValue", 0) / 100;
            }
        }
        if (modifier != null) {
            Mob attackOwner = attacker != null ? attacker.getAttackOwner() : null;
            //GameLog.warn.println("wack");
            //GameLog.debug.println(bonus);
            //GameLog.debug.println((Float)attackOwner.buffManager.getModifier(modifier));
            return attackOwner != null ? (Float)attackOwner.buffManager.getModifier(modifier) + bonus + 20 : 0.0F;
            //return 5;
        } else if (bonus > 1) {
            //GameLog.warn.println("wack2");
            //GameLog.debug.println(bonus);
            return bonus;
        } else {
            //GameLog.warn.println("wack3");
            //GameLog.debug.println(bonus);
            return 0.0F;
        }
    }
}
