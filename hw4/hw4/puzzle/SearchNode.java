package hw4.puzzle;

public class SearchNode implements Comparable {
    protected WorldState wordState;
    protected int nMoves;
    protected int estimateStepsToGoal;
    protected SearchNode lastSearchNode;

    public SearchNode(WorldState ws, int n, SearchNode lastNode) {
        wordState = ws;
        nMoves = n;
        estimateStepsToGoal = ws.estimatedDistanceToGoal();
        lastSearchNode = lastNode;
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass() == this.getClass()) {
            int thisScore = this.nMoves + this.estimateStepsToGoal;
            SearchNode n = (SearchNode) o;
            int nScore = n.nMoves + n.estimateStepsToGoal;
            return thisScore - nScore;
        }
        return -1;
    }

}
