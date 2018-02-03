
public class DispatchingRule {

    public static void FIFO(int[][] dispatchOrder, ProcessTime pTime, int timeLine[][],
            boolean canDuplicate) {
        int jobCount = pTime.getJobCount();
        int resourceCount = pTime.getResourceCount();

        int[] C = new int[jobCount];
        int[] T = new int[resourceCount];

        setFlowTime(C);
        setResourceTime(T);

        int startTime;
        for (int l = 0; l < jobCount * resourceCount; l++) {
            int j = dispatchOrder[l][0];
            int i = dispatchOrder[l][1];

            if (C[j] >= T[i] && !canDuplicate) {
                startTime = C[j];
            } else {
                startTime = T[i];
            }

            timeLine[j][i] = startTime;

            startTime += pTime.getProcessTime(j, i);
            C[j] = startTime;
            T[i] = startTime;
        }
    }

    private static void setFlowTime(int[] C) {
        for (int j = 0; j < C.length; j++) {
            C[j] = 0;
        }
    }

    private static void setResourceTime(int[] T) {
        for (int i = 0; i < T.length; i++) {
            T[i] = 0;
        }
    }
}
