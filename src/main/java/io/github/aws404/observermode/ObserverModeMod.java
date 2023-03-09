package io.github.aws404.observermode;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObserverModeMod implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static GameMode OBSERVER_MODE;

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
	}
}
