import java.util.Arrays;

public class NBody {
    public static double readRadius(String file) {
        In in = new In(file);
        in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String file) {
        In in = new In(file);
        int N = in.readInt();
        Planet[] planets = new Planet[N];

        in.readDouble();

        for (int i=0; i<N; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            Planet P = new Planet(xP, yP, xV, yV, m, img);
            planets[i] = P;
        }
        return planets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dT = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        int nPlanets = planets.length;
        String background = "images/starfield.jpg";

        StdDraw.setScale(-1*radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, background);
        for (int i=0; i<nPlanets; i++) {
            planets[i].draw();
        }


        StdDraw.enableDoubleBuffering();

        for (double t = 0; t<=T; t+= dT) {
            double[] xForces = new double[nPlanets];
            double[] yForces = new double[nPlanets];
            for (int i=0; i<nPlanets; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
                ;           }
            StdDraw.picture(0, 0, background);
            for (int i=0; i<nPlanets; i++) {
                planets[i].update(dT, xForces[i], yForces[i]);
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }

    }
}