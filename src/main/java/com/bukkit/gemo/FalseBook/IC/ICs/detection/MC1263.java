package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1263 extends BaseIC {

    public MC1263() {
        this.ICName = "BLOCK SENSOR";
        this.ICNumber = "[MC1263]";
        setICGroup(ICGroup.DETECTION);
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Output: High if the block is present", "", "");
        this.chipState.setLines("Y offset, with 0 being the IC block. Leave blank to default to the block below.", "BlockID[:SubID]");
        this.ICDescription = "The MC1263 checks for the presence of a specified block relative to the block behind the IC sign whenever the input goes from low to high. By default it checks the block directly underneath but this can be changed.<br /><br />The <a href=\"MC0263.html\">MC0263</a> is the selftriggered version.";
    }

    public void checkCreation(SignChangeEvent event) {
        if (event.getLine(2).length() < 1) {
            event.setLine(2, "-1");
        }

        String yOffset = event.getLine(2);
        try {
            if (yOffset.length() > 0) {
                Integer.parseInt(yOffset);
            }
        } catch (NumberFormatException e) {
            SignUtils.cancelSignCreation(event, "The third line must be a number or be blank.");
            return;
        }

        String[] split = event.getLine(3).split(":");
        try {
            if (!BlockUtils.isValidBlock(Integer.valueOf(split[0]).intValue())) {
                SignUtils.cancelSignCreation(event, "Block not found");
                return;
            }
        } catch (Exception e) {
            if (!BlockUtils.isValidBlock(BlockUtils.getItemIDFromName(event.getLine(3)))) {
                SignUtils.cancelSignCreation(event, "Block not found");
                return;
            }
        }
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if ((currentInputs.isInputOneHigh()) && (previousInputs.isInputOneLow())) {
            Block block = null;

            ArrayList<FBItemType> thisItems = SignUtils.parseLineToItemListWithSize(signBlock.getLine(3), "-", false, 1, 1);
            int offSet;
            if (thisItems == null) {
                return;
            }
            try {
                offSet = Integer.valueOf(signBlock.getLine(2)).intValue();
            } catch (Exception e) {
                offSet = -1;
            }
            block = getICBlock(signBlock).getBlock().getRelative(0, offSet, 0);
            FBItemType item = thisItems.get(0);

            if ((block.getTypeId() == item.getItemID()) && ((block.getData() == item.getItemData()) || (item.usesWildcart()))) {
                switchLever(Lever.BACK, signBlock, true);
            } else {
                switchLever(Lever.BACK, signBlock, false);
            }
            block = null;
        }
    }
}