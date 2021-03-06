
package edu.uthscsa.ric.visualization.surface.io.formats.gifti;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public class GiftiWriterDataHandler {

	private final FloatBuffer floatBuffer;
	private final IntBuffer intBuffer;
	private final float[] offset;
	private final float[] scale;
	private final boolean isIndex, isNormals;
	private int index, compIndex;
	private int capacity;



	/**
	 * @param dataArray
	 * @param offset
	 * @param scale
	 */
	public GiftiWriterDataHandler(DataArray dataArray, float[] offset, float[] scale) {
		this.floatBuffer = dataArray.getFloatBuffer();
		this.intBuffer = dataArray.getIntBuffer();
		this.offset = offset;
		this.scale = scale;
		isIndex = (intBuffer != null);
		isNormals = dataArray.isNormals();

		if (isIndex) {
			capacity = intBuffer.capacity();
		} else {
			capacity = floatBuffer.capacity();
		}
	}



	/**
	 * @return
	 */
	public boolean hasNext() {
		return index < capacity;
	}



	/**
	 * @return
	 */
	public int next() {
		if (isIndex) {
			return intBuffer.get(index++);
		}

		if (isNormals) {
			float val = floatBuffer.get(index++);

			if (compIndex == 0) {
				val = (scale[0] * val);
			} else if (compIndex == 1) {
				val = (scale[1] * val);
			} else if (compIndex == 2) {
				val = (scale[2] * val);
			}

			compIndex++;
			compIndex %= 3;

			return Float.floatToIntBits(val);
		} else {
			float val = floatBuffer.get(index++);

			if (compIndex == 0) {
				val = (scale[0] * (val + offset[0]));
			} else if (compIndex == 1) {
				val = (scale[1] * (val + offset[1]));
			} else if (compIndex == 2) {
				val = (scale[2] * (val + offset[2]));
			}

			compIndex++;
			compIndex %= 3;

			return Float.floatToIntBits(val);
		}
	}
}
