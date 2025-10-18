package gunsmith.forms.bonusparts;

import gunsmith.gunsmith;
import necesse.engine.GameLog;
import necesse.engine.GlobalData;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.network.client.Client;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.fairType.FairType;
import necesse.gfx.fairType.TypeParsers;
import necesse.gfx.fairType.parsers.TypeParser;
import necesse.gfx.forms.ContainerComponent;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.containerSlot.FormContainerSlot;
import necesse.gfx.forms.components.lists.FormRecipeList;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.containerComponent.ContainerFormSwitcher;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.InventoryItem;
import necesse.inventory.InventoryUpdateListener;
import necesse.inventory.item.upgradeUtils.UpgradedItem;
import necesse.inventory.recipe.CanCraft;

import java.awt.*;
import java.util.Iterator;

public class BonusPartContainerForm<T extends BonusPartContainer> extends ContainerFormSwitcher<T> {
    public Form mainForm = (Form)this.addComponent(new Form(400, 120));
    public UpgradedItem lastUpgradedItem;
    private final int upgradeContentY;
    private FormContentBox upgradeContent;
    public FormLabelEdit label;
    public FormLocalLabel instructionsStart;
    public FormLocalLabel instructionsSame;
    public FormLocalLabel instructionsDifferent;
    public FormLocalLabel instructionsTotals;
    public FormTextInput inputField;
    protected FormLocalTextButton upgradeButton;
    protected FormRecipeList recipeUpdateListener;
    protected InventoryUpdateListener inventoryUpdateListener;

    public BonusPartContainerForm(Client client, final T container) {
        super(client, container);
        FormFlow flow = new FormFlow(5);
        this.mainForm.addComponent((FormLocalLabel)flow.nextY(new FormLocalLabel(container.upgradeEntity.getObject().getLocalization(), new FontOptions(20), 0, this.mainForm.getWidth() / 2, 5), 5));
        this.instructionsStart = this.mainForm.addComponent((FormLocalLabel) flow.nextY(new FormLocalLabel("ui", "bonusPartsInstructionsStart", new FontOptions(16).alpha(10), 0, this.mainForm.getWidth() / 2, 4, this.mainForm.getWidth() - 10), 5));
        this.instructionsDifferent = this.mainForm.addComponent((FormLocalLabel)flow.nextY(new FormLocalLabel("ui", "bonusPartsInstructionsDifferent", new FontOptions(16), 0, this.mainForm.getWidth() / 2, 4, this.mainForm.getWidth() - 10), 5));
        this.instructionsSame = this.mainForm.addComponent((FormLocalLabel) flow.sameY(new FormLocalLabel("ui", "bonusPartsInstructionsSame", new FontOptions(16).alpha(10), 0, this.mainForm.getWidth() / 2, 4, this.mainForm.getWidth() - 10), 5));
        this.instructionsTotals = this.mainForm.addComponent((FormLocalLabel)flow.nextY(new FormLocalLabel("ui", "bonusPartsInstructionsTotals", new FontOptions(16), 0, this.mainForm.getWidth() / 2, 4, this.mainForm.getWidth() - 10), 5));
        int containerRow = flow.next(45);
        final GameTexture[] decalsTop = new GameTexture[]{gunsmith.icon_barrel,gunsmith.icon_pickaxe,gunsmith.icon_shovel};
        final GameTexture[] decalsMiddle = new GameTexture[]{gunsmith.icon_body,gunsmith.icon_brace,gunsmith.icon_brace};
        final GameTexture[] decalsBottom = new GameTexture[]{gunsmith.icon_stock,gunsmith.icon_shaft,gunsmith.icon_shaft};
        this.mainForm.addComponent(new FormContainerSlot(client, container, container.BARREL_SLOT, this.mainForm.getWidth() / 2 - 90, containerRow) {
            public void drawDecal(PlayerMob perspective) {
                int decal = (int)(System.currentTimeMillis() % (long)(decalsTop.length * 1000)) / 1000;
                this.setDecal(decalsTop[decal]);
                super.drawDecal(perspective);
            }
        });
        this.mainForm.addComponent(new FormContainerSlot(client, container, container.BODY_SLOT, this.mainForm.getWidth() / 2 - 10, containerRow) {
            public void drawDecal(PlayerMob perspective) {
                int decal = (int)(System.currentTimeMillis() % (long)(decalsMiddle.length * 1000)) / 1000;
                this.setDecal(decalsMiddle[decal]);
                super.drawDecal(perspective);
            }
        });
        this.mainForm.addComponent(new FormContainerSlot(client, container, container.STOCK_SLOT, this.mainForm.getWidth() / 2 + 70, containerRow) {
            public void drawDecal(PlayerMob perspective) {
                int decal = (int)(System.currentTimeMillis() % (long)(decalsBottom.length * 1000)) / 1000;
                this.setDecal(decalsBottom[decal]);
                super.drawDecal(perspective);
            }
        });
        this.upgradeContentY = flow.next();
        int upgradeButtonMaxWidth = 300;
        int upgradeButtonWidth = Math.min(this.mainForm.getWidth() - 8, upgradeButtonMaxWidth);
        this.upgradeButton = (FormLocalTextButton)this.mainForm.addComponent(new FormLocalTextButton("ui", "bonusMergeButton", this.mainForm.getWidth() / 2 - upgradeButtonWidth / 2, flow.next(28), upgradeButtonWidth, FormInputSize.SIZE_24, ButtonColor.BASE) {
        });
        this.upgradeButton.onClicked((e) -> container.craftButton.runAndSend());
        this.lastUpgradedItem = container.getUpgradedItem();
        this.updateFormContent();
        this.makeCurrent(this.mainForm);
    }

