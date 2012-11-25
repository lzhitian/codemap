/*
 * Copyright (C) 2003-2006 Bjørn-Ove Heimsund
 * 
 * This file is part of MTJ.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package no.uib.cipr.matrix;

import org.netlib.lapack.LAPACK;
import org.netlib.util.intW;

/**
 * Computes RQ decompositions
 */
public class RQ extends OrthogonalComputer {

    /**
     * Constructs an empty RQ decomposition
     * 
     * @param m
     *            Number of rows
     * @param n
     *            Number of columns. Must be larger than or equal the number of
     *            rows
     */
    public RQ(int m, int n) {
        super(m, n, true);

        if (n < m)
            throw new IllegalArgumentException("n < m");

        int lwork;

        // Query optimal workspace. First for computing the factorization
        {
            work = new double[1];
            intW info = new intW(0);
            LAPACK.getInstance().dgerqf(m, n, new double[0], Matrices.ld(m), new double[0],
                    work, -1, info);

            if (info.val != 0)
                lwork = m;
            else
                lwork = (int) work[0];
            lwork = Math.max(1, lwork);
            work = new double[lwork];
        }

        // Workspace needed for generating an explicit orthogonal matrix
        {
            workGen = new double[1];
            intW info = new intW(0);
            LAPACK.getInstance().dorgrq(m, n, m, new double[0],Matrices.ld(m),
                    new double[0], workGen, -1, info);

            if (info.val != 0)
                lwork = m;
            else
                lwork = (int) workGen[0];
            lwork = Math.max(1, lwork);
            workGen = new double[lwork];
        }

    }

    /**
     * Convenience method to compute an RQ decomposition
     * 
     * @param A
     *            Matrix to decompose. Not modified
     * @return Newly allocated decomposition
     */
    public static RQ factorize(Matrix A) {
        return new RQ(A.numRows(), A.numColumns()).factor(new DenseMatrix(A));
    }

    @Override
    public RQ factor(DenseMatrix A) {

        if (Q.numRows() != A.numRows())
            throw new IllegalArgumentException("Q.numRows() != A.numRows()");
        else if (Q.numColumns() != A.numColumns())
            throw new IllegalArgumentException(
                    "Q.numColumns() != A.numColumns()");
        else if (R == null)
            throw new IllegalArgumentException("R == null");

        /*
         * Calculate factorisation, and extract the triangular factor
         */
        intW info = new intW(0);
        LAPACK.getInstance().dgerqf(m, n, A.getData(), Matrices.ld(m), tau, work,
                work.length, info);

        if (info.val < 0)
            throw new IllegalArgumentException();

        R.zero();
        for (MatrixEntry e : A)
            if (e.column() >= (n - m) + e.row())
                R.set(e.row(), e.column() - (n - m), e.get());

        /*
         * Generate the orthogonal matrix
         */
        info.val = 0;
        LAPACK.getInstance().dorgrq(m, n, k, A.getData(), Matrices.ld(m), tau, workGen,
                workGen.length, info);

        if (info.val < 0)
            throw new IllegalArgumentException();

        Q.set(A);

        return this;
    }

    /**
     * Returns the upper triangular factor
     */
    public UpperTriangDenseMatrix getR() {
        return R;
    }
}
