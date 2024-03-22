import java.util.List;


public class Particle {
    private double x;
    private double y;
    private double angle; //in degrees, 0 degrees is east
    private double velocity; //in pixels per second

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

    public void update(double deltaTime) {
        //Move the particle
        double deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
        double deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;
       
    //wall begone for this version
        // for(Wall wall: walls){

        //     double x1 = wall.getX1();
        //     double x2 = wall.getX2();
        //     double y1 = wall.getY1();
        //     double y2 = wall.getY2();

        //     //wall is completely vertical
        //     if(x1 == x2 && Math.abs(x1-x) <= 5 && (y1 <= y && y <= y2) || (y1 >= y && y >= y2)){
        //         angle = 180-angle;
        //         deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
        //         deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;
        //     }

        //     //wall is completely horizontal
        //     else if(y1 == y2 && Math.abs(y1-y) <= 5 && (x1 <= x && x <= x2) || (x1 >= x && x >= x2)){
        //         angle = 360-angle;
        //         deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
        //         deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;

        //     }
            
        //     //wall is at an angle
        //     else if(x1 != x2 && y1 != y2 && hasCollision(wall)){
        //         double slope = (y2 - y1) / (x2 - x1);
        //         double theta = Math.atan(slope);

        //         double incidence = angle - theta;
        //         angle = 180+incidence;
        //         deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
        //         deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;

        //     }
        // }

        //Bounce the other way when boundary is hit
        if (x < 0 || x > ParticleSimulator.SCREEN_WIDTH) {
            angle = 180-angle;
            deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
            deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;

        }
        if (y < 0 || y > ParticleSimulator.SCREEN_HEIGHT) {
            angle = -angle;
            deltaX = velocity * Math.cos(Math.toRadians(angle)) * deltaTime;
            deltaY = velocity * Math.sin(Math.toRadians(angle)) * deltaTime;

        } 
        
        x += deltaX;
        y += deltaY;
    }

    public void explorerMode(){
        this.x *= 2;
        this.y *= 2;
        this.velocity *= 2;
    }

    public void exitExplorer(){
        this.x /= 2;
        this.y /= 2;
        this.velocity /=2;
    }

        public boolean hasCollision(Wall wall) {
            double x1 = wall.getX1();
            double x2 = wall.getX2();
            double y1 = wall.getY1();
            double y2 = wall.getY2();

            
            //makes sure x1 is smaller than x2
            if (x1 > x2) {
                double temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
        
            if(x1 <= x && x <= x2){
                double m = (y2 - y1) / (x2 - x1); //slope
                double b = y1 - m * x1; //y intercept
            

                double yOnLine = m * x + b;
                double tolerance = 10; 


                if(Math.abs(y - yOnLine) <= tolerance) {
                    return true;
                }
        }
            return false;

        }
}
