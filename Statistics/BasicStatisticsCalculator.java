package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Performs basic statistical calculations for a set of data
 *
 * Date 2018-12-07
 *
 * @author Giedrius Kristinaitis
 */
public class BasicStatisticsCalculator {

    /**
     * Main method
     * @param args arguments for the program
     */
    public static void main(String[] args){
        new BasicStatisticsCalculator();
    }

    /**
     * Default class constructor. Performs calculations using the default data
     */
    public BasicStatisticsCalculator() {
        // default data for the program
        final double[] data = {
                9.67, 17.78, 4.22, 17.53, 2.83, 0.04, 3.38, 3.83, 2.96, 13.06,
                5.98, 6.51, 3.65, 7.48, 3.93, 9.86, 11.28, 7.81, 2.82, 6.72,
                6.67, 11.44, 6.47, 4.28, 0.78, 6.27, 0.43, 0.88, 1.92, 17.41,
                0.55, 9.22, 10.94, 4.57, 0.22, 9.12, 3.01, 2.25, 7.53, 0.09,
                4.84, 12.97, 2.93, 17.15, 7.27, 20.07, 6.65, 1.51, 6.95, 3.18,
                7.56, 1.81, 2.14, 4.88, 8.23, 6.55, 1.53, 12.55, 0.23, 4.49,
                4.47, 2.95, 21.20, 2.62, 6.98, 1.10, 7.11, 11.27, 9.28, 2.29,
                2.69, 2.64, 16.37, 1.10, 1.37, 0.44, 7.98, 5.82, 1.26, 6.89,
                18.71, 0.92, 7.39, 18.68, 9.32, 0.86, 2.50, 18.89, 4.53, 1.06,
                5.82, 5.61, 0.19, 0.40, 0.25, 7.21, 8.07, 2.09, 2.97, 1.84
        };

        performCalculations(data);
    }

    /**
     * Class constructor. Performs calculations with the specified data
     * @param data data array
     */
    public BasicStatisticsCalculator(double[] data) {
        performCalculations(data);
    }

    /**
     * Performs calculations using the specified data
     * @param data data array
     */
    private void performCalculations(double[] data) {
        System.out.println("Average: " + average(data, false));
        System.out.println("1st quartile: " + quartile(data, 1));
        System.out.println("3rd quartile: " + quartile(data, 3));
        System.out.println("Median: " + median(data));
        System.out.println("Mode: " + mode(data));
        System.out.println("Min: " + min(data));
        System.out.println("Max: " + max(data));
        System.out.println("Variance (Dispersion): " + dispersion(data));
        System.out.println("Standard deviation: " + Math.sqrt(dispersion(data)));

        System.out.println("\nSorted data: ");
        Arrays.stream(data).sorted().forEach(x -> System.out.println(x));
        System.out.println();

        int numberOfIntervals = calculateNumberOfIntervals(data.length);
        List<Interval> intervals = groupData(data, numberOfIntervals);
        printIntervals(intervals);
        System.out.println();

        double intervalAverage = intervalAverage(intervals, data.length);
        System.out.println("Interval average: " + intervalAverage);
        System.out.println("Interval dispersion: " + intervalDispersion(intervals, data.length, intervalAverage));
    }

    /**
     * Calculates the dispersion of intervals
     * @param intervals list of intervals
     * @param count number of data elements
     * @param average interval average
     * @return dispersion
     */
    private double intervalDispersion(List<Interval> intervals, int count, double average) {
        double sum = 0;

        for(Interval interval: intervals) {
            sum += Math.pow((interval.start + interval.end) / 2 - average, 2) * interval.frequency;
        }

        return sum * ((double) 1 / (count - 1));
    }

    /**
     * Calculates the average value of the intervals
     * @param intervals list of intervals
     * @param count number of data elements
     * @return average
     */
    private double intervalAverage(List<Interval> intervals, int count) {
        double sum = 0;

        for(Interval interval: intervals) {
            sum += (interval.start + interval.end) / 2 * interval.frequency;
        }

        return sum / count;
    }

    /**
     * Prints intervals to the console
     * @param intervals list of intervals
     */
    private void printIntervals(List<Interval> intervals) {
        System.out.println("Data intervals:");

        int index = 0;

        for(Interval interval: intervals) {
            System.out.println("#" + (++index) + ". (" + interval.start + "; " + interval.end
                + "): frequency = " + interval.frequency + "; relative frequency: " + interval.relativeFrequency);
        }
    }

