package rs.ac.bg.etf.automaton;

public class Automaton3bits_1 implements Automaton {

    int state;

    Automaton3bits_1(int state) {
        this.state = state;
    }
    Automaton3bits_1() {
        this(0);
    }

    @Override
    public void updateAutomaton(boolean outcome) {
        if (outcome) {
            if (state < 7) {
                state++;
            }
        } else {
            if (state > 0) {
                state--;
            }
        }
    }

    @Override
    public boolean predict() {
        return state >= 4;
    }
}
