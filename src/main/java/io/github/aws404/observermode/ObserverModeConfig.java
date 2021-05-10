package io.github.aws404.observermode;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

import static com.google.common.collect.ImmutableList.of;

public class ObserverModeConfig extends Config {

    public static final ConfigItemGroup mainGroup = new MainConfig();

    public ObserverModeConfig() {
        super(of(mainGroup), new File(FabricLoader.getInstance().getConfigDir().toFile(), "observermode.json"), "observermode");
    }

    public static class MainConfig extends ConfigItemGroup {

        public static final ConfigItem<Boolean> canSpectateMobs = new ConfigItem<>("can_spectate_mobs", true, "can_spectate_mobs");
        public static final ConfigItem<Boolean> canSpectatePlayers = new ConfigItem<>("can_spectate_players", false, "can_spectate_players");
        public static final ConfigItem<Boolean> canFly = new ConfigItem<>("can_fly", false, "can_fly", item -> ObserverModeMod.configChanged = true);

        public MainConfig() {
            super(of(canSpectateMobs, canSpectatePlayers, canFly), "options");
        }

    }
}
