package gunsmith.buffs;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameTooltips.ListGameTooltips;

import java.util.concurrent.atomic.AtomicBoolean;

public class DowsingPickaxeCooldown extends Buff {
    public DowsingPickaxeCooldown() {
        this.canCancel = false;
        this.isImportant = true;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.SLOW, 0.05F * buff.getStacks());
        buff.setModifier(BuffModifiers.MINING_SPEED, -0.05F);
    }
    public int getStackSize(ActiveBuff buff) {
        return 5;
    }
    public boolean overridesStackDuration() {
        return true;
    }
    public void onStacksUpdated(ActiveBuff buff, ActiveBuff other) {
        if (buff.getStacks() > 0) {
            buff.setDurationLeftSeconds(10);
            buff.setModifier(BuffModifiers.SLOW, 0.05F * buff.getStacks());
        }
    }

    public int getRemainingStacksDuration(ActiveBuff buff, AtomicBoolean sendUpdatePacket) {
        return 10;
    }
    public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips(this.getDisplayName());
        tooltips.add(Localization.translate("buff", "DowsingPickaxeCooldownTip"));
        tooltips.add(Localization.translate("buff", "DowsingPickaxeCooldownTip2","value",ab.getStacks()*5));
        return tooltips;
    }
}
