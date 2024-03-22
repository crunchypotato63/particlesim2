public class Sprite {
    private double x;
    private double y;
    private final int screenX;
    private final int screenY;
    private final int size;

    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = 38;
        this.screenX = size*16;
        this.screenY = size*9;
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

    public int getScreenX(){
        return screenX;
    }

    public int getScreenY(){
        return screenY;
    }

    public int getSize(){
        return size;
    }

}
