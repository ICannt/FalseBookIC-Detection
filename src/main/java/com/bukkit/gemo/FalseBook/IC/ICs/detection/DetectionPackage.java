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
        
        addUpgrader("[MC1230]", new ICUpgraderMC("ic.day"));
        addUpgrader("[MC1260]", new ICUpgraderMC("ic.watersensor"));
        addUpgrader("[MC1261]", new ICUpgraderMC("ic.lavasensor"));
        addUpgrader("[MC1262]", new ICUpgraderMC("ic.lightsensor"));
        addUpgrader("[MC1263]", new ICUpgraderMC("ic.blocksensor"));
        addUpgrader("[MC1264]", new ICUpgraderMC("ic.itemsensor"));
        addUpgrader("[MC1270]", new ICUpgraderMC("ic.powersensor"));
        addUpgrader("[MC1271]", new ICUpgraderMC("ic.detector"));
        addUpgrader("[MC1272]", new ICUpgraderMC("ic.pdetector"));
        addUpgrader("[MC1280]", new ICUpgraderMC("ic.sun"));
        addUpgrader("[MC1281]", new ICUpgraderMC("ic.rain"));
        addUpgrader("[MC1282]", new ICUpgraderMC("ic.storm"));
    }
}