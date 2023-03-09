# MiniGameSpec Is a update and an extension of aws404's Fabric Observer Gamemode mod
It adds a new server-sided gamemode to Minecraft Fabric servers. Observer mode is a customisable hybrid between Spectator and Adventure mode.

## How does it work?
This exploits the method**s** the client uses to get the players gamemode (kind-of a bug). For some features, the client will use the player's gamemode sent with Game Join, Game State Change and other packets, while other features will use the gamemode sent with the player list packets. By telling the client that the player is in spectator mode but telling the player list the player is in adventure mode, we get observer mode.  
There are a variety of packets that have mixins to ensure that the client is never aware of being in observer mode, by replaceing it with spectator or adventure depending on the packet.

## Libraries
Observer mode takes advantage of the following libraries included in the mod file:
* [Fabric-ASM](https://github.com/Chocohead/Fabric-ASM)
