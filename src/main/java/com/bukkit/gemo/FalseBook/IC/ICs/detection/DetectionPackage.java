package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.ICUpgrade;
import com.bukkit.gemo.FalseBook.IC.ICs.ICUpgraderMC;

public class DetectionPackage extends ExternalICPackage {

    public DetectionPackage() {
        setAPI_VERSION("1.1");

        setShowImportMessages(false);
        addIC(ICDay.class);
        addIC(ICWaterSensor.class);
        addIC(ICLavaSensor.class);
        addIC(ICLightSensor.class);
        addIC(ICBlockSensor.class);
        addIC(ICItemSensor.class);
        addIC(ICPowerSensor.class);
        addIC(ICDetector.class);
        addIC(ICPDetector.class);
        addIC(ICSun.class);
        addIC(ICRain.class);
        addIC(ICStorm.class);
        
        ICUpgrade.addUpgrader("[MC1230]", new ICUpgraderMC("ic.day"));
        ICUpgrade.addUpgrader("[MC1260]", new ICUpgraderMC("ic.watersensor"));
        ICUpgrade.addUpgrader("[MC1261]", new ICUpgraderMC("ic.lavasensor"));
        ICUpgrade.addUpgrader("[MC1262]", new ICUpgraderMC("ic.lightsensor"));
        ICUpgrade.addUpgrader("[MC1263]", new ICUpgraderMC("ic.blocksensor"));
        ICUpgrade.addUpgrader("[MC1264]", new ICUpgraderMC("ic.itemsensor"));
        ICUpgrade.addUpgrader("[MC1270]", new ICUpgraderMC("ic.powersensor"));
        ICUpgrade.addUpgrader("[MC1271]", new ICUpgraderMC("ic.detector"));
        ICUpgrade.addUpgrader("[MC1272]", new ICUpgraderMC("ic.pdetector"));
        ICUpgrade.addUpgrader("[MC1280]", new ICUpgraderMC("ic.sun"));
        ICUpgrade.addUpgrader("[MC1281]", new ICUpgraderMC("ic.rain"));
        ICUpgrade.addUpgrader("[MC1282]", new ICUpgraderMC("ic.storm"));
    }
}