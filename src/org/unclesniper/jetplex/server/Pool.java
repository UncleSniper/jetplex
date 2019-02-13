package org.unclesniper.jetplex.server;

import java.util.Arrays;

public class Pool<ElementT extends Poolable> {

	private static class Chunk {

		final int chunkIndex;

		int occupiedBitmap = 1;

		Chunk nextFree;

		final Object[] elements = new Object[32];

		Chunk(int chunkIndex, Chunk nextFree) {
			this.chunkIndex = chunkIndex;
			this.nextFree = nextFree;
		}

	}

	private int size;

	private Chunk[] chunks = new Chunk[0];

	private Chunk firstFree;

	private int chunkCount;

	public Pool() {}

	public int alloc(ElementT element) {
		final int id;
		if(firstFree != null) {
			final int exponent = Integer.numberOfTrailingZeros(~firstFree.occupiedBitmap);
			firstFree.occupiedBitmap |= 1 << exponent;
			firstFree.elements[exponent] = element;
			id = firstFree.chunkIndex * 32 + exponent;
			if(~firstFree.occupiedBitmap == 0)
				firstFree = firstFree.nextFree;
		}
		else {
			if(chunkCount == chunks.length) {
				int newCapacity;
				if(chunks.length > 0) {
					newCapacity = chunks.length * 2;
					if(newCapacity < 0)
						newCapacity = Integer.MAX_VALUE;
				}
				else
					newCapacity = 4;
				chunks = Arrays.copyOf(chunks, newCapacity);
			}
			final Chunk chunk = new Chunk(chunkCount, firstFree);
			chunk.elements[0] = element;
			chunks[chunkCount++] = chunk;
			firstFree = chunk;
			id = chunk.chunkIndex * 32;
		}
		++size;
		return id;
	}

	@SuppressWarnings("unchecked")
	public boolean free(int id, boolean destroy) {
		if(id < 0)
			return false;
		final int chunkIndex = id / 32;
		if(chunkIndex >= chunkCount)
			return false;
		final int exponent = id % 32;
		final Chunk chunk = chunks[chunkIndex];
		final int mask = 1 << exponent;
		if((chunk.occupiedBitmap & mask) == 0)
			return false;
		if(destroy && chunk.elements[exponent] != null)
			((ElementT)chunk.elements[exponent]).destroy();
		chunk.elements[exponent] = null;
		if(~chunk.occupiedBitmap == 0) {
			chunk.nextFree = firstFree;
			firstFree = chunk;
		}
		chunk.occupiedBitmap &= ~mask;
		--size;
		return true;
	}

	@SuppressWarnings("unchecked")
	ElementT get(int id) {
		if(id < 0)
			return null;
		final int chunkIndex = id / 32;
		if(chunkIndex >= chunkCount)
			return null;
		final Chunk chunk = chunks[chunkIndex];
		return (ElementT)chunks[chunkIndex].elements[id % 32];
	}

}