    protected void init() {
        super.init();
        GlobalData.craftingLists.add(this.recipeUpdateListener = new FormRecipeList() {
            public void updateCraftable() {
                BonusPartContainerForm.this.updateFormContent();
            }

            public void updateRecipes() {
            }
        });
        this.inventoryUpdateListener = ((BonusPartContainer)this.container).upgradeEntity.inventory.addSlotUpdateListener(new InventoryUpdateListener() {
            public void onSlotUpdate(int slot) {
                BonusPartContainerForm.this.lastUpgradedItem = ((BonusPartContainer) BonusPartContainerForm.this.container).getUpgradedItem();
                BonusPartContainerForm.this.updateFormContent();
            }

            public boolean isDisposed() {
                return BonusPartContainerForm.this.isDisposed();
            }
        });
    }

    public static TypeParser<?>[] getParsers(FontOptions fontOptions) {
        return new TypeParser[]{TypeParsers.GAME_COLOR, TypeParsers.REMOVE_URL, TypeParsers.URL_OPEN, TypeParsers.ItemIcon(fontOptions.getSize()), TypeParsers.MobIcon(fontOptions.getSize()), TypeParsers.InputIcon(fontOptions)};
    }

    private void runEditUpdate() {
        if (!this.label.isTyping()) {
            //this.container.NAME = this.label.getText();

        }
        this.container.NAME = label.getText();
    }

