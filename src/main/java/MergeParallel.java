import java.util.concurrent.RecursiveAction;

/**
 * Created by Admin on 17.11.2015.
 */
public class MergeParallel extends RecursiveAction {
    final int[] numbers;
    final int   startPos, endPos;
    final int[] result ;
    static int  troughput;

    MergeParallel(final int[] data, int start, int end) {
        this.startPos = start;
        this.endPos  = end;
        this.numbers = data;
        result = new int[data.length];
    }

    public void setTroughput(int number)
    {
        troughput = number;
    }

    private void merge(MergeParallel left, MergeParallel right) {
        int i=0, leftPos=0, rightPos=0, leftSize = left.size(), rightSize = right.size();
        while (leftPos < leftSize && rightPos < rightSize)
            result[i++] = (left.result[leftPos] <= right.result[rightPos])
                    ? left.result[leftPos++]
                    : right.result[rightPos++];
        while (leftPos < leftSize)
            result[i++] = left.result[leftPos++];
        while (rightPos < rightSize)
            result[i++] = right.result[rightPos++];
    }

    public int size() {
        return endPos-startPos;
    }

    void quick_sort(int b, int e)
    {	int c = b; int d = e;
        while (c < d) {
            int x = result[(c+d)/2]; int i = c; int j = d;
            while (i < j) {
                while (result[i]<x) i++; while (result[j]>x) j--;
                if (i <= j) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                    i++;
                    j--; }
            }
            if (j-c < d-i)
            { if (c < j) quick_sort(c,j); c = i; }
            else { if (i < d) quick_sort(i,d); d = j; }
        }
    }

    protected void compute() {
        if (size() < troughput) {
            System.arraycopy(numbers, startPos, result, 0, size());
            quick_sort(0, size());
        } else {
            int midpoint = size() / 2;
            MergeParallel left =  new MergeParallel(numbers, startPos, startPos + midpoint);
            MergeParallel right = new MergeParallel(numbers, startPos + midpoint, endPos);
            invokeAll(left,right);
            merge(left, right);
        }
    }
}
