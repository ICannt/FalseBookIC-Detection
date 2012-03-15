package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.Slime;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class ICDetector extends BaseIC {

    List<String> Types = new ArrayList<String>();

    public ICDetector() {
        this.ICName = "DETECTION";
        this.ICNumber = "ic.detector";
        setICGroup(ICGroup.DETECTION);

        this.Types.add("PLAYER");
        this.Types.add("MOBHOSTILE");
        this.Types.add("MOBPEACEFUL");
        this.Types.add("ANYMOB");
        this.Types.add("ANY");
        this.Types.add("CART");
        this.Types.add("STORAGECART");
        this.Types.add("POWEREDCART");
        this.chipState = new BaseChip(true, false, false, "Clock", "", "");
        this.chipState.setOutputs("Output", "", "");
        this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "detection type");
        this.ICDescription = "The MC1271 outputs high if the specified type is detected in the given distance around the ic, when the input (the \"clock\") goes from low to high.<br /><br /><b>Detection types:</b><ul><li>PLAYER</li><li>MOBHOSTILE</li><li>MOBPEACEFUL</li><li>ANYMOB</li><li>ANY</li><li>CART</li><li>STORAGECART</li><li>POWEREDCART</li></ul><br /><br />The <a href=\"MC0271.html\">MC0271</a> is the selftriggered version.";
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
        if (event.getLine(2).length() > 0) {
            boolean f = false;
            for (int i = 0; i < this.Types.size(); i++) {
                if (this.Types.get(i).equalsIgnoreCase(event.getLine(2))) {
                    f = true;
                    event.setLine(2, this.Types.get(i));
                }
            }
            if (!f) {
                SignUtils.cancelSignCreation(event, "Type not found.");
                return;
            }
        } else {
            SignUtils.cancelSignCreation(event, "Please enter a Type in Line 4.");
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
            Location blockLoc = getICBlock(signBlock, offsetVector);

            int nowType = -1;
            for (int i = 0; i < this.Types.size(); i++) {
                if (this.Types.get(i).equalsIgnoreCase(signBlock.getLine(2))) {
                    nowType = i;
                }
            }
            int nowTyp = nowType;
            boolean result = false;
            List<LivingEntity> liste = signBlock.getWorld().getLivingEntities();
            switch (nowTyp) {
                case 0:
                    Player[] playerList = Bukkit.getServer().getOnlinePlayers();
                    for (Player player : playerList) {
                        if ((player.isDead()) || (!player.isOnline())) {
                            continue;
                        }
                        if (BlockUtils.isInRange(player.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    switchLever(Lever.BACK, signBlock, result);
                    playerList = (Player[]) null;
                    break;
                case 1:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if ((!(ent instanceof Monster)) && (!(ent instanceof Ghast)) && (!(ent instanceof Giant)) && (!(ent instanceof Slime))) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
                    break;
                case 2:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if ((!(ent instanceof Animals)) && (!(ent instanceof Villager)) && (!(ent instanceof IronGolem)) && (!(ent instanceof Ocelot))) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
                    break;
                case 3:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if ((!(ent instanceof Animals)) && (!(ent instanceof Monster)) && (!(ent instanceof Ghast)) && (!(ent instanceof Giant)) && (!(ent instanceof Slime)) && (!(ent instanceof Villager)) && (!(ent instanceof IronGolem)) && (!(ent instanceof Ocelot))) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
                    break;
                case 4:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if ((!(ent instanceof Player)) && (!(ent instanceof Animals)) && (!(ent instanceof Monster)) && (!(ent instanceof Ghast)) && (!(ent instanceof Giant)) && (!(ent instanceof Slime)) && (!(ent instanceof Minecart)) && (!(ent instanceof StorageMinecart)) && (!(ent instanceof PoweredMinecart)) && (!(ent instanceof Villager))) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
                    break;
                case 5:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if (!(ent instanceof Minecart)) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
                    break;
                case 6:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if (!(ent instanceof StorageMinecart)) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
                    break;
                case 7:
                    for (Entity ent : liste) {
                        if (ent.isDead()) {
                            continue;
                        }
                        if (!(ent instanceof PoweredMinecart)) {
                            continue;
                        }
                        if (BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                            result = true;
                            break;
                        }
                    }
                    liste.clear();
                    liste = null;
                    switchLever(Lever.BACK, signBlock, result);
            }

            blockLoc = null;
        }
    }
}