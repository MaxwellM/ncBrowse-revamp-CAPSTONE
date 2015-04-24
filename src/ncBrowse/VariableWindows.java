package ncBrowse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

public class VariableWindows extends JFrame {
    private BorderLayout borderLayout3 = new BorderLayout(0, 0);
    public static JFrame variableWindow1;
    public static JFrame variableWindow2;
    public static JFrame variableWindow3;
    public static JFrame variableWindow4;
    public static JFrame variableWindow5;
    public static JFrame variableWindow6;
    public static JFrame variableWindow7;
    public static JFrame variableWindow8;
    int fullWidth;
    int fullHeight;
    public static int winSizeWidth;
    public static int winSizeHeight;
    int locWin1X;
    int locWin1Y;
    int locWin2X;
    int locWin2Y;
    int locWin3X;
    int locWin3Y;
    int locWin4X;
    int locWin4Y;
    int locWin5X;
    int locWin5Y;
    int locWin6X;
    int locWin6Y;
    int locWin7X;
    int locWin7Y;
    int locWin8X;
    int locWin8Y;
    static int locXMid;
    static int locYMid;

    public VariableWindows(){
        Rectangle2D result = new Rectangle2D.Double();
        GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : localGE.getScreenDevices()) {
            for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
                Rectangle2D.union(result, graphicsConfiguration.getBounds(), result);
            }
        }
        fullWidth = (int) result.getWidth();
        fullHeight = (int) result.getHeight();
        locXMid = (fullWidth/2);
        locYMid = (fullHeight/2);
        winSizeWidth = (fullWidth/4);
        winSizeHeight = (fullHeight/2);
        locWin1X = 0;
        locWin1Y = 0;
        locWin2X = winSizeWidth;
        locWin2Y = 0;
        locWin3X = (winSizeWidth*2);
        locWin3Y = 0;
        locWin4X = (winSizeWidth*3);
        locWin4Y = 0;
        locWin5X = 0;
        locWin5Y = winSizeHeight;
        locWin6X = winSizeWidth;
        locWin6Y = winSizeHeight;
        locWin7X = (winSizeWidth*2);
        locWin7Y = (winSizeHeight);
        locWin8X = (winSizeWidth*3);
        locWin8Y = (winSizeHeight);
    }

    public void openVariableWindows(){
        prepareWin1GUI();
        prepareWin2GUI();
        prepareWin3GUI();
        prepareWin4GUI();
        prepareWin5GUI();
        prepareWin6GUI();
        prepareWin7GUI();
        prepareWin8GUI();
    }

    public static JFrame getVarWin1Frame() {
        return variableWindow1;
    	}
    public static JFrame getVarWin2Frame() {
        return variableWindow2;
    }
    public static JFrame getVarWin3Frame() {
        return variableWindow3;
    }
    public static JFrame getVarWin4Frame() {
        return variableWindow4;
    }
    public static JFrame getVarWin5Frame() {
        return variableWindow5;
    }
    public static JFrame getVarWin6Frame() {
        return variableWindow6;
    }
    public static JFrame getVarWin7Frame() {
        return variableWindow7;
    }
    public static JFrame getVarWin8Frame() {
        return variableWindow8;
    }
    public static int getWinSizeWidth(){
        return winSizeWidth;
    }
    public static int getWinSizeHeight(){
        return winSizeHeight;
    }
    public static int getLocXMid(){
        return locXMid;
    }
    public static int getLocYMid(){
        return locYMid;
    }

    public void prepareWin1GUI(){
        variableWindow1 = new JFrame();

        variableWindow1.setTitle("Variable Window 1");
        variableWindow1.setSize(winSizeWidth, winSizeHeight);
        variableWindow1.setLocation(locWin1X, locWin1Y);
        variableWindow1.setLayout(borderLayout3);
        variableWindow1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow1.setVisible(false);
                variableWindow1.dispose();
            }
        });
        variableWindow1.setVisible(false);
    }
    public void prepareWin2GUI(){
        variableWindow2 = new JFrame();
        variableWindow2.setTitle("Variable Window 2");
        variableWindow2.setSize(winSizeWidth, winSizeHeight);
        variableWindow2.setLocation(locWin2X, locWin2Y);
        variableWindow2.setLayout(borderLayout3);
        variableWindow2.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow2.setVisible(false);
                variableWindow2.dispose();
            }
        });
        variableWindow2.setVisible(false);
    }
    public void prepareWin3GUI(){
        variableWindow3 = new JFrame();
        variableWindow3.setTitle("Variable Window 3");
        variableWindow3.setSize(winSizeWidth, winSizeHeight);
        variableWindow3.setLocation(locWin3X, locWin3Y);
        variableWindow3.setLayout(borderLayout3);
        variableWindow3.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow3.setVisible(false);
                variableWindow3.dispose();
            }
        });
        variableWindow3.setVisible(false);
    }
    public void prepareWin4GUI(){
        variableWindow4 = new JFrame();
        variableWindow4.setTitle("Variable Window 4");
        variableWindow4.setSize(winSizeWidth, winSizeHeight);
        variableWindow4.setLocation(locWin4X, locWin4Y);
        variableWindow4.setLayout(borderLayout3);
        variableWindow4.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow4.setVisible(false);
                variableWindow4.dispose();
            }
        });
        variableWindow4.setVisible(false);
    }
    public void prepareWin5GUI(){
        variableWindow5 = new JFrame();
        variableWindow5.setTitle("Variable Window 5");
        variableWindow5.setSize(winSizeWidth, winSizeHeight);
        variableWindow5.setLocation(locWin5X, locWin5Y);
        variableWindow5.setLayout(borderLayout3);
        variableWindow5.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow5.setVisible(false);
                variableWindow5.dispose();
            }
        });
        variableWindow5.setVisible(false);
    }
    public void prepareWin6GUI(){
        variableWindow6 = new JFrame();
        variableWindow6.setTitle("Variable Window 6");
        variableWindow6.setSize(winSizeWidth, winSizeHeight);
        variableWindow6.setLocation(locWin6X, locWin6Y);
        variableWindow6.setLayout(borderLayout3);
        variableWindow6.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow6.setVisible(false);
                variableWindow6.dispose();
            }
        });
        variableWindow6.setVisible(false);
    }
    public void prepareWin7GUI(){
        variableWindow7 = new JFrame();
        variableWindow7.setTitle("Variable Window 7");
        variableWindow7.setSize(winSizeWidth, winSizeHeight);
        variableWindow7.setLocation(locWin7X, locWin7Y);
        variableWindow7.setLayout(borderLayout3);
        variableWindow7.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow7.setVisible(false);
                variableWindow7.dispose();
            }
        });
        variableWindow7.setVisible(false);
    }
    public void prepareWin8GUI(){
        variableWindow8 = new JFrame();
        variableWindow8.setTitle("Variable Window 8");
        variableWindow8.setSize(winSizeWidth, winSizeHeight);
        variableWindow8.setLocation(locWin8X, locWin8Y);
        variableWindow8.setLayout(borderLayout3);
        variableWindow8.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow8.setVisible(false);
                variableWindow8.dispose();
            }
        });
        variableWindow8.setVisible(false);
    }

    public static void setAllVisible(){
        variableWindow1.setVisible(true);
        variableWindow2.setVisible(true);
        variableWindow3.setVisible(true);
        variableWindow4.setVisible(true);
        variableWindow5.setVisible(true);
        variableWindow6.setVisible(true);
        variableWindow7.setVisible(true);
        variableWindow8.setVisible(true);
    }
}