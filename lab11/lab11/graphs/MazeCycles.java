package lab11.graphs;

import java.util.Arrays;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    public int[] visited; //0: not visited; 1: in progress; 2: visited
    private int[] path;
    private int pathSize = 0;
    private boolean findLoop = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        visited = new int[maze.V()];
        Arrays.fill(visited, 0);
        path = new int[maze.V()];
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        for (int s = 0; s < maze.V(); s++) {
            if (visited[s] == 0){
                path[pathSize] = s;
                pathSize += 1;
                dfs(s, s);
                if (findLoop) {
                    drawLoop();
                    return;
                }
                pathSize -= 1;
            }
        }

    }

    // Helper methods go here
    private void dfs(int s, int parent) {
        if (findLoop) {
            return;
        }
        visited[s] = 1;
        marked[s] =  true;
        announce();
        for (int v : maze.adj(s)) {
            if (v == parent) {
                continue;
            }
            if (visited[v] == 1) {
                path[pathSize] = v;
                pathSize += 1;
                findLoop = true;
                return;
            } else if (visited[v] == 0) {
                path[pathSize] = v;
                pathSize += 1;
                dfs(v, s);
                if (findLoop) {
                    return;
                }
                pathSize -= 1;
            }
        }

        visited[s] = 2;
    }

    private void drawLoop() {
        for (int i = pathSize-1; i>=1; i--) {
            edgeTo[path[i]] = path[i-1];
            announce();
            if (path[i-1] == path[pathSize-1]) {
                return;
            }
        }
    }

}

