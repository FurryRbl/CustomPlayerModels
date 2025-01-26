package com.tom.cpm.common;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;

public class BlockMeta {
	private final int block;
	private final int meta;

	public BlockMeta(int block, int meta) {
		this.block = block;
		this.meta = meta;
	}

	public int getBlockId() {
		return block;
	}

	public Block getBlock() {
		return Blocks.blocksList[block];
	}

	public int getMeta() {
		return meta;
	}
}
