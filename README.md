# Guardian Portal Plugin  [![Modrinth](https://img.shields.io/modrinth/dt/guardian-portal-optimization?logo=modrinth)](https://modrinth.com/mod/guardian-portal-optimization)

Optimizes guardian farms that use portals to transport guardians into the Nether, where they are killed.

It does this by:
- Removing certain **Goals** (part of the guardian's AI)
- Reducing the guardian's default health by **50%**

For example, guardians that travel through a Nether portal will no longer look for targets or become aggressive.

Instead of disabling the AI completely, this plugin only removes specific goals. This allows guardians to retain natural movement, such as jumping and falling. If their AI were disabled completely, they would simply float in the air without moving.

## AFK Support

This plugin also allows a player to AFK their guardian farm without requiring a second player.

For example, Paper has settings that can disable guardian farms when another player is in the Nether and the exit portal is not loaded. Normally, this requires a second player to stand on the other side of the portal to keep it active.

With this plugin, that is no longer necessary.
