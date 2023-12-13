package com.github.urisinger.dungeonroutes.octree;


import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Head {
    int size;

    float xOffset, yOffset, zOffset;
    Node headNode;

    public Head(int size, float x, float y, float z) {
        this.size = size;
        xOffset = x;
        yOffset = y;
        zOffset = z;
        this.headNode = new Node();
        this.headNode.nodeType = NodeType.Air;
    }

    public void insert(float x, float y, float z, boolean isAir){
        if(!isAir) {
            headNode.insert(this.size, x - xOffset, y - yOffset, z - zOffset, isAir ? NodeType.Air : NodeType.Block);
        }
    }

    @SubscribeEvent
    public void RenderWorldLastEvent(RenderWorldLastEvent event){
        headNode.draw(this.size,new AxisAlignedBB(-size/2 + xOffset,-size/2 + yOffset,-size/2 + zOffset,size/2 + xOffset,size/2 + yOffset,size/2 + zOffset),Color.BLUE);
    }

    @SubscribeEvent
    public void onKeyInputEvent(InputEvent.KeyInputEvent event){
        if(Keyboard.isKeyDown(Keyboard.KEY_J)){
            long startTime = System.currentTimeMillis();
            for(float i = -size/2; i < size/2; i++){
                for(float j = -size/2; j < size/2; j++) {
                    for (float k = -size/2; k < size/2; k++) {
                        Minecraft mc = Minecraft.getMinecraft();
                        BlockPos pos = new BlockPos(i + xOffset,j + yOffset,k + zOffset);
                        boolean isAir = mc.theWorld.isAirBlock(pos);
                        insert(i + 0.5f + xOffset,j + 0.5f + yOffset,k + 0.5f + zOffset, isAir);
                    }
                }
            }
            long calcTime = System.currentTimeMillis() - startTime;

            Minecraft mc = Minecraft.getMinecraft();

            mc.thePlayer.sendChatMessage("it took " + calcTime + "ms to insert blocks");
        }
    }
}
