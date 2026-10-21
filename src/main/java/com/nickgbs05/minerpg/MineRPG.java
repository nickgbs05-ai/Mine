
package com.nickgbs05.minerpg;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MineRPG implements ModInitializer {
    public static final String MOD_ID = "mine-rpg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Map<UUID, RPGPlayer> PLAYERS = new HashMap<>();

    public static RPGPlayer getPlayer(UUID uuid) {
        return PLAYERS.computeIfAbsent(uuid, id -> new RPGPlayer());
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Mine RPG iniciado!");
		RPGCombat.register();

        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {
                dispatcher.register(
                    literal("xp_rpg")
                        .then(argument("quantidade",
                                IntegerArgumentType.integer(1, 10000))
                            .executes(context -> {
                                var player = context.getSource().getPlayerOrException();
                                int amount = IntegerArgumentType.getInteger(
                                    context, "quantidade"
                                );

                                RPGPlayer data = getPlayer(player.getUUID());
                                int oldLevel = data.getLevel();
                                int levelsGained = data.addXp(amount);

                                player.sendSystemMessage(Component.literal(
                                    "§a+" + amount + " XP RPG §7| Nível "
                                    + data.getLevel() + " ("
                                    + data.getXp() + "/"
                                    + data.getXpToNextLevel() + " XP)"
                                ));

                                if (levelsGained > 0) {
                                    player.sendSystemMessage(Component.literal(
                                        "§6§lLEVEL UP! §eVocê chegou ao nível "
                                        + data.getLevel() + "!"
                                    ));
                                }

                                return 1;
                            })
                        )
                );

                dispatcher.register(
                    literal("nivel_rpg")
                        .executes(context -> {
                            var player = context.getSource().getPlayerOrException();
                            RPGPlayer data = getPlayer(player.getUUID());

                            player.sendSystemMessage(Component.literal(
                                "§6Nível " + data.getLevel()
                                + " §7| XP: " + data.getXp()
                                + "/" + data.getXpToNextLevel()
                            ));

                            return 1;
                        })
                );
            }
        );
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
