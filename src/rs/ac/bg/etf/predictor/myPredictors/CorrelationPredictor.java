package rs.ac.bg.etf.predictor.myPredictors;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class CorrelationPredictor implements Predictor {
    BHR bhr;
    Automaton [][] phts;
    int mask;

    public CorrelationPredictor(int bhrSize, int phtSize, Automaton.AutomatonType type) {
        bhr = new BHR(bhrSize);
        phts = new Automaton[1 << bhrSize][];
        for(int i = 0; i < (1 << bhrSize); i++) {
            phts[i] = Automaton.instanceArray(type, 1 << phtSize);
        }

        mask = (1 << phtSize) - 1;
    }

    @Override
    public boolean predict(Instruction branch) {
        return phts[bhr.getValue()][(int) (branch.getAddress() & mask)].predict();
    }

    @Override
    public void update(Instruction branch) {
        boolean outcome = branch.isTaken();

        phts[bhr.getValue()][(int) (branch.getAddress() & mask)].updateAutomaton(outcome);
        bhr.insertOutcome(outcome);
    }
}
