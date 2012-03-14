package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MC1264 extends BaseIC {

    public MC1264() {
        this.ICName = "ITEM SENSOR";
        this.ICNumber = "[MC1264]";
        setICGroup(ICGroup.DETECTION);
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "item to detect");
        this.ICDescription = "The MC1264 outputs high if a specified item is detected in the given distance around the ic, when the input (the \"clock\") goes from low to high.<br /><br />The <a href=\"MC0264.html\">MC0264</a> is the selftriggered version.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (!Parser.isIntegerWithOffset(event.getLine(2))) {
            SignUtils.cancelSignCreation(event, "Line 3 must be a number or a number with a vector.");
            return;
        }

        Integer radius = Parser.getIntegerFromOffsetLine(event.getLine(2), 0);
        Vector vector = Parser.getVectorFromOffsetLine(event.getLine(2));
        if (radius < 0) {
            radius = 0;
        }
        if ((vector.getBlockX() != 0) || (vector.getBlockY() != 0) || (vector.getBlockZ() != 0)) {
            event.setLine(2, radius + "=" + vector.getBlockX() + ":" + vector.getBlockY() + ":" + vector.getBlockZ());
        } else {
            event.setLine(2, radius.toString());
        }
        if (SignUtils.parseLineToItemListWithSize(event.getLine(3), "-", false, 1, 9999) == null) {
            SignUtils.cancelSignCreation(event, "Please enter at least one item in Line 4.");
            return;
        }
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if ((currentInputs.isInputOneHigh()) && (previousInputs.isInputOneLow())) {
            if (!Parser.isIntegerWithOffset(signBlock.getLine(2))) {
                return;
            }
            int range = Parser.getIntegerFromOffsetLine(signBlock.getLine(2), 0);
            if (range < 0) {
                range = 0;
            }
            Vector offsetVector = Parser.getVectorFromOffsetLine(signBlock.getLine(2));

            boolean result = false;
            Location blockLoc = getICBlock(signBlock, offsetVector);

            ArrayList<FBItemType> itemList = SignUtils.parseLineToItemListWithSize(signBlock.getLine(3), "-", false, 1, 9999);
            if (itemList == null) {
                return;
            }

            HashMap<String, Integer> foundList = new HashMap<String, Integer>();
            ArrayList<Item> bukkitItemList = (ArrayList<Item>) signBlock.getWorld().getEntitiesByClass(Item.class);
            ItemStack itemStack = null;
            for (Item item : bukkitItemList) {
                itemStack = item.getItemStack();
                for (int i = 0; i < itemList.size(); i++) {
                    if ((itemStack.getTypeId() != itemList.get(i).getItemID()) || ((!itemList.get(i).usesWildcart()) && (itemStack.getDurability() != itemList.get(i).getItemData()))
                            || (!BlockUtils.isInRange(item.getLocation(), blockLoc, range))) {
                        continue;
                    }
                    int oldAmount = 0;
                    if (foundList.containsKey(itemList.get(i).getString())) {
                        oldAmount = foundList.get(itemList.get(i).getString()).intValue();
                    }
                    oldAmount += itemStack.getAmount();
                    foundList.put(itemList.get(i).getString(), Integer.valueOf(oldAmount));
                    if ((itemList.get(i).getAmount() == -1) || (oldAmount >= itemList.get(i).getAmount())) {
                        result = true;
                        break;
                    }
                }

            }

            bukkitItemList.clear();
            bukkitItemList = null;

            blockLoc = null;
            itemList.clear();
            itemList = null;

            switchLever(Lever.BACK, signBlock, result);
        }
    }
}