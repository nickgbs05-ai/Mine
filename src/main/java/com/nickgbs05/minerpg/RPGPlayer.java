
package com.nickgbs05.minerpg;

public class RPGPlayer {
    private int level = 1;
    private int xp = 0;

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getXpToNextLevel() {
        return level * 100;
    }

    public int addXp(int amount) {
        if (amount <= 0) {
            return 0;
        }

        int oldLevel = level;
        xp += amount;

        while (xp >= getXpToNextLevel()) {
            xp -= getXpToNextLevel();
            level++;
        }

        return level - oldLevel;
    }
}
