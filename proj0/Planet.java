public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double gravity = 6.67e-11;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dist;
        dist = Math.pow(p.xxPos-this.xxPos, 2) + Math.pow(p.yyPos-this.yyPos, 2);
        dist = Math.sqrt(dist);
        return dist;
    }

    public double calcForceExertedBy(Planet p) {
        double dist = calcDistance(p);
        double force = gravity * this.mass * p.mass / dist / dist;
        return force;
    }

    public double calcForceExertedByX(Planet p) {
        double force = calcForceExertedBy(p);
        double dist = calcDistance(p);
        double forceX = (p.xxPos-this.xxPos)/dist*force;
        return forceX;
    }

    public double calcForceExertedByY(Planet p) {
        double force = calcForceExertedBy(p);
        double dist = calcDistance(p);
        double forceY = (p.yyPos-this.yyPos)/dist*force;
        return forceY;
    }

    public  double calcNetForceExertedByX(Planet[] pArray) {
        double netForceX = 0;
        for (Planet p: pArray) {
            if (p != this) {
                netForceX += calcForceExertedByX(p);
            }
        }
        return netForceX;
    }

    public double calcNetForceExertedByY(Planet[] pArray) {
        double netForceY = 0;
        for (Planet p: pArray) {
            if (p != this) {
                netForceY += calcForceExertedByY(p);
            }
        }
        return netForceY;
    }

    public void update(double dt, double forceX, double forceY) {
        double accerX = forceX/mass;
        double accerY = forceY/mass;
        xxVel += dt*accerX;
        yyVel += dt*accerY;
        xxPos += dt*xxVel;
        yyPos += dt*yyVel;
    }

    public void draw() {
        String img = "images/" + imgFileName;
        StdDraw.picture(xxPos, yyPos, img);
    }
}