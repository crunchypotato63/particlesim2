import java.util.List;

public class Particle {
    private double x;
    private double y;
    private double angle; // in degrees, 0 degrees is east
    private double velocity; // in pixels per second

    public Particle(double x, double y, double angle, double velocity) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.velocity = velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void update(double deltaTime, List<Wall> walls) {
        //Move the particle
        double deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
        double deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;
       

        //Bounce the other way when wall is hit
        if (x < 0 || x > ParticleSimulator.CANVAS_WIDTH) {
            angle = 180-angle;
            deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;

        }
        if (y < 0 || y > ParticleSimulator.CANVAS_HEIGHT) {
            angle = -angle;
            deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;

        } 
        
        x += deltaX;
        y += deltaY;
    }
}
