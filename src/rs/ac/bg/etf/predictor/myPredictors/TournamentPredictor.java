package rs.ac.bg.etf.predictor.myPredictors;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class TournamentPredictor implements Predictor {

    BHR bhr;
    Automaton phts[][];
    int selector[];

    int phtMask;
    int selectorMask;
    int historySize;

    public TournamentPredictor(int bhrSize, int phtSize, int selectorSize, Automaton.AutomatonType type) {
        bhr = new BHR(bhrSize);
        phts = new Automaton[2][];

        phts[0] = Automaton.instanceArray(type, 1 << bhrSize);
        phts[1] = Automaton.instanceArray(type, 1 << phtSize);
        selector = new int[1 << selectorSize];
        for (int i = 0; i < selector.length; i++) {
            selector[i] = 0;
        }

        phtMask = (1 << phtSize) - 1;
        selectorMask = (1 << selectorSize) - 1;
        this.historySize = historySize;
    }

    @Override
    public boolean predict(Instruction branch) {
        int selectorIndex = (int) (branch.getAddress() & selectorMask);
        int phtIndex = (int) (selector[selectorIndex] >= 0 ? 1 : 0);
        int phtAddress = (int) (phtIndex == 0 ? bhr.getValue() : (int)branch.getAddress() & phtMask);

        return phts[phtIndex][phtAddress].predict();
    }

    @Override
    public void update(Instruction branch) {
        int selectorIndex = (int) (branch.getAddress() & selectorMask);
        int phtIndex = (int) (selector[selectorIndex] >= 0 ? 1 : 0);
        int phtAddress = (int) (phtIndex == 0 ? bhr.getValue() : (int)branch.getAddress() & phtMask);

        boolean outcome = branch.isTaken();
        boolean prediction = phts[phtIndex][phtAddress].predict();

        phts[phtIndex][phtAddress].updateAutomaton(outcome);

        if ((phtIndex == 0 && outcome == prediction) || (phtIndex == 1 && outcome != prediction)) {
            selector[selectorIndex] = Math.min(selector[selectorIndex] + 1, historySize - 1);
        } else {
            selector[selectorIndex] = Math.max(selector[selectorIndex] - 1, -historySize);
        }
    }
}
