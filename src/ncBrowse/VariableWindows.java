package ncBrowse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

public class VariableWindows extends  JFrame{
    //private NcFile ncFile_ = null;
    //private WindowList windowList_;
    public static JLabel  commentlabel1;
    public static JLabel  commentlabel2;
    public static JLabel  commentlabel3;
    public static JLabel  commentlabel4;
    public static JLabel  commentlabel5;
    public static JLabel  commentlabel6;
    public static JLabel  commentlabel7;
    public static JLabel  commentlabel8;
    //private static  variableWindow1;
    private BorderLayout borderLayout3 = new BorderLayout(0, 0);
    public static JFrame variableWindow1;
    private JFrame variableWindow2;
    private JFrame variableWindow3;
    private JFrame variableWindow4;
    private JFrame variableWindow5;
    private JFrame variableWindow6;
    private JFrame variableWindow7;
    private JFrame variableWindow8;
    public JLabel headerLabel1;
    private JLabel headerLabel2;
    private JLabel headerLabel3;
    private JLabel headerLabel4;
    private JLabel headerLabel5;
    private JLabel headerLabel6;
    private JLabel headerLabel7;
    private JLabel headerLabel8;
    public JLabel statusLabel1;
    private JLabel statusLabel2;
    private JLabel statusLabel3;
    private JLabel statusLabel4;
    private JLabel statusLabel5;
    private JLabel statusLabel6;
    private JLabel statusLabel7;
    private JLabel statusLabel8;
    public JPanel controlPanel1;
    private JPanel controlPanel2;
    private JPanel controlPanel3;
    private JPanel controlPanel4;
    private JPanel controlPanel5;
    private JPanel controlPanel6;
    private JPanel controlPanel7;
    private JPanel controlPanel8;
    public static JTextArea commentTextArea1;
    public static JTextArea commentTextArea2;
    public static JTextArea commentTextArea3;
    public static JTextArea commentTextArea4;
    public static JTextArea commentTextArea5;
    public static JTextArea commentTextArea6;
    public static JTextArea commentTextArea7;
    public static JTextArea commentTextArea8;
    int fullWidth;
    int fullHeight;
    public int winSizeWidth;
    public int winSizeHeight;
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

    public VariableWindows(){
        Rectangle2D result = new Rectangle2D.Double();
        GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : localGE.getScreenDevices()) {
            for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
                result.union(result, graphicsConfiguration.getBounds(), result);
            }
        }
        fullWidth = (int) result.getWidth();
        fullHeight = (int) result.getHeight();
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
//        prepareWin1GUI();
//        prepareWin2GUI();
//        prepareWin3GUI();
//        prepareWin4GUI();
//        prepareWin5GUI();
//        prepareWin6GUI();
//        prepareWin7GUI();
//        prepareWin8GUI();
    }

    public void openVariableWindows(){
        //LocalNcFile.getVariableWindowData();
        prepareWin1GUI();
        prepareWin2GUI();
        prepareWin3GUI();
        prepareWin4GUI();
        prepareWin5GUI();
        prepareWin6GUI();
        prepareWin7GUI();
        prepareWin8GUI();
        showTextAreaWin1();
        showTextAreaWin2();
        showTextAreaWin3();
        showTextAreaWin4();
        showTextAreaWin5();
        showTextAreaWin6();
        showTextAreaWin7();
        showTextAreaWin8();
    }

//    public static void main(String[] args){
//        VariableWindow1  swingControlDemo = new VariableWindow1();
//        swingControlDemo.showTextAreaWin1();
//    }
    public static JFrame getVarWin1Frame() {
        return variableWindow1;
    	}

    public void prepareWin1GUI(){
        variableWindow1 = new JFrame();

        variableWindow1.setTitle("Variable Window 1");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow1.setSize(winSizeWidth, winSizeHeight);
        variableWindow1.setLocation(locWin1X, locWin1Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

       // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow1.setLayout(borderLayout3);
        variableWindow1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow1.setVisible(false);
                variableWindow1.dispose();
            }
        });
