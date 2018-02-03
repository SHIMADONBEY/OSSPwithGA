/**
 * 個体群クラス
 * 
 * @author b113048
 */
/**
 * @author b113048
 */
public class Individuals {
    /* 個体数 */
    private int mPopCount;
    /* 個体群 */
    private Chromosome[] mIndivs;
    /* エリート個体 */
    private Chromosome mElite;
    /* エリート個体が更新されたか */
    private boolean updated;

    public Individuals(int n, int m, int p) {
        mPopCount = p;
        mIndivs = new Chromosome[mPopCount];
        mElite = new Chromosome(n, m);
        updated = false;

        for (int i = 0; i < mPopCount; i++) {
            mIndivs[i] = new Chromosome(n, m);
        }
    }

    public Individuals(ProcessTime pTime, int p) {
        mPopCount = p;
        mIndivs = new Chromosome[mPopCount];
        mElite = new Chromosome(pTime);
        updated = false;

        for (int i = 0; i < mPopCount; i++) {
            mIndivs[i] = new Chromosome(pTime);
        }
    }

    public int getPopCount() {
        return mPopCount;
    }

    public void setPopCount(int popCount) {
        this.mPopCount = popCount;
    }

    public Chromosome[] getChromosome() {
        return mIndivs;
    }

    public Chromosome getChromosome(int i) {
        return mIndivs[i];
    }

    public void setChromosome(int i, Chromosome chr) {
        mIndivs[i].setGene(chr.getGene());
        mIndivs[i].setFitness(chr.getFitness());
        mIndivs[i].setTimeLine(chr.getTimeLine());
    }

    public Chromosome getElite() {
        return mElite;
    }

    public void setElite(Chromosome elite) {
        mElite.setGene(elite.getGene());
        mElite.setFitness(elite.getFitness());
        mElite.setTimeLine(elite.getTimeLine());
    }

    public boolean hasUpdated() {
        return updated;
    }

    /* 親の選択リスト */
    /**
     * @return 交配ペア配列
     */
    public int[] getParentList() {
        int[] x = new int[mPopCount];

        int i;
        int k;
        int swp;

        for (i = 0; i < mPopCount; i++) {
            x[i] = i;
        }

        for (i = 0; i < mPopCount; i++) {
            k = (int) (Math.random() * (mPopCount - i)) + i;
            swp = x[i];
            x[i] = x[k];
            x[k] = swp;
        }

        return x;
    }

    /**
     * @return 適応度の平均値
     */
    public double getAverageFitness() {
        double total = 0;
        for (int i = 0; i < mPopCount; i++) {
            total += getChromosome(i).getFitness();
        }
        return total / (double) mPopCount;
    }

    public void generateGene() {
        int idBest = 0;

        for (int i = 0; i < mPopCount; i++) {
            mIndivs[i].generateGene();
            mIndivs[i].calcFitness();

            if (mIndivs[i].getFitness() > mIndivs[idBest].getFitness()) {
                idBest = i;
            }
        }

        setElite(mIndivs[idBest]);
    }

    /**
     * 交叉を行う
     * 
     * @param id1 親個体ID1
     * @param id2 親個体ID2
     */
    public void crossover(double crossRate, int id1, int id2) {
        int length; // 遺伝子長
        if (mIndivs[id1].getLength() != mIndivs[id2].getLength()) {
            System.err.println("交叉エラー");
            return;
        }

        if (Math.random() >= crossRate) {
            return;
        }

        // パス表現
        length = mIndivs[id1].getLength();
        int[][] path = new int[2][length];

        setOneColumn(mIndivs[id1].getGene(),
                mIndivs[id1].getSizeM(), path[0]);
        setOneColumn(mIndivs[id2].getGene(),
                mIndivs[id2].getSizeM(), path[1]);

        length = mIndivs[id1].getLength();

        int idxA = (int) Math.random() * length;
        int idxB = (int) (Math.random() * (length - (idxA + 1)) + 1 + idxA);

        int[][] changeList = new int[2][idxB - idxA + 1];

        int i;
        int j;
        for (i = idxA; i <= idxB; i++) {
            for (j = 0; j < 2; j++) {
                changeList[j][i - idxA] = path[j][i];
            }
        }

        int k;
        for (i = idxA; i <= idxB; i++) {
            for (j = 0; j < 2; j++) {
                for (k = 0; path[j][k] != changeList[j][i - idxA]; k++)
                    ;

                int swp = path[j][i];
                path[j][i] = path[j][k];
                path[j][k] = swp;
            }
        }

        changeList = null;

        int[][][] pmxResult = new int[2][length][2];

        for (j = 0; j < 2; j++) {
            setTwoColumn(path[j], mIndivs[id1].getSizeM(), pmxResult[j]);
        }

        path = null;

        mIndivs[id1].setGene(pmxResult[0]);
        mIndivs[id2].setGene(pmxResult[1]);

        pmxResult = null;

    }

