
package com.nickgbs05.minerpg;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;

public class RPGCombat {

    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(
            (world, attacker, killed) -> {
                if (!(attacker instanceof ServerPlayer player)) {
                    return;
                }

                int xp = getXpForMob(killed);

                if (xp <= 0) {
                    return;
                }

                RPGPlayer data = MineRPG.getPlayer(player.getUUID());
                int levelsGained = data.addXp(xp);

                player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal(
                        "§a+" + xp + " XP RPG §7| Nível "
                        + data.getLevel() + " ("
                        + data.getXp() + "/"
                        + data.getXpToNextLevel() + " XP)"
                    )
                );

                if (levelsGained > 0) {
                    player.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                            "§6§lLEVEL UP! §eVocê chegou ao nível "
                            + data.getLevel() + "!"
                        )
                    );
                }
            }
        );
    }

    private static int getXpForMob(LivingEntity mob) {
        EntityType<?> type = mob.getType();

        if (type == EntityType.ZOMBIE) return 20;
        if (type == EntityType.SKELETON) return 25;
        if (type == EntityType.CREEPER) return 30;
        if (type == EntityType.SPIDER) return 20;
        if (type == EntityType.ENDERMAN) return 50;

        return 0;
    }
}
