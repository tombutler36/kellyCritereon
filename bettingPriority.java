import java.text.DecimalFormat;
import java.util.*;

public class bettingPriority {

    class Bet{
        String betName;
        double fstar;

        public Bet(String name, double decimalOdds, double winProb){
            this.betName = name;
            this.fstar = kellyCrit(decimalOdds, winProb, 1-winProb);
        }
    }

    private Bet[] array;
    private int size;

    //Construct the max heap

    public bettingPriority(){
        this.array = new Bet[26];
        this.size = 0;
    }

    //Calculate kelly criterion value

    public double kellyCrit (double decimalOdds, double winProb, double loseProb){
        double result = (((decimalOdds - 1)*(winProb)) - (loseProb))/(decimalOdds -1);
        return result;
    }

    //MAX Heap utility methods

    private void resize() {
        Bet[] newArray = new Bet[2 * this.array.length];
        for (int i = 0; i < this.array.length; i++) {
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    private int parent(int index) {
        return Math.floorDiv((index - 1), 2);
    }

    private int leftChild(int index) {
        return ((2*index) + 1);
    }

    private int rightChild(int index) {
        return ((2*index) + 2);
    }

    private void swap(int index1, int index2) {
        Bet temp1 = this.array[index1];
        Bet temp2 = this.array[index2];
        this.array[index1] = temp2;
        this.array[index2] = temp1;

    }

    private boolean isLeaf(int index){
        if (index >= size/2 && index <= size){
            return true;
        }
        else {
            return false;
        }
    }

    // Add Bet to heap
    public void add(String name, double decimalodds, double winProb) {
        if (this.array.length == this.size){
            resize();
        }
        this.array[size] = new Bet(name, decimalodds, winProb);
        Bet percNode = this.array[size];
        int percNodeInd = size;
        while (percNodeInd != 0 && percNode.fstar > this.array[parent(percNodeInd)].fstar){
            int parentInd = parent(percNodeInd);
            swap(percNodeInd, parentInd);
            percNodeInd = parentInd;

        }
        this.size ++;
    }

    // Poll highest kelly criterion bet

    public Bet poll() {
        if (size == 0){
            return null;
        }
        // save the return bet to a temporary variable
        Bet rootBet = this.array[0];
        // replace the root with the last bet
        this.array[0] = this.array[size-1];
        // decrement the size
        this.size --;
        // percolate down
        maxHeapify(0);
        // return the stored value
        return rootBet;
    }

    private void maxHeapify(int index){
        if (!isLeaf(index)){
            if (array[index].fstar < array[leftChild(index)].fstar || array[index].fstar < array[rightChild(index)].fstar){
                if (array[leftChild(index)].fstar > array[rightChild(index)].fstar){
                    swap(index, leftChild(index));
                    maxHeapify(leftChild(index));
                }
                else{
                    swap(index, rightChild(index));
                    maxHeapify(rightChild(index));
                }
            }
        }
    }

    //Extra Max heap methods
    public int size() {
        return this.size;
    }

    public Bet peek() {
        return this.array[0];
    }


    public static void main(String args[]) {

        bettingPriority bets = new bettingPriority();


        //Add games with decimal odds an win probability to heap

        bets.add("Texans", 2.7, .37);
        bets.add("Browns", 1.5, .667);
        bets.add("Jaguars", 7.5, .133);
        bets.add("Packers", 1.09, .917);
        bets.add("Eagles", 1.5, .667);
        bets.add("Giants", 2.7, .37);
        bets.add("Buccaneers", 1.37, .73);
        bets.add("Panthers", 3.2, .313);
        bets.add("Washington Football Team", 2.5, .40);
        bets.add("Lions", 1.59, .63);
        bets.add("Bills", 2.3, .435);
        bets.add("Cardinals", 1.67, .60);
        bets.add("Broncos", 2.7, .37);
        bets.add("Raiders", 1.5, .667);
        bets.add("Chargers", 2.1, .476);
        bets.add("Dolphins", 1.77, .565);
        bets.add("Bengals", 3.5, .286);
        bets.add("Steelers", 1.33, .75);
        bets.add("49ers", 4.2, .238);
        bets.add("Saints", 1.23, .815);
        bets.add("Seahawks", 2.2, .455);
        bets.add("Rams", 1.71, .583);
        bets.add("Ravens", 1.29, .773);
        bets.add("Patriots", 3.7, .27);
        bets.add("Vikings", 1.59, .63);
        bets.add("Bears", 2.5, .40);


        Double positiveSum = 0.0;
        while (bets.size != 0){
            Bet mostOptimal = bets.poll();
            String name = mostOptimal.betName;
            Double kellyCrit = mostOptimal.fstar;
            if (kellyCrit > 0) {
                positiveSum = positiveSum + kellyCrit;
            }
            DecimalFormat df = new DecimalFormat("##.###");
            System.out.println(name+ " with a Kelly Criterion percentage of " + df.format((kellyCrit*100)) + "%");
        }
        DecimalFormat df = new DecimalFormat("##.###");
        System.out.println("Total positive sum is " + df.format(positiveSum * 100) + "%");


    }
}