//        headerLabel1 = new JLabel("Header", JLabel.CENTER);
//        statusLabel1 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel1.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel1 = new JPanel();
//        controlPanel1.setLayout(new BorderLayout(0,0));
//
//        variableWindow1.add(headerLabel1, BorderLayout.NORTH);
//        variableWindow1.add(controlPanel1, BorderLayout.CENTER);
//        variableWindow1.add(statusLabel1, BorderLayout.SOUTH);
        variableWindow1.setVisible(true);
    }
    public void prepareWin2GUI(){
        variableWindow2 = new JFrame();
        variableWindow2.setTitle("Variable Window 2");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow2.setSize(winSizeWidth, winSizeHeight);
        variableWindow2.setLocation(locWin2X, locWin2Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow2.setLayout(borderLayout3);
        variableWindow2.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow2.setVisible(false);
                variableWindow2.dispose();
            }
        });
//        headerLabel2 = new JLabel("Header", JLabel.CENTER);
//        statusLabel2 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel2.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel2 = new JPanel();
//        controlPanel2.setLayout(new BorderLayout(0,0));
//
//        variableWindow2.add(headerLabel2, BorderLayout.NORTH);
//        variableWindow2.add(controlPanel2, BorderLayout.CENTER);
//        variableWindow2.add(statusLabel2, BorderLayout.SOUTH);
        variableWindow2.setVisible(true);
    }
    public void prepareWin3GUI(){
        variableWindow3 = new JFrame();
        variableWindow3.setTitle("Variable Window 3");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow3.setSize(winSizeWidth, winSizeHeight);
        variableWindow3.setLocation(locWin3X, locWin3Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow3.setLayout(borderLayout3);
        variableWindow3.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow3.setVisible(false);
                variableWindow3.dispose();
            }
        });
//        headerLabel3 = new JLabel("Header", JLabel.CENTER);
//        statusLabel3 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel3.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel3 = new JPanel();
//        controlPanel3.setLayout(new BorderLayout(0,0));
//
//        variableWindow3.add(headerLabel3, BorderLayout.NORTH);
//        variableWindow3.add(controlPanel3, BorderLayout.CENTER);
//        variableWindow3.add(statusLabel3, BorderLayout.SOUTH);
        variableWindow3.setVisible(true);
    }
    public void prepareWin4GUI(){
        variableWindow4 = new JFrame();
        variableWindow4.setTitle("Variable Window 4");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow4.setSize(winSizeWidth, winSizeHeight);
        variableWindow4.setLocation(locWin4X, locWin4Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow4.setLayout(borderLayout3);
        variableWindow4.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow4.setVisible(false);
                variableWindow4.dispose();
            }
        });
//        headerLabel4 = new JLabel("Header", JLabel.CENTER);
//        statusLabel4 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel4.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel4 = new JPanel();
//        controlPanel4.setLayout(new BorderLayout(0,0));
//
//        variableWindow4.add(headerLabel4, BorderLayout.NORTH);
//        variableWindow4.add(controlPanel4, BorderLayout.CENTER);
//        variableWindow4.add(statusLabel4, BorderLayout.SOUTH);
        variableWindow4.setVisible(true);
    }
    public void prepareWin5GUI(){
        variableWindow5 = new JFrame();
        variableWindow5.setTitle("Variable Window 5");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow5.setSize(winSizeWidth, winSizeHeight);
        variableWindow5.setLocation(locWin5X, locWin5Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow5.setLayout(borderLayout3);
        variableWindow5.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow5.setVisible(false);
                variableWindow5.dispose();
            }
        });
//        headerLabel5 = new JLabel("Header", JLabel.CENTER);
//        statusLabel5 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel5.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel5 = new JPanel();
//        controlPanel5.setLayout(new BorderLayout(0,0));
//
//        variableWindow5.add(headerLabel5, BorderLayout.NORTH);
//        variableWindow5.add(controlPanel5, BorderLayout.CENTER);
//        variableWindow5.add(statusLabel5, BorderLayout.SOUTH);
        variableWindow5.setVisible(true);
    }
    public void prepareWin6GUI(){
        variableWindow6 = new JFrame();
        variableWindow6.setTitle("Variable Window 6");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow6.setSize(winSizeWidth, winSizeHeight);
        variableWindow6.setLocation(locWin6X, locWin6Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow6.setLayout(borderLayout3);
        variableWindow6.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow6.setVisible(false);
                variableWindow6.dispose();
            }
        });
