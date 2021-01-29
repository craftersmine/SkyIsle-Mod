package craftersmine.skyisle.generation;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

public class IslandsWorldType extends WorldType {

    public IslandsWorldType() {
        super("islands");
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        return new IslandWorldChunkGenerator(world);
    }

    @Override
    public boolean isCustomizable()
    {
        return false;
    }
}