    public void updateFormContent() {
        if (this.upgradeContent != null) {
            this.mainForm.removeComponent(this.upgradeContent);
        }
        InventoryItem itemSlot1 = this.container.upgradeEntity.inventory.getItem(0);
        InventoryItem itemSlot2 = this.container.upgradeEntity.inventory.getItem(1);
        InventoryItem itemSlot3 = this.container.upgradeEntity.inventory.getItem(2);

        if (this.container.upgradeEntity.inventory.isSlotClear(0) && this.container.upgradeEntity.inventory.isSlotClear(1) && this.container.upgradeEntity.inventory.isSlotClear(2)) {
            this.instructionsSame.setText("");
            this.instructionsDifferent.setText("");
            this.instructionsTotals.setText("");
            this.instructionsSame.setColor(Color.BLACK);
            this.instructionsDifferent.setColor(Color.BLACK);
            this.instructionsTotals.setColor(Color.BLACK);
        } else if ((!this.container.upgradeEntity.inventory.isSlotClear(0) && (!itemSlot1.getGndData().getString("BarrelBonus").isEmpty() || !itemSlot1.getGndData().getString("BodyBonus").isEmpty()  || !itemSlot1.getGndData().getString("StockBonus").isEmpty())) || (!this.container.upgradeEntity.inventory.isSlotClear(1) && (!itemSlot2.getGndData().getString("BarrelBonus").isEmpty() || !itemSlot2.getGndData().getString("BodyBonus").isEmpty()  || !itemSlot2.getGndData().getString("StockBonus").isEmpty())) || (!this.container.upgradeEntity.inventory.isSlotClear(2) && (!itemSlot3.getGndData().getString("BarrelBonus").isEmpty() || !itemSlot3.getGndData().getString("BodyBonus").isEmpty()  || !itemSlot3.getGndData().getString("StockBonus").isEmpty()))) {
            this.instructionsSame.setColor(Color.DARK_GRAY);
            this.instructionsSame.setText("");
            this.instructionsDifferent.setText(Localization.translate("ui", "bonusPartsInstructionsDifferent"), 400);
            this.instructionsTotals.setText(Localization.translate("ui", "bonusPartsInstructionsTotals"), 400);
            this.instructionsDifferent.setColor(Color.BLACK);
            this.instructionsTotals.setColor(Color.BLACK);
        } else {
            this.instructionsSame.setColor(Color.BLACK);
            this.instructionsSame.setText(Localization.translate("ui", "bonusPartsInstructionsSame"), 400);
            this.instructionsDifferent.setText("");
            this.instructionsTotals.setText("");
            this.instructionsDifferent.setColor(Color.DARK_GRAY);
            this.instructionsTotals.setColor(Color.DARK_GRAY);
        }
        FormFlow flow = new FormFlow(this.upgradeContentY);
        this.upgradeContent = (FormContentBox)this.mainForm.addComponent(new FormContentBox(0, flow.next(), this.mainForm.getWidth(), 300));
        FormFlow upgradeFlow = new FormFlow();
        CanCraft canCraft = null;
        /*if (container.sameType()) {
            *//*this.upgradeContent.addComponent((FormLocalLabel)upgradeFlow.nextY(new FormLocalLabel("ui", "benchOutput", new FontOptions(20), 0, this.upgradeContent.getWidth() / 2, 0, this.upgradeContent.getWidth() - 10), 2));
            FormFairTypeLabel label;
            InventoryItem result = container.getCraftedItem();
            StringBuilder displayString = new StringBuilder("[item=" + result.item.getStringID() + "]{");
            Iterator<Integer> GNDKeys = result.getGndData().getDirtyKeys().iterator();
            while (GNDKeys.hasNext()) {
                Integer keyValue = GNDKeys.next();
                displayString.append(keyValue).append("={");
                displayString.append("gndType=").append(result.getGndData().getItem(keyValue).getStringID()).append(",");
                displayString.append("value=").append(result.getGndData().getItem(keyValue)).append("}");
                if (GNDKeys.hasNext()) {
                    displayString.append(",");
                }
            }
            displayString.append("}");
            String display = displayString.toString();
            label = new FormFairTypeLabel(display, this.upgradeContent.getMinContentWidth() / 2, 10);
            label.setMaxWidth(this.upgradeContent.getMinContentWidth() - 10);
            label.setTextAlign(FairType.TextAlign.CENTER);
            label.setParsers(new TypeParser[]{TypeParsers.ItemIcon(30)});
            this.upgradeContent.addComponent((FormFairTypeLabel)upgradeFlow.nextY(label, 2));*//*


        } else {
            //this.label.setText("");
        }*/

        if (this.upgradeContent.getHeight() > upgradeFlow.next()) {
            this.upgradeContent.setHeight(upgradeFlow.next());
        }

        this.upgradeContent.setContentBox(new Rectangle(this.upgradeContent.getWidth(), upgradeFlow.next()));
        flow.next(this.upgradeContent.getHeight());
        flow.nextY(this.upgradeButton, 4);

        if (!this.container.upgradeEntity.inventory.isSlotClear(0) && !this.container.upgradeEntity.inventory.isSlotClear(1) && !this.container.upgradeEntity.inventory.isSlotClear(2)) {
            if (container.sameItem() || container.allHaveBonus()) {
                this.upgradeButton.setActive(true);
            }
        } else {
            this.upgradeButton.setActive(false);
        }

        this.mainForm.setHeight(flow.next());
        this.onWindowResized(WindowManager.getWindow());
    }

    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        /*if (this.useNearby.isHovering()) {
            Renderer.hudManager.fadeHUD();
        }*/

        super.draw(tickManager, perspective, renderBox);
    }

    public void onWindowResized(GameWindow window) {
        super.onWindowResized(window);
        ContainerComponent.setPosFocus(this.mainForm);
    }

    public boolean shouldOpenInventory() {
        return true;
    }

    public void dispose() {
        super.dispose();
        Settings.hasCraftingListExpanded.cleanListeners();
        Settings.hasCraftingFilterExpanded.cleanListeners();
        /*if (this.rangeElement != null) {
            this.rangeElement.remove();
        }*/

        if (this.recipeUpdateListener != null) {
            GlobalData.craftingLists.remove(this.recipeUpdateListener);
        }

        if (this.inventoryUpdateListener != null) {
            this.inventoryUpdateListener.dispose();
        }

    }
}