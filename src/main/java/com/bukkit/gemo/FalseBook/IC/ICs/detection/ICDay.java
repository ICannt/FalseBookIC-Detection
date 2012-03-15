package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class ICDay extends BaseIC {

    public ICDay() {
        this.ICName = "IS IT DAY";
        this.ICNumber = "ic.day";
        setICGroup(ICGroup.DETECTION);
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Output: High if it is day", "", "");
        this.ICDescription = "The MC1230 outputs high if it is day when the input (the \"clock\") goes from low to high. Precisely it is high when the server time is less than 13000 ticks. Essentially this means that a low is outputted when it starts getting dark and it goes high after it has been bright for some time.<br /><br />The <a href=\"MC0230.html\">MC0230</a> is the selftriggered version.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(1, "");
        event.setLine(2, "");
        event.setLine(3, "");
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if ((currentInputs.isInputOneHigh()) && (previousInputs.isInputOneLow())) {
            switchLever(Lever.BACK, signBlock, signBlock.getWorld().getTime() < 13000L);
        }
    }
}