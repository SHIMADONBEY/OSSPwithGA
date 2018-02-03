/**
 * 染色体クラス
 * 
 * @author b113048
 */
public class Chromosome {
    private double mFitness; // 適合度
    private int[][] mGene; // 遺伝子配列
    private TimeLine mTimeLine; // タイムライン
    private int mSizeN;
    private int mSizeM;

    private final double LAMBDA = 16379.0;

    public Chromosome(int n, int m) {
        mSizeM = m;
        mSizeN = n;
        mTimeLine = new TimeLine(n, m);
        mGene = new int[mSizeN * mSizeM][2];
        mFitness = 0.0;
    }

    public Chromosome(ProcessTime pTime) {
        mSizeM = pTime.getResourceCount();
        mSizeN = pTime.getJobCount();
        mTimeLine = new TimeLine(pTime);

        mGene = new int[mSizeN * mSizeM][2];
        mFitness = 0.0;
    }

    public double getFitness() {
        return mFitness;
    }

    public void setFitness(double fitness) {
        mFitness = fitness;
    }

    public int[][] getGene() {
        return mGene;
    }

    public int[] getGene(int locus) {
        return mGene[locus];
    }

    /**
     * @param gene 遺伝子配列
     */
    public void setGene(int[][] gene) {
        for (int i = 0; i < mSizeN * mSizeM; i++) {
            for (int j = 0; j < 2; j++)
                mGene[i][j] = gene[i][j];
        }
    }

    public int getLength() {
        return mSizeN * mSizeM;
    }

    public int getSizeN() {
        return mSizeN;
    }

    public int getSizeM() {
        return mSizeM;
    }

    /**
     * @return 適合度関数
     */
    public void calcFitness() {
        DispatchingRule.FIFO(mGene, mTimeLine.getProcessTime(), mTimeLine.getTimeLine(),
                mTimeLine.getProcessTime().isDuplicatable());

        mFitness = ((LAMBDA - mTimeLine.getMakeSpan()) / (double) LAMBDA);
    }

    public TimeLine getTimeLine() {
        return mTimeLine;
    }

    public void setTimeLine(TimeLine timeLine) {
        mTimeLine.setTimeLine(timeLine.getTimeLine());
    }

    public void setTimeLine(int[][] timeLine) {
        mTimeLine.setTimeLine(timeLine);
    }

    public void generateGene() {
        int[] randomRow = new int[mSizeN * mSizeM];
        int m = mTimeLine.getProcessTime().getResourceCount();

        int i;

        for (i = 0; i < mSizeN * mSizeM; i++) {
            randomRow[i] = i;
        }

        for (i = 0; i < mSizeN * mSizeM; i++) {
            int j = (int) (Math.random() * (mSizeN * mSizeM - i)) + i;
            int swp = randomRow[i];
            randomRow[i] = randomRow[j];
            randomRow[j] = swp;
        }

        for (i = 0; i < mSizeN * mSizeM; i++) {
            mGene[i][0] = randomRow[i] / m;
            mGene[i][1] = randomRow[i] % m;
        }

    }

    public void mutate(double mutationRate) {
        int length = mSizeM * mSizeN;
        // 突然変異を行うか判定
        if (Math.random() >= mutationRate) {
            return;
        }

        int idxA = (int) Math.random() * length;
        int idxC = ((int) (Math.random() * (length - 1) + 1) + idxA) % length;

        for (int k = 0; k < 2; k++) {
            int swp = mGene[idxA][k]; // スワップ変数
            mGene[idxA][k] = mGene[idxC][k];
            mGene[idxC][k] = swp;
        }
    }

    public void changeJobNeighbor(int jobNum) {
        int place = (int) (Math.random() * mSizeM) + 1;

        int fore = place - 1;
        if (fore == 0) {
            fore = mSizeM;
        }

        int idxPlace;
        int idxFore;
        int curJobCount = 0;

        for (idxFore = 0; idxFore < mSizeN * mSizeM; idxFore++) {
            if (mGene[idxFore][0] == jobNum) {
                curJobCount++;
                if (curJobCount == fore) {
                    break;
                }
            }
        }

        curJobCount = 0;
        for (idxPlace = 0; idxPlace < mSizeN * mSizeM; idxPlace++) {
            if (mGene[idxPlace][0] == jobNum) {
                curJobCount++;
                if (curJobCount == place) {
                    break;
                }
            }
        }

        int swp = mGene[idxFore][1];
        mGene[idxFore][1] = mGene[idxPlace][1];
        mGene[idxPlace][1] = swp;
    }

    public void changeResourceNeighbor(int resourceNum) {
        int mSizeN = mTimeLine.getProcessTime().getJobCount();
        int place = (int) (Math.random() * mSizeN) + 1;

        int fore = place - 1;
        if (fore == 0) {
            fore = mSizeN;
        }

        int idxPlace;
        int idxFore;
        int curResourceCount = 0;

        for (idxFore = 0; idxFore < mSizeN * mSizeM; idxFore++) {
            if (mGene[idxFore][1] == resourceNum) {
                curResourceCount++;
                if (curResourceCount == fore) {
                    break;
                }
            }
        }

        curResourceCount = 0;
        for (idxPlace = 0; idxPlace < mSizeN * mSizeM; idxPlace++) {
            if (mGene[idxPlace][1] == resourceNum) {
                curResourceCount++;
                if (curResourceCount == place) {
                    break;
                }
            }
        }

        int swp = mGene[idxFore][0];
        mGene[idxFore][0] = mGene[idxPlace][0];
        mGene[idxPlace][0] = swp;
    }

    public void enforceGene() {

    }

}
