package gunsmith.items.tools;

import gunsmith.events.DeflectingToolItemMobAbilityEvent;
import gunsmith.gunsmith;
import gunsmith.packets.RequestItemPacket;
import gunsmith.scripts.RGBToHex;
import necesse.engine.GameState;
import necesse.engine.GlobalData;
import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.ComparableSequence;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameMath;
import necesse.engine.util.LineHitbox;
import necesse.engine.world.GameClock;
import necesse.engine.world.WorldSettings;
import necesse.entity.Entity;
import necesse.entity.TileEntity;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.*;
import necesse.entity.mobs.attackHandler.ToolDamageItemAttackHandler;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.itemAttack.ItemAttackDrawOptions;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.*;
import necesse.inventory.container.Container;
import necesse.inventory.container.ContainerActionResult;
import necesse.inventory.container.item.ItemInventoryContainer;
import necesse.inventory.container.slots.ContainerSlot;
import necesse.inventory.enchants.ToolItemModifiers;
import necesse.inventory.item.*;
import necesse.inventory.item.miscItem.InternalInventoryItemInterface;
import necesse.inventory.item.toolItem.TileDamageOption;
import necesse.inventory.item.toolItem.ToolType;
import necesse.inventory.item.toolItem.shovelToolItem.CustomShovelToolItem;
import necesse.inventory.item.toolItem.shovelToolItem.ShovelToolItem;
import necesse.inventory.lootTable.presets.ToolsLootTable;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.IngredientCounter;
import necesse.inventory.recipe.IngredientUser;
import necesse.level.gameObject.ObjectHoverHitbox;
import necesse.level.maps.Level;
import necesse.level.maps.LevelTile;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModularShovel extends CustomShovelToolItem implements InternalInventoryItemInterface, TickItem {
    public HashSet<String> insertPurposes = new HashSet();
    public boolean isInsertPurposesBlacklist = false;
    public HashSet<String> requestPurposes = new HashSet();
    public boolean isRequestPurposeBlacklist = true;
    public HashSet<String> pickupDisabledPurposeIgnores = new HashSet();
    public boolean allowRestockFrom = true;
    protected boolean isValidPurpose(HashSet<String> purposes, boolean isBlacklist, String purpose, boolean isPickupDisabled) {
        if (isPickupDisabled && this.pickupDisabledPurposeIgnores.contains(purpose)) {
            return isBlacklist;
        } else {
            return isBlacklist != purposes.contains(purpose);
        }
    }
    public ModularShovel() {
        super(0,0,0,0,50,80,500);
        this.damageType = DamageTypeRegistry.MELEE;
        this.showAttackAllDirections = true;
        this.attackXOffset = -4;
        this.attackYOffset = 3;
    }
    public boolean animDrawBehindHand(InventoryItem item) {
        return true;
    }
    public InventoryItem defaultBarrel(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","HeadShovelWood")).getDefaultItem(null,1);
        if (item.getGndData().hasKey("BarrelBonus")) {
            item.getGndData().copyKeysToTarget(defaultItem.getGndData(),"BarrelBonus","BarrelBonusValue");
        }
        return defaultItem;
    }
    public InventoryItem defaultBody(InventoryItem item){
        InventoryItem defaultItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyShovelBasic")).getDefaultItem(null,1);
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
    public boolean isValidPouchItem(InventoryItem item) {
        return this.isValidRequestItem(item.item);
    }
    public boolean isValidRequestItem(Item item) {
        return item != null ? item.getStringID().equalsIgnoreCase("bucket") || item.getStringID().equalsIgnoreCase("landfill") : !this.isValidRequestType(null);
    }
    public boolean isValidAddItem(InventoryItem item) {
        return this.isValidPouchItem(item);
    }

    public boolean isValidRequestType(Item.Type type) {
        return false;
    }

    public int getInternalInventorySize() {
        return 1;
    }
    public Supplier<ContainerActionResult> getInventoryRightClickAction(Container container, InventoryItem item, int slotIndex, ContainerSlot slot) {
        return () -> {
            PlayerInventorySlot playerSlot = null;
            if (slot.getInventory() == container.getClient().playerMob.getInv().main) {
                playerSlot = new PlayerInventorySlot(container.getClient().playerMob.getInv().main, slot.getInventorySlot());
            }

            if (slot.getInventory() == container.getClient().playerMob.getInv().cloud) {
                playerSlot = new PlayerInventorySlot(container.getClient().playerMob.getInv().cloud, slot.getInventorySlot());
            }

            if (playerSlot != null) {
                if (container.getClient().isServer()) {
                    ServerClient client = container.getClient().getServerClient();
                    this.openContainer(client, playerSlot);
                }

                return new ContainerActionResult(-1002911334);
            } else {
                return new ContainerActionResult(208675834, Localization.translate("itemtooltip", "rclickinvopenerror"));
            }
        };
    }

    protected void openContainer(ServerClient client, PlayerInventorySlot inventorySlot) {
        PacketOpenContainer p = new PacketOpenContainer(ContainerRegistry.ITEM_INVENTORY_CONTAINER, ItemInventoryContainer.getContainerContent(this, inventorySlot));
        ContainerRegistry.openAndSendContainer(client, p);
    }
    public ComparableSequence<Integer> getInventoryAddPriority(Level level, PlayerMob player, Inventory inventory, int inventorySlot, InventoryItem item, InventoryItem input, String purpose) {
        ComparableSequence<Integer> last = super.getInventoryAddPriority(level, player, inventory, inventorySlot, item, input, purpose);
        if (this.isValidAddItem(input) && (this.isValidPurpose(this.insertPurposes, this.isInsertPurposesBlacklist, purpose, this.isPickupDisabled(item)) || this.allowRestockFrom && purpose.equals("restockfrom"))) {
            last = last.beforeBy(-10000);
            Inventory internalInventory = this.getInternalInventory(item);
            ArrayList<SlotPriority> addList = internalInventory.getPriorityAddList(level, player, input, 0, internalInventory.getSize() - 1, purpose);

            for(SlotPriority slotPriority : addList) {
                boolean isValid = internalInventory.isItemValid(slotPriority.slot, input);
                int stackLimit = internalInventory.getItemStackLimit(slotPriority.slot, input);
                InventoryItem invItem = internalInventory.getItem(slotPriority.slot);
                if (invItem.item.inventoryCanAddItem(level, player, invItem, input, purpose, isValid, stackLimit) > 0) {
                    return last.thenBy(inventorySlot).thenBy(((SlotPriority)addList.get(0)).comparable);
                }
            }

            return last.thenBy(10000);
        } else {
            return last;
        }
    }

    public ComparableSequence<Integer> getInventoryPriority(Level level, PlayerMob player, Inventory inventory, int inventorySlot, InventoryItem item, String purpose) {
        return super.getInventoryPriority(level, player, inventory, inventorySlot, item, purpose).beforeBy(-10000);
    }

    public int getInventoryAmount(Level level, PlayerMob player, InventoryItem item, Item requestItem, String purpose) {
        int amount = super.getInventoryAmount(level, player, item, requestItem, purpose);
        if (this.isValidPurpose(this.requestPurposes, this.isRequestPurposeBlacklist, purpose, this.isPickupDisabled(item)) && this.isValidRequestItem(requestItem)) {
            Inventory internalInventory = this.getInternalInventory(item);
            amount += internalInventory.getAmount(level, player, requestItem, purpose);
        }

        return amount;
    }

    public int getInventoryAmount(Level level, PlayerMob player, InventoryItem item, Item.Type requestType, String purpose) {
        int amount = super.getInventoryAmount(level, player, item, requestType, purpose);
        if (this.isValidRequestType(requestType)) {
            Inventory internalInventory = this.getInternalInventory(item);
            amount += internalInventory.getAmount(level, player, requestType, purpose);
        }

        return amount;
    }

    public void countIngredientAmount(Level level, PlayerMob player, Inventory inventory, int inventorySlot, InventoryItem item, String purpose, IngredientCounter handler) {
        if (purpose.equals("buy") || purpose.equals("crafting")) {
            this.getInternalInventory(item).countIngredientAmount(level, player, purpose, handler);
        }

        super.countIngredientAmount(level, player, inventory, inventorySlot, item, purpose, handler);
    }

    public void useIngredientAmount(Level level, PlayerMob player, Inventory inventory, int inventorySlot, InventoryItem item, String purpose, IngredientUser handler) {
        if (purpose.equals("buy") || purpose.equals("crafting")) {
            this.getInternalInventory(item).useIngredientAmount(level, player, purpose, handler);
        }

        super.useIngredientAmount(level, player, inventory, inventorySlot, item, purpose, handler);
    }

    public Item getInventoryFirstItem(Level level, PlayerMob player, InventoryItem item, Item[] requestItems, String purpose) {
        if (this.isValidPurpose(this.requestPurposes, this.isRequestPurposeBlacklist, purpose, this.isPickupDisabled(item)) && Arrays.stream(requestItems).anyMatch(this::isValidRequestItem)) {
            Inventory internalInventory = this.getInternalInventory(item);
            Item firstItem = internalInventory.getFirstItem(level, player, requestItems, purpose);
            if (firstItem != null) {
                return firstItem;
            }
        }

        return super.getInventoryFirstItem(level, player, item, requestItems, purpose);
    }

    public Item getInventoryFirstItem(Level level, PlayerMob player, InventoryItem item, Item.Type requestType, String purpose) {
        if (this.isValidRequestType(requestType)) {
            Inventory internalInventory = this.getInternalInventory(item);
            Item firstItem = internalInventory.getFirstItem(level, player, requestType, purpose);
            if (firstItem != null) {
                return firstItem;
            }
        }

        return super.getInventoryFirstItem(level, player, item, requestType, purpose);
    }

    public boolean inventoryAddItem(Level level, PlayerMob player, Inventory myInventory, int mySlot, InventoryItem me, InventoryItem input, String purpose, boolean isValid, int stackLimit, boolean combineIsValid, InventoryAddConsumer addConsumer) {
        if (this.isValidAddItem(input) && this.isValidPurpose(this.insertPurposes, this.isInsertPurposesBlacklist, purpose, this.isPickupDisabled(me))) {
            Inventory internalInventory = this.getInternalInventory(me);
            boolean success = internalInventory.addItem(level, player, input, purpose, addConsumer);
            if (success) {
                if (input.isNew()) {
                    me.setNew(true);
                }

                this.saveInternalInventory(me, internalInventory);
                return true;
            }
        }

        return super.inventoryAddItem(level, player, myInventory, mySlot, me, input, purpose, isValid, stackLimit, combineIsValid, addConsumer);
    }

    public int inventoryCanAddItem(Level level, PlayerMob player, InventoryItem item, InventoryItem input, String purpose, boolean isValid, int stackLimit) {
        if (this.isValidAddItem(input)) {
            Inventory internalInventory = this.getInternalInventory(item);
            return internalInventory.canAddItem(level, player, input, purpose);
        } else {
            return super.inventoryCanAddItem(level, player, item, input, purpose, isValid, stackLimit);
        }
    }

    public int removeInventoryAmount(Level level, PlayerMob player, InventoryItem item, Item requestItem, int amount, String purpose) {
        int removed = 0;
        if (this.isValidPurpose(this.requestPurposes, this.isRequestPurposeBlacklist, purpose, this.isPickupDisabled(item)) && this.isValidRequestItem(requestItem)) {
            Inventory internalInventory = this.getInternalInventory(item);
            removed = internalInventory.removeItems(level, player, requestItem, amount, purpose);
            if (removed > 0) {
                this.saveInternalInventory(item, internalInventory);
            }
        }

        return removed < amount ? removed + super.removeInventoryAmount(level, player, item, requestItem, amount, purpose) : removed;
    }

    public int removeInventoryAmount(Level level, PlayerMob player, InventoryItem item, Item.Type requestType, int amount, String purpose) {
        int removed = 0;
        if (this.isValidRequestType(requestType)) {
            Inventory internalInventory = this.getInternalInventory(item);
            removed = internalInventory.removeItems(level, player, requestType, amount, purpose);
            if (removed > 0) {
                this.saveInternalInventory(item, internalInventory);
            }
        }

        return removed < amount ? removed + super.removeInventoryAmount(level, player, item, requestType, amount, purpose) : removed;
    }

    public int removeInventoryAmount(Level level, PlayerMob player, InventoryItem item, Inventory inventory, int inventorySlot, Ingredient ingredient, int amount, Collection<InventoryItemsRemoved> collect) {
        Inventory internalInventory = this.getInternalInventory(item);
        int removed = internalInventory.removeItems(level, player, ingredient, amount, collect);
        if (removed > 0) {
            this.saveInternalInventory(item, internalInventory);
        }

        return removed < amount ? removed + super.removeInventoryAmount(level, player, item, inventory, inventorySlot, ingredient, amount, collect) : removed;
    }

    public boolean isValidItem(InventoryItem item) {
        return item == null ? true : this.isValidPouchItem(item);
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
                tooltips.add(Localization.translate("itemtooltip", "Head") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "HeadShovelWood")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Brace") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyShovelBasic")+"Name"));
                tooltips.add(Localization.translate("itemtooltip", "Shaft") + " " + Localization.translate("item", item.getGndData().getString("StockItem", "ShaftPickaxeWood")+"Name"));
            } else if (statsKey.isDown() || (statsKey.isDown() && statsKey == partsKey) || (partsKey.isDown() && statsKey == partsKey)) {
                if (statsKey == partsKey) {
                    tooltips.add(Localization.translate("itemtooltip", "Head") + " " + Localization.translate("item", item.getGndData().getString("BarrelItem", "HeadShovelWood")+"Name"));
                    tooltips.add(Localization.translate("itemtooltip", "Brace") + " " + Localization.translate("item", item.getGndData().getString("BodyItem", "BodyShovelBasic")+"Name"));
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
                tooltips.add(Localization.translate("barreltooltip", item.getGndData().getString("BarrelItem", "HeadShovelWood")+"Tip"));
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
                tooltips.add(Localization.translate("bodytooltip", item.getGndData().getString("BodyItem", "BodyShovelBasic")+"Tip"),400);
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
                if (item.getGndData().getString("BodyItem","BodyShovelBasic").equalsIgnoreCase("BodyShovelGrass")) {
                    tooltips.add(Localization.translate("itemtooltip", "rclickinvopentip"));
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
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BarrelItem", "HeadShovelWood"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("StockItem", "ShaftPickaxeWood"))).initDraw().color(color).size(size).draw(x, y);
        new GameSprite(GameTexture.fromFile("items/" + item.getGndData().getString("BodyItem", "BodyShovelBasic"))).initDraw().color(color).size(size).depth(-1F).draw(x, y);

        //This is where we generate the attack texture
        //This is the ONLY spot where OpenGL doesn't throw a fit about generating new textures
        float bonusSize = 0;
        if (ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item))) != 1){
            bonusSize = ModularBase.getFlatAttackRangeOnlyBonusMod(item,defaultBarrel(item),defaultBody(item),defaultStock(item),this.attackRange.getValue(this.getUpgradeTier(item)))-1;
        }
        File texture;
        if (bonusSize != 0) {
            int bonusSizeText = Math.round((bonusSize + 1) * 100);
            texture = new File(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadShovelWood") + item.getGndData().getString("BodyItem", "BodyShovelBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeWood") + bonusSizeText + ".png");
        } else {
            texture = new File(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadShovelWood") + item.getGndData().getString("BodyItem", "BodyShovelBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeWood") + ".png");
        }
        if (!texture.exists()) {
            GameTexture baseWorld = GameTexture.fromFile("player/weapons/shovel/ModularShovelBase.png", true);
            GameTexture barrelMatTex = GameTexture.fromFile("player/weapons/shovel/"+item.getGndData().getString("BarrelItem", "HeadShovelWood")+".png", true);
            GameTexture bodyMatTex = GameTexture.fromFile("player/weapons/shovel/"+item.getGndData().getString("BodyItem", "BodyShovelBasic")+".png", true);
            GameTexture stockMatTex = GameTexture.fromFile("player/weapons/pickaxe/"+item.getGndData().getString("StockItem", "ShaftPickaxeWood")+".png", true);
            GameTexture attackTex = new GameTexture("debug",baseWorld.getWidth(),baseWorld.getHeight());
            attackTex.merge(barrelMatTex, 0, 0, MergeFunction.NORMAL);
            attackTex.merge(stockMatTex, 6, 6, MergeFunction.NORMAL);
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
            finalized.saveTextureImage(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadShovelWood") + item.getGndData().getString("BodyItem", "BodyShovelBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeWood"));
            if (bonusSize != 0) {
                int bonusSizeText = Math.round((bonusSize + 1) * 100);
                finalizedBonus.saveTextureImage(gunsmith.modDirectory + "/attackTextures/" + item.getGndData().getString("BarrelItem", "HeadShovelWood") + item.getGndData().getString("BodyItem", "BodyShovelBasic") + item.getGndData().getString("StockItem", "ShaftPickaxeWood") + bonusSizeText);
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
            texture = new File(gunsmith.modDirectory + "/attackTextures/"+item.getGndData().getString("BarrelItem", "HeadShovelWood")+item.getGndData().getString("BodyItem", "BodyShovelBasic")+item.getGndData().getString("StockItem", "ShaftPickaxeWood")+bonusSizeText+".png");
        } else {
            texture = new File(gunsmith.modDirectory + "/attackTextures/"+item.getGndData().getString("BarrelItem", "HeadShovelWood")+item.getGndData().getString("BodyItem", "BodyShovelBasic")+item.getGndData().getString("StockItem", "ShaftPickaxeWood")+".png");
        }
        GameTexture attTex = null;
        try {
            attTex = GameTexture.fromFileRawOutside(texture.getPath(), true);
        } catch (FileNotFoundException e) {
            if (!texture.exists()) {
                player.getClient().network.sendPacket(new RequestItemPacket(player.getPlayerSlot(), item));
            }
            return new GameSprite(GameTexture.fromFile("player/weapons/demonicshovel.png"));
        }
        if (!texture.exists()) {
            player.getClient().network.sendPacket(new RequestItemPacket(player.getPlayerSlot(),item));
            return new GameSprite(GameTexture.fromFile("player/weapons/demonicshovel.png"));
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
    ///SHOVEL SPECIAL
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
    public InventoryItem startToolItemEventAbilityEvent(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int seed) {
        int animTime = this.getAttackAnimTime(item, attackerMob);
        if (defaultBody(item).getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Block")) {
            Shape circle = new Ellipse2D.Float(attackerMob.x - (this.getFlatAttackRange(item)*3) / 2, attackerMob.y - (this.getFlatAttackRange(item)*3) / 2, this.getFlatAttackRange(item)*3, this.getFlatAttackRange(item)*3);
            DeflectingToolItemMobAbilityEvent event = new DeflectingToolItemMobAbilityEvent(attackerMob, seed, item, x - attackerMob.getX(), y - attackerMob.getY() + attackHeight, animTime, animTime / 2,circle);
            attackerMob.addAndSendAttackerLevelEvent(event);
        } else {
            ToolItemMobAbilityEvent event = new ToolItemMobAbilityEvent(attackerMob, seed, item, x - attackerMob.getX(), y - attackerMob.getY() + attackHeight, animTime, animTime);
            attackerMob.addAndSendAttackerLevelEvent(event);
        }
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

        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","HeadShovelWood")).getDefaultItem(null,1);
        if (barrelItem.getGndData().getString("BarrelName","Wood").equalsIgnoreCase("Magma")) {
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
           return bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Bucket");
        } else {
            return false;
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
    public InventoryItem onLevelInteract(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int seed, GNDItemMap mapContent) {
        InventoryItem bodyItem = defaultBody(item);
        if (attackerMob.isPlayer) {
            if (level.isClient()) {
                if (bodyItem.getGndData().getString("BodyName", "Basic").equalsIgnoreCase("Bucket")) {
                    //BucketAbility(level, attackerMob);
                }
            }
        }
        return item;
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
        return 150.0F;
    }
    public float getHitboxSwingAngleOffset(InventoryItem item, int dir, float swingAngle) {
        return 50.0F;
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

    @Override
    public void tick(Inventory inventory, int i, InventoryItem inventoryItem, GameClock gameClock, GameState gameState, Entity entity, TileEntity tileEntity, WorldSettings worldSettings, Consumer<InventoryItem> consumer) {

    }
}
