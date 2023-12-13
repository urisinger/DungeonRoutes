package com.github.urisinger.dungeonroutes.octree;

import com.github.urisinger.dungeonroutes.utils.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;

import java.awt.*;


public class Node{
    Node[] subNodes;
    NodeType nodeType;

    public Node(){
        subNodes = null;
        this.nodeType = NodeType.Air;
    }

    boolean insert(int size, float x,float y, float z, NodeType blockT) {
        if (this.nodeType == blockT) {
            return false;
        }

        if (size <= 1) {
            this.nodeType = blockT;
            return true;
        }

        if (this.subNodes == null) {
            // Create a new list with size 8
            this.subNodes = new Node[8];
                for (int i = 0; i < 8; i++) {
                this.subNodes[i] = new Node();
                this.subNodes[i].nodeType = this.nodeType;
            }
            this.nodeType = NodeType.None;
        }


        size /= 2;
        // Call insert again with coordinates centered at the center point of the next node
        int boxIndex = 0;
        if (x >= 0)
            boxIndex += 1;
        if (y >= 0)
            boxIndex += 2;
        if (z >= 0)
            boxIndex += 4;

        // Adjust the coordinates to center at the next node

        float newX = x - Math.signum(x) * size / 2;
        float newY = y - Math.signum(y) * size / 2;
        float newZ = z - Math.signum(z) * size / 2;



        boolean shouldMerge = subNodes[boxIndex].insert(size, newX, newY, newZ, blockT);

        if(shouldMerge){
            NodeType lastNodeType = subNodes[0].nodeType;
            for(int i = 1; i < 8; i++){
                if(subNodes[i].nodeType != lastNodeType){
                    shouldMerge = false;
                    break;
                }
                lastNodeType = subNodes[i].nodeType;
            }
            if(shouldMerge){
                this.nodeType = lastNodeType;
                this.subNodes = null;
            }
        }
        return shouldMerge;
    }

    public void draw(float size ,AxisAlignedBB BB, Color color){
        if(this.subNodes == null) {
            if (this.nodeType == NodeType.Block) {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;
                AxisAlignedBB newBB = BB.offset(-x, -y, -z);
                RenderUtils.drawBox(newBB, color);
            }
            return;
        }

        for(int i = 0; i < 8; i++){
            double x = size/2 * ((i & 1) != 0 ? -1 : 1);
            double y = size/2 * (((i >> 1) & 1) != 0 ? -1 : 1);
            double z = size/2 * (((i >> 2) & 1) != 0 ? -1 : 1);
            double centerX = BB.maxX - size/2;
            double centerY = BB.maxY - size/2;
            double centerZ = BB.maxZ - size/2;
            AxisAlignedBB newBB = new AxisAlignedBB(centerX,centerY,centerZ, centerX - x, centerY - y, centerZ - z);
            this.subNodes[i].draw(size/2, newBB, color);
        }
    }
}
