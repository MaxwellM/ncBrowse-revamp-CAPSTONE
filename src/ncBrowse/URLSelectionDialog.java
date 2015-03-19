package ncBrowse;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTMLDocument;

/**
 * Title:        netCDF File Browser
 * Description:  General purpose netCDF file Browser.
 * Copyright:    Copyright (c) 2000
 * Company:      NOAA/PMEL/EPIC
 * @author Donald Denbo
 * @version 1.0
 */

public class URLSelectionDialog extends JDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel URLPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel navigationPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  JButton goButton = new JButton();
  JButton acceptButton = new JButton();
  JButton cancelButton = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel fileURLPanel = new JPanel();
  JScrollPane httpScrollPane = new JScrollPane();
  JLabel jLabel2 = new JLabel();
  JTextField fileURLField = new JTextField();
  JEditorPane navigationText = new JEditorPane();
  DefaultComboBoxModel cbModel;
  ImageIcon backImage_;
  URL currentURL_ = null;
  Vector previousURLs_ = new Vector(5,5);
  String[] defaultURLs = {"http://plover.pmel.noaa.gov/java/ncBrowse/data",
                          "http://www.cdc.noaa.gov/Datasets",
			  "http://www.coaps.fsu.edu/WOCE/SAC/fsuwinds"};

  private URL fileURL = null;
  JComboBox webURLcBox = new JComboBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JButton backButton = new JButton();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  public URLSelectionDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    cbModel = new DefaultComboBoxModel(defaultURLs);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    goButton_actionPerformed(null);
  }

  public URLSelectionDialog() {
    this(null, "", false);
  }
  void jbInit() throws Exception {
    backImage_ = new ImageIcon(getClass().getResource("images/Back16.gif"));
    panel1.setLayout(borderLayout1);
    jLabel1.setText("Web URL:");
    goButton.setText("Go");
    goButton.addActionListener(new URLSelectionDialog_goButton_actionAdapter(this));
    acceptButton.setText("Accept");
    acceptButton.addActionListener(new URLSelectionDialog_acceptButton_actionAdapter(this));
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new URLSelectionDialog_cancelButton_actionAdapter(this));
    buttonPanel.setBorder(BorderFactory.createEtchedBorder());
    navigationPanel.setLayout(borderLayout2);
    jLabel2.setText("Data File URL:");
    fileURLField.setColumns(40);
    navigationText.addHyperlinkListener(new URLSelectionDialog_navigationText_hyperlinkAdapter(this));
    navigationText.setContentType("text/html");
    navigationText.setEditable(false);
    this.setTitle("Open Data File from the Web");
    panel1.setMinimumSize(new Dimension(550, 700));
    panel1.setPreferredSize(new Dimension(550, 700));
    webURLcBox.setEditable(true);
    webURLcBox.setModel(cbModel);
    webURLcBox.addActionListener(new URLSelectionDialog_webURLcBox_actionAdapter(this));
    URLPanel.setLayout(gridBagLayout1);
    backButton.setEnabled(false);
    backButton.setIcon(backImage_);
    backButton.setMargin(new Insets(2, 6, 2, 10));
    backButton.setText("Back");
    backButton.addActionListener(new URLSelectionDialog_backButton_actionAdapter(this));
    fileURLPanel.setLayout(gridBagLayout2);
    getContentPane().add(panel1);
    panel1.add(URLPanel, BorderLayout.NORTH);
    URLPanel.add(jLabel1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(10, 5, 10, 0), 0, 0));
    URLPanel.add(webURLcBox, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 10, 5), 0, 0));
    URLPanel.add(goButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 10), 0, 0));
    URLPanel.add(backButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 0, 0));
    panel1.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(acceptButton, null);
    buttonPanel.add(cancelButton, null);
    panel1.add(navigationPanel, BorderLayout.CENTER);
    navigationPanel.add(fileURLPanel, BorderLayout.SOUTH);
    fileURLPanel.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 0), 0, 0));
    fileURLPanel.add(fileURLField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 15), 0, 0));
    navigationPanel.add(httpScrollPane, BorderLayout.CENTER);
    httpScrollPane.getViewport().add(navigationText, null);
  }

  public URL getURL() {
    return fileURL;
  }

  void goButton_actionPerformed(ActionEvent e) {
    String newSelection = (String)webURLcBox.getSelectedItem();
    try {
      navigationText.setContentType("text/html");
      navigationText.setPage(newSelection);
      if(currentURL_ != null) {
        previousURLs_.addElement(currentURL_);
        backButton.setEnabled(true);
      }
      currentURL_ = new URL(newSelection);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    updateCBModel(newSelection);
  }


  void acceptButton_actionPerformed(ActionEvent e) {
    try {
      fileURL = new URL(fileURLField.getText());
    } catch (java.net.MalformedURLException ex) {
      ex.printStackTrace();
      fileURL = null;
    }
    setVisible(false);
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    fileURL = null;
    setVisible(false);
  }

  void navigationText_hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        JEditorPane pane = (JEditorPane) e.getSource();
        if (e instanceof HTMLFrameHyperlinkEvent) {
          HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
          HTMLDocument doc = (HTMLDocument)pane.getDocument();
          doc.processHTMLFrameHyperlinkEvent(evt);
        } else {
          try {
            URL url = e.getURL();
            String file = url.getFile();
            if(file.endsWith(".nc") || file.endsWith(".cdf")) {
              fileURLField.setText(url.toExternalForm());
            } else {
              fileURLField.setText(" ");
              pane.setPage(e.getURL());
              if(currentURL_ != null) {
                previousURLs_.addElement(currentURL_);
                backButton.setEnabled(true);
              }
              currentURL_ = e.getURL();
            }
          } catch (Throwable t) {
            t.printStackTrace();
          }
        }
    }

  }

  void webURLcBox_actionPerformed(ActionEvent e) {
    JComboBox cb = (JComboBox)e.getSource();
    String newSelection = (String)cb.getSelectedItem();
    try {
      navigationText.setContentType("text/html");
      navigationText.setPage(newSelection);
      if(currentURL_ != null) {
        previousURLs_.addElement(currentURL_);
        backButton.setEnabled(true);
      }
      currentURL_ = new URL(newSelection);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    updateCBModel(newSelection);
  }

  private void updateCBModel(String selection) {
    int index = cbModel.getIndexOf(selection);
    if(index < 0 || index >= cbModel.getSize()) {
      cbModel.insertElementAt(selection, 0);
    }
  }

  void backButton_actionPerformed(ActionEvent e) {
    currentURL_ = (URL)previousURLs_.lastElement();
    previousURLs_.removeElementAt(previousURLs_.lastIndexOf(currentURL_));
    if(previousURLs_.size() < 1) backButton.setEnabled(false);
    try {
      navigationText.setPage(currentURL_);
    } catch (IOException event) {
      event.printStackTrace();
    }
  }
}



