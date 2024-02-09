
public class Main {

    public class Ball{
        public int xPos;
        public int yPos;
        public int velocity;
        public int angle;

        public Ball(int x, int y, int v, int a){
            xPos = x;
            yPos = y;
            velocity = v;
            angle = a;
        }

        public void setPos(int x, int y){
            xPos = x;
            yPos = y;
        }

        public void setAngle(int a){
            angle = a;
        }

        public void setVelocity(int v){
            velocity = v;
        }

        public int getXPos(){
            return xPos;
        }

        public int getYPos(){
            return yPos;
        }

        public int getVelocity(){
            return velocity;
        }

        public int getAngle(){
            return angle;
        }
    }
}