//        headerLabel6 = new JLabel("Header", JLabel.CENTER);
//        statusLabel6 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel6.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel6 = new JPanel();
//        controlPanel6.setLayout(new BorderLayout(0,0));
//
//        variableWindow6.add(headerLabel6, BorderLayout.NORTH);
//        variableWindow6.add(controlPanel6, BorderLayout.CENTER);
//        variableWindow6.add(statusLabel6, BorderLayout.SOUTH);
        variableWindow6.setVisible(true);
    }
    public void prepareWin7GUI(){
        variableWindow7 = new JFrame();
        variableWindow7.setTitle("Variable Window 7");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow7.setSize(winSizeWidth, winSizeHeight);
        variableWindow7.setLocation(locWin7X, locWin7Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow7.setLayout(borderLayout3);
        variableWindow7.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow7.setVisible(false);
                variableWindow7.dispose();
            }
        });
//        headerLabel7 = new JLabel("Header", JLabel.CENTER);
//        statusLabel7 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel7.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel7 = new JPanel();
//        controlPanel7.setLayout(new BorderLayout(0,0));
//
//        variableWindow7.add(headerLabel7, BorderLayout.NORTH);
//        variableWindow7.add(controlPanel7, BorderLayout.CENTER);
//        variableWindow7.add(statusLabel7, BorderLayout.SOUTH);
        variableWindow7.setVisible(true);
    }
    public void prepareWin8GUI(){
        variableWindow8 = new JFrame();
        variableWindow8.setTitle("Variable Window 8");

//        Dimension ss = variableWindow1.getToolkit().getScreenSize();
//        Dimension as = variableWindow1.getSize();
        variableWindow8.setSize(winSizeWidth, winSizeHeight);
        variableWindow8.setLocation(locWin8X, locWin8Y);
//        variableWindow1.setLocation(ss.width/2 - as.width/2,
//                (int)(ss.getHeight()*0.05));

        // variableWindow1.setSize((int) result.getWidth(), (int) result.getHeight());


//        variableWindow1.setLayout(new GridLayout(3, 1));
        variableWindow8.setLayout(borderLayout3);
        variableWindow8.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                //System.exit(0);
                variableWindow8.setVisible(false);
                variableWindow8.dispose();
            }
        });
