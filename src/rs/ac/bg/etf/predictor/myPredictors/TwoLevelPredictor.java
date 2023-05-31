package rs.ac.bg.etf.predictor.myPredictors;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class TwoLevelPredictor implements Predictor {
    BHR bhr;
    Automaton[] pht;

    public TwoLevelPredictor(int size, Automaton.AutomatonType type) {
        bhr = new BHR(size);
        pht = Automaton.instanceArray(type, 1 << size);
    }

    @Override
    public boolean predict(Instruction branch) {
        return pht[bhr.getValue()].predict();
    }

    @Override
    public void update(Instruction branch) {
        boolean outcome = branch.isTaken();

        pht[bhr.getValue()].updateAutomaton(outcome);
        bhr.insertOutcome(outcome);
    }
}
