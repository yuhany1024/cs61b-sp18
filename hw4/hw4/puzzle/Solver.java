package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private WorldState initState;
    private MinPQ<SearchNode> pq;
    private  List<WorldState> sequence;
    private SearchNode lastNode = null;
    private int nNodeEnque = 0;

    public Solver(WorldState initial) {
        initState = initial;
        SearchNode node = new SearchNode(initial, 0, null);
        pq = new MinPQ<>();
        pq.insert(node);
        nNodeEnque += 1;
        sequence = new ArrayList<>();

        while (!pq.isEmpty()) {
            node = pq.delMin();
            if (node.wordState.estimatedDistanceToGoal() == 0) {
                lastNode = node;
                break;
            }
            for (WorldState nextState: node.wordState.neighbors()) {
                if (node.lastSearchNode != null
                        && nextState.equals(node.lastSearchNode.wordState)) {
                    continue;
                }
                SearchNode nextNode = new SearchNode(nextState, node.nMoves + 1, node);
                pq.insert(nextNode);
                nNodeEnque += 1;
            }
        }
    }

    public int moves() {
        return lastNode.nMoves;
    }

    public Iterable<WorldState> solution() {
        SearchNode node = lastNode;
        while (node != null) {
            sequence.add(node.wordState);
            node = node.lastSearchNode;
        }
        Collections.reverse(sequence);
        return sequence;
    }
}
