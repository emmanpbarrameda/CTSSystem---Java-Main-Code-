// ©  M A D E		B Y		E M M A N		B A R R A M E D A  © //
//CTS System

package MainPackage;

import SystemDB.DBconnection;
import UppercaseTypeFilterPackage.UppercaseALLFilter_API;
import UppercaseTypeFilterPackage.UppercaseDocumentFilter_API;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Collections;
//import java.util.Date;
//import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author EMMANUEL PEÑAFLORIDA BARRAMEDA
 */
public final class EditGUIOptions extends javax.swing.JDialog {

    Connection conn;

    //for app name
    String mainAppNameFromDB; ///this string will get DATA from db

    //for app top name
    String mainTopAppNameFromDB; ///this string will get DATA from db

    //for company name
    String companyNameFromDB; ///this string will get DATA from db

    //Normal Popups Title
    String mainnameString; ///this string will get DATA from db

    //Error Popups Title
    String mainErrorString; ///this string will get DATA from db

    //for money currency
    String pesoSignString; ///this string will get DATA from db

    //for main depending
    String maindependingFromDB; ///this string will get DATA from db

    //for TOAST
    final String restartwait = "System is Restarting...";
    final Dimension dimToast = Toolkit.getDefaultToolkit().getScreenSize();
    final int widthvarToast = this.getSize().width;
    final int heightvatToast = this.getSize().height;
    final int xPosToast = (dimToast.width - widthvarToast) / 2;
    final int yPosToast = (dimToast.width - heightvatToast) / 2;
    final ToastManager restartToast = new ToastManager(restartwait, xPosToast, yPosToast);

    //for restart
    public static final String SUN_JAVA_COMMAND = "sun.java.command";
    Runnable runBeforeRestart;

    DocumentFilter firstletterCaps = new UppercaseDocumentFilter_API();

    DocumentFilter ALLCAPS = new UppercaseALLFilter_API();

    /**
     * Creates new form AddEmployeeGUI
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public EditGUIOptions() throws SQLException, IOException {
        initComponents();
        //connection to database
        DBconnection c = new DBconnection();
        conn = c.getconnection();

        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width / 2 - getWidth() / 2, size.height / 2 - getHeight() / 2);

        this.getRootPane().setBorder(new MatteBorder(0, 1, 1, 1, (new Color(0,102,204))));
        this.setModal(true); //this.setAlwaysOnTop(true);
        GUINaming_DATA();
        insertGUINaming();
        //--------------------- DISABLE the AUTO TAB AND SHIFT in JTextFields UI ---------------------// 
        //this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.<KeyStroke>emptySet());
        this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.<KeyStroke>emptySet());
        this.setFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, Collections.<KeyStroke>emptySet());
        this.setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, Collections.<KeyStroke>emptySet());

        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        this.setFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        this.setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, Collections.EMPTY_SET);

        //CAPS on first Letter
        ((AbstractDocument) tf1.getDocument()).setDocumentFilter(firstletterCaps);
        ((AbstractDocument) tf2.getDocument()).setDocumentFilter(firstletterCaps);
        ((AbstractDocument) tf4.getDocument()).setDocumentFilter(firstletterCaps);
        ((AbstractDocument) tf5.getDocument()).setDocumentFilter(firstletterCaps);

        //all caps
        ((AbstractDocument) tf3.getDocument()).setDocumentFilter(ALLCAPS);

        //no paste
        //tf15.setTransferHandler(null);
        //tf16.setTransferHandler(null);
    }

    //-------------------- START VOID CODES HERE --------------------//
    //GUINaming from Database
    public void GUINaming_DATA() throws SQLException {
        ResultSet rsGNaming = null;
        Statement stGNaming = null;
        try {
            stGNaming = conn.createStatement();
            rsGNaming = stGNaming.executeQuery("select * FROM GUINames");

            //set the GUI Main Title
            mainAppNameFromDB = rsGNaming.getString("MainAppName");
            this.setTitle(mainAppNameFromDB + " | SETTINGS");

            //set the GUI Top Title
            mainTopAppNameFromDB = rsGNaming.getString("MainTopAppName");
            guiTitle.setText(mainTopAppNameFromDB + " | SETTINGS");

            //set the Default Normal Popups Title Message
            mainnameString = rsGNaming.getString("PopupNormal");

            //set the Default Error Popups Title Message
            mainErrorString = rsGNaming.getString("PopupError");

            //set the main depending
            maindependingFromDB = rsGNaming.getString("Depending");
            lblmain.setText(maindependingFromDB);

        } catch (SQLException e) {
        } finally {
            try {
                stGNaming.close();
                rsGNaming.close();
            } catch (SQLException e) {
                System.out.println("ERR:" + e);
            }
        }
    }

    /*
    public void auditEdit() {
        Date currentDate = GregorianCalendar.getInstance().getTime();
        DateFormat df = DateFormat.getDateInstance();
        String dateString = df.format(currentDate);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
        String timeString = sdf.format(d);

        String value0 = timeString;
        String value1 = dateString;
        String val = txt_emp.getText();
        try {
            String reg = "insert into Audit (emp_id, date, status) values ('" + val + "','" + value0 + " / " + value1 + "','Name is Edited by " + txt_emp.getText() + "')";
            try (PreparedStatement pstAudit = conn.prepareStatement(reg)) {
                pstAudit.execute();
                pstAudit.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }*/
    private void clear() throws SQLException {
        tf1.setText("");
        tf2.setText("");
        tf3.setText("");
        tf4.setText("");
        tf5.setText("");
        tf6.setText("");
        tf7.setText("");

        cb1.setSelected(false);
        cb2.setSelected(false);
        cb3.setSelected(false);
        cb4.setSelected(false);
        cb5.setSelected(false);
        cb6.setSelected(false);
        cb7.setSelected(false);

        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb3.setEnabled(true);
        cb4.setEnabled(true);
        cb5.setEnabled(true);
        cb6.setEnabled(true);
        cb7.setEnabled(true);

        tf1.setEditable(false);
        tf2.setEditable(false);
        tf3.setEditable(false);
        tf4.setEditable(false);
        tf5.setEditable(false);
        tf6.setEditable(false);
        tf7.setEditable(false);

        tf1.requestFocus(false);
        tf2.requestFocus(false);
        tf3.requestFocus(false);
        tf4.requestFocus(false);
        tf5.requestFocus(false);
        tf6.requestFocus(false);
        tf7.requestFocus(false);

        savechangesBTN.setEnabled(true);
        checkAllBTN.setEnabled(true);
        uncheckAllBTN.setEnabled(true);
        reset2defaultBTN.setEnabled(true);
        applyBTN.setEnabled(false);
    }

    //forceClose/Clicked on X (Exit) button. and the Detector files will become deleted
    public void forceCloseDeleteDetectorFiles() {
        //delete the userdata.ecoders
        try {

            File f = new File(System.getProperty("user.dir"));
            File dir = f.getAbsoluteFile();
            String path = dir.toString();

            File file1 = new File(path + "/data/userdata.ecoders");

            if (file1.exists()) {
                if (file1.delete());
                System.out.println("File deleted");

            } else {
                System.out.println("Cannot delete login detector file.");
            }

        } catch (Exception e) {
            System.err.println("ERR:" + e);
        }
    }

    //delete the UserLevel Files
    public void deleteUserLevelFiles() {
        //find the filepath of the current running/open jar/exe file
        File f = new File(System.getProperty("user.dir"));
        File dir = f.getAbsoluteFile();
        String path = dir.toString();

        File file = new File(path + "/data/level/");
        for (File dirFilesDelete : file.listFiles()) {
            if (!dirFilesDelete.isDirectory()) {
                dirFilesDelete.delete();
            }
        }
    }

    //delete the User (USERNAME) Files
    public static void deleteUsernameFiles() {
        //find the filepath of the current running/open jar/exe file
        File f = new File(System.getProperty("user.dir"));
        File dir = f.getAbsoluteFile();
        String path = dir.toString();

        File file = new File(path + "/data/user/");
        for (File dirFilesDelete : file.listFiles()) {
            if (!dirFilesDelete.isDirectory()) {
                dirFilesDelete.delete();
            }
        }
    }

