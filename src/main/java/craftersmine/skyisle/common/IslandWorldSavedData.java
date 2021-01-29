package craftersmine.skyisle.common;

import craftersmine.skyisle.Main;
import craftersmine.skyisle.generation.Island;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class IslandWorldSavedData extends WorldSavedData {

    public static final String DATA_NAME = Main.MODID + "_islands";

    public static final String USERUUID_KEY = "userUUID";
    public static final String PLAYERUSERNAME_KEY = "playerUsername";
    public static final String ISLANDSPAWNPOS_KEY = "islandSpawnPos";
    public static final String ISLANDXPOS_KEY = "islandXPos";
    public static final String ISLANDZPOS_KEY = "islandZPos";
    public static final String ISLANDLOCKED_KEY = "islandLocked";

    NBTTagCompound islandsData = new NBTTagCompound();

    public ArrayList<Island> islands = new ArrayList<>();

    public IslandWorldSavedData() {
        super(DATA_NAME);
    }

    public IslandWorldSavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        Set<String> keys = nbt.getKeySet();
        for (String key : keys) {
            NBTTagCompound userIslandData = nbt.getCompoundTag(key);
            Island island = new Island(
                    BlockPos.fromLong(userIslandData.getLong(ISLANDSPAWNPOS_KEY)),
                    UUID.fromString(userIslandData.getString(USERUUID_KEY)),
                    userIslandData.getString(PLAYERUSERNAME_KEY),
                    userIslandData.getLong(ISLANDXPOS_KEY),
                    userIslandData.getLong(ISLANDZPOS_KEY),
                    userIslandData.getBoolean(ISLANDLOCKED_KEY));
            islands.add(island);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        for (Island island : islands) {
            NBTTagCompound userIslandNbt = new NBTTagCompound();
            userIslandNbt.setString(USERUUID_KEY, island.playerUUID.toString());
            userIslandNbt.setString(PLAYERUSERNAME_KEY, island.playerUsername);
            userIslandNbt.setLong(ISLANDSPAWNPOS_KEY, island.islandWorldPosition.toLong());
            userIslandNbt.setLong(ISLANDXPOS_KEY, island.islandX);
            userIslandNbt.setLong(ISLANDZPOS_KEY, island.islandZ);
            userIslandNbt.setBoolean(ISLANDLOCKED_KEY, island.locked);

            islandsData.setTag(island.playerUUID.toString(), userIslandNbt);
        }

        return islandsData;
    }

    public boolean addUserIsland(Island island)
    {
        boolean hasUserIsland = false;

        for (Island i : islands) {
            if (i.playerUUID.equals(island.playerUUID))
            {
                hasUserIsland = true;
                break;
            }
            else continue;
        }

        if (hasUserIsland)
            return false;

        islands.add(island);
        markDirty();
        return true;
    }

    public void updateIsland(UUID playerUUID, Island island)
    {
        for (Island island1 : islands)
        {
            if (island1.playerUUID.toString().equals(playerUUID.toString()))
            {
                islands.remove(island1);
                islands.add(island);
            }
        }
        markDirty();
    }

    public Island getIslandData(UUID userUUID)
    {
        for (Island island : islands) {
            if (island.playerUUID.toString().equals(userUUID.toString()))
                return island;
        }
        return null;
    }

    public static IslandWorldSavedData getData(World world) {
        MapStorage storage = world.getPerWorldStorage();
        IslandWorldSavedData data = (IslandWorldSavedData) storage.getOrLoadData(IslandWorldSavedData.class, DATA_NAME);
        if (data == null)
        {
            data = new IslandWorldSavedData();
            storage.setData(DATA_NAME, data);
        }
        return data;
    }
}
