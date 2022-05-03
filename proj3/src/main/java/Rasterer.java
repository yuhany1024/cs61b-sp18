import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final double ROOT_LRLON = MapServer.ROOT_LRLON;
    private static final double ROOT_LRLAT = MapServer.ROOT_LRLAT;
    private static final double ROOT_ULLON = MapServer.ROOT_ULLON;
    private static final double ROOT_ULLAT = MapServer.ROOT_ULLAT;
    private static final int TILE_SIZE = MapServer.TILE_SIZE;

    private double queryLrlon;
    private double queryUllon;
    private double queryUllat;
    private double queryLrlat;
    private double queryWidth;
    private double queryHeight;

    private int depth;

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "rasterUlLon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "rrasterLrLon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "rasterLrLat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        // read the query
        queryLrlon = params.get("lrlon");
        queryUllon = params.get("ullon");
        queryUllat = params.get("ullat");
        queryLrlat = params.get("lrlat");
        queryWidth = params.get("w");
        queryHeight = params.get("h");

        // check whether it's a valid query
        if (!isValidQuery()) {
            results.put("query_success", false);
        } else {
            // compute the LonDPP
            double queryLonDPP = computeLonDPP(queryLrlon, queryUllon, queryWidth);
            depth = (int) Math.ceil(log2((ROOT_LRLON - ROOT_ULLON) / TILE_SIZE / queryLonDPP));
            depth = Math.min(depth, 7);

            // compute x, y of the raster box
            int rasterUlX = (int) Math.floor((queryUllon - ROOT_ULLON) * Math.pow(2, depth)
                    / (ROOT_LRLON - ROOT_ULLON));
            rasterUlX = Math.max(rasterUlX, 0);
            int rasterUlY = (int) Math.floor((queryUllat - ROOT_ULLAT) * Math.pow(2, depth)
                    / (ROOT_LRLAT - ROOT_ULLAT));
            rasterUlY = Math.max(rasterUlY, 0);
            int rasterLrX = (int) Math.ceil((queryLrlon - ROOT_ULLON) * Math.pow(2, depth)
                    / (ROOT_LRLON - ROOT_ULLON) - 1);
            rasterLrX = Math.min(rasterLrX, (int) Math.pow(2, depth) - 1);
            int rasterLrY = (int) Math.ceil((queryLrlat - ROOT_ULLAT) * Math.pow(2, depth)
                    / (ROOT_LRLAT - ROOT_ULLAT) - 1);
            rasterLrY = Math.min(rasterLrY, (int) Math.pow(2, depth) - 1);

            String[][] renderGrid = generateGrid(rasterUlX, rasterUlY, rasterLrX, rasterLrY);
            double rasterUlLon = ROOT_ULLON + (ROOT_LRLON - ROOT_ULLON)
                    / Math.pow(2, depth) *  rasterUlX;
            double rasterUlLat = ROOT_ULLAT + (ROOT_LRLAT - ROOT_ULLAT)
                    / Math.pow(2, depth) *  rasterUlY;
            double rasterLrLon = ROOT_ULLON + (ROOT_LRLON - ROOT_ULLON)
                    / Math.pow(2, depth) *  (rasterLrX + 1);
            double rasterLrLat = ROOT_ULLAT + (ROOT_LRLAT - ROOT_ULLAT)
                    / Math.pow(2, depth) *  (rasterLrY + 1);

            results.put("render_grid", renderGrid);
            results.put("raster_ul_lon", rasterUlLon);
            results.put("raster_ul_lat", rasterUlLat);
            results.put("raster_lr_lon", rasterLrLon);
            results.put("raster_lr_lat", rasterLrLat);
            results.put("depth", depth);
            results.put("query_success", true);
        }

        return results;
    }

    private double computeLonDPP(double lrlon,  double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    private double log2(double num) {
        return Math.log(num) / Math.log(2);
    }

    private boolean isValidQuery() {
        if (queryHeight <= 0 || queryWidth <= 0) {
            return false;
        }
        if (queryLrlon <= queryUllon || queryLrlat >= queryUllat) {
            return false;
        }
        if (queryUllat <= ROOT_LRLAT || queryLrlat >= ROOT_ULLAT) {
            return false;
        }
        if (queryLrlon <= ROOT_ULLON || queryUllon >= ROOT_LRLON) {
            return false;
        }
        return true;
    }

    private String[][] generateGrid(int ulx, int uly, int lrx, int lry) {
        int width = lrx - ulx + 1;
        int height = lry - uly + 1;
        String[][] grid = new String[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int y = uly + i;
                int x = ulx + j;
                grid[i][j] = "d" + Integer.toString(depth) + "_x" + Integer.toString(x)
                        + "_y" + Integer.toString(y) + ".png";
            }
        }
        return grid;
    }
}
