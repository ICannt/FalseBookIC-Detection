package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;

public class DetectionPackage extends ExternalICPackage {

    public DetectionPackage() {
        setAPI_VERSION("1.1");

        setShowImportMessages(false);
        addIC(MC1230.class);
        addIC(MC1260.class);
        addIC(MC1261.class);
        addIC(MC1262.class);
        addIC(MC1263.class);
        addIC(MC1264.class);
        addIC(MC1270.class);
        addIC(MC1271.class);
        addIC(MC1272.class);
        addIC(MC1280.class);
        addIC(MC1281.class);
        addIC(MC1282.class);
    }
}