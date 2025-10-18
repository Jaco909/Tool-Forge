package gunsmith.buffs;

import necesse.engine.GameLog;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameTooltips.ListGameTooltips;

import java.util.concurrent.atomic.AtomicBoolean;

public class AccelerationPickaxeStacks extends Buff {
    public AccelerationPickaxeStacks() {
        this.canCancel = false;
        this.isImportant = true;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.SLOW, 0F);
    }
    public int getStackSize(ActiveBuff buff) {
        return 100;
    }
    public void onStacksUpdated(ActiveBuff buff, ActiveBuff other) {
        if (buff.owner.getWorldTime() > buff.getGndData().getLong("lastTime")+60000) {
            buff.setDurationLeftSeconds(0);
            buff.setStacks(0,0,null);
        } else {
            if (buff.getStacks() > 0) {
                float mult = Math.max(0.3F, 1F - ((float) buff.getStacks() / 100));
                buff.setDurationLeftSeconds(15 * mult);
            }
        }
    }
    public boolean shouldDrawDuration(ActiveBuff buff) {
        return false;
    }
    public boolean overridesStackDuration() {
        return true;
    }
    public int getRemainingStacksDuration(ActiveBuff buff, AtomicBoolean sendUpdatePacket) {
        return 15;
    }
    public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips(this.getDisplayName());
        tooltips.add(Localization.translate("buff", "AccelerationPickaxeStacksTip"));
        tooltips.add(Localization.translate("buff", "AccelerationPickaxeStacksTip2","value",ab.getStacks()));
        return tooltips;
    }
}