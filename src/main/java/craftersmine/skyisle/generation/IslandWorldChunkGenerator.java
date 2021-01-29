package craftersmine.skyisle.generation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;

public class IslandWorldChunkGenerator implements IChunkGenerator {

    private static final IBlockState AIR_DEFAULT_STATE = Blocks.AIR.getDefaultState();

    private World world;
    private WorldType terrainType;

    public IslandWorldChunkGenerator(World worldIn) {
        this.world = worldIn;
    }

    public Chunk generateChunk(int x, int z) {
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        if (x == 0 && z == 0)
            generateSpawnIsland(chunkPrimer);
        else
        setChunkBlocks(chunkPrimer);
        Chunk chunk = new Chunk(world, chunkPrimer, x, z);
        chunk.generateSkylightMap();
        return chunk;
    }

    public void setChunkBlocks(ChunkPrimer chunkPrimer) {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = 0; y < 256; y++)
                {
                    chunkPrimer.setBlockState(x, y ,z, AIR_DEFAULT_STATE);
                }
    }

    public void generateSpawnIsland(ChunkPrimer chunkPrimer) {
        for (int y = 4; y >= 0; y--)
        {
            for (int x = 0; x <= 6; x++)
                for (int z = 0; z <= 6; z++)
                {
                    if (y == 0)
                        chunkPrimer.setBlockState(x, 64 - y, z, Blocks.GRASS.getDefaultState());
                    else
                        chunkPrimer.setBlockState(x, 64 - y, z, Blocks.DIRT.getDefaultState());
                }
        }
    }

    @Override
    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        if (x == 1 && z == 1)
        {
            createIslandChestAt(1, 1);
        }
    }

    public void createIslandChestAt(int worldX, int worldZ)
    {
        world.setBlockState(new BlockPos(worldX, 65, worldZ), Blocks.CHEST.correctFacing(world, new BlockPos(worldX, 65, worldZ), Blocks.CHEST.getDefaultState()));
        TileEntity chest = world.getTileEntity(new BlockPos(worldX, 65, worldZ));
        if (chest instanceof TileEntityChest)
        {
            ((TileEntityChest)chest).setInventorySlotContents(0, new ItemStack(Blocks.SAPLING, 2));
            ((TileEntityChest)chest).setInventorySlotContents(1, new ItemStack(Blocks.CACTUS, 3));
            ((TileEntityChest)chest).setInventorySlotContents(2, new ItemStack(Items.WHEAT_SEEDS, 3));
            ((TileEntityChest)chest).setInventorySlotContents(3, new ItemStack(Items.DYE, 3, 3));
            ((TileEntityChest)chest).setInventorySlotContents(4, new ItemStack(Items.LAVA_BUCKET, 1));
            ((TileEntityChest)chest).setInventorySlotContents(5, new ItemStack(Items.WATER_BUCKET, 1));
            ((TileEntityChest)chest).setInventorySlotContents(6, new ItemStack(Items.DYE, 32, 15));
            ((TileEntityChest)chest).setInventorySlotContents(7, new ItemStack(Blocks.REEDS, 3));
        }
    }

    public void generateIslandAt(int worldX, int worldZ) {
        for (int y = 4; y >= 0; y--)
        {
            for (int x = -3; x <= 3; x++)
                for (int z = -3; z <= 3; z++)
                {
                    if (y == 0)
                        world.setBlockState(new BlockPos(x + worldX, 64 - y, z + worldZ), Blocks.GRASS.getDefaultState());
                    else
                        world.setBlockState(new BlockPos(x + worldX, 64 - y, z + worldZ), Blocks.DIRT.getDefaultState());
                }
        }

        createIslandChestAt(1 + worldX, 1 + worldZ);
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {

        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return Biomes.PLAINS.getSpawnableList(creatureType);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}
