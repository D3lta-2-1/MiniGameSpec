package io.github.aws404.observermode;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

public class GameModeEnumAdder implements Runnable {

    /**
     * Using the Fabric-ASM early riser to add observer mode to the gamemode enum
     */
    @Override
    public void run() {
        String gameMode = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_1934");
        ClassTinkerers.enumBuilder(gameMode, int.class, String.class).addEnum("OBSERVER", 4, "observer").build();
    }
}
