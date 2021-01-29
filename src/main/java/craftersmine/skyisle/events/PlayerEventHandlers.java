package craftersmine.skyisle.events;

import craftersmine.skyisle.common.IslandWorldSavedData;
import craftersmine.skyisle.generation.Island;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerEventHandlers {
    @SubscribeEvent
    public void playerRespawnEventHandler(PlayerEvent.PlayerRespawnEvent evt) {
        World world = evt.player.getEntityWorld();

        BlockPos entitySpawnPoint = evt.player.getBedLocation();

        if (entitySpawnPoint == null) {
            IslandWorldSavedData data = IslandWorldSavedData.getData(world);
            Island island = data.getIslandData(evt.player.getUniqueID());
            if (island != null) {
                if (island.islandWorldPosition != null)
                    evt.player.setPositionAndUpdate(island.islandWorldPosition.getX(), island.islandWorldPosition.getY() + 2, island.islandWorldPosition.getZ());
                else evt.player.setPositionAndUpdate(3, 66, 3);
            } else evt.player.setPositionAndUpdate(3, 66, 3);
        }
    }

    @SubscribeEvent
    public void playerLoggedInEventHandler(PlayerEvent.PlayerLoggedInEvent evt) {
        World world = evt.player.getEntityWorld();

        BlockPos entitySpawnPoint = evt.player.getBedLocation();

        if (entitySpawnPoint == null) {
            IslandWorldSavedData data = IslandWorldSavedData.getData(world);
            Island island = data.getIslandData(evt.player.getUniqueID());
            if (island == null) {

                evt.player.setPositionAndUpdate(3, 66, 3);
            }
        }
    }
}
