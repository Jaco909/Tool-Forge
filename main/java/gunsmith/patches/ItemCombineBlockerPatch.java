package gunsmith.patches;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.placeableItem.objectItem.ObjectItem;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = Item.class, name = "canCombineItem", arguments = {Level.class, PlayerMob.class, InventoryItem.class, InventoryItem.class, String.class})
public class ItemCombineBlockerPatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static boolean onExit(@Advice.This Item item, @Advice.Argument(0) Level level, @Advice.Argument(1) PlayerMob player, @Advice.Argument(2) InventoryItem me, @Advice.Argument(3) InventoryItem them, @Advice.Argument(4) String purpose, @Advice.Return(readOnly = false) boolean out) {
        if (me.item.getStringID().contains("Modular") || them.item.getStringID().contains("Modular")) {
            if (!me.item.getStringID().contains("ModularBullet") || !me.item.getStringID().contains("ModularArrow") || !them.item.getStringID().contains("ModularBullet") || !them.item.getStringID().contains("ModularArrow")) {
                out = false;
            } else {
                out = them == null ? false : item.isSameItem(level, me, them, purpose);
            }
        } else {
            out = them == null ? false : item.isSameItem(level, me, them, purpose);
        }
        return out;
    }
}
