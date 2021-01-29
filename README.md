# SkyIsle-Mod
 SkyBlock island generator for multiplayer

## Description
It is recommended to play with ExNihilo Creatio mod
This mod provides a new generator called "SkyIslands" or "ISLANDS" for server.properties
It generates a spawn island at 0,0 with a chest contatining:
 - 2 Oak Saplings
 - 3 Cactuses
 - 3 Wheat Seeds
 - 3 Cocoa Beans
 - 1 Lava Bucket
 - 1 Water Bucket
 - 32 Bone Meal
 - 3 Sugar Canes
 
Also players can generate their own islands with command `/island create`

## Server commands
**All commands are server-only**
Command | Description
`/island create` | Creates new island for player
`/island return` | Returns player to his island if it exists
`/island visit <player_name>` | Teleports player to another player island
`/island list` | Shows a list of available islands
`/island info` | Shows info about your island
`/island info <player_name>` | Shows info about another player island **<OP ONLY>**
`/island lock` | Prevents another player to teleport to your island via `/island visit` command
`/island unlock` | Allows another player to teleport to your island via `/island visit` command

