package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class ICLightSensor extends BaseIC {

    public ICLightSensor() {
        this.ICName = "LIGHT SENSOR";
        this.ICNumber = "ic.lightsensor";
        setICGroup(ICGroup.DETECTION);
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Output: High if threshold is reached", "", "");
        this.chipState.setLines("Minimum light level, 0 to 15.", "");
        this.ICDescription = "The MC1262 checks to see if the light level of the block above the block behind the IC sign is greater than or equal to a configurable threshold whenever the input goes from low to high.<br /><br />The <a href=\"MC0262.html\">MC0262</a> is the selftriggered version.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(2, "");
        event.setLine(3, "");

        if (event.getLine(1).length() < 1) {
            event.setLine(1, "15");
        }

        if (!Parser.isInteger(event.getLine(1))) {
            SignUtils.cancelSignCreation(event, "The second line must be a number or be blank.");
            return;
        }
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if ((currentInputs.isInputOneHigh()) && (previousInputs.isInputOneLow())) {
            if (!Parser.isInteger(signBlock.getLine(1))) {
                return;
            }
            int minLight = Parser.getInteger(signBlock.getLine(1), 5);

            if (getICBlock(signBlock).getBlock().getRelative(0, 1, 0).getLightLevel() >= minLight) {
                switchLever(Lever.BACK, signBlock, true);
            } else {
                switchLever(Lever.BACK, signBlock, false);
            }
        }
    }
}