    /**
     * 次世代への生き残り
     * 
     * @param obj 継承候補個体群
     */
    public void survive(Individuals obj) {
        int objPopCount = obj.getPopCount(); // 生き残り候補数
        int i;

        int[] ranking = new int[objPopCount]; // ID列
        double[] eval = new double[objPopCount]; // 評価列

        for (i = 0; i < objPopCount; i++) {
            ranking[i] = i;
            eval[i] = obj.getChromosome(i).getFitness();
        }

        /* 適合度の高い順にID列を並べ替え */
        Sort.sortValue(ranking, eval);

        /* 上位を生き残らせる */
        for (i = 0; i < mPopCount; i++) {
            setChromosome(i, obj.getChromosome(ranking[i]));
        }

        /* エリート戦略 */
        if (obj.getElite().getFitness() < eval[ranking[0]]) {
            /* エリート更新 */
            updated = true;
            setElite(obj.getChromosome(ranking[0]));
        } else {
            /* エリート再生 */
            setElite(obj.getElite());
            setChromosome(mPopCount - 1, obj.getElite());
        }
    }

    public void evaluation() {
        for (int i = 0; i < mPopCount; i++) {
            mIndivs[i].enforceGene();
            mIndivs[i].calcFitness();
        }
    }

    public void generateNeighbor(Individuals obj) {
        int k = 0;
        int generateCount = (int) ((mPopCount - obj.getPopCount())
                / (2 * obj.getPopCount()));

        for (int i = 0; i < obj.getPopCount(); i++) {
            setChromosome(k, obj.getChromosome(i));
            k++;

            for (int j = 0; j < generateCount; j++) {
                setChromosome(k, obj.getChromosome(i));
                int jobNum = (int) (Math.random() * (obj.getChromosome(i).getSizeN()));
                mIndivs[k].changeJobNeighbor(jobNum);
                k++;
            }
            for (int j = 0; j < generateCount; j++) {
                setChromosome(k, obj.getChromosome(i));
                int resourceNum = (int) (Math.random() * (obj.getChromosome(i).getSizeM()));
                mIndivs[k].changeResourceNeighbor(resourceNum);
                k++;
            }
        }
    }

    /**
     * @param obj 継承候補個体群
     */
    public void putChromosomes(Individuals obj) {
        int n_obj = obj.getPopCount();
        double[] sigma = new double[n_obj];
        double multiplier = (mPopCount) / (obj.getPopCount() * obj.getAverageFitness());

        int i;
        int j;
        int k;

        j = 0;
        for (i = 0; i < n_obj; i++) {
            sigma[i] = obj.getChromosome(i).getFitness() * multiplier;
            for (k = 0; k < (int) sigma[i]; k++) {
                setChromosome(j, obj.getChromosome(i));
                j++;
            }
            sigma[i] -= (int) sigma[i];
        }

        for (i = 1; i < n_obj; i++) {
            sigma[i] += sigma[i - 1];
        }

        for (i = j; i < mPopCount; i++) {
            double u = Math.random() * sigma[n_obj - 1];

            k = 0;
            while (u >= sigma[k]) {
                k++;
            }

            setChromosome(i, obj.getChromosome(k));
        }
    }

    private void setOneColumn(int[][] twoColumn, int m, int[] oneColumn) {
        for (int k = 0; k < twoColumn.length; k++) {
            oneColumn[k] = twoColumn[k][0] * m + twoColumn[k][1];
        }
    }

    private void setTwoColumn(int[] oneColumn, int m, int[][] twoColumn) {
        for (int k = 0; k < twoColumn.length; k++) {
            twoColumn[k][0] = oneColumn[k] / m;
            twoColumn[k][1] = oneColumn[k] % m;
        }
    }

}
