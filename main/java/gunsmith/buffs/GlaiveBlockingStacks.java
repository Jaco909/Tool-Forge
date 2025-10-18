package gunsmith.buffs;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameTooltips.ListGameTooltips;

import java.util.concurrent.atomic.AtomicBoolean;

public class GlaiveBlockingStacks extends Buff {
    public GlaiveBlockingStacks() {
        this.canCancel = false;
        this.isImportant = true;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.SLOW, 0F);
    }
    public int getStackSize(ActiveBuff buff) {
        return 3;
    }
    public void onStacksUpdated(ActiveBuff buff, ActiveBuff other) {
        if (buff.getStacks() > 0) {
            buff.setDurationLeftSeconds(5);
        }
    }
    public boolean shouldDrawDuration(ActiveBuff buff) {
        return true;
    }
    public boolean overridesStackDuration() {
        return true;
    }
    public int getRemainingStacksDuration(ActiveBuff buff, AtomicBoolean sendUpdatePacket) {
        return 5;
    }
    public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips(this.getDisplayName());
        tooltips.add(Localization.translate("buff", "GlaiveBlockingStacksTip","value",3-ab.getStacks()));
        return tooltips;
    }
}