class URLSelectionDialog_goButton_actionAdapter implements java.awt.event.ActionListener {
  URLSelectionDialog adaptee;

  URLSelectionDialog_goButton_actionAdapter(URLSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.goButton_actionPerformed(e);
  }
}

class URLSelectionDialog_acceptButton_actionAdapter implements java.awt.event.ActionListener {
  URLSelectionDialog adaptee;

  URLSelectionDialog_acceptButton_actionAdapter(URLSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.acceptButton_actionPerformed(e);
  }
}

class URLSelectionDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  URLSelectionDialog adaptee;

  URLSelectionDialog_cancelButton_actionAdapter(URLSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class URLSelectionDialog_navigationText_hyperlinkAdapter implements javax.swing.event.HyperlinkListener {
  URLSelectionDialog adaptee;

  URLSelectionDialog_navigationText_hyperlinkAdapter(URLSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void hyperlinkUpdate(HyperlinkEvent e) {
    adaptee.navigationText_hyperlinkUpdate(e);
  }
}

class URLSelectionDialog_webURLcBox_actionAdapter implements java.awt.event.ActionListener {
  URLSelectionDialog adaptee;

  URLSelectionDialog_webURLcBox_actionAdapter(URLSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.webURLcBox_actionPerformed(e);
  }
}

class URLSelectionDialog_backButton_actionAdapter implements java.awt.event.ActionListener {
  URLSelectionDialog adaptee;

  URLSelectionDialog_backButton_actionAdapter(URLSelectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.backButton_actionPerformed(e);
  }
}
