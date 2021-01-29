package craftersmine.skyisle.generation;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Island {
    public BlockPos islandWorldPosition;
    public UUID playerUUID;
    public String playerUsername;
    public long islandX;
    public long islandZ;
    public boolean locked;

    public Island(BlockPos islandWorldPos, UUID ownerId, String playerUsername, long x, long z, boolean locked)
    {
        this.islandWorldPosition = islandWorldPos;
        this.playerUUID = ownerId;
        this.playerUsername = playerUsername;
        this.islandX = x;
        this.islandZ = z;
        this.locked = locked;
    }
}
