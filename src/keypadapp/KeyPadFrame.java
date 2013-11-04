package keypadapp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author mikelrob
 */
public class KeyPadFrame extends JFrame implements ActionListener{
    
    //ivars for top panel
    private JPanel topPanel;
    
    //ivars for info panel
    private JPanel infoPanel;
    private JLabel topLabel, middleLabel, bottomLabel;
    
    //ivars for button panel
    private JPanel buttonPanel;
    private PadButton oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton,
                    sevenButton, eightButton, nineButton, zeroButton, enterButton, cancelButton;
    
    //ivars for input storage and door status
    private String inputString, userCode, masterCode;
    private final String doorClosedString = "Door is Closed";
    private final String doorOpenString = "Door is Open";
    private final String enterCodeString = "Please enter a code";
    private final String invalidCodeString = "Invalid Code!";
    private final String enterNewCodeString = "Please enter a new code";
    private final String codeChangedString = "Code changed";
    private final String blankString = " ";
    private boolean doorIsOpen;
    private boolean isSettingUserCode;
    private final int delay = 2000; //milliseconds
    private DoorAnimationPanel doorStatusPanel;
    
    /*
     * Default contructor 
     */
    public KeyPadFrame(){
        //firstly call super constructor with Frame Title string
        super("KeyPad Security System");
        //next set position and size
        setBounds(100, 100, 600, 500);    //optional
        setResizable(false);

        //set what happens when when the close button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //initialise each of the panels
        initInfoPanel();
        initButtonPanel();
        initTopPanel();
        
        //get content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        //add the Panels in their respective positions
        contentPane.add(infoPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.EAST);
        contentPane.add(topPanel, BorderLayout.NORTH);
        
        //init strings and status bools
        inputString = "";
        userCode = "1234";
        masterCode = "4321";
        doorIsOpen = false;
        isSettingUserCode = false;
        
        //lastly show the Frame
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                setVisible(true);
            }
        });   
    }

    /*
     * Helper method to initialise the Info Panel
     */
    private void initInfoPanel() {
        //init Panel with GridBagLayout as components are not same size
        infoPanel = new JPanel(new GridBagLayout());
        //GridBag object dictates where component gets laid out
        GridBagConstraints gbc = new GridBagConstraints();
        //firstly set middle column and first row
        gbc.gridx = 1;
        gbc.gridy = 0;
        //set component does not resize
        gbc.fill = GridBagConstraints.NONE;
        //init and add door status icon
        doorStatusPanel = new DoorAnimationPanel(200);
        infoPanel.add(doorStatusPanel, gbc);
        //init and add command labels
        topLabel = new JLabel(blankString, javax.swing.SwingConstants.CENTER);
        gbc.gridy = 1;      //next row
        infoPanel.add(topLabel, gbc);
        middleLabel = new JLabel(doorClosedString, javax.swing.SwingConstants.CENTER);
        gbc.gridy = 2;      //next row
        infoPanel.add(middleLabel, gbc);
        bottomLabel = new JLabel(enterCodeString, javax.swing.SwingConstants.CENTER);
        gbc.gridy = 3;      //next row
        infoPanel.add(bottomLabel, gbc);
    }

    /*
     * Helper methos to initialise Button panel with Grid of Buttons
     */
    private void initButtonPanel() {
        //init Panel with GridLayout
        buttonPanel = new JPanel(new GridLayout(4, 3));
        //init and add buttons
        buttonPanel.add(oneButton = new PadButton("1", 1));
        oneButton.setEnabled(true);
        oneButton.addActionListener(this);
        buttonPanel.add(twoButton = new PadButton("2", 2));
        twoButton.setEnabled(true);
        twoButton.addActionListener(this);
        buttonPanel.add(threeButton = new PadButton("3", 3));
        threeButton.setEnabled(true);
        threeButton.addActionListener(this);
        buttonPanel.add(fourButton = new PadButton("4", 4));
        fourButton.setEnabled(true);
        fourButton.addActionListener(this);
        buttonPanel.add(fiveButton = new PadButton("5", 5));
        fiveButton.setEnabled(true);
        fiveButton.addActionListener(this);
        buttonPanel.add(sixButton = new PadButton("6", 6));
        sixButton.setEnabled(true);
        sixButton.addActionListener(this);
        buttonPanel.add(sevenButton = new PadButton("7", 7));
        sevenButton.setEnabled(true);
        sevenButton.addActionListener(this);
        buttonPanel.add(eightButton = new PadButton("8", 8));
        eightButton.setEnabled(true);
        eightButton.addActionListener(this);
        buttonPanel.add(nineButton = new PadButton("9", 9));
        nineButton.setEnabled(true);
        nineButton.addActionListener(this);
        buttonPanel.add(enterButton = new PadButton("Enter/Open", 10));
        enterButton.setEnabled(false);
        enterButton.addActionListener(this);
        buttonPanel.add(zeroButton = new PadButton("0", 0));
        zeroButton.setEnabled(true);
        zeroButton.addActionListener(this);
        buttonPanel.add(cancelButton = new PadButton("Cancel/Close", 11));
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(this);
    }
    
    /*
     * Helper method to initialise top panel with the custom image drawing component
     */
    private void initTopPanel(){
        topPanel = new JPanel(new GridLayout(0,1));

        HeaderImageComponent headerImage = new HeaderImageComponent();
        //complier complains uncaught exception even though i thought i caught it in object constructor
        try {
            headerImage.init();
        } catch (MalformedURLException MUE){
            System.err.println("Caught in initTopPanel: " + MUE.getMessage());
        }
        
        topPanel.add(headerImage);
        
    }

    /**
     * Implemented method from with ActionListener interface to respond to 
     * button press events. 
     * @param ae ActionEvent passed to method from sender
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        PadButton source = (PadButton) ae.getSource();
//        System.out.println("Button tag is " + source.tag());
        int buttonTag = source.tag();
        //determine which button is pressed
        switch(buttonTag){
            case 0: case 1: case 2: case 3: case 4:
            case 5: case 6: case 7: case 8: case 9: 
                //number button pressed
                inputString += buttonTag;       //append input to input string
                break;
            case 10:
                //enter/open button pressed              
                enterButtonPressed();
                break;
            case 11:
                //cancel/close button pressed
                cancelButtonPressed();
                break;
            default:
                //execution should not arrive here
                System.err.println("An error may have occured");
                break;
        }
        
        //set enter button enable or disabled as per context
        if (inputString.length() == 4){
            enterButton.setEnabled(true);
        } else {
            enterButton.setEnabled(false);
        }
        
        if (doorIsOpen){
            //disable all buttons except close
            oneButton.setEnabled(false);
            twoButton.setEnabled(false);
            threeButton.setEnabled(false);
            fourButton.setEnabled(false);
            fiveButton.setEnabled(false);
            sixButton.setEnabled(false);
            sevenButton.setEnabled(false);
            eightButton.setEnabled(false);
            nineButton.setEnabled(false);
            zeroButton.setEnabled(false);
            enterButton.setEnabled(false);
        } else {
            oneButton.setEnabled(true);
            twoButton.setEnabled(true);
            threeButton.setEnabled(true);
            fourButton.setEnabled(true);
            fiveButton.setEnabled(true);
            sixButton.setEnabled(true);
            sevenButton.setEnabled(true);
            eightButton.setEnabled(true);
            nineButton.setEnabled(true);
            zeroButton.setEnabled(true);
        }
        
        //if door is open or input string is not empty
          if (doorIsOpen || inputString.length() != 0){
              cancelButton.setEnabled(true);
          } else {
              cancelButton.setEnabled(false);
          }       
//        System.out.println(inputString);
    }
    
    /*
     * Method called from inner class to update topPanel size
     * @param img       Image object being loaded
     * @param infoflags Indicates which infomation is available
     * @param newX      
     * @param newY      
     * @param newWidth  The width info of the img
     * @param newHeight The height info of the img
     */
    private boolean updateToImage(Image img, int infoflags, int newX, int newY, int newWidth, int newHeight){
        
        //if width and height info is available then set topPanel size hint
        if ((infoflags & WIDTH) == WIDTH && (infoflags & HEIGHT) == HEIGHT){
            Dimension dim = new Dimension(newWidth, newHeight);
            topPanel.setPreferredSize(dim);
//            System.out.println("updateToImage" + dim);
        }

        repaint();
        
        //determine whether image is fininshed loading
        //to indicate whether another callback is required
        boolean imageLoaded;
        if ((infoflags & ALLBITS) != 0){
            imageLoaded = false;
        } else {
            imageLoaded = true;
        }
        return imageLoaded;
    }

    /*
     * Helper method which tests the status of the app to determine which path 
     * of execution to run following the enter button being pressed
     */
    private void enterButtonPressed() {
        //test to see if we are setting a new code
        if (isSettingUserCode && inputString.length() == 4){
            //test to ensure it is not the same as the master code
            if(!inputString.contentEquals(masterCode)){
                //set the new code
                userCode = inputString;
                isSettingUserCode = false;
                //update UI labels
                topLabel.setText(codeChangedString);
                middleLabel.setText(doorClosedString);
                bottomLabel.setText(enterCodeString);
                Timer delayedActionTimer = new Timer(delay,new TopLabelDismissalActionListener());
                delayedActionTimer.start();
            } else {
                //failed test - invalid code
                topLabel.setText(invalidCodeString);
                Timer delayedActionTimer = new Timer(delay, new TopLabelDismissalActionListener());
                delayedActionTimer.start();
            }
            
        //test to see if user code is entered
        } else if (inputString.contentEquals(userCode)){
            //open door
            doorIsOpen = true;
            doorStatusPanel.playDoorOpenAnimation();
            //update UI labels
            middleLabel.setText(doorOpenString);
            bottomLabel.setText(blankString);
        //test to see if master coe is entered
        } else if (inputString.contentEquals(masterCode)){
            //set the flag to set user code
            isSettingUserCode = true;
            //update UI labels
            middleLabel.setText(enterNewCodeString);
            bottomLabel.setText(blankString);
        
        //if none of the above then code is invalid
        } else {
            topLabel.setText(invalidCodeString);
                Timer delayedActionTimer = new Timer(delay,new TopLabelDismissalActionListener());
                delayedActionTimer.start();
        }
        //reset input string as enter button has been pressed
        inputString = "";
    }

    /*
     * Helper method which closes the door if open and then clears the
     * input string
     */
    private void cancelButtonPressed() {
        //cancel/close button pressed
        
        //if door is open then close it
        if (doorIsOpen){
            //close the door
            doorIsOpen = false;
            doorStatusPanel.playDoorCloseAnimation();
            //update info label
            middleLabel.setText(doorClosedString);
            bottomLabel.setText(enterCodeString);
        }
        
        //reset input string either way
        inputString = "";
    }
    
    /*
     * Inner class used to dismiss text from topLabel using a Timer.
     * This class implements the ActionListener which is used the Timer and 
     * has access to the label ivar of the parent class to enable clearing of text.
     */
    private class TopLabelDismissalActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            topLabel.setText(blankString);
        }
    }
    
    /*
     * Inner class used to extend use of JButton by adding an integer tag to 
     * allow the object to be used in a switch statement.
     */
    private class PadButton extends JButton{
        // a read only tag which is set by constructor.
        private int tag;
        
        public PadButton(String newButtonString, int newTag){
            super(newButtonString);
            tag = newTag;
        }
        
        /*
         * A method to access the private member variable.
         * This method returns to caller, the tag integer which is switchable
         * @return 
         */
        public int tag(){
            return tag;
        }
    }
    /*
     * Inner class used to provide image drawing in the component 
     */
    private final class HeaderImageComponent extends JComponent implements ImageObserver{
        
        //ivar for image to draw
        private Image headerImage;
        
        /*
         * Default Constructor
         */
        public HeaderImageComponent(){
            super();
            //catch URL exception which should occur
            try{
                init();
            } catch (MalformedURLException MUE){
                System.err.println("Caught in Constructor: " + MUE.getMessage());
            }
        }
        
        /*
         * This init method will load the image from disk
         */
        public void init() throws MalformedURLException{
            //load image
            URL url = getClass().getResource("MDRTechHeader.png");
            headerImage = Toolkit.getDefaultToolkit().getImage(url);
            //setSize of component to match image
            //uses imageObserver Interface to callback when iamge is loaded
            setSize(headerImage.getWidth(this), headerImage.getHeight(this));
//            System.out.println("imageComponent init(): " + getSize());
            repaint();
        }
            
        @Override
        public void paint(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            
            // set Anti-Aliasing Rendering Hint
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            //set clip
            Shape oldClip = g2.getClip();
            Rectangle newClip = new Rectangle(getSize().width, getSize().height);
            g2.setClip(newClip);
//            System.out.println("imageComponent paint(): " + g2.getClip());
            
            g2.drawImage(headerImage, 0, 0, this);
//            System.out.println("Image painted");
        }
        
        @Override
        public boolean imageUpdate(Image img, int infoflags, int newX, int newY, int newWidth, int newHeight){
            //call to instance method of KeyPadFrame
            return updateToImage(img, infoflags, newX, newY, newWidth, newHeight);
        }
    }
}

