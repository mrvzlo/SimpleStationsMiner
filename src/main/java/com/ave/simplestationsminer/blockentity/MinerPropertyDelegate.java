package com.ave.simplestationsminer.blockentity;

import net.minecraft.screen.PropertyDelegate;

public interface MinerPropertyDelegate extends PropertyDelegate {
    public static int SIZE = 5;

    @Override
    default int size() {
        return SIZE;
    }

    default int getCoolant() {
        return this.get(0);
    }

    default int getRedstone() {
        return this.get(1);
    }

    default int getProgress() {
        return this.get(2);
    }

    default int getFuel() {
        return this.get(3);
    }

    default boolean isTooHigh() {
        return this.get(4) == 1;
    }

    public static MinerPropertyDelegate create(MinerBlockEntity be) {
        return new MinerPropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> be.coolant;
                    case 1 -> be.redstone;
                    case 2 -> (int) be.progress;
                    case 3 -> be.fuel;
                    case 4 -> be.invalidDepth ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> be.coolant = value;
                    case 1 -> be.redstone = value;
                    case 2 -> be.progress = value;
                    case 3 -> be.fuel = value;
                    case 4 -> be.invalidDepth = value == 1;
                }
            }
        };
    }
}