package com.ave.simplestationsminer.blockentity;

import net.minecraft.screen.PropertyDelegate;

public class MinerPropertyDelegate {

    public static PropertyDelegate create(MinerBlockEntity be) {
        return new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> be.coolant;
                    case 1 -> be.redstone;
                    case 2 -> (int) be.progress;
                    case 3 -> be.working ? 1 : 0;
                    case 4 -> be.fuel;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        be.coolant = value;
                    case 1:
                        be.redstone = value;
                    case 2:
                        be.progress = value;
                    case 3:
                        be.working = value != 0;
                    case 4:
                        be.fuel = value;
                }
            }

            @Override
            public int size() {
                return 5;
            }
        };
    }
}