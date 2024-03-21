public class Sprite {
    public double x;
    public double y;

    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
