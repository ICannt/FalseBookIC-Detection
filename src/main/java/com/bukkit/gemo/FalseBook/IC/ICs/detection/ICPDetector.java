package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class ICPDetector extends BaseIC {

    public ICPDetector() {
        this.ICName = "P-DETECTION";
        this.ICNumber = "ic.pdetector";
        setICGroup(ICGroup.DETECTION);
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "p:<part of a playername> OR g:<groupname> OR -g:<groupname>");
        this.ICDescription = "The MC1272 outputs high if a specified player is detected in the given distance around the ic, when the input (the \"clock\") goes from low to high.<br /><br />The <a href=\"MC0272.html\">MC0272</a> is the selftriggered version.";
    }

    public void checkCreation(SignChangeEvent event) {
        event.setLine(3, "");
        
        if (!Parser.isIntegerWithOffset(event.getLine(1))) {
            SignUtils.cancelSignCreation(event, "Line 2 must be a number or a number with a vector.");
            return;
        }

        Integer radius = Parser.getIntegerFromOffsetLine(event.getLine(1), 0);
        Vector vector = Parser.getVectorFromOffsetLine(event.getLine(1));
        if (radius < 0) {
            radius = 0;
        }
        if ((vector.getBlockX() != 0) || (vector.getBlockY() != 0) || (vector.getBlockZ() != 0)) {
            event.setLine(1, radius + "=" + vector.getBlockX() + ":" + vector.getBlockY() + ":" + vector.getBlockZ());
        } else {
            event.setLine(1, radius.toString());
        }
        if (event.getLine(2).length() < 0) {
            SignUtils.cancelSignCreation(event, "Please enter a Playername in Line 3");
            return;
        }
        String[] split = event.getLine(2).split(":");
        if (split.length < 2) {
            SignUtils.cancelSignCreation(event, "Wrong syntax in Line 3. Use p:<playername> or g:<groupname> or -g:<groupname>");
            return;
        }
        if ((!split[0].equalsIgnoreCase("p")) && (!split[0].equalsIgnoreCase("g")) && (!split[0].equalsIgnoreCase("-g"))) {
            SignUtils.cancelSignCreation(event, "Wrong syntax in Line 3. Use p:<playername> or g:<groupname> or -g:<groupname>");
            return;
        }
    }

    public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
        if ((currentInputs.isInputOneHigh()) && (previousInputs.isInputOneLow())) {
            if (!Parser.isIntegerWithOffset(signBlock.getLine(1))) {
                return;
            }
            int range = Parser.getIntegerFromOffsetLine(signBlock.getLine(1), 0);
            if (range < 0) {
                range = 0;
            }
            Vector offsetVector = Parser.getVectorFromOffsetLine(signBlock.getLine(1));

            boolean result = false;
            Location blockLoc = getICBlock(signBlock, offsetVector);

            String[] split = signBlock.getLine(2).split(":");
            if (split.length < 2) {
                return;
            }
            if (split[0].equalsIgnoreCase("p")) {
                Player[] playerList = Bukkit.getServer().getOnlinePlayers();
                for (Player player : playerList) {
                    if ((player.isDead()) || (!player.isOnline())) {
                        continue;
                    }
                    if (player.getName().toLowerCase().indexOf(split[1].toLowerCase()) <= -1) {
                        continue;
                    }
                    if (BlockUtils.isInRange(player.getLocation(), blockLoc, range)) {
                        result = true;
                        break;
                    }
                }
                switchLever(Lever.BACK, signBlock, result);
                playerList = (Player[]) null;
            } else if (split[0].equalsIgnoreCase("g")) {
                Player[] playerList = Bukkit.getServer().getOnlinePlayers();
                for (Player player : playerList) {
                    if ((player.isDead()) || (!player.isOnline())) {
                        continue;
                    }
                    if (!UtilPermissions.isPlayerInGroup(player, split[1], signBlock.getWorld().getName())) {
                        continue;
                    }
                    if (BlockUtils.isInRange(player.getLocation(), blockLoc, range)) {
                        result = true;
                        break;
                    }
                }
                switchLever(Lever.BACK, signBlock, result);
                playerList = (Player[]) null;
            } else if (split[0].equalsIgnoreCase("-g")) {
                Player[] playerList = Bukkit.getServer().getOnlinePlayers();
                for (Player player : playerList) {
                    if ((player.isDead()) || (!player.isOnline())) {
                        continue;
                    }
                    if (!UtilPermissions.isPlayerInGroupWithoutInhetirance(player, split[1], signBlock.getWorld().getName())) {
                        continue;
                    }
                    if (BlockUtils.isInRange(player.getLocation(), blockLoc, range)) {
                        result = true;
                        break;
                    }
                }
                switchLever(Lever.BACK, signBlock, result);
                playerList = (Player[]) null;
            }
            blockLoc = null;
        }
    }
}