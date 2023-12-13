package com.github.urisinger.dungeonroutes;

import com.github.urisinger.dungeonroutes.octree.Head;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "dungeonroutes", useMetadata=true)
public class ExampleMod {
    Head octree = new Head(32,0,16,0);
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(octree);
    }
}
