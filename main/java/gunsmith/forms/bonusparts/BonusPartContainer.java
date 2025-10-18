package gunsmith.forms.bonusparts;

import com.sun.jna.StringArray;
import gunsmith.forms.gunsmith.GunPartItemContainerSlot;
import gunsmith.objects.bonuspartsbench.BonusPartStationObjectEntity;
import necesse.engine.GameLog;
import necesse.engine.GameTileRange;
import necesse.engine.GlobalData;
import necesse.engine.Settings;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.objectEntity.interfaces.OEInventory;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.InventoryRange;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.EmptyCustomAction;
import necesse.inventory.container.object.CraftingStationContainer;
import necesse.inventory.container.slots.ContainerSlot;
import necesse.inventory.item.upgradeUtils.UpgradableItem;
import necesse.inventory.item.upgradeUtils.UpgradedItem;
import necesse.inventory.recipe.CanCraft;
import necesse.inventory.recipe.Recipe;
import necesse.level.maps.Level;
import necesse.level.maps.multiTile.MultiTile;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class BonusPartContainer extends Container {
    public final int BARREL_SLOT;
    public final int BODY_SLOT;
    public final int STOCK_SLOT;
    public String NAME;
    public static int MAX_NAME_LENGTH = 40;
    public final BonusPartStationObjectEntity upgradeEntity;
    public final EmptyCustomAction craftButton;
    public final GameTileRange ingredientRange;
    private LinkedHashSet<Inventory> nearbyInventories = new LinkedHashSet();

    public BonusPartContainer(final NetworkClient client, int uniqueSeed, BonusPartStationObjectEntity upgradeEntity, PacketReader reader) {
        super(client, uniqueSeed);
        this.upgradeEntity = upgradeEntity;
        this.BARREL_SLOT = this.addSlot(new BonusPartItemContainerSlot(upgradeEntity.inventory, 0));
        this.addInventoryQuickTransfer(this.BARREL_SLOT, this.BARREL_SLOT);
        this.BODY_SLOT = this.addSlot(new BonusPartItemContainerSlot(upgradeEntity.inventory, 1));
        this.addInventoryQuickTransfer(this.BODY_SLOT, this.BODY_SLOT);
        this.STOCK_SLOT = this.addSlot(new BonusPartItemContainerSlot(upgradeEntity.inventory, 2));
        this.NAME = "";
        this.addInventoryQuickTransfer(this.STOCK_SLOT, this.STOCK_SLOT);
        MultiTile multiTile = upgradeEntity.getLevelObject().getMultiTile();
        Rectangle tileRectangle = multiTile.getTileRectangle(0, 0);
        this.ingredientRange = new GameTileRange(CraftingStationContainer.nearbyCraftTileRange, tileRectangle);
        this.nearbyInventories.addAll(this.craftInventories);
        //Iterator var7 = this.getNearbyInventories(upgradeEntity.getLevel(), upgradeEntity.getTileX(), upgradeEntity.getTileY(), this.ingredientRange, OEInventory::canUseForNearbyCrafting).iterator();

        /*while (var7.hasNext()) {
            InventoryRange inventoryRange = (InventoryRange) var7.next();
            this.nearbyInventories.add(inventoryRange.inventory);
        }*/

        this.craftButton = (EmptyCustomAction) this.registerAction(new EmptyCustomAction() {
            protected void run() {
                if (sameItem() || allHaveBonus()) {
                    InventoryItem result = getCraftedItem();
                    if (BonusPartContainer.this.getSlot(BonusPartContainer.this.CLIENT_DRAGGING_SLOT).isClear()) {
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.CLIENT_DRAGGING_SLOT).setItem(result);
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).setItem((InventoryItem) null);
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).setItem((InventoryItem) null);
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).setItem((InventoryItem) null);
                    } else {
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).setItem(result);
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).setItem((InventoryItem) null);
                        BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).setItem((InventoryItem) null);
                    }
                }
            }
        });
    }

    public boolean sameItem() {
        if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).isClear() || BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).isClear() || BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).isClear()) {
            return false;
        } else {
            InventoryItem item1 = BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem();
            InventoryItem item2 = BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem();
            InventoryItem item3 = BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem();

            return item1.item == item2.item && item1.item == item3.item;
        }
    }
    public boolean allHaveBonus() {
        if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).isClear() || BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).isClear() || BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).isClear()) {
            return false;
        } else {
            boolean slot1Bonus;
            boolean slot2Bonus;
            boolean slot3Bonus;
            InventoryItem item1 = BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem();
            InventoryItem item2 = BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem();
            InventoryItem item3 = BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem();

            slot1Bonus = item1.getGndData().hasKey("BarrelBonus") || item1.getGndData().hasKey("BodyBonus") || item1.getGndData().hasKey("StockBonus");
            slot2Bonus = item2.getGndData().hasKey("BarrelBonus") || item2.getGndData().hasKey("BodyBonus") || item2.getGndData().hasKey("StockBonus");
            slot3Bonus = item3.getGndData().hasKey("BarrelBonus") || item3.getGndData().hasKey("BodyBonus") || item3.getGndData().hasKey("StockBonus");

            return slot1Bonus && slot2Bonus && slot3Bonus;

            /*String slot1Bonus ="";
            String slot2Bonus ="";
            String slot3Bonus ="";
            InventoryItem item1 = BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem();
            InventoryItem item2 = BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem();
            InventoryItem item3 = BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem();

            if (item1.getGndData().hasKey("BarrelBonus")) {
                slot1Bonus = item1.getGndData().getString("BarrelBonus");
            } else if (item1.getGndData().hasKey("BodyBonus")) {
                slot1Bonus = item1.getGndData().getString("BodyBonus");
            } else if (item1.getGndData().hasKey("StockBonus")) {
                slot1Bonus = item1.getGndData().getString("StockBonus");
            } else {
                slot1Bonus ="";
            }

            if (item2.getGndData().hasKey("BarrelBonus")) {
                slot2Bonus = item2.getGndData().getString("BarrelBonus");
            } else if (item2.getGndData().hasKey("BodyBonus")) {
                slot2Bonus = item2.getGndData().getString("BodyBonus");
            } else if (item2.getGndData().hasKey("StockBonus")) {
                slot2Bonus = item2.getGndData().getString("StockBonus");
            } else {
                slot2Bonus ="";
            }

            if (item3.getGndData().hasKey("BarrelBonus")) {
                slot3Bonus = item3.getGndData().getString("BarrelBonus");
            } else if (item3.getGndData().hasKey("BodyBonus")) {
                slot3Bonus = item3.getGndData().getString("BodyBonus");
            } else if (item3.getGndData().hasKey("StockBonus")) {
                slot3Bonus = item3.getGndData().getString("StockBonus");
            } else {
                slot3Bonus ="";
            }
            if (!slot1Bonus.isEmpty() && !slot2Bonus.isEmpty() && !slot3Bonus.isEmpty()) {
                return true;
            } else {
                return false;
            }*/
        }
    }

    private boolean useNearbyInventories() {
        return this.client.isServer() ? this.client.craftingUsesNearbyInventories : (Boolean) Settings.craftingUseNearby.get();
    }

    public Collection<Inventory> getCraftInventories() {
        return (Collection) (this.useNearbyInventories() ? this.nearbyInventories : super.getCraftInventories());
    }

    public UpgradedItem getUpgradedItem() {
        ContainerSlot slot = this.getSlot(this.BARREL_SLOT);
        if (!slot.isClear()) {
            InventoryItem slotItem = slot.getItem();
            if (slotItem.item instanceof UpgradableItem && ((UpgradableItem) slotItem.item).getCanBeUpgradedError(slotItem) == null) {
                return ((UpgradableItem) slotItem.item).getUpgradedItem(slotItem);
            }
        }

        return null;
    }

    public InventoryItem getCraftedItem() {
        if (allHaveBonus()) {
            //get random bonus item
            int randomSlot = GameRandom.globalRandom.getIntBetween(0,2);
            InventoryItem result;
            if (randomSlot == 0) {
                result = new InventoryItem(BonusPartContainer.this.getSlot(this.BARREL_SLOT).getItem().item, 1);
            } else if (randomSlot == 1) {
                result = new InventoryItem(BonusPartContainer.this.getSlot(this.BODY_SLOT).getItem().item, 1);
            } else {
                result = new InventoryItem(BonusPartContainer.this.getSlot(this.STOCK_SLOT).getItem().item, 1);
            }

            //get random bonus type
            int randomBonus = GameRandom.globalRandom.getIntBetween(0,2);
            float randomBonusValue = 0;
            InventoryItem resultBonus;
            if (randomBonus == 0) {
                resultBonus = this.getSlot(this.BARREL_SLOT).getItem();
            } else if (randomBonus == 1) {
                resultBonus = this.getSlot(this.BODY_SLOT).getItem();
            } else {
                resultBonus = this.getSlot(this.STOCK_SLOT).getItem();
            }
            String bonusType = "";
            if (resultBonus.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Barrel")) {
                bonusType = resultBonus.getGndData().getString("BarrelBonus");
                randomBonusValue = resultBonus.getGndData().getFloat("BarrelBonusValue");
            } else if (resultBonus.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Body")) {
                bonusType = resultBonus.getGndData().getString("BodyBonus");
                randomBonusValue = resultBonus.getGndData().getFloat("BodyBonusValue");
            } else if (resultBonus.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Stock")) {
                GameLog.warn.println("wack");
                bonusType = resultBonus.getGndData().getString("StockBonus");
                randomBonusValue = resultBonus.getGndData().getFloat("StockBonusValue");
            }

            //set bonus type
            if (result.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Barrel")) {
                result.getGndData().setString("BarrelBonus", bonusType);
            } else if (result.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Body")) {
                result.getGndData().setString("BodyBonus", bonusType);
            } else if (result.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Stock")) {
                GameLog.warn.println("wack2");
                result.getGndData().setString("StockBonus", bonusType);
            }

            //calculate new modifier value
            float low = result.item.getDefaultItem(null,1).getGndData().getFloat("Low" + bonusType);

            //check for the highest
            if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getString("BarrelBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getFloat("BarrelBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getFloat("BarrelBonusValue");
                }
            } else if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getString("BodyBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getFloat("BodyBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getFloat("BodyBonusValue");
                }
            } else if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getString("StockBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getFloat("StockBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().getGndData().getFloat("StockBonusValue");
                }
            }
            if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getString("BarrelBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getFloat("BarrelBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getFloat("BarrelBonusValue");
                }
            } else if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getString("BodyBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getFloat("BodyBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getFloat("BodyBonusValue");
                }
            } else if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getString("StockBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getFloat("StockBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.BODY_SLOT).getItem().getGndData().getFloat("StockBonusValue");
                }
            }
            if (BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getString("BarrelBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getFloat("BarrelBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getFloat("BarrelBonusValue");
                }
            } else if (BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getString("BodyBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getFloat("BodyBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getFloat("BodyBonusValue");
                }
            } else if (BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getString("StockBonus").equalsIgnoreCase(bonusType)) {
                if (BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getFloat("StockBonusValue") > low) {
                    low = BonusPartContainer.this.getSlot(BonusPartContainer.this.STOCK_SLOT).getItem().getGndData().getFloat("StockBonusValue");
                }
            }

            float high = result.item.getDefaultItem(null,1).getGndData().getFloat("High" + bonusType);
            float newValue = low*GameRandom.globalRandom.getFloatBetween(1.35F,1.7F);
            if (newValue > high) {
                newValue = high;
            }
            newValue = Math.round(Math.round(newValue * 100.0) / 100.0);

            //set modifier value
            if (result.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Barrel")) {
                result.getGndData().setFloat("BarrelBonusValue", newValue);
            } else if (result.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Body")) {
                result.getGndData().setFloat("BodyBonusValue", newValue);
            } else if (result.item.getDefaultItem(null,1).getGndData().getString("Part").equalsIgnoreCase("Stock")) {
                GameLog.warn.println("wack3");
                result.getGndData().setFloat("StockBonusValue", newValue);
            }
            GameLog.debug.println("Generated Item: " + result.getItemDisplayName());
            GameLog.debug.println("Generated Bonus: " + bonusType);
            GameLog.debug.println("Generated Bonus Value: " + newValue);

            return result;
        } else if (sameItem() && !allHaveBonus()) {
            InventoryItem result = new InventoryItem(BonusPartContainer.this.getSlot(BonusPartContainer.this.BARREL_SLOT).getItem().item,1);
            result.getGndData().setBoolean("BonusActivate",true);
            result.getGndData().setBoolean("isCrafted",true);
            return result;
        } else {
            return null;
        }
    }


    public void tick() {
        super.tick();
        if (this.client.isClient() && (Boolean) Settings.craftingUseNearby.get()) {
            boolean updateCraftable = false;

            Inventory inv;
            for (Iterator var2 = this.nearbyInventories.iterator(); var2.hasNext(); inv.clean()) {
                inv = (Inventory) var2.next();
                if (inv.isDirty()) {
                    updateCraftable = true;
                }
            }

            if (updateCraftable) {
                GlobalData.updateCraftable();
            }
        }

    }

    public boolean isValid(ServerClient client) {
        if (!super.isValid(client)) {
            return false;
        } else {
            return !this.upgradeEntity.removed();
        }
    }

    public static void openAndSendContainer(int containerID, ServerClient client, Level level, int tileX, int tileY, Packet extraContent) {
        if (!level.isServer()) {
            throw new IllegalStateException("Level must be a server level");
        } else {
            Packet packet = new Packet();
            PacketWriter writer = new PacketWriter(packet);
            if (extraContent != null) {
                writer.putNextContentPacket(extraContent);
            }

            PacketOpenContainer p = PacketOpenContainer.LevelObject(containerID, tileX, tileY, packet);
            ContainerRegistry.openAndSendContainer(client, p);
        }
    }

    public static void openAndSendContainer(int containerID, ServerClient client, Level level, int tileX, int tileY) {
        openAndSendContainer(containerID, client, level, tileX, tileY, (Packet) null);
    }
}