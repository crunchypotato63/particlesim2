import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyHandler implements KeyListener{

    public boolean upPress, dwnPress, lftPress, rghtPress;

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP){
            upPress = true;
        }
        if (code == KeyEvent.VK_DOWN){
            dwnPress = true;
        }
        if (code == KeyEvent.VK_LEFT){
            lftPress = true;
        }
        if (code == KeyEvent.VK_RIGHT){
            rghtPress = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP){
            upPress = false;
        }
        if (code == KeyEvent.VK_DOWN){
            dwnPress = false;
        }
        if (code == KeyEvent.VK_LEFT){
            lftPress = false;
        }
        if (code == KeyEvent.VK_RIGHT){
            rghtPress = false;
        }
    }
    
}
