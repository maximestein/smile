/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package smile.clustering.linkage;

/**
 * A measure of dissimilarity between clusters (i.e. sets of observations).
 *
 * <h2>References</h2>
 * <ol>
 * <li> Anil K. Jain, Richard C. Dubes. Algorithms for clustering data. 1988.</li>
 * </ol>
 *
 * @see smile.clustering.HierarchicalClustering
 *
 * @author Haifeng Li
 *
 * [EBX] Modifications made to file:
 *  - init() now takes a float[] as an input instead of a double[][]. The proximity variable is
 *  directly assigned since we provide directly a 1D condensed matrix. The 1D condensed matrix
 *  was built from the 2D dense matrix in the original version
 *
 */
public abstract class Linkage {
    /** The data size. */
    long size;

    /**
     * Linearized proximity matrix to store the pair-wise distance measure
     * as dissimilarity between clusters. To save space, we only need the
     * lower half of matrix. And we use float instead of double to save
     * more space, which also help speed performance. During the
     * clustering, this matrix will be updated to reflect the dissimilarity
     * of merged clusters.
     */
    float[] proximity;

    /** Initialize the linkage with the lower triangular proximity matrix. */
    void init(float[] proximity) {
        size = getSize(proximity);
        this.proximity = proximity;
    }

    private int getSize(float[] proximity) {
        int i = 0;
        int sizeCondensed = proximity.length;
        int result = 0;
        while (result != sizeCondensed) {
            i++;
            result += i;
        }
        return i;
    }

    int index(long i, long j) {
        // row wise
        // return i > j ? i*(i+1)/2 + j : j*(j+1)/2 + i;
        // column wise
        return i > j ? (int) (proximity.length - (size-j)*(size-j+1)/2 + i - j) :
            (int) (proximity.length - (size-i)*(size-i+1)/2 + j - i);
    }

    /** Returns the proximity matrix size. */
    public long size() {
        return size;
    }

    /**
     * Returns the distance/dissimilarity between two clusters/objects, which
     * are indexed by integers.
     */
    public float d(int i, int j) {
        return proximity[index(i, j)];
    }

    /**
     * Merge two clusters into one and update the proximity matrix.
     * @param i cluster id.
     * @param j cluster id.
     */
    public abstract void merge(int i, int j);
}
