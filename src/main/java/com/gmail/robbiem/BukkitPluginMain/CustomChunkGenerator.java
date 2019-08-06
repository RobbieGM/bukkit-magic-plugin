package com.gmail.robbiem.BukkitPluginMain;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class CustomChunkGenerator extends ChunkGenerator {
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 6);
		generator.setScale(0.01);
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int height = (int) (generator.noise(chunkX*16 + x, chunkZ*16 + z, 1, 1, true) * 30d + 65d);
				chunk.setBlock(x, height, z, Material.GRASS_BLOCK);
				chunk.setBlock(x, height - 1, z, Material.DIRT);
				chunk.setBlock(x, height - 2, z, Material.BEDROCK);
			}
		}
		return chunk;
	}
}