//        headerLabel8 = new JLabel("Header", JLabel.CENTER);
//        statusLabel8 = new JLabel("Status",JLabel.CENTER);
//
//        statusLabel8.setSize((winSizeWidth-50), winSizeHeight/4);
//
//        controlPanel8 = new JPanel();
//        controlPanel8.setLayout(new BorderLayout(0,0));
//
//        variableWindow8.add(headerLabel8, BorderLayout.NORTH);
//        variableWindow8.add(controlPanel8, BorderLayout.CENTER);
//        variableWindow8.add(statusLabel8, BorderLayout.SOUTH);
        variableWindow8.setVisible(true);
    }
    public void setTextAreaWin1(){
        variableWindow1.add(headerLabel1, BorderLayout.NORTH);
        variableWindow1.add(controlPanel1, BorderLayout.CENTER);
        variableWindow1.add(statusLabel1, BorderLayout.SOUTH);
    }

    public void showTextAreaWin1(){
        commentlabel1 = new JLabel();
        commentTextArea1 = new JTextArea();
        headerLabel1 = new JLabel("Header", JLabel.CENTER);
        statusLabel1 = new JLabel("Status",JLabel.CENTER);

        statusLabel1.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel1 = new JPanel();
        controlPanel1.setLayout(new BorderLayout(0,0));

//        variableWindow1.add(headerLabel1, BorderLayout.NORTH);
//        variableWindow1.add(controlPanel1, BorderLayout.CENTER);
//        variableWindow1.add(statusLabel1, BorderLayout.SOUTH);
        headerLabel1.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea1);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel1.setText(commentTextArea1.getText());
            }
        });

        controlPanel1.add(commentlabel1, BorderLayout.NORTH);
        controlPanel1.add(scrollPane, BorderLayout.CENTER);
        controlPanel1.add(showButton, BorderLayout.SOUTH);
        variableWindow1.setVisible(true);
    }
    public void showTextAreaWin2(){
        commentlabel2 = new JLabel();
        commentTextArea2 = new JTextArea();
        headerLabel2 = new JLabel("Header", JLabel.CENTER);
        statusLabel2 = new JLabel("Status",JLabel.CENTER);

        statusLabel2.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel2 = new JPanel();
        controlPanel2.setLayout(new BorderLayout(0,0));

        variableWindow2.add(headerLabel2, BorderLayout.NORTH);
        variableWindow2.add(controlPanel2, BorderLayout.CENTER);
        variableWindow2.add(statusLabel2, BorderLayout.SOUTH);
        headerLabel2.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea2);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel2.setText(commentTextArea2.getText());
            }
        });

        controlPanel2.add(commentlabel2, BorderLayout.NORTH);
        controlPanel2.add(scrollPane, BorderLayout.CENTER);
        controlPanel2.add(showButton, BorderLayout.SOUTH);
        variableWindow2.setVisible(true);
    }
    public void showTextAreaWin3(){
        commentlabel3 = new JLabel();
        commentTextArea3 = new JTextArea();
        headerLabel3 = new JLabel("Header", JLabel.CENTER);
        statusLabel3 = new JLabel("Status",JLabel.CENTER);

        statusLabel3.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel3 = new JPanel();
        controlPanel3.setLayout(new BorderLayout(0,0));

        variableWindow3.add(headerLabel3, BorderLayout.NORTH);
        variableWindow3.add(controlPanel3, BorderLayout.CENTER);
        variableWindow3.add(statusLabel3, BorderLayout.SOUTH);
        headerLabel3.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea3);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel3.setText(commentTextArea3.getText());
            }
        });

        controlPanel3.add(commentlabel3, BorderLayout.NORTH);
        controlPanel3.add(scrollPane, BorderLayout.CENTER);
        controlPanel3.add(showButton, BorderLayout.SOUTH);
        variableWindow3.setVisible(true);
    }
    public void showTextAreaWin4(){
        commentlabel4 = new JLabel();
        commentTextArea4 = new JTextArea();
        headerLabel4 = new JLabel("Header", JLabel.CENTER);
        statusLabel4 = new JLabel("Status",JLabel.CENTER);

        statusLabel4.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel4 = new JPanel();
        controlPanel4.setLayout(new BorderLayout(0,0));

        variableWindow4.add(headerLabel4, BorderLayout.NORTH);
        variableWindow4.add(controlPanel4, BorderLayout.CENTER);
        variableWindow4.add(statusLabel4, BorderLayout.SOUTH);
        headerLabel4.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea4);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel4.setText(commentTextArea4.getText());
            }
        });

        controlPanel4.add(commentlabel4, BorderLayout.NORTH);
        controlPanel4.add(scrollPane, BorderLayout.CENTER);
        controlPanel4.add(showButton, BorderLayout.SOUTH);
        variableWindow4.setVisible(true);
    }
    public void showTextAreaWin5(){
        commentlabel5 = new JLabel();
        commentTextArea5 = new JTextArea();
        headerLabel5 = new JLabel("Header", JLabel.CENTER);
        statusLabel5 = new JLabel("Status",JLabel.CENTER);

        statusLabel5.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel5 = new JPanel();
        controlPanel5.setLayout(new BorderLayout(0,0));

        variableWindow5.add(headerLabel5, BorderLayout.NORTH);
        variableWindow5.add(controlPanel5, BorderLayout.CENTER);
        variableWindow5.add(statusLabel5, BorderLayout.SOUTH);
        headerLabel5.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea5);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel5.setText(commentTextArea5.getText());
            }
        });

        controlPanel5.add(commentlabel5, BorderLayout.NORTH);
        controlPanel5.add(scrollPane, BorderLayout.CENTER);
        controlPanel5.add(showButton, BorderLayout.SOUTH);
        variableWindow5.setVisible(true);
    }
    public void showTextAreaWin6(){
        commentlabel6 = new JLabel();
        commentTextArea6 = new JTextArea();
        headerLabel6 = new JLabel("Header", JLabel.CENTER);
        statusLabel6 = new JLabel("Status",JLabel.CENTER);

        statusLabel6.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel6 = new JPanel();
        controlPanel6.setLayout(new BorderLayout(0,0));

        variableWindow6.add(headerLabel6, BorderLayout.NORTH);
        variableWindow6.add(controlPanel6, BorderLayout.CENTER);
        variableWindow6.add(statusLabel6, BorderLayout.SOUTH);
        headerLabel6.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea6);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel6.setText(commentTextArea6.getText());
            }
        });

        controlPanel6.add(commentlabel6, BorderLayout.NORTH);
        controlPanel6.add(scrollPane, BorderLayout.CENTER);
        controlPanel6.add(showButton, BorderLayout.SOUTH);
        variableWindow6.setVisible(true);
    }
    public void showTextAreaWin7(){
        commentlabel7 = new JLabel();
        commentTextArea7 = new JTextArea();
        headerLabel7 = new JLabel("Header", JLabel.CENTER);
        statusLabel7 = new JLabel("Status",JLabel.CENTER);

        statusLabel7.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel7 = new JPanel();
        controlPanel7.setLayout(new BorderLayout(0,0));

        variableWindow7.add(headerLabel7, BorderLayout.NORTH);
        variableWindow7.add(controlPanel7, BorderLayout.CENTER);
        variableWindow7.add(statusLabel7, BorderLayout.SOUTH);
        headerLabel7.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea7);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel7.setText(commentTextArea7.getText());
            }
        });

        controlPanel7.add(commentlabel7, BorderLayout.NORTH);
        controlPanel7.add(scrollPane, BorderLayout.CENTER);
        controlPanel7.add(showButton, BorderLayout.SOUTH);
        variableWindow7.setVisible(true);
    }
    public void showTextAreaWin8(){
        commentlabel8 = new JLabel();
        commentTextArea8 = new JTextArea();
        headerLabel8 = new JLabel("Header", JLabel.CENTER);
        statusLabel8 = new JLabel("Status",JLabel.CENTER);

        statusLabel8.setSize((winSizeWidth-50), winSizeHeight/4);

        controlPanel8 = new JPanel();
        controlPanel8.setLayout(new BorderLayout(0,0));

        variableWindow8.add(headerLabel8, BorderLayout.NORTH);
        variableWindow8.add(controlPanel8, BorderLayout.CENTER);
        variableWindow8.add(statusLabel8, BorderLayout.SOUTH);
        headerLabel8.setText("Control in action: JTextArea");

//        JLabel  commentlabel = new JLabel("Variable: ", JLabel.RIGHT);
//        commentlabel.setText("Variable: "+ncFile_.getVariables().get(0).toString());
//        JTextArea commentTextArea =
//                new JTextArea("This is a Swing tutorial "
//                        +"to make GUI application in Java.",5,20);

//        commentTextArea1.setText("Variable: ");
        JScrollPane scrollPane = new JScrollPane(commentTextArea8);
        scrollPane.setPreferredSize(new Dimension((winSizeWidth-50) , (winSizeHeight/2)));

        JButton showButton = new JButton("Show");

        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel8.setText(commentTextArea8.getText());
            }
        });

        controlPanel8.add(commentlabel8, BorderLayout.NORTH);
        controlPanel8.add(scrollPane, BorderLayout.CENTER);
        controlPanel8.add(showButton, BorderLayout.SOUTH);
        variableWindow8.setVisible(true);
    }
}