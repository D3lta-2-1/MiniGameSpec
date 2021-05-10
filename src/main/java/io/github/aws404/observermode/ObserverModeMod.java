package io.github.aws404.observermode;

import com.chocohead.mm.api.ClassTinkerers;
import com.mojang.brigadier.arguments.BoolArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class ObserverModeMod implements DedicatedServerModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final ConfigHolder<ObserverModeConfig> CONFIG = AutoConfig.register(ObserverModeConfig.class, JanksonConfigSerializer::new);

	public static GameMode OBSERVER_MODE;

	private MinecraftServer server;

	@Override
	public void onInitializeServer() {
		// Load the observer mode enum, if this fails then there was an error with the early riser,
		// so throw an error to stop the mod from loading.
		try {
			OBSERVER_MODE = ClassTinkerers.getEnum(GameMode.class, "OBSERVER");
		} catch (Exception e) {
			LOGGER.error("Observer mode could not be found! There was an ASM error, deactivating mod.");
			throw new RuntimeException("Observer mode could not be found! There was an ASM error.");
		}

		LOGGER.info("Successfully found Observer mode with id: {}", OBSERVER_MODE.getId());

		// Register config command
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			for (Field field : CONFIG.getConfigClass().getFields()) {
				dispatcher.register(CommandManager.literal("observermode")
						.then(CommandManager.literal(field.getName())
								.executes(context -> {
									server = context.getSource().getMinecraftServer();
									CONFIG.load();
									try {
										context.getSource().sendFeedback(new LiteralText("Value: " + field.get(CONFIG.get()).toString()), false);
									} catch (IllegalAccessException e) {
										e.printStackTrace();
										return 0;
									}
									return 1;
								})
								.then(CommandManager.argument("value", BoolArgumentType.bool())
										.executes(context -> {
											server = context.getSource().getMinecraftServer();
											try {
												field.set(CONFIG.get(), BoolArgumentType.getBool(context, "value"));
											} catch (IllegalAccessException e) {
												e.printStackTrace();
												return 0;
											}
											CONFIG.save();
											context.getSource().sendFeedback(new LiteralText(String.format("Setting '%s' changed!", field.getName())), true);
											return 1;
										})
								)
						)
				);
			}
		});

		CONFIG.registerSaveListener((configHolder, observerModeConfig) -> {
			if (server == null) {
				return ActionResult.FAIL;
			}

			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				player.interactionManager.getGameMode().setAbilities(player.abilities);
				player.sendAbilitiesUpdate();
			}
			return ActionResult.SUCCESS;
		});
	}
}
