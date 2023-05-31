package rs.ac.bg.etf.predictor.myPredictors;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class gSharePredictor implements Predictor {

    BHR bhr;
    Automaton pht[];
    int mask;

    public gSharePredictor(int size, Automaton.AutomatonType type) {
        bhr = new BHR(size);
        pht = Automaton.instanceArray(type, 1 << size);
        mask = (1 << size) - 1;
    }

    @Override
    public boolean predict(Instruction branch) {
        int maskedAddress = (int) (branch.getAddress() & mask);
        return pht[bhr.getValue() ^ maskedAddress].predict();
    }

    

    @Override
    public void update(Instruction branch) {
        boolean outcome = branch.isTaken();
        int maskedAddress = (int) (branch.getAddress() & mask);

        pht[bhr.getValue() ^ maskedAddress].updateAutomaton(outcome);
        bhr.insertOutcome(outcome);
    }
}
