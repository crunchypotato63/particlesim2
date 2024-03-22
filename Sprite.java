public class Sprite {
    public double x;
    public double y;
    public final double screenX;
    public final double screenY;

    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
        this.screenX = 0;
        this.screenY = 0;
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
