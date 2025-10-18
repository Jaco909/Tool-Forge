package gunsmith.buffs;

import gunsmith.packets.GlaiveDryadStacksRemove;
import necesse.engine.GameLog;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameTooltips.ListGameTooltips;

import java.util.concurrent.atomic.AtomicBoolean;

public class GlaiveDryadStacks extends Buff {
    public GlaiveDryadStacks() {
        this.canCancel = false;
        this.isImportant = true;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.MELEE_ATTACK_SPEED, 0.02F);
        buff.setModifier(BuffModifiers.ATTACK_MOVEMENT_MOD, 0.98F);
    }
    public int getStackSize(ActiveBuff buff) {
        return 50;
    }
    /*public void onStacksUpdated(ActiveBuff buff, ActiveBuff other) {
        if (buff.getStacks() > 0) {
            buff.setDurationLeft(1);
        }
    }*/
    public void serverTick(ActiveBuff buff) {
    }

    public void clientTick(ActiveBuff buff) {
        if (!Control.MOUSE1.isDown()) {
           buff.remove();
           if (buff.owner.isPlayer) {
               buff.owner.getClient().network.sendPacket(new GlaiveDryadStacksRemove(true));
           }
        }
    }
    public boolean shouldDrawDuration(ActiveBuff buff) {
        return true;
    }
    public boolean overridesStackDuration() {
        return false;
    }
    public int getRemainingStacksDuration(ActiveBuff buff, AtomicBoolean sendUpdatePacket) {
        return 10000;
    }
}