package craftersmine.skyisle.commands;

import craftersmine.skyisle.common.IslandWorldSavedData;
import craftersmine.skyisle.generation.Island;
import craftersmine.skyisle.generation.IslandWorldChunkGenerator;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import craftersmine.skyisle.Main;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiConfigEntries;

import java.util.UUID;

public class CommandIsland extends CommandBase {

    @Override
    public String getName() {
        return "island";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.island.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1)
        {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            switch (args[0])
            {
                case "create":
                    if (!server.isDedicatedServer())
                        throw new CommandException("commands.island.only_server");

                    UUID userId = player.getUniqueID();

                    if (player.dimension != 0)
                        throw new CommandException("commands.island.invalid_dimension");
                    if (server.getWorld(0).getWorldType().getName().equals(Main.islandsWorldType.getName()))
                    {
                        IslandWorldSavedData islandsData = IslandWorldSavedData.getData(server.getWorld(0));

                        if (islandsData.getIslandData(player.getUniqueID()) != null)
                            throw new CommandException("commands.island.island_already_exists");

                        BlockPos validNewIslandWorldPos = null;

                        int x = 1;
                        int z = 1;

                        for (Island island : islandsData.islands) {
                            if (island.islandX == x) {
                                x++;
                            }
                        }
                        for (Island island : islandsData.islands) {
                            if (island.islandZ == z) {
                                z++;
                            }
                        }

                        validNewIslandWorldPos = new BlockPos(x * 500, 64, z * 500);

                        ((IslandWorldChunkGenerator)server.getWorld(0)
                                .getChunkProvider().chunkGenerator)
                                .generateIslandAt(validNewIslandWorldPos.getX(), validNewIslandWorldPos.getZ());

                        player.setPositionAndUpdate(validNewIslandWorldPos.getX(), 65, validNewIslandWorldPos.getZ());

                        islandsData.addUserIsland(
                                new Island(validNewIslandWorldPos,
                                player.getUniqueID(), player.getName(), x, z, false));

                        player.sendMessage(new TextComponentTranslation("commands.island.successfully_created"));
                        Main.logger.info(player.getName() + " (" + player.getUniqueID() + ") created an island @ [" + validNewIslandWorldPos.getX() + ", " + validNewIslandWorldPos.getZ() + "]");
                    }
                    else throw new CommandException("commands.island.invalid_world_type");
                    break;
                case "return":
                    IslandWorldSavedData islandsData = IslandWorldSavedData.getData(server.getWorld(0));

                    if (islandsData.getIslandData(player.getUniqueID()) == null)
                        throw new CommandException("commands.island.island_not_exists");

                    World world = player.getEntityWorld();

                    BlockPos entitySpawnPoint = player.getBedLocation();

                    if (entitySpawnPoint == null) {
                        IslandWorldSavedData data = IslandWorldSavedData.getData(world);
                        Island island = data.getIslandData(player.getUniqueID());
                        player.setPositionAndUpdate(island.islandWorldPosition.getX(), island.islandWorldPosition.getY() + 2, island.islandWorldPosition.getZ());
                        sender.sendMessage(new TextComponentTranslation("commands.island.teleport_successful"));
                        Main.logger.info(player.getName() + " (" + player.getUniqueID() + ") has returned to island or @ [" + island.islandWorldPosition.getX() + ", " + island.islandWorldPosition.getZ() + "]");
                        return;
                    }
                    else player.setPositionAndUpdate(entitySpawnPoint.getX(),entitySpawnPoint.getY(),entitySpawnPoint.getZ());

                    sender.sendMessage(new TextComponentTranslation("commands.island.teleport_successful"));
                    Main.logger.info(player.getName() + " (" + player.getUniqueID() + ") has returned to island or @ [" + entitySpawnPoint.getX() + ", " + entitySpawnPoint.getZ() + "]");
                    break;
                case "visit":
                    IslandWorldSavedData dataV = IslandWorldSavedData.getData(server.getWorld(0));
                    String usrName = "";
                    if (args.length >= 2)
                        usrName = args[1];
                    if (!usrName.equals(""))
                    {
                        for (Island isl : dataV.islands)
                        {
                            if (isl.playerUsername.equals(usrName))
                            {
                                if (!isl.locked) {
                                    BlockPos islandPos = isl.islandWorldPosition;
                                    player.setPositionAndUpdate(islandPos.getX(), islandPos.getY() + 1, islandPos.getZ());
                                    player.sendMessage(new TextComponentTranslation("commands.island.visit_successful", usrName));
                                }
                                else player.sendMessage(new TextComponentTranslation("commands.island.visit_locked"));
                                return;
                            }
                        }
                        player.sendMessage(new TextComponentTranslation("commands.island.visit_no_island_found"));
                    }
                    else throw new WrongUsageException("commands.island.visit.usage");
                    break;
                case "list":
                    player.sendMessage(new TextComponentTranslation("commands.island.list_title"));
                    IslandWorldSavedData data = IslandWorldSavedData.getData(server.getWorld(0));
                    for (Island island : data.islands) {
                        player.sendMessage(new TextComponentString(island.playerUsername + " @ [" + island.islandWorldPosition.getX() + ", " + island.islandWorldPosition.getZ() + "]"));
                    }
                    player.sendMessage(new TextComponentTranslation("commands.island.list_tip"));
                    break;
                case "lock":
                    IslandWorldSavedData data1 = IslandWorldSavedData.getData(server.getWorld(0));
                    Island island = data1.getIslandData(player.getUniqueID());
                    island.locked = true;
                    data1.updateIsland(player.getUniqueID(), island);
                    player.sendMessage(new TextComponentTranslation("commands.island.island_locked"));
                    break;
                case "unlock":
                    IslandWorldSavedData data2 = IslandWorldSavedData.getData(server.getWorld(0));
                    Island island1 = data2.getIslandData(player.getUniqueID());
                    island1.locked = false;
                    data2.updateIsland(player.getUniqueID(), island1);
                    player.sendMessage(new TextComponentTranslation("commands.island.island_unlocked"));
                    break;
                case "info":
                    IslandWorldSavedData data3 = IslandWorldSavedData.getData(server.getWorld(0));
                    if (server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null)
                    {
                        String playerName = "";
                        if (args.length >= 2)
                            playerName = args[1];
                        if (!playerName.equals(""))
                        {
                            for (Island isle : data3.islands)
                            {
                                if (isle.playerUsername.equals(playerName))
                                {
                                    sendInfo(player, isle, true);
                                    return;
                                }
                            }
                            player.sendMessage(new TextComponentTranslation("commands.island.info.island_not_exists", playerName));
                        }
                        else sendInfo(player, data3.getIslandData(player.getUniqueID()), true);
                    }
                    else {
                        Island usrI = data3.getIslandData(player.getUniqueID());
                        if (usrI != null)
                            sendInfo(player, usrI, false);
                        else throw new CommandException("commands.island.island_not_exists");
                    }

                    break;
                default:
                    throw new WrongUsageException("commands.island.usage");
            }
        }
        else throw new WrongUsageException("commands.island.usage");
    }

    public void sendInfo(EntityPlayerMP player, Island island, boolean isOp)
    {
        player.sendMessage(new TextComponentTranslation("commands.island.info.title"));
        player.sendMessage(new TextComponentTranslation("commands.island.info.ownerName", island.playerUsername));
        if (isOp)
            player.sendMessage(new TextComponentTranslation("commands.island.info.ownerUUID", island.playerUUID.toString()));
        player.sendMessage(new TextComponentTranslation("commands.island.info.location", island.islandWorldPosition.getX() + ", " + island.islandWorldPosition.getZ()));
        String boolCol = "\u00a7c";
        if (island.locked)
            boolCol = "\u00a7a";
        player.sendMessage(new TextComponentTranslation("commands.island.info.isLocked", boolCol, island.locked));
    }
}
