package io.github.aws404.observermode;

import com.chocohead.mm.api.ClassTinkerers;
import com.oroarmor.config.command.ConfigCommand;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObserverModeMod implements DedicatedServerModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final ObserverModeConfig CONFIG = new ObserverModeConfig();

	public static GameMode OBSERVER_MODE;
	public static boolean configChanged = false;

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

		// Load config from file
		CONFIG.readConfigFromFile();
		CONFIG.saveConfigToFile();
		// Register config command
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> new ConfigCommand(CONFIG).register(dispatcher, dedicated));

		// If the config has changed, update all players abilities
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (configChanged) {
				for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
					player.interactionManager.getGameMode().setAbilities(player.abilities);
					player.sendAbilitiesUpdate();
				}
				configChanged = false;
			}
		});
	}
}
