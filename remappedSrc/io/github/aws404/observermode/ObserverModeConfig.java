package io.github.aws404.observermode;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "observer")
public class ObserverModeConfig implements ConfigData {

    public boolean canSpectateMobs = true;
    public boolean canSpectatePlayers = false;
    public boolean canFly = false;

}
