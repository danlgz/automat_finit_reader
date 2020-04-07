import java.util.*;

public class AFDState {
    private Hashtable<Character, Integer> nextStateDict;
    private int statePosition;

    public AFDState(int statePosition) {
        this.statePosition = statePosition;
    }

    public void setStage(char symbol, int goToStatePosition) {
        this.nextStateDict.put(symbol, goToStatePosition);
    }

    public int getNextState(char symbol) {
        if (!this.nextStateDict.containsKey(symbol)) return 0;
        return this.nextStateDict.get(symbol);
    }

    public int getStatePosition() {
        return this.statePosition;
    }
}
