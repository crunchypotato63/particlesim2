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
       

        for(Wall wall: walls){
            if(hasCollision(wall)){
                x=0;
                y=0;

            }
        }

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

        public boolean hasCollision(Wall wall) {
            double x1 = wall.getX1();
            double x2 = wall.getX2();
            double y1 = wall.getY1();
            double y2 = wall.getY2();
        
            if (x1 > x2) {
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        
            
            double m = (y2 - y1) / (x2 - x1);
            double b = y1 - m * x1;
        

            double yOnLine = m * x + b;
            if (Double.compare(y, yOnLine) != 1) {
                return false;
            }
        

            return x >= x1 && x <= x2;


        }
        

    
}