    //restart automatically
    public void restartEditGUI() throws IOException, SQLException {
        final JDialog frameEditGUI = new JDialog();
        frameEditGUI.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        frameEditGUI.pack();
        frameEditGUI.setModal(true);
        String[] options = new String[1];
        options[0] = "Okay, Restart the " + mainAppNameFromDB + " Now";
        int jop = JOptionPane.showOptionDialog(frameEditGUI.getContentPane(), "<html><center>Updated Successfully! <br>You need to Restart the " + mainAppNameFromDB + " <br>and Login Again to save your Changes on the Database.</br></center></html>", mainnameString, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        switch (jop) {
            case 0:
                //yes now
                getToolkit().beep();

                //forceCloseDeleteDetectorFiles(); //<-- delete the userData detector file.
                //deleteUserLevelFiles(); //<-- delete the userLevel detector file.
                //deleteUsernameFiles(); //<-- delete the userName detector file.

                restartToast.showToast();

                //restart void
                try {
                    // java binary
                    String java = System.getProperty("java.home") + "/bin/java";
                    // vm arguments
                    List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
                    StringBuffer vmArgsOneLine = new StringBuffer();
                    vmArguments.stream().filter((arg) -> (!arg.contains("-agentlib"))).map((arg) -> {
                        vmArgsOneLine.append(arg);
                        return arg;
                    }).forEachOrdered((_item) -> {
                        vmArgsOneLine.append(" ");
                    });

                    // if it's the agent argument : we ignore it otherwise the
                    // address of the old application and the new one will be in conflict
                    // init the command to execute, add the vm args
                    final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

                    // program main and program arguments
                    String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
                    // program main is a jar
                    if (mainCommand[0].endsWith(".jar")) {
                        // if it's a jar, add -jar mainJar
                        cmd.append("-jar ").append(new File(mainCommand[0]).getPath());
                    }
                    if (mainCommand[0].endsWith(".exe")) {
                        // if it's a jar, add -jar mainJar
                        cmd.append("-exe ").append(new File(mainCommand[0]).getPath());
                    }
                    if (mainCommand[0].endsWith(".exe")) {
                        // if it's a jar, add -jar mainJar
                        cmd.append("-exec ").append(new File(mainCommand[0]).getPath());

                    } else {
                        // else it's a .class, add the classpath and mainClass
                        cmd.append("-cp \"").append(System.getProperty("java.class.path")).append("\" ").append(mainCommand[0]);
                    }

                    // finally add program arguments
                    for (int i = 1; i < mainCommand.length; i++) {
                        cmd.append(" ");
                        cmd.append(mainCommand[i]);
                    }

                    // execute the command in a shutdown hook, to be sure that all the
                    // resources have been disposed before restarting the application
                    Runtime.getRuntime().addShutdownHook(new Thread() {

                        @Override
                        public void run() {
                            try {
                                Runtime.getRuntime().exec(cmd.toString());
                            } catch (IOException e) {
                            }
                        }
                    });

                    // execute some custom code before restarting
                    if (runBeforeRestart != null) {
                        runBeforeRestart.run();
                    }

                    // exit
                    System.exit(0);
                } catch (Exception e) {
                    // something went wrong
                    throw new IOException("Error while trying to restart the " + mainAppNameFromDB + "", e);
                }
                break;

            //default code (WHEN X (EXIT) Button Click
            default:
                getToolkit().beep();
                restartEditGUI(); //<-- open the void JDIALOG again, when the user click the X (EXIT) Button
                break;
        }
    }

    private void insertGUINaming() throws SQLException {
        ResultSet rsGUINaming = null;
        PreparedStatement pstGUINaming = null;
        try {
            String query = "SELECT * FROM GUINames WHERE Depending=?";
            pstGUINaming = conn.prepareStatement(query);
            pstGUINaming.setString(1, lblmain.getText());
            rsGUINaming = pstGUINaming.executeQuery();
            if (lblmain.getText().isEmpty()) {
                //getToolkit().beep();
                clear();

            } else if (rsGUINaming.next()) {

                String s1 = rsGUINaming.getString("MainAppName");
                tf1.setText(s1);

                String s2 = rsGUINaming.getString("MainCompanyName");
                tf2.setText(s2);

                String s3 = rsGUINaming.getString("MainTopAppName");
                tf3.setText(s3);

                String s4 = rsGUINaming.getString("PopupNormal");
                tf4.setText(s4);

                String s5 = rsGUINaming.getString("PopupError");
                tf5.setText(s5);

                String s6 = rsGUINaming.getString("TimerSpeed");
                tf6.setText(s6);

                String s7 = rsGUINaming.getString("StopwatchSpeed");
                tf7.setText(s7);
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong! ERR: " + e, mainErrorString, JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                pstGUINaming.close();
                rsGUINaming.close();
            } catch (SQLException e) {
                System.out.println("ERR:" + e);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        viewpanel = new javax.swing.JPanel();
        lbl1 = new javax.swing.JLabel();
        cb1 = new javax.swing.JCheckBox();
        tf1 = new javax.swing.JTextField();
        lbl2 = new javax.swing.JLabel();
        cb2 = new javax.swing.JCheckBox();
        tf2 = new javax.swing.JTextField();
        lbl3 = new javax.swing.JLabel();
        cb3 = new javax.swing.JCheckBox();
        tf3 = new javax.swing.JTextField();
        lbl4 = new javax.swing.JLabel();
        cb4 = new javax.swing.JCheckBox();
        tf4 = new javax.swing.JTextField();
        cb5 = new javax.swing.JCheckBox();
        lbl5 = new javax.swing.JLabel();
        tf5 = new javax.swing.JTextField();
        lbl6 = new javax.swing.JLabel();
        cb6 = new javax.swing.JCheckBox();
        tf6 = new javax.swing.JTextField();
        lbl7 = new javax.swing.JLabel();
        cb7 = new javax.swing.JCheckBox();
        tf7 = new javax.swing.JTextField();
        txt_emp = new javax.swing.JLabel();
        lblmain = new javax.swing.JLabel();
        reset2defaultBTN = new javax.swing.JButton();
        checkAllBTN = new javax.swing.JButton();
        savechangesBTN = new javax.swing.JButton();
        uncheckAllBTN = new javax.swing.JButton();
        applyBTN = new javax.swing.JButton();
        pnlTop = new javax.swing.JPanel();
        guiTitle = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(249, 250, 253));
        setUndecorated(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainpanel.setBackground(new java.awt.Color(249, 250, 253));
        mainpanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONFIGURE GUI", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(0, 102, 204))); // NOI18N
        mainpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBackground(new java.awt.Color(249, 250, 253));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        viewpanel.setBackground(new java.awt.Color(249, 250, 253));
        viewpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl1.setText("Main Application Name:");
        viewpanel.add(lbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 150, 30));

        cb1.setBackground(new java.awt.Color(249, 250, 253));
        cb1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb1.setContentAreaFilled(false);
        cb1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb1.setFocusPainted(false);
        cb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb1ActionPerformed(evt);
            }
        });
        viewpanel.add(cb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, 30));

        tf1.setEditable(false);
        tf1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf1.setOpaque(false);
        tf1.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        viewpanel.add(tf1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 180, 30));

        lbl2.setText("Company Name:");
        viewpanel.add(lbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 150, 30));

        cb2.setBackground(new java.awt.Color(249, 250, 253));
        cb2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb2.setContentAreaFilled(false);
        cb2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb2.setFocusPainted(false);
        cb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb2ActionPerformed(evt);
            }
        });
        viewpanel.add(cb2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, -1, 30));

        tf2.setEditable(false);
        tf2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf2.setOpaque(false);
        tf2.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        viewpanel.add(tf2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 180, 30));

        lbl3.setText("Main Top App Name:");
        viewpanel.add(lbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 150, 30));

        cb3.setBackground(new java.awt.Color(249, 250, 253));
        cb3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb3.setContentAreaFilled(false);
        cb3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb3.setFocusPainted(false);
        cb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb3ActionPerformed(evt);
            }
        });
        viewpanel.add(cb3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, -1, 30));

        tf3.setEditable(false);
        tf3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf3.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf3.setOpaque(false);
        tf3.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        viewpanel.add(tf3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 180, 30));

        lbl4.setText("Popup Normal Name:");
        viewpanel.add(lbl4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 150, 30));

        cb4.setBackground(new java.awt.Color(249, 250, 253));
        cb4.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb4.setContentAreaFilled(false);
        cb4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb4.setFocusPainted(false);
        cb4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb4ActionPerformed(evt);
            }
        });
        viewpanel.add(cb4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, -1, 30));

        tf4.setEditable(false);
        tf4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf4.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf4.setOpaque(false);
        tf4.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        viewpanel.add(tf4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 180, 30));

        cb5.setBackground(new java.awt.Color(249, 250, 253));
        cb5.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb5.setContentAreaFilled(false);
        cb5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb5.setFocusPainted(false);
        cb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb5ActionPerformed(evt);
            }
        });
        viewpanel.add(cb5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 170, -1, 30));

        lbl5.setText("Popup Error Name:");
        viewpanel.add(lbl5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 150, 30));

        tf5.setEditable(false);
        tf5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf5.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf5.setOpaque(false);
        tf5.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        viewpanel.add(tf5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 180, 30));

        lbl6.setText("Timer Speed:");
        viewpanel.add(lbl6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 150, 30));

        cb6.setBackground(new java.awt.Color(249, 250, 253));
        cb6.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb6.setContentAreaFilled(false);
        cb6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb6.setFocusPainted(false);
        cb6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb6ActionPerformed(evt);
            }
        });
        viewpanel.add(cb6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, -1, 30));

        tf6.setEditable(false);
        tf6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf6.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf6.setToolTipText("Default Timer Speed is: 1000 (1 second)");
        tf6.setOpaque(false);
        tf6.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        tf6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf6KeyTyped(evt);
            }
        });
        viewpanel.add(tf6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 180, 30));

        lbl7.setText("Stopwatch Speed:");
        viewpanel.add(lbl7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 150, 30));

        cb7.setBackground(new java.awt.Color(249, 250, 253));
        cb7.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        cb7.setContentAreaFilled(false);
        cb7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cb7.setFocusPainted(false);
        cb7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb7ActionPerformed(evt);
            }
        });
        viewpanel.add(cb7, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 250, -1, 30));

        tf7.setEditable(false);
        tf7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        tf7.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf7.setToolTipText("Default Stopwatch Speed is: 9");
        tf7.setOpaque(false);
        tf7.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        tf7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf7KeyTyped(evt);
            }
        });
        viewpanel.add(tf7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 180, 30));

        jScrollPane1.setViewportView(viewpanel);

        mainpanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 340, 290));

        txt_emp.setForeground(new java.awt.Color(249, 250, 253));
        txt_emp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_emp.setText("emp");
        mainpanel.add(txt_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, 90, -1));

        lblmain.setBackground(new java.awt.Color(249, 250, 253));
        lblmain.setForeground(new java.awt.Color(249, 250, 253));
        lblmain.setFocusable(false);
        mainpanel.add(lblmain, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 24, 140, 20));

        reset2defaultBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        reset2defaultBTN.setText("Reset to Default");
        reset2defaultBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reset2defaultBTN.setFocusPainted(false);
        reset2defaultBTN.setFocusable(false);
        reset2defaultBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset2defaultBTNActionPerformed(evt);
            }
        });
        mainpanel.add(reset2defaultBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, -1, -1));

        checkAllBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        checkAllBTN.setText("Check All");
        checkAllBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkAllBTN.setFocusPainted(false);
        checkAllBTN.setFocusable(false);
        checkAllBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAllBTNActionPerformed(evt);
            }
        });
        mainpanel.add(checkAllBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 340, -1, -1));

        savechangesBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        savechangesBTN.setText("Save Changes");
        savechangesBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        savechangesBTN.setFocusPainted(false);
        savechangesBTN.setFocusable(false);
        savechangesBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savechangesBTNActionPerformed(evt);
            }
        });
        mainpanel.add(savechangesBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 370, -1, -1));

        uncheckAllBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        uncheckAllBTN.setText("Uncheck All");
        uncheckAllBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        uncheckAllBTN.setFocusPainted(false);
        uncheckAllBTN.setFocusable(false);
        uncheckAllBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uncheckAllBTNActionPerformed(evt);
            }
        });
        mainpanel.add(uncheckAllBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 340, -1, -1));

        applyBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        applyBTN.setText("Apply");
        applyBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        applyBTN.setEnabled(false);
        applyBTN.setFocusPainted(false);
        applyBTN.setFocusable(false);
        applyBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBTNActionPerformed(evt);
            }
        });
        mainpanel.add(applyBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 400, -1, -1));

        getContentPane().add(mainpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 360, 440));

        pnlTop.setBackground(new java.awt.Color(0, 102, 204));
        pnlTop.setPreferredSize(new java.awt.Dimension(116, 30));
        pnlTop.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlTopMouseDragged(evt);
            }
        });
        pnlTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlTopMousePressed(evt);
            }
        });
        pnlTop.setLayout(null);

        guiTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        guiTitle.setForeground(new java.awt.Color(255, 255, 255));
        guiTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/MAINICON_18px.png"))); // NOI18N
        guiTitle.setText("CTS");
        guiTitle.setIconTextGap(2);
        guiTitle.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                guiTitleMouseDragged(evt);
            }
        });
        guiTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                guiTitleMousePressed(evt);
            }
        });
        pnlTop.add(guiTitle);
        guiTitle.setBounds(5, 0, 220, 30);

        exitButton.setBackground(new java.awt.Color(0, 102, 204));
        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_multiply_18px_1.png"))); // NOI18N
        exitButton.setBorder(null);
        exitButton.setBorderPainted(false);
        exitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitButton.setDefaultCapable(false);
        exitButton.setFocusPainted(false);
        exitButton.setFocusable(false);
        exitButton.setIconTextGap(0);
        exitButton.setRequestFocusEnabled(false);
        exitButton.setVerifyInputWhenFocusTarget(false);
        exitButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                exitButtonMouseMoved(evt);
            }
        });
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButtonMouseExited(evt);
            }
        });
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        pnlTop.add(exitButton);
        exitButton.setBounds(335, 0, 20, 30);

        getContentPane().add(pnlTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    int xx,xy;
    private void cb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb1ActionPerformed
        if (cb1.isSelected()) {
            tf1.setEditable(true);
            tf1.requestFocusInWindow();
        } else {
            tf1.setEditable(false);
        }
    }//GEN-LAST:event_cb1ActionPerformed

    private void cb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb2ActionPerformed
        if (cb2.isSelected()) {
            tf2.setEditable(true);
            tf2.requestFocusInWindow();
        } else {
            tf2.setEditable(false);
        }
    }//GEN-LAST:event_cb2ActionPerformed

    private void cb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb3ActionPerformed
        if (cb3.isSelected()) {
            tf3.setEditable(true);
            tf3.requestFocusInWindow();
        } else {
            tf3.setEditable(false);
        }
    }//GEN-LAST:event_cb3ActionPerformed

    private void cb4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb4ActionPerformed
        if (cb4.isSelected()) {
            tf4.setEditable(true);
            tf4.requestFocusInWindow();
        } else {
            tf4.setEditable(false);
        }
    }//GEN-LAST:event_cb4ActionPerformed

    private void cb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb5ActionPerformed
        if (cb5.isSelected()) {
            tf5.setEditable(true);
            tf5.requestFocusInWindow();
        } else {
            tf5.setEditable(false);
        }
    }//GEN-LAST:event_cb5ActionPerformed

    private void applyBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBTNActionPerformed
        PreparedStatement pstGNUpdate = null;
        if (tf1.getText().isEmpty() | tf2.getText().isEmpty() | tf3.getText().isEmpty() | tf4.getText().isEmpty() | tf5.getText().isEmpty() | tf6.getText().isEmpty() | tf7.getText().isEmpty()) {
            if (tf1.isEditable() | tf2.isEditable() | tf3.isEditable() | tf4.isEditable() | tf5.isEditable() | tf6.isEditable() | tf7.isEditable()) {
                JOptionPane.showMessageDialog(rootPane, "<html><center>One of the Required field is empty!</center></html>", mainErrorString, JOptionPane.ERROR_MESSAGE);
            }

        } else {
            int opendata = JOptionPane.showConfirmDialog(rootPane, "<html><center>Are you sure that do you want to update this changes? <br>You cannot undo this action!</center></html>", mainnameString, JOptionPane.YES_NO_OPTION);
            if (opendata == 0) {
                try {
                    String value = lblmain.getText();
                    String query;

                    query = "UPDATE GUINames SET MainAppName=?,MainCompanyName=?,MainTopAppName=?,PopupNormal=?,PopupError=?,TimerSpeed=?,StopwatchSpeed=? WHERE Depending='" + value + "'";

                    pstGNUpdate = conn.prepareStatement(query);
                    pstGNUpdate.setString(1, tf1.getText());
                    pstGNUpdate.setString(2, tf2.getText());
                    pstGNUpdate.setString(3, tf3.getText());
                    pstGNUpdate.setString(4, tf4.getText());
                    pstGNUpdate.setString(5, tf5.getText());
                    pstGNUpdate.setString(6, tf6.getText());
                    pstGNUpdate.setString(7, tf7.getText());

                    pstGNUpdate.executeUpdate();
                    //JOptionPane.showMessageDialog(null,"Updated Successfully.",mainnameString,JOptionPane.INFORMATION_MESSAGE,null);
                    //auditEdit();
                    savechangesBTN.setEnabled(true);
                    checkAllBTN.setEnabled(true);
                    uncheckAllBTN.setEnabled(true);
                    reset2defaultBTN.setEnabled(true);
                    applyBTN.setEnabled(false);
                    restartEditGUI();
                } catch (SQLException e) {

                } catch (IOException ex) {
                    Logger.getLogger(EditGUIOptions.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        pstGNUpdate.close();
                    } catch (SQLException e) {
                        System.out.println("ERR:" + e);
                    }
                }

            } else {
                cb1.setSelected(false);
                cb2.setSelected(false);
                cb3.setSelected(false);
                cb4.setSelected(false);
                cb5.setSelected(false);
                cb6.setSelected(false);
                cb7.setSelected(false);

                cb1.setEnabled(true);
                cb2.setEnabled(true);
                cb3.setEnabled(true);
                cb4.setEnabled(true);
                cb5.setEnabled(true);
                cb6.setEnabled(true);
                cb7.setEnabled(true);

                tf1.setEditable(false);
                tf2.setEditable(false);
                tf3.setEditable(false);
                tf4.setEditable(false);
                tf5.setEditable(false);
                tf6.setEditable(false);
                tf7.setEditable(false);

                tf1.requestFocus(false);
                tf2.requestFocus(false);
                tf3.requestFocus(false);
                tf4.requestFocus(false);
                tf5.requestFocus(false);
                tf6.requestFocus(false);
                tf7.requestFocus(false);

                savechangesBTN.setEnabled(true);
                checkAllBTN.setEnabled(true);
                uncheckAllBTN.setEnabled(true);
                reset2defaultBTN.setEnabled(true);
                applyBTN.setEnabled(false);
            }
        }
    }//GEN-LAST:event_applyBTNActionPerformed

    private void checkAllBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAllBTNActionPerformed
        //selected
        if (cb1.isSelected() | cb2.isSelected() | cb3.isSelected() | cb4.isSelected() | cb5.isSelected() | cb6.isSelected() | cb7.isSelected()) {
            JOptionPane.showMessageDialog(rootPane, "<html><center>All Checkbox is Already Selected.</center></html>", mainErrorString, JOptionPane.ERROR_MESSAGE);
            System.out.println("Already Selected");
            //not selected
        } else if (!(cb1.isSelected() | cb2.isSelected() | cb3.isSelected() | cb4.isSelected() | cb5.isSelected() | cb6.isSelected() | cb7.isSelected())) {

            System.out.println("All CB is Selected");
            cb1.setSelected(true);
            cb2.setSelected(true);
            cb3.setSelected(true);
            cb4.setSelected(true);
            cb5.setSelected(true);
            cb6.setSelected(true);
            cb7.setSelected(true);

            tf1.setEditable(true);
            tf2.setEditable(true);
            tf3.setEditable(true);
            tf4.setEditable(true);
            tf5.setEditable(true);
            tf6.setEditable(true);
            tf7.setEditable(true);
            //checkAllBTN.setEnabled(false);
            //uncheckAllBTN.setEnabled(true);
        }
    }//GEN-LAST:event_checkAllBTNActionPerformed

    private void uncheckAllBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uncheckAllBTNActionPerformed
        //selected
        if (cb1.isSelected() | cb2.isSelected() | cb3.isSelected() | cb4.isSelected() | cb5.isSelected() | cb6.isSelected() | cb7.isSelected()) {
            System.out.println("All CB is Unselected");
            cb1.setSelected(false);
            cb2.setSelected(false);
            cb3.setSelected(false);
            cb4.setSelected(false);
            cb5.setSelected(false);
            cb6.setSelected(false);
            cb7.setSelected(false);

            tf1.setEditable(false);
            tf2.setEditable(false);
            tf3.setEditable(false);
            tf4.setEditable(false);
            tf5.setEditable(false);
            tf6.setEditable(false);
            tf7.setEditable(false);

            tf1.requestFocus(false);
            tf2.requestFocus(false);
            tf3.requestFocus(false);
            tf4.requestFocus(false);
            tf5.requestFocus(false);
            tf6.requestFocus(false);
            tf7.requestFocus(false);

            //checkAllBTN.setEnabled(false);
            //uncheckAllBTN.setEnabled(true);
            //not selected
        } else if (!(cb1.isSelected() | cb2.isSelected() | cb3.isSelected() | cb4.isSelected() | cb5.isSelected() | cb6.isSelected() | cb7.isSelected())) {

            JOptionPane.showMessageDialog(rootPane, "<html><center>All Checkbox is Already Unselected.</center></html>", mainErrorString, JOptionPane.ERROR_MESSAGE);
            System.out.println("Already Unselected");
        }
    }//GEN-LAST:event_uncheckAllBTNActionPerformed

    private void savechangesBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savechangesBTNActionPerformed
        if (tf1.getText().isEmpty() | tf2.getText().isEmpty() | tf3.getText().isEmpty() | tf4.getText().isEmpty() | tf5.getText().isEmpty() | tf6.getText().isEmpty() | tf7.getText().isEmpty()) {
            if (tf1.isEditable() | tf2.isEditable() | tf3.isEditable() | tf4.isEditable() | tf5.isEditable() | tf6.isEditable() | tf7.isEditable()) {
                JOptionPane.showMessageDialog(rootPane, "<html><center>One of the Required field is empty!</center></html>", mainErrorString, JOptionPane.ERROR_MESSAGE);
            }

            if (!(tf1.isEditable() | tf2.isEditable() | tf3.isEditable() | tf4.isEditable() | tf5.isEditable() | tf6.isEditable() | tf7.isEditable())) {

                JOptionPane.showMessageDialog(rootPane, "<html><center>One of the Required field is empty!</center></html>", mainErrorString, JOptionPane.ERROR_MESSAGE);
            }

        } else {
            cb1.setSelected(false);
            cb2.setSelected(false);
            cb3.setSelected(false);
            cb4.setSelected(false);
            cb5.setSelected(false);
            cb6.setSelected(false);
            cb7.setSelected(false);

            cb1.setEnabled(false);
            cb2.setEnabled(false);
            cb3.setEnabled(false);
            cb4.setEnabled(false);
            cb5.setEnabled(false);
            cb6.setEnabled(false);
            cb7.setEnabled(false);

            tf1.setEditable(false);
            tf2.setEditable(false);
            tf3.setEditable(false);
            tf4.setEditable(false);
            tf5.setEditable(false);
            tf6.setEditable(false);
            tf7.setEditable(false);

            tf1.requestFocus(false);
            tf2.requestFocus(false);
            tf3.requestFocus(false);
            tf4.requestFocus(false);
            tf5.requestFocus(false);
            tf6.requestFocus(false);
            tf7.requestFocus(false);
            savechangesBTN.setEnabled(false);
            checkAllBTN.setEnabled(false);
            uncheckAllBTN.setEnabled(false);
            reset2defaultBTN.setEnabled(false);
            applyBTN.setEnabled(true);
            applyBTN.requestFocusInWindow();
        }
    }//GEN-LAST:event_savechangesBTNActionPerformed

    private void reset2defaultBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset2defaultBTNActionPerformed
        try {
            insertGUINaming();
        } catch (SQLException ex) {
            Logger.getLogger(EditGUIOptions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_reset2defaultBTNActionPerformed

    private void tf6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf6KeyTyped
        char c = evt.getKeyChar();
        try {
            if (!((c >= '0') && (c <= '9')
                    || (c == KeyEvent.VK_BACK_SPACE)
                    || (c == KeyEvent.VK_DELETE)
                    || (c == KeyEvent.VK_ENTER)
                    || (c == KeyEvent.VK_TAB))) {
                evt.consume();

                // if on the text field the numbers are bigger than 2000, consumes the last number typed    
            } else if (Integer.parseInt(tf6.getText() + c) > 2000) {
                evt.consume();
                tf6.requestFocusInWindow();
            }
        } catch (NumberFormatException e) {
        }

        //no space character
        if (c == KeyEvent.VK_SPACE) {
            evt.consume();
        }

        //limit the number length
        String l = tf6.getText();
        if (!(l.length() < 4)) {
            evt.consume();
        }
    }//GEN-LAST:event_tf6KeyTyped

    private void cb6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb6ActionPerformed
        if (cb6.isSelected()) {
            tf6.setEditable(true);
            tf6.requestFocusInWindow();
        } else {
            tf6.setEditable(false);
        }
    }//GEN-LAST:event_cb6ActionPerformed

    private void tf7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf7KeyTyped
        char c = evt.getKeyChar();
        try {
            if (!((c >= '0') && (c <= '9')
                    || (c == KeyEvent.VK_BACK_SPACE)
                    || (c == KeyEvent.VK_DELETE)
                    || (c == KeyEvent.VK_ENTER)
                    || (c == KeyEvent.VK_TAB))) {
                evt.consume();

                // if on the text field the numbers are bigger than 20, consumes the last number typed    
            } else if (Integer.parseInt(tf7.getText() + c) > 20) {
                evt.consume();
                tf7.requestFocusInWindow();
            }
        } catch (NumberFormatException e) {
        }

        //no space character
        if (c == KeyEvent.VK_SPACE) {
            evt.consume();
        }

        //limit the number length
        String l = tf7.getText();
        if (!(l.length() < 2)) {
            evt.consume();
        }
    }//GEN-LAST:event_tf7KeyTyped

    private void cb7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb7ActionPerformed
        if (cb7.isSelected()) {
            tf7.setEditable(true);
            tf7.requestFocusInWindow();
        } else {
            tf7.setEditable(false);
        }
    }//GEN-LAST:event_cb7ActionPerformed

    private void tf24KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf24KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tf24KeyTyped

    private void cb24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb24ActionPerformed

    private void exitButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseMoved

    }//GEN-LAST:event_exitButtonMouseMoved

    private void exitButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseEntered
        setColor1(exitButton);
    }//GEN-LAST:event_exitButtonMouseEntered

    private void exitButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseExited
        resetColor1(exitButton);
    }//GEN-LAST:event_exitButtonMouseExited

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // Exit/Close Button code:
        setColor1(exitButton);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void pnlTopMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_pnlTopMouseDragged

    private void pnlTopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_pnlTopMousePressed

    private void guiTitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guiTitleMousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_guiTitleMousePressed

    private void guiTitleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guiTitleMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_guiTitleMouseDragged

    // set Color to pnlTop (EXIT)
    void setColor1(JButton btn1) {
        btn1.setBackground(new Color(255,51,51)); 
    }

    void resetColor1(JButton btn1) {
        btn1.setBackground(new Color(0,102,204));
    }
    
    // set Color to pnlTop (MINIMIZE, SETTINGS)
    void setColor2(JButton btn2) {
        btn2.setBackground(new Color(155,182,211));
    }

    void resetColor2(JButton btn2) {
        btn2.setBackground(new Color(0,102,204));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize modern LaF" + ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new EditGUIOptions().setVisible(true);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(EditGUIOptions.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyBTN;
    private javax.swing.JCheckBox cb1;
    private javax.swing.JCheckBox cb2;
    private javax.swing.JCheckBox cb3;
    private javax.swing.JCheckBox cb4;
    private javax.swing.JCheckBox cb5;
    private javax.swing.JCheckBox cb6;
    private javax.swing.JCheckBox cb7;
    private javax.swing.JButton checkAllBTN;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel guiTitle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl7;
    private javax.swing.JLabel lblmain;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JButton reset2defaultBTN;
    private javax.swing.JButton savechangesBTN;
    private javax.swing.JTextField tf1;
    private javax.swing.JTextField tf2;
    private javax.swing.JTextField tf3;
    private javax.swing.JTextField tf4;
    private javax.swing.JTextField tf5;
    private javax.swing.JTextField tf6;
    private javax.swing.JTextField tf7;
    private javax.swing.JLabel txt_emp;
    private javax.swing.JButton uncheckAllBTN;
    private javax.swing.JPanel viewpanel;
    // End of variables declaration//GEN-END:variables
}