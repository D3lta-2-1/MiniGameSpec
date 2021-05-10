# Fabric Observer Gamemode
Adds a new server-sided gamemode to Minecraft Fabric servers. Observer mode is a customisable hybrid between Spectator and Adventure mode.

## Setup
1. Install the mods dependencies:
    * [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files)
    * [ClothConfig](https://www.curseforge.com/minecraft/mc-mods/cloth-config/files)
2. Install this mod using a jar from the versions page.
3. Start your server and configure using the `/observermode` command or directly to the `config/observermode.json` file.

## Config
* `allow_fly` - If players in Observer mode should be able to fly (`true`/`false`)
* `can_spectate_mobs` - If players in Observer mode should be able to specate non-player entities (`true`/`false`)
* `can_spectate_players` - If players in Observer mode should be able to specate other non-specator/observer players (`true`/`false`)

## Libraries
Observer mode takes advantage of the following libraries included in the mod file:
* [Fabric-ASM](https://github.com/Chocohead/Fabric-ASM)