    /**
     * Groups the specified data into intervals
     * @param data data array
     * @param numberOfIntervals number of intervals
     * @return list of intervals
     */
    private List<Interval> groupData(double[] data, int numberOfIntervals) {
        List<Interval> intervals = new ArrayList<>();

        int minValue = ((int) (min(data) / 10)) * 10;
        int maxValue = ((int) (max(data) / 10) + 1) * 10;
        double length = (maxValue - minValue) / numberOfIntervals;

        double[] sortedData = Arrays.copyOf(data, data.length);
        Arrays.sort(sortedData);

        for (int i = 0; i < numberOfIntervals; i++) {
            Interval interval = new Interval(i * length, i * length + length, 0, 0);

            final int index = i;

            interval.frequency = Arrays.stream(sortedData)
                    .filter(x -> x >= interval.start && (index == numberOfIntervals - 1 ? x <= interval.end : x < interval.end)).count();

            interval.relativeFrequency = (double) interval.frequency / data.length;

            intervals.add(interval);
        }

        return intervals;
    }

    /**
     * Finds the minimum value in the data array
     * @param data data array
     * @return min value
     */
    private double min(double[] data) {
        return Arrays.stream(data).min().getAsDouble();
    }

    /**
     * Finds the maximum value in the data array
     * @param data data array
     * @return max value
     */
    private double max(double[] data) {
        return Arrays.stream(data).max().getAsDouble();
    }

    /**
     * Finds the number of intervals to group the data into
     * @param count total number of data elements
     * @return number of intervals
     */
    private int calculateNumberOfIntervals(int count) {
        return (int) Math.ceil(1 + Math.log(count) / Math.log(2));
    }

    /**
     * Finds the dispersion of the data array
     * @param data data array
     * @return dispersion
     */
    private double dispersion(double[] data) {
        return average(data, true) - Math.pow(average(data, false), 2);
    }

    /**
     * Finds the mode of the data array
     * @param data data array
     * @return mode
     */
    private double mode(double[] data) {
        int index = 0;
        int maxCount = 0;

        for (int i = 0; i < data.length; i++) {
            int count = 0;

            for (double value: data) {
                if (value == data[i]) {
                    count++;
                }

                if (count > maxCount) {
                    maxCount = count;
                    index = i;
                }
            }
        }

        return data[index];
    }

    /**
     * Finds the median of the data array
     * @param data data array
     * @return median
     */
    private double median(double[] data) {
        double[] sortedData = Arrays.copyOf(data, data.length);
        Arrays.sort(sortedData);

        int middlePosition = sortedData.length / 2;

        if (sortedData.length % 2 == 0) {
            return (sortedData[middlePosition - 1] + sortedData[middlePosition + 1]) / 2;
        }

        return sortedData[middlePosition + 1];
    }

    /**
     * Calculates the quartile with the specified index
     * @param data data array
     * @param index index of the quantile
     * @return quartile
     */
    private double quartile(double[] data, int index) {
        if (index != 1 && index != 3) {
            throw new IllegalArgumentException("Invalid index (can be 1 or 3)");
        }

        double[] sortedData = Arrays.copyOf(data, data.length);
        Arrays.sort(sortedData);

        double position = index * (sortedData.length + 1) / 4;

        int lowerIndex = (int) Math.floor(position);
        int higherIndex = (int) Math.ceil(position);

        double multiplier = index == 1 ? 0.25 : 0.75;

        return sortedData[lowerIndex] + multiplier * (sortedData[higherIndex] - sortedData[lowerIndex]);
    }

    /**
     * Calculates the average value of the data array
     * @param data data array
     * @param quadratic is it quadratic average or not
     * @return average
     */
    private double average(double[] data, boolean quadratic) {
        if (quadratic) {
            return Arrays.stream(data).map(x -> x *= x).average().getAsDouble();
        }

        return Arrays.stream(data).average().getAsDouble();
    }

    /**
     * Holds interval data
     */
    protected class Interval {

        // beginning of the interval
        protected double start;

        // ending of the interval
        protected double end;

        // how many values fall into the interval
        protected long frequency;

        // relative frequency
        protected double relativeFrequency;

        /**
         * Class constructor
         * @param start starting value
         * @param end ending value
         * @param frequency number of values
         */
        public Interval(double start, double end, long frequency, long relativeFrequency) {
            this.start = start;
            this.end = end;
            this.frequency = frequency;
            this.relativeFrequency = relativeFrequency;
        }
    }
}
