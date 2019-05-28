import java.util.*;
import java.lang.Math;

public class MonteCarlo {
    // write this program in a "macro" way in main().
    private static final int SAMPLE_NUM = 10000;
    private static final int TIME = 20;                          // time in years
    private static final double DELTA_TIME_INTERVAL = 1.0 / 250;   // around 250 trading days per year

    private static final double INFLATION = 3.5 / 100;
    private static final int INITIAL_INVESTMENT = 100000;
    private static final double MU_AGGRESSIVE = 9.4324 / 100;
    private static final double SIGMA_AGGRESSIVE = 15.675 / 100;
    private static final double MU_CONSERVATIVE = 6.189 / 100;
    private static final double SIGMA_CONSERVATIVE = 6.3438 / 100;

    public static void main(String[] args) {
        int steps = (int) (TIME / DELTA_TIME_INTERVAL);
        int median = (int) (SAMPLE_NUM * 0.5);
        int top10 = (int) (SAMPLE_NUM * (1 - 0.1));
        int bottom10 = (int) (SAMPLE_NUM * (1 - 0.9));

        double[][] value_aggressive = new double[SAMPLE_NUM][2];
        double[][] value_conservative = new double[SAMPLE_NUM][2];

        for (int i = 0; i < SAMPLE_NUM; i++) {
            value_aggressive[i][0] = INITIAL_INVESTMENT;
            value_conservative[i][0] = INITIAL_INVESTMENT;
        }

        weinerProcess(value_aggressive, steps, MU_AGGRESSIVE, SIGMA_AGGRESSIVE);
        weinerProcess(value_conservative, steps, MU_CONSERVATIVE, SIGMA_CONSERVATIVE);

        double[] output_aggressive = new double[SAMPLE_NUM];
        double[] output_conservative = new double[SAMPLE_NUM];

        for (int i = 0; i < SAMPLE_NUM; i++) {
            output_aggressive[i] = value_aggressive[i][1];
            output_conservative[i] = value_conservative[i][1];
        }

        Arrays.sort(output_aggressive);
        Arrays.sort(output_conservative);

        System.out.println("Top 10% return of aggressive portfolio is: " + output_aggressive[top10]);
        System.out.println("Median return of aggressive portfolio is: " + output_aggressive[median]);
        System.out.println("Bottom 10% return of aggressive portfolio is: " + output_aggressive[bottom10]);
        System.out.println();

        System.out.println("Top 10% return of conservative portfolio is: " + output_conservative[top10]);
        System.out.println("Median return of conservative portfolio is: " + output_conservative[median]);
        System.out.println("Bottom 10% return of conservative portfolio is: " + output_conservative[bottom10]);

    }

    private static void weinerProcess(double[][] valueMatrix, int steps, double mu, double sigma) {
        Random random = new Random();
        int curStep = 0;
        int nextStep = 1 - curStep;
        for (int i = 1; i < steps; i++) {
            for (int j = 0; j < SAMPLE_NUM; j++) {
                valueMatrix[j][nextStep] = valueMatrix[j][curStep] *
                        Math.exp( (mu - 0.5 * sigma * sigma - INFLATION) * DELTA_TIME_INTERVAL +
                                sigma * Math.sqrt(DELTA_TIME_INTERVAL) * random.nextGaussian()
                        );
            }
            curStep = nextStep;
            nextStep = 1 - curStep;
        }
        if (nextStep < curStep) { // switch result if final value is in the [0] column
            for (int i = 0; i < SAMPLE_NUM; i++) {
                double tmp = valueMatrix[i][nextStep];
                valueMatrix[i][nextStep] = valueMatrix[i][curStep];
                valueMatrix[i][curStep] = tmp;
            }
        }
    }
}
