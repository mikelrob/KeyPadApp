package keypadapp;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This class provides an animated representation of a door.
 * The available behaviors include starting the open and close animations.
 * There are also internal methods to provide helper functionality to initialise
 * the object and aid the drawing of the door representation.
 * @author mikelrob
 */
public class DoorAnimationPanel extends JPanel{
    
    //ivars to store cor-ords of line end points
    private int [] xs, ys;
    private int size;
    
    //ivars to store value to aid calculate of end point for animation
    private double delay;
    private double angle;
    private double degsPerSecond;
    private int duration;
    private double numFrames;
    private final double RADSTODEGS = 0.0174532925;
    
    //ivars to store size of frame
    private int width;
    private int height;
    
    //ivars to store references to the timers required for animation
    Timer openTimer, closeTimer;
    Timer stopOpenTimer, stopCloseTimer;
    
    /*
     * Default constructor creates the animation panel with size 100 px sq
     */
    public DoorAnimationPanel(){
        init(100);
    }
    
    /*
     * Constructor which allows the creator to specify size of aniation panel
     */
    public DoorAnimationPanel(int newSize){
        init(newSize);
    }
    
    /*
     * This method is a helper routine to allow multiple constructors to 
     * set up the object in the same manner
     */
    private void init(int newSize){
        width = newSize;
        height = newSize;
        //set the size hints for the layout manager
        Dimension dim = new Dimension(width, height);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
        
        delay = 1000/24;                    //24fps
        duration = 2000;                    //millis
        numFrames = duration / delay;
        degsPerSecond = 95 / numFrames;     //used 95 degrees to ensure speed is fast
        
        size = 3;
        xs = new int[size];
        ys = new int[size];
        
        //init with door closed
        angle = 0.0;

        xs[0] = width;
        ys[0] =  ys[1] = height;
        xs[1] = 0;
        xs[2] = width;
        ys[2] = height;
        
        xs[0] = (int) scaleAndTranslate(xs[0]);
        ys[0] = (int) scaleAndTranslate(ys[0]);
        xs[1] = (int) scaleAndTranslate(xs[1]);
        ys[1] = (int) scaleAndTranslate(ys[1]);
        xs[2] = (int) scaleAndTranslate(xs[2]);
        ys[2] = (int) scaleAndTranslate(ys[2]);
        
        //init Timers used for starting and stopping animation
        openTimer = new Timer((int) delay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                angle += degsPerSecond;
                //ensure door does not open past perpendicular
                if (angle > 90){
                    angle = 90;
                }
                
                //set the end position of the door edge
                //calc position from angle and panel size
                xs[2] = (int)(width * Math.cos(angle * RADSTODEGS));
                ys[2] = height - (int)(height * Math.sin(angle * RADSTODEGS));
                
                //points for drawing are along axis
                //scaleAndTranslate brings the drawing into the center
                xs[2] = (int) scaleAndTranslate(xs[2]);
                ys[2] = (int) scaleAndTranslate(ys[2]);
                
//                System.out.println("(" + xs[2] + ", " + ys[2] + ")");
                repaint();
            }
        });
        openTimer.setRepeats(true);
        
        stopOpenTimer = new Timer(duration, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                openTimer.stop();
//                System.out.println("open stopped");
            }
        });
        stopOpenTimer.setRepeats(false);
        
        closeTimer = new Timer((int)delay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                angle -= degsPerSecond;
                //ensure door does not close past parallel
                if (angle < 0){
                    angle = 0;
                }
                
                //set the end position of the door edge
                //calc position from angle and panel size
                xs[2] = (int)(width * Math.cos(angle * RADSTODEGS));
                ys[2] = height - (int)(height * Math.sin(angle * RADSTODEGS));
                
                //again scaleAndTranslate drawing point into center
                xs[2] = (int) scaleAndTranslate(xs[2]);
                ys[2] = (int) scaleAndTranslate(ys[2]);
                
//                System.out.println("(" + xs[2] + ", " + ys[2] + ")");
                repaint();
            }
        });
        closeTimer.setRepeats(true);
        
        stopCloseTimer = new Timer(duration, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                closeTimer.stop();
//                System.out.println("close stopped");
            }
        });
        stopCloseTimer.setRepeats(false);
    }
    
    /*
     * This method allows the caller to trigger the open animation to start
     */
    public void playDoorOpenAnimation(){

        //firstly stop the animation if its running
        if (closeTimer.isRunning()){
            closeTimer.stop();
        }
        
        //trigger animation start
        openTimer.start();
        stopOpenTimer.start();
        
    }
    
    /*
     * This method allows the caller to trigger the close animation to start
     */
    public void playDoorCloseAnimation(){
        
        //firstly stop the amimation if its running
        if (openTimer.isRunning()){
            openTimer.stop();
        }
        
        //trigger animation start
        closeTimer.start();
        stopCloseTimer.start();
        
    }
    
    /*
     * This is a helper method to move the animation points from their calculated
     * postion to a more central position
     */
    private double scaleAndTranslate(double value){
        double newValue = value;
        
        //scale to 50%
        newValue *= 0.5;
        
        //translate by 25%
        newValue += (0.25 * width); //width and height are equal
        
        
        return newValue;
    }
    
    /*
     * The paint method is overridden here to draw the two lines
     * representing the wall and the door
     */
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        //first set render hints and clear the panel
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, getSize().width, getSize().height);
        
        //set the stroke and draw the lines
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawPolyline(xs, ys, size);
        
    }
}
