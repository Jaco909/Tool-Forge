package gunsmith.buffs;

import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.particle.Particle;
import necesse.gfx.gameTooltips.ListGameTooltips;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AccelerationPickaxeBuff extends Buff {
    int stacks = 1;
    public AccelerationPickaxeBuff() {
        this.canCancel = false;
        this.isImportant = true;
    }
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.MINING_SPEED, 0.01F * buff.getGndData().getInt("stacks",1));
        buff.setModifier(BuffModifiers.TOOL_DAMAGE, (float)buff.getGndData().getInt("stacks",1)/100);
    }
    public void clientTick(ActiveBuff buff) {
        if (buff.owner.isVisible()) {
            Mob owner = buff.owner;
            if ((buff.owner.getWorldTime() % 2) == 0) {
                owner.getLevel().entityManager.addParticle(owner.x + (float) (GameRandom.globalRandom.nextGaussian() * (double) 6.0F), owner.y + (float) (GameRandom.globalRandom.nextGaussian() * (double) 8.0F), Particle.GType.IMPORTANT_COSMETIC).movesConstant(owner.dx / 10.0F, owner.dy / 10.0F).color(new Color(255, 152, 101)).height(16.0F);
            }
        }
    }
    /*public int getStackSize(ActiveBuff buff) {
        return 100;
    }*/
    /*public boolean overridesStackDuration() {
        return true;
    }*/
    /*public int getRemainingStacksDuration(ActiveBuff buff, AtomicBoolean sendUpdatePacket) {
        return 10;
    }*/
    /*public void onStacksUpdated(ActiveBuff buff, ActiveBuff other) {
        if (buff.getStacks() > 0) {
            buff.setModifier(BuffModifiers.MINING_SPEED, 0.005F * buff.getStacks());
        }
    }*/
    public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
        ListGameTooltips tooltips = new ListGameTooltips(this.getDisplayName());
        tooltips.add(Localization.translate("buff", "AccelerationPickaxeBuffTip", "value", ab.getGndData().getInt("stacks",1)));
        return tooltips;
    }
}