package localsearch;

import solution.Solution;

public class LocalOptimal implements LocalSearch {

    private Solution currentSolution;
    private Boolean elementHasBeenAdded;
    private Boolean elementHasBeenReplaced;

    public LocalOptimal() {}

    public Solution search(final Solution initialSoution) {
        this.setInitialState(initialSoution);
        while (!this.stopCriteriaMet()) {
            this.setNothingDone();
            this.addElement();
            if (this.elementHasBeenAdded) continue;
            this.replaceElement();
        }
        return this.currentSolution;
    }

    private void setInitialState(final Solution initialSoution) {
        this.currentSolution = initialSoution;
        this.elementHasBeenAdded = Boolean.TRUE;
        this.elementHasBeenReplaced = Boolean.TRUE;
    }

    private void setNothingDone() {
        this.elementHasBeenAdded = Boolean.FALSE;
        this.elementHasBeenReplaced = Boolean.FALSE;
    }

    private void addElement() {}

    private void replaceElement() {}

    private Boolean stopCriteriaMet() {
        // stop when nothing has been done in the last iteration
        return !this.elementHasBeenAdded && !this.elementHasBeenReplaced;
    }
}
