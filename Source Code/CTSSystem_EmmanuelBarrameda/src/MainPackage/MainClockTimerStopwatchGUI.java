// ©  M A D E		B Y		E M M A N		B A R R A M E D A  © //
//CTS System
package MainPackage;

import SystemDB.DBconnection;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.formdev.flatlaf.FlatLightLaf;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

/**
 *
 * @author Emmanuel Peñaflorida Barrameda
 */
public final class MainClockTimerStopwatchGUI extends javax.swing.JFrame {

    //for Database Connection Variable
    Connection conn;

    //for app name
    String mainAppNameFromDB; ///this string will get DATA from db

    //for app name
    String mainTopAppNameFromDB; ///this string will get DATA from db

    //for company name
    String companyNameFromDB; ///this string will get DATA from db

    //Normal Popups Title
    String mainnameString; ///this string will get DATA from db

    //Error Popups Title
    String mainErrorString; ///this string will get DATA from db

    //for stopwatch Time duration
    long stopwatchTimeFromDB; ///this string will get DATA from db

    //for timer Time duration
    int timerTimeFromDB; ///this string will get DATA from db

    Timer timer;
    String recordString = "Recorded Laps:";
    int count = 0;
    int min, min1;
    int sec = 1;
    int sec1 = 1;
    boolean flag = true;
    boolean ifStop = false;

    private int hourSW;
    private int minuteSW;
    private int secondSW;
    private int csecondSW;
    private boolean isStartSW;
    int countSW = 0;
    String strSW = "Recorded Laps:";

    //for restart
    public static final String SUN_JAVA_COMMAND = "sun.java.command";
    Runnable runBeforeRestart;

    //for finding the saving and opening file location from the current running jar/exe file
    File current_file = new File(System.getProperty("user.dir"));
    File current_dir = current_file.getAbsoluteFile();
    String currentRunningPath = current_dir.toString();

    /**
     * Creates new form MainClockTimerStopwatchGUI
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public MainClockTimerStopwatchGUI() throws SQLException, IOException {
        initComponents();

        //get connection from database class
        DBconnection c = new DBconnection();
        conn = c.getconnection();

        setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("/Images/MAINICON_128px.png")).getImage());
        MainClockTimerStopwatchGUI.this.getRootPane().setBorder(new MatteBorder(0, 1, 1, 1, (new Color(0, 102, 204))));
        GUINaming_DATA();
        currentdate();
        showtime();
        daydateLABEL.setText(now("EEEEEE") + ", " + (now("MMMMM dd")));//day sun2sat and date

        setTime();

        btnResetTimer.setVisible(false);
        btnRestartTimer.setVisible(false);
        recordfieldPanelSW.setVisible(false);
        //btnRecord.setVisible(false);
        //recordBoard.setVisible(false);
        //btnResetRecordTime.setVisible(false);
        //recordboardScroll.setVisible(false);
        //lblMin.setFont(new Font("Digital-7 Mono", Font.BOLD, 46));

        //default white color: [249,250,253]
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
            this.setTitle(mainAppNameFromDB);

            //set the GUI Top Title
            mainTopAppNameFromDB = rsGNaming.getString("MainTopAppName");
            guiTitle.setText(mainTopAppNameFromDB);

            //company name
            companyNameFromDB = rsGNaming.getString("MainCompanyName");
            //companynameLBL.setText(companyNameFromDB);

            //set the Default Normal Popups Title Message
            mainnameString = rsGNaming.getString("PopupNormal");

            //set the Default Error Popups Title Message
            mainErrorString = rsGNaming.getString("PopupError");

            //String timerString = get Timer Speed from Database
            String timerString = rsGNaming.getString("TimerSpeed");
            timerTimeFromDB = Integer.parseInt(timerString); //get data from String to Int
            System.out.println("Timer Speed: " + timerTimeFromDB);

            //String stopwatchString = get Stopwatch Speed from Database
            String stopwatchString = rsGNaming.getString("StopwatchSpeed");
            stopwatchTimeFromDB = Long.parseLong(stopwatchString); //get data from String to Long
            System.out.println("Stopwatch Speed: " + stopwatchTimeFromDB);

        } catch (SQLException e) {
        } finally {
            try {
                stGNaming.close();
                rsGNaming.close();
            } catch (SQLException e) {
                System.err.println("ERR:" + e);
            }
        }
    }

    //dateformat
    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public void currentdate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        daydateLABEL.setText(sdf.format(d));
    }

    void showtime() {
        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            //Date d2 = new Date();
            String timeClockSpace = " ";
            //SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
            //SimpleDateFormat sdf2 = new SimpleDateFormat("a");
            currenttimeLABEL.setText(sdf.format(d));
            //ampm.setText(sdf2.format(d2));
        }).start();
    }

    public void setTime() {
        //minute
        cbMinTimer.updateUI();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                cbMinTimer.addItem("0" + i);
            } else {
                cbMinTimer.addItem("" + i);
            }
        }

        //sec
        cbSecTimer.updateUI();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                cbSecTimer.addItem("0" + i);
            } else {
                cbSecTimer.addItem("" + i);
            }
        }
    }

    //not working
    public void Last1MinuteVoid() throws UnsupportedAudioFileException {
        /*
            InputStream inputStream = getClass().getResourceAsStream("/soundeffects/sound.wav");
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
         */
        try {
            //File soundFile = new File(""+getClass().getResource("/soundeffects/sound.wav"));
            URL inputStream = getClass().getResource("/soundeffects/bird flying.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(inputStream);
            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
            System.out.println(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("ERR: " + e);
        }
    }

    public void record() {
        count++;
        recordString += "\n" + count + "] " + lblMin.getText() + ":" + lblSec.getText() + " ";
        //recordBoard.setText(recordString);
    }

    public void listSW() {
        countSW++;
        strSW += "\n" + countSW + ") " + lblHourSW.getText() + ":" + lblMinSW.getText() + ":" + lblSecSW.getText() + "." + lblCSecSW.getText() + "";
        recordfieldSW.setText(strSW);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlClock = new javax.swing.JPanel();
        daydateLABEL = new javax.swing.JLabel();
        currenttimeLABEL = new javax.swing.JLabel();
        pnlTimer = new javax.swing.JPanel();
        pagitan = new javax.swing.JLabel();
        lblSec = new javax.swing.JLabel();
        lblMin = new javax.swing.JLabel();
        cbMinTimer = new javax.swing.JComboBox<>();
        cbSecTimer = new javax.swing.JComboBox<>();
        secondsTimerLABEL = new javax.swing.JLabel();
        minutesTimerLABEL = new javax.swing.JLabel();
        btnResetTimer = new javax.swing.JButton();
        btnStartTimer = new javax.swing.JButton();
        btnRestartTimer = new javax.swing.JButton();
        btnStopTimer = new javax.swing.JButton();
        pnlStopwatch = new javax.swing.JPanel();
        lblHourSW = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblMinSW = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblSecSW = new javax.swing.JLabel();
        lblCSecSW = new javax.swing.JLabel();
        btnPauseSW = new javax.swing.JButton();
        btnRecordSW = new javax.swing.JButton();
        btnResetSW = new javax.swing.JButton();
        btnStartSW = new javax.swing.JButton();
        recordfieldPanelSW = new javax.swing.JPanel();
        hideTimeRecordSW = new javax.swing.JButton();
        resetTimeRecordSW = new javax.swing.JButton();
        recordfieldScrollSW = new javax.swing.JScrollPane();
        recordfieldSW = new javax.swing.JTextArea();
        pnlTop = new javax.swing.JPanel();
        guiTitle = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        minimizeButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CTS SYSTEM");
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(249, 250, 253));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(249, 250, 253));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        pnlClock.setBackground(new java.awt.Color(249, 250, 253));
        pnlClock.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        daydateLABEL.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        daydateLABEL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        daydateLABEL.setText("Saturday, January 30");
        pnlClock.add(daydateLABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 980, -1));

        currenttimeLABEL.setFont(new java.awt.Font("Agency FB", 0, 200)); // NOI18N
        currenttimeLABEL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currenttimeLABEL.setText("1:00:00 PM");
        pnlClock.add(currenttimeLABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 980, -1));

        jTabbedPane1.addTab("Clock", new javax.swing.ImageIcon(getClass().getResource("/Images/clock_64px.png")), pnlClock); // NOI18N

        pnlTimer.setBackground(new java.awt.Color(249, 250, 253));
        pnlTimer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pagitan.setFont(new java.awt.Font("Agency FB", 0, 250)); // NOI18N
        pagitan.setText(":");
        pnlTimer.add(pagitan, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, -1, 220));

        lblSec.setFont(new java.awt.Font("Agency FB", 0, 250)); // NOI18N
        lblSec.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSec.setText("00");
        lblSec.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlTimer.add(lblSec, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, 300, 220));

        lblMin.setFont(new java.awt.Font("Agency FB", 0, 250)); // NOI18N
        lblMin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMin.setText("00");
        lblMin.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnlTimer.add(lblMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 280, 220));

        cbMinTimer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbMinTimer.setToolTipText("");
        cbMinTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbMinTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMinTimerActionPerformed(evt);
            }
        });
        pnlTimer.add(cbMinTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 170, -1));

        cbSecTimer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbSecTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbSecTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSecTimerActionPerformed(evt);
            }
        });
        pnlTimer.add(cbSecTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 260, 170, -1));

        secondsTimerLABEL.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        secondsTimerLABEL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        secondsTimerLABEL.setText("SECONDS");
        pnlTimer.add(secondsTimerLABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 240, 170, -1));

        minutesTimerLABEL.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        minutesTimerLABEL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minutesTimerLABEL.setText("MINUTES");
        pnlTimer.add(minutesTimerLABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, 170, -1));

        btnResetTimer.setBackground(new java.awt.Color(249, 250, 253));
        btnResetTimer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnResetTimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/time_reset_44px.png"))); // NOI18N
        btnResetTimer.setText("Reset");
        btnResetTimer.setBorderPainted(false);
        btnResetTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetTimer.setDefaultCapable(false);
        btnResetTimer.setFocusPainted(false);
        btnResetTimer.setFocusable(false);
        btnResetTimer.setRequestFocusEnabled(false);
        btnResetTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetTimerActionPerformed(evt);
            }
        });
        pnlTimer.add(btnResetTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 370, 190, 50));

        btnStartTimer.setBackground(new java.awt.Color(249, 250, 253));
        btnStartTimer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnStartTimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/time_play_64px.png"))); // NOI18N
        btnStartTimer.setText("Start");
        btnStartTimer.setBorderPainted(false);
        btnStartTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStartTimer.setDefaultCapable(false);
        btnStartTimer.setFocusPainted(false);
        btnStartTimer.setFocusable(false);
        btnStartTimer.setIconTextGap(0);
        btnStartTimer.setRequestFocusEnabled(false);
        btnStartTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartTimerActionPerformed(evt);
            }
        });
        pnlTimer.add(btnStartTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 310, 240, 50));

        btnRestartTimer.setBackground(new java.awt.Color(249, 250, 253));
        btnRestartTimer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnRestartTimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/time_restart_44px.png"))); // NOI18N
        btnRestartTimer.setText("Restart");
        btnRestartTimer.setBorderPainted(false);
        btnRestartTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRestartTimer.setDefaultCapable(false);
        btnRestartTimer.setFocusPainted(false);
        btnRestartTimer.setFocusable(false);
        btnRestartTimer.setRequestFocusEnabled(false);
        btnRestartTimer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnRestartTimerMousePressed(evt);
            }
        });
        btnRestartTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestartTimerActionPerformed(evt);
            }
        });
        pnlTimer.add(btnRestartTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 370, 240, 50));

        btnStopTimer.setBackground(new java.awt.Color(249, 250, 253));
        btnStopTimer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnStopTimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/time_stop_button_44px.png"))); // NOI18N
        btnStopTimer.setText("Stop");
        btnStopTimer.setBorderPainted(false);
        btnStopTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStopTimer.setDefaultCapable(false);
        btnStopTimer.setEnabled(false);
        btnStopTimer.setFocusPainted(false);
        btnStopTimer.setFocusable(false);
        btnStopTimer.setIconTextGap(0);
        btnStopTimer.setRequestFocusEnabled(false);
        btnStopTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopTimerActionPerformed(evt);
            }
        });
        pnlTimer.add(btnStopTimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 310, 190, 50));

        jTabbedPane1.addTab("Timer", new javax.swing.ImageIcon(getClass().getResource("/Images/timer_icon_64px.png")), pnlTimer); // NOI18N

        pnlStopwatch.setBackground(new java.awt.Color(249, 250, 253));
        pnlStopwatch.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnlStopwatch.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        pnlStopwatch.setOpaque(false);
        pnlStopwatch.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHourSW.setFont(new java.awt.Font("Agency FB", 0, 200)); // NOI18N
        lblHourSW.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHourSW.setText("0");
        lblHourSW.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnlStopwatch.add(lblHourSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 180));

        jLabel3.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Hour");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlStopwatch.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 170, 80, -1));

        jLabel4.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Min");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlStopwatch.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 170, 60, -1));

        lblMinSW.setFont(new java.awt.Font("Agency FB", 0, 200)); // NOI18N
        lblMinSW.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinSW.setText("00");
        lblMinSW.setFocusable(false);
        lblMinSW.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnlStopwatch.add(lblMinSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 190, 180));

        jLabel7.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Sec");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlStopwatch.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(682, 170, 60, -1));

        lblSecSW.setFont(new java.awt.Font("Agency FB", 0, 200)); // NOI18N
        lblSecSW.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSecSW.setText("00");
        lblSecSW.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnlStopwatch.add(lblSecSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 220, 180));

        lblCSecSW.setFont(new java.awt.Font("Agency FB", 0, 150)); // NOI18N
        lblCSecSW.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCSecSW.setText("00");
        lblCSecSW.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblCSecSW.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnlStopwatch.add(lblCSecSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 60, 230, 160));

        btnPauseSW.setBackground(new java.awt.Color(249, 250, 253));
        btnPauseSW.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnPauseSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pause_30px_1.png"))); // NOI18N
        btnPauseSW.setText("Stop");
        btnPauseSW.setBorderPainted(false);
        btnPauseSW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPauseSW.setDefaultCapable(false);
        btnPauseSW.setEnabled(false);
        btnPauseSW.setFocusPainted(false);
        btnPauseSW.setFocusable(false);
        btnPauseSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseSWActionPerformed(evt);
            }
        });
        pnlStopwatch.add(btnPauseSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 250, -1, -1));

        btnRecordSW.setBackground(new java.awt.Color(249, 250, 253));
        btnRecordSW.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnRecordSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pencil_30px.png"))); // NOI18N
        btnRecordSW.setText("Record");
        btnRecordSW.setBorderPainted(false);
        btnRecordSW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecordSW.setDefaultCapable(false);
        btnRecordSW.setEnabled(false);
        btnRecordSW.setFocusPainted(false);
        btnRecordSW.setFocusable(false);
        btnRecordSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecordSWActionPerformed(evt);
            }
        });
        pnlStopwatch.add(btnRecordSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, -1, -1));

        btnResetSW.setBackground(new java.awt.Color(249, 250, 253));
        btnResetSW.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnResetSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Synchronize_30px.png"))); // NOI18N
        btnResetSW.setText("Reset");
        btnResetSW.setBorderPainted(false);
        btnResetSW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetSW.setDefaultCapable(false);
        btnResetSW.setEnabled(false);
        btnResetSW.setFocusPainted(false);
        btnResetSW.setFocusTraversalPolicyProvider(true);
        btnResetSW.setFocusable(false);
        btnResetSW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetSWMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnResetSWMousePressed(evt);
            }
        });
        btnResetSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSWActionPerformed(evt);
            }
        });
        pnlStopwatch.add(btnResetSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 250, -1, -1));

        btnStartSW.setBackground(new java.awt.Color(249, 250, 253));
        btnStartSW.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnStartSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Play_30px_1.png"))); // NOI18N
        btnStartSW.setText("Start");
        btnStartSW.setBorderPainted(false);
        btnStartSW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStartSW.setDefaultCapable(false);
        btnStartSW.setFocusPainted(false);
        btnStartSW.setFocusable(false);
        btnStartSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartSWActionPerformed(evt);
            }
        });
        pnlStopwatch.add(btnStartSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 250, -1, -1));

        recordfieldPanelSW.setBackground(new java.awt.Color(249, 250, 253));
        recordfieldPanelSW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        hideTimeRecordSW.setBackground(new java.awt.Color(249, 250, 253));
        hideTimeRecordSW.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        hideTimeRecordSW.setText("Hide Record");
        hideTimeRecordSW.setBorderPainted(false);
        hideTimeRecordSW.setContentAreaFilled(false);
        hideTimeRecordSW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        hideTimeRecordSW.setDefaultCapable(false);
        hideTimeRecordSW.setFocusPainted(false);
        hideTimeRecordSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideTimeRecordSWActionPerformed(evt);
            }
        });
        recordfieldPanelSW.add(hideTimeRecordSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, 100, 10));

        resetTimeRecordSW.setBackground(new java.awt.Color(249, 250, 253));
        resetTimeRecordSW.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        resetTimeRecordSW.setText("Reset Record");
        resetTimeRecordSW.setBorderPainted(false);
        resetTimeRecordSW.setContentAreaFilled(false);
        resetTimeRecordSW.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resetTimeRecordSW.setDefaultCapable(false);
        resetTimeRecordSW.setFocusPainted(false);
        resetTimeRecordSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetTimeRecordSWActionPerformed(evt);
            }
        });
        recordfieldPanelSW.add(resetTimeRecordSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 130, -1, 10));

        recordfieldScrollSW.setBackground(new java.awt.Color(249, 250, 253));

        recordfieldSW.setBackground(new java.awt.Color(249, 250, 253));
        recordfieldSW.setColumns(20);
        recordfieldSW.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        recordfieldSW.setRows(5);
        recordfieldSW.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recorded Laps", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 102, 204))); // NOI18N
        recordfieldSW.setSelectedTextColor(new java.awt.Color(249, 250, 253));
        recordfieldScrollSW.setViewportView(recordfieldSW);

        recordfieldPanelSW.add(recordfieldScrollSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 130));

        pnlStopwatch.add(recordfieldPanelSW, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 450, 140));

        jTabbedPane1.addTab("Stopwatch", new javax.swing.ImageIcon(getClass().getResource("/Images/stopwatch_64px.png")), pnlStopwatch); // NOI18N

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 980, 540));

        pnlTop.setBackground(new java.awt.Color(0, 102, 204));
        pnlTop.setPreferredSize(new java.awt.Dimension(116, 30));
        pnlTop.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlTopMouseDragged(evt);
            }
        });
        pnlTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlTopMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlTopMousePressed(evt);
            }
        });
        pnlTop.setLayout(null);

        guiTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        guiTitle.setForeground(new java.awt.Color(255, 255, 255));
        guiTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/MAINICON_18px.png"))); // NOI18N
        guiTitle.setText(" CTS SYSTEM");
        guiTitle.setIconTextGap(2);
        guiTitle.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                guiTitleMouseDragged(evt);
            }
        });
        guiTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                guiTitleMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                guiTitleMousePressed(evt);
            }
        });
        pnlTop.add(guiTitle);
        guiTitle.setBounds(5, 0, 200, 30);

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
        exitButton.setBounds(950, 0, 20, 30);

        minimizeButton.setBackground(new java.awt.Color(0, 102, 204));
        minimizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_minus_18px_1.png"))); // NOI18N
        minimizeButton.setBorder(null);
        minimizeButton.setBorderPainted(false);
        minimizeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        minimizeButton.setDefaultCapable(false);
        minimizeButton.setFocusPainted(false);
        minimizeButton.setFocusable(false);
        minimizeButton.setIconTextGap(0);
        minimizeButton.setRequestFocusEnabled(false);
        minimizeButton.setVerifyInputWhenFocusTarget(false);
        minimizeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizeButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizeButtonMouseExited(evt);
            }
        });
        minimizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimizeButtonActionPerformed(evt);
            }
        });
        pnlTop.add(minimizeButton);
        minimizeButton.setBounds(920, 0, 18, 30);

        aboutButton.setBackground(new java.awt.Color(0, 102, 204));
        aboutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/settings_18px.png"))); // NOI18N
        aboutButton.setBorder(null);
        aboutButton.setBorderPainted(false);
        aboutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        aboutButton.setDefaultCapable(false);
        aboutButton.setFocusPainted(false);
        aboutButton.setFocusable(false);
        aboutButton.setIconTextGap(0);
        aboutButton.setInheritsPopupMenu(true);
        aboutButton.setRequestFocusEnabled(false);
        aboutButton.setVerifyInputWhenFocusTarget(false);
        aboutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                aboutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                aboutButtonMouseExited(evt);
            }
        });
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });
        pnlTop.add(aboutButton);
        aboutButton.setBounds(890, 0, 18, 30);

        jPanel1.add(pnlTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(980, 569));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    int xy, xx; //<-- for pnlTop

    private void exitButtonMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseMoved

    }//GEN-LAST:event_exitButtonMouseMoved

    private void exitButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseEntered
        setColor1(exitButton);
        resetColor1(minimizeButton);
        resetColor1(aboutButton);
    }//GEN-LAST:event_exitButtonMouseEntered

    private void exitButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseExited
        resetColor1(exitButton);
        resetColor1(minimizeButton);
        resetColor1(aboutButton);
    }//GEN-LAST:event_exitButtonMouseExited

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // Exit/Close Button code:
        setColor1(exitButton);
        resetColor1(minimizeButton);
        resetColor1(aboutButton);

        int p = JOptionPane.showConfirmDialog(rootPane, "Do You Want To Exit?", mainnameString, JOptionPane.YES_NO_OPTION);

        //yes
        if (p == 0) {
            System.exit(0);

        //no
        } else {
            resetColor1(exitButton);
            resetColor1(minimizeButton);
            resetColor1(aboutButton);
        }
    }//GEN-LAST:event_exitButtonActionPerformed

    private void minimizeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeButtonMouseEntered
        resetColor2(exitButton);
        resetColor2(aboutButton);
        setColor2(minimizeButton);
    }//GEN-LAST:event_minimizeButtonMouseEntered

    private void minimizeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeButtonMouseExited
        resetColor2(exitButton);
        resetColor2(minimizeButton);
        resetColor2(aboutButton);
    }//GEN-LAST:event_minimizeButtonMouseExited

    private void minimizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeButtonActionPerformed
        // Minimize Button code:
        resetColor2(exitButton);
        resetColor2(aboutButton);
        setColor2(minimizeButton);

        MainClockTimerStopwatchGUI.this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizeButtonActionPerformed

    private void pnlTopMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_pnlTopMouseDragged

    private void pnlTopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_pnlTopMousePressed

    private void cbMinTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMinTimerActionPerformed
        lblMin.setText("" + cbMinTimer.getSelectedItem());
        min = Integer.parseInt(lblMin.getText());

        if (cbMinTimer.getSelectedItem().equals("00") | cbMinTimer.getSelectedItem().equals("01")) {
            minutesTimerLABEL.setText("MINUTE");
        } else {
            minutesTimerLABEL.setText("MINUTES");
        }

    }//GEN-LAST:event_cbMinTimerActionPerformed

    private void cbSecTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSecTimerActionPerformed
        lblSec.setText("" + cbSecTimer.getSelectedItem());
        sec = Integer.parseInt(lblSec.getText());

        if (cbSecTimer.getSelectedItem().equals("00") | cbSecTimer.getSelectedItem().equals("01")) {
            secondsTimerLABEL.setText("SECOND");
        } else {
            secondsTimerLABEL.setText("SECONDS");
        }
    }//GEN-LAST:event_cbSecTimerActionPerformed

    private void btnStartTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartTimerActionPerformed

        //if not selected, the timer will not start
        if (lblMin.getText().equals("00") && lblSec.getText().equals("00")) {
            JOptionPane.showMessageDialog(rootPane, "Please select.", mainErrorString, JOptionPane.ERROR_MESSAGE);

        } else {
            //START TIMER CODE
            btnStartTimer.setEnabled(false);
            btnStartTimer.setText("Continue");
            btnStopTimer.setEnabled(true);
            btnStopTimer.setText("Pause");

            cbMinTimer.setEnabled(false);
            cbSecTimer.setEnabled(false);

            btnResetTimer.setVisible(true);
            btnRestartTimer.setVisible(true);
            btnRestartTimer.setEnabled(true);

            //btnRecord.setVisible(true);
            //btnRecord.setEnabled(true);
            //recordBoard.setVisible(true);
            //recordboardScroll.setVisible(true);
            //btnResetRecordTime.setVisible(true);
            //btnResetRecordTime.setEnabled(false);
            //time speed, normal = 1000
            timer = new Timer(timerTimeFromDB, (new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    lblMin.setForeground(Color.BLACK);
                    lblSec.setForeground(Color.BLACK);

                    if (ifStop) {
                        min = min1;
                        sec = sec1;
                        ifStop = false;
                    }

                    if (sec == 0) {
                        sec = 60;
                        min--;
                    }

                    //59 seconds remaining
                    if (min == 0) {
                        lblMin.setForeground(Color.RED);
                        lblSec.setForeground(Color.RED);
                        flag = false;
                    }

                    //last 1 minute warning
                    //if (min == 1 && sec == 01) {
                    if (min == 0 && sec == 59) {
                        System.out.println("Last 1 Minute");
                        minutesTimerLABEL.setText("MINUTE");
                        flag = false;
                    }

                    //last 1 minute and 59 secs warning
                    if (min == 1 && sec == 59) {
                        System.out.println("Last 1 Minute and 59 sec - warn");
                        minutesTimerLABEL.setText("MINUTE");
                        flag = false;
                    }

                    //times up
                    if (min < 0) {
                        //JOptionPane.showMessageDialog(rootPane, "Times Up", "Stopped!", 0);
                        System.out.println("Times Up!");
                        minutesTimerLABEL.setText("MINUTES");
                        JOptionPane.showMessageDialog(rootPane, "Times Up!", mainnameString, JOptionPane.INFORMATION_MESSAGE);
                        timer.stop();
                        setTime();
                        min = 0;
                        sec = 1;
                        cbSecTimer.setSelectedItem("00");
                        cbMinTimer.setSelectedItem("00");

                        btnStartTimer.setEnabled(true);
                        btnStartTimer.setText("Start");
                        btnStopTimer.setEnabled(false);
                        btnStopTimer.setText("Stop");

                        btnResetTimer.setVisible(false);
                        btnRestartTimer.setVisible(false);

                        cbMinTimer.setEnabled(true);
                        cbSecTimer.setEnabled(true);

                        lblMin.setForeground(Color.BLACK);
                        lblSec.setForeground(Color.BLACK);

                        //btnRecord.setVisible(true);
                        //btnRecord.setEnabled(false);
                        //recordBoard.setVisible(true);
                        //recordboardScroll.setVisible(true);
                        //btnResetRecordTime.setVisible(true);
                        //btnResetRecordTime.setEnabled(true);
                        //timer runner is here @ else
                    } else {
                        sec--;
                        //System.out.println("Sounds here");                      

                        //if seconds is lessthan 10, it will add 0 on the seconds, e.g: 07
                        if (sec < 10) {
                            lblSec.setText("0" + sec);
                            flag = false;
                        }

                        //lessthan 10 minute
                        if (min < 10) {
                            lblMin.setText("0" + min);
                            lblMin.setToolTipText("0" + min);

                            //lessthan 10 seconds (means 9 seconds)
                            if (sec < 10) {
                                lblSec.setText("0" + sec);
                                lblSec.setToolTipText("0" + sec);
                                secondsTimerLABEL.setText("SECONDS");

                                //greater than 10 secs
                            } else {
                                lblSec.setText("" + sec);
                                lblSec.setToolTipText("" + sec);
                                secondsTimerLABEL.setText("SECONDS");
                            }

                            //lessthan 2 (means 1)
                            if (sec < 2) {
                                secondsTimerLABEL.setText("SECOND");

                                //greater than 1
                            } else {
                                secondsTimerLABEL.setText("SECONDS");
                            }

                            flag = false;

                            //greater than 10 minute
                        } else {
                            lblMin.setText("" + min);
                            lblMin.setToolTipText("" + min);

                            //lessthan 10 seconds (means 9 seconds)
                            if (sec < 10) {
                                lblSec.setText("0" + sec);
                                lblSec.setToolTipText("0" + sec);
                                secondsTimerLABEL.setText("SECONDS");

                                //greater than 10 secs
                            } else {
                                lblSec.setText("" + sec);
                                lblSec.setToolTipText("" + sec);
                                secondsTimerLABEL.setText("SECONDS");
                            }

                            //lessthan 2 (means 1)
                            if (sec < 2) {
                                secondsTimerLABEL.setText("SECOND");

                                //greater than 1
                            } else {
                                secondsTimerLABEL.setText("SECONDS");
                            }

                            flag = false;
                        }

                        if (flag) {
                            lblMin.setText("" + min);
                            lblSec.setText("" + sec);
                        }
                    }
                }
            }));
            timer.start(); //<-- start the timer runner
        }
    }//GEN-LAST:event_btnStartTimerActionPerformed

    private void btnStopTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopTimerActionPerformed
        min1 = min;
        sec1 = sec;
        ifStop = true;
        timer.stop();

        btnStartTimer.setEnabled(true);
        btnStartTimer.setText("Continue");

        btnStopTimer.setEnabled(false);
        btnStopTimer.setText("Pause");

        btnRestartTimer.setEnabled(false);

        //btnRecord.setVisible(true);
        //recordBoard.setVisible(true);
        //recordboardScroll.setVisible(true);
        //btnResetRecordTime.setVisible(true);
        //btnResetRecordTime.setEnabled(false);
    }//GEN-LAST:event_btnStopTimerActionPerformed

    private void btnRestartTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestartTimerActionPerformed
        timer.stop();
        timer.stop();
        timer.stop();
        setTime();
        min = Integer.parseInt((String) cbMinTimer.getSelectedItem());
        sec = 1 + Integer.parseInt((String) cbSecTimer.getSelectedItem());
        //cbSec.setSelectedItem("00");
        //cbMin.setSelectedItem("00");

        btnStartTimer.setEnabled(true);
        btnStartTimer.setText("Start");
        btnStopTimer.setEnabled(false);
        btnStopTimer.setText("Stop");

        btnResetTimer.setVisible(false);
        btnRestartTimer.setVisible(false);

        //cbMin.setEnabled(true);
        //cbSec.setEnabled(true);
        //btnRecord.setVisible(true);
        //recordBoard.setVisible(true);
        //recordboardScroll.setVisible(true);
        //btnResetRecordTime.setVisible(true);
        //btnResetRecordTime.setEnabled(false);
        btnStartTimer.doClick();

        //set txt if minute or minutes
        //lessthan 2 minutes (means 1)
        if (min < 2) {
            minutesTimerLABEL.setText("MINUTE");

            //greater than
        } else {
            minutesTimerLABEL.setText("MINUTES");
        }

        //set txt if second or seconds
        //lessthan 2 (means 1)
        if (sec < 2) {
            secondsTimerLABEL.setText("SECOND");

            //greater than
        } else {
            secondsTimerLABEL.setText("SECONDS");
        }

    }//GEN-LAST:event_btnRestartTimerActionPerformed

    private void btnResetTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTimerActionPerformed
        timer.stop();
        timer.stop();
        setTime();
        min = 0;
        sec = 1;
        cbSecTimer.setSelectedItem("00");
        cbMinTimer.setSelectedItem("00");

        btnStartTimer.setEnabled(true);
        btnStartTimer.setText("Start");
        btnStopTimer.setEnabled(false);
        btnStopTimer.setText("Stop");

        btnResetTimer.setVisible(false);
        btnRestartTimer.setVisible(false);

        cbMinTimer.setEnabled(true);
        cbSecTimer.setEnabled(true);

        lblMin.setForeground(Color.BLACK);
        lblSec.setForeground(Color.BLACK);

        //btnRecord.setVisible(true);
        //btnRecord.setEnabled(false);
        //recordBoard.setVisible(true);
        //recordboardScroll.setVisible(true);
        //btnResetRecordTime.setVisible(true);
        //btnResetRecordTime.setEnabled(true);
    }//GEN-LAST:event_btnResetTimerActionPerformed

    private void btnRecordSWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecordSWActionPerformed
        //p5.setVisible(true);
        //btnRecordSW.setVisible(false);
        recordfieldPanelSW.setVisible(true);
        listSW();
    }//GEN-LAST:event_btnRecordSWActionPerformed

    private void btnResetSWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetSWActionPerformed
        //p4.setVisible(true);
        //btnResetSW.setVisible(false);
        isStartSW = false;
        isStartSW = false;
        csecondSW = 0;
        secondSW = 0;
        minuteSW = 0;
        hourSW = 0;
        lblHourSW.setText("" + hourSW);
        lblMinSW.setText("0" + minuteSW);
        lblSecSW.setText("0" + secondSW);
        lblCSecSW.setText("0" + csecondSW);

        btnRecordSW.setEnabled(false);
        btnPauseSW.setEnabled(false);
        btnStartSW.setEnabled(true);
        btnStartSW.setText("Start");
        btnPauseSW.setText("Stop");

        btnRecordSW.setEnabled(false);
        btnRecordSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pencil_30px.png")));

        btnPauseSW.setEnabled(false);
        btnPauseSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pause_30px_1.png")));
        btnPauseSW.setText("Stop");

        btnStartSW.setEnabled(true);
        //btnStartSW.setText("Start");
        //btnStartSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Play_30px_1.png")));

        new Thread(() -> {
            try {
                //btnResetSW.setEnabled(true);
                btnResetSW.setText("Resetting...");
                btnResetSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Synchronize_30px_1.png")));

                btnStartSW.setEnabled(false);

                recordfieldPanelSW.setVisible(false);
                Thread.sleep(1000);
                btnResetSW.doClick();

            } catch (InterruptedException e) {
            }
            //code here
            btnResetSW.setEnabled(false);
            btnResetSW.setText("Reset");
            btnResetSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Synchronize_30px.png")));
            btnResetSW.setFocusable(false);

            btnStartSW.setEnabled(true);
            btnStartSW.setText("Start");
            btnStartSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Play_30px_1.png")));
            recordfieldPanelSW.setVisible(true);

        }).start();

    }//GEN-LAST:event_btnResetSWActionPerformed

    private void btnStartSWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartSWActionPerformed

        recordfieldPanelSW.setVisible(true);
        btnRecordSW.setEnabled(true);
        btnRecordSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pencil_30px_1.png")));

        btnResetSW.setEnabled(true);
        btnResetSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Synchronize_30px_1.png")));

        btnPauseSW.setEnabled(true);
        btnPauseSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pause_30px_2.png")));
        btnPauseSW.setText("Pause");

        btnStartSW.setEnabled(false);
        btnStartSW.setText("Continue");
        btnStartSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Play_30px.png")));

        isStartSW = true;

        Thread th = new Thread() {
            @Override
            public void run() {

                while (isStartSW == true) {
                    try {
                        sleep(stopwatchTimeFromDB);
                        //clock speed of 9 or 10

                        csecondSW++;
                        if (csecondSW == 100) { //99 or 100
                            secondSW++;
                            csecondSW = 0;
                        }
                        if (secondSW == 60) {
                            minuteSW++;
                            secondSW = 0;
                        }
                        if (minuteSW == 60) {
                            hourSW++;
                            minuteSW = 0;
                        }

                        //lblHourSW.setText(""+hourSW);
                        //lblMinSW.setText("0"+minuteSW);
                        //lblSecSW.setText("0"+secondSW);
                        //lblCSecSW.setText(""+csecondSW);
                        //csecond
                        if (csecondSW > 9) {
                            lblCSecSW.setText("" + csecondSW);
                        } else {
                            lblCSecSW.setText("0" + csecondSW);
                        }

                        //second
                        if (secondSW > 9) {
                            lblSecSW.setText("" + secondSW);
                            lblSecSW.setToolTipText("" + secondSW + " seconds");
                        } else {
                            lblSecSW.setText("0" + secondSW);
                            lblSecSW.setToolTipText("0" + secondSW + " seconds");
                        }

                        //minute
                        if (minuteSW > 9) {
                            lblMinSW.setText("" + minuteSW);
                            lblMinSW.setToolTipText("" + minuteSW + " minutes");
                        } else {
                            lblMinSW.setText("0" + minuteSW);
                            lblMinSW.setToolTipText("0" + minuteSW + " minutes");
                        }

                        //hour
                        if (hourSW > 9) {
                            lblHourSW.setText("" + hourSW);
                        } else {
                            lblHourSW.setText("" + hourSW);
                        }

                        //hour word
                        if (hourSW > 1) {
                            jLabel3.setText("Hours");
                        } else {
                            jLabel3.setText("Hour");
                        }

                        //hours limit
                        if (hourSW > 99) {
                            btnResetSW.doClick();
                        }

                        //catch clause
                    } catch (InterruptedException ex) {
                        System.out.print("something is wrong");
                    }
                }
            }
        };
        th.start();
    }//GEN-LAST:event_btnStartSWActionPerformed

    private void btnPauseSWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseSWActionPerformed
        //p2.setVisible(true);
        //btnPauseSW.setVisible(false);
        //btnStartSW.setVisible(true);
        //p3.setVisible(false);
        //p4.setVisible(false);
        //btnResetSW.setVisible(true);
        btnPauseSW.setEnabled(false);
        btnPauseSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Pause_30px_1.png")));

        btnStartSW.setEnabled(true);
        btnStartSW.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Play_30px_1.png")));

        isStartSW = false;
    }//GEN-LAST:event_btnPauseSWActionPerformed

    private void btnResetSWMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetSWMouseClicked
    }//GEN-LAST:event_btnResetSWMouseClicked

    private void btnResetSWMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnResetSWMousePressed
        btnResetSW.doClick();
    }//GEN-LAST:event_btnResetSWMousePressed

    private void resetTimeRecordSWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetTimeRecordSWActionPerformed
        recordfieldScrollSW.getViewport().setViewPosition(new Point(0, 0)); //scrollup the jScrollPane to up
        strSW = "Recorded Laps:";
        recordfieldSW.setText("");
        recordfieldSW.repaint();
        countSW = 0;
    }//GEN-LAST:event_resetTimeRecordSWActionPerformed

    private void aboutButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutButtonMouseEntered
    }//GEN-LAST:event_aboutButtonMouseEntered

    private void aboutButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutButtonMouseExited
        resetColor2(exitButton);
        resetColor2(minimizeButton);
        resetColor2(aboutButton);
    }//GEN-LAST:event_aboutButtonMouseExited

    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed

        //for ABOUT
        final JPopupMenu popup = new JPopupMenu();
        final JMenuItem menuAuthor = new JMenuItem("Developer Information");
        final JMenuItem menuLight = new JMenuItem("Light Mode");
        final JMenuItem menuDark = new JMenuItem("Dark Mode");
        final JMenuItem menuSettings = new JMenuItem("Configure GUI Settings");

        // Minimize Button code:
        resetColor2(exitButton);
        resetColor2(minimizeButton);
        setColor2(aboutButton);

        //about
        ActionListener openAuthor
                = (ActionEvent e) -> {
                    About aboutpanel = new About();
                    aboutpanel.setVisible(true);
                };
        //settings
        ActionListener openSettings
                = (ActionEvent e) -> {
                    EditGUIOptions settingspanel;
                    try {
                        settingspanel = new EditGUIOptions();
                        settingspanel.setVisible(true);
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(MainClockTimerStopwatchGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };

        ActionListener lightmodeAction
                = (ActionEvent e) -> {
                    System.out.println("light");
                    try {
                        UIManager.setLookAndFeel(new FlatLightLaf());
                        SwingUtilities.updateComponentTreeUI(this);
                        this.pack();
                    } catch (UnsupportedLookAndFeelException ex) {
                        System.err.println("Failed to initialize modern Light LaF " + ex);
                    }
                };

        ActionListener darkmodeAction
                = (ActionEvent e) -> {
                    System.out.println("dark");
                    try {
                        UIManager.setLookAndFeel(new FlatDarkLaf());
                        SwingUtilities.updateComponentTreeUI(this);
                        this.pack();
                    } catch (UnsupportedLookAndFeelException ex) {
                        System.err.println("Failed to initialize modern Dark LaF " + ex);
                    }
                };

        menuAuthor.addActionListener(openAuthor);
        menuSettings.addActionListener(openSettings);
        menuLight.addActionListener(lightmodeAction);
        menuDark.addActionListener(darkmodeAction);

        popup.removeAll();
        popup.add(menuAuthor);
        popup.addSeparator();
        popup.add(menuSettings);
        popup.updateUI();

        popup.show(aboutButton, aboutButton.getWidth() / 2, aboutButton.getHeight() / 1);
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void pnlTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseClicked
        //for SHORTCUTS
        final JPopupMenu popupRightClick = new JPopupMenu();
        final JMenuItem menuMinimize = new JMenuItem("Minimize");
        final JMenuItem menuClose = new JMenuItem("Close Permanently");
        final JMenuItem menuRestore = new JMenuItem("Restore to Default Location");

        JLabel ShortcutsLBL = new JLabel();
        ShortcutsLBL.setText(" Shortcuts");
        ShortcutsLBL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10));

        if (SwingUtilities.isRightMouseButton(evt)) {

            //------------------------------------------//
            //restore
            ActionListener restoreActionListener
                    = (ActionEvent e) -> {
                        this.setLocationRelativeTo(null);
                    };
            menuRestore.addActionListener(restoreActionListener);

            //------------------------------------------//
            //minimize
            ActionListener minimizeActionListener
                    = (ActionEvent e) -> {
                        MainClockTimerStopwatchGUI.this.setState(Frame.ICONIFIED);
                    };
            menuMinimize.addActionListener(minimizeActionListener);

            //------------------------------------------//
            //close taskbar
            ActionListener closeActionListener
                    = (ActionEvent e) -> {
                        exitButton.doClick();
                    };
            menuClose.addActionListener(closeActionListener);

            //----------------------------------------------------------//
            //set jmenus cursor
            menuRestore.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            menuMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            menuClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            //set popup background
            popupRightClick.setBackground(new Color(249, 250, 253));

            //add popup on click
            popupRightClick.removeAll();
            popupRightClick.add(menuRestore);
            popupRightClick.addSeparator();
            popupRightClick.add(ShortcutsLBL);
            popupRightClick.add(menuMinimize);
            popupRightClick.add(menuClose);
            popupRightClick.updateUI();

            //show popup on right click.
            //popupRightClick.show(pnlTop, evt.getXOnScreen(), evt.getYOnScreen()/3);
            popupRightClick.show(pnlTop, evt.getX(), evt.getY() / 3);
        }
    }//GEN-LAST:event_pnlTopMouseClicked

    private void guiTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guiTitleMouseClicked
        //for SHORTCUTS
        final JPopupMenu popupRightClick = new JPopupMenu();
        final JMenuItem menuMinimize = new JMenuItem("Minimize");
        final JMenuItem menuClose = new JMenuItem("Close Permanently");
        final JMenuItem menuRestore = new JMenuItem("Restore to Default Location");

        JLabel ShortcutsLBL = new JLabel();
        ShortcutsLBL.setText(" Shortcuts");
        ShortcutsLBL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10));

        if (SwingUtilities.isRightMouseButton(evt)) {

            //------------------------------------------//
            //restore
            ActionListener restoreActionListener
                    = (ActionEvent e) -> {
                        this.setLocationRelativeTo(null);
                    };
            menuRestore.addActionListener(restoreActionListener);

            //------------------------------------------//
            //minimize
            ActionListener minimizeActionListener
                    = (ActionEvent e) -> {
                        MainClockTimerStopwatchGUI.this.setState(Frame.ICONIFIED);
                    };
            menuMinimize.addActionListener(minimizeActionListener);

            //------------------------------------------//
            //close taskbar
            ActionListener closeActionListener
                    = (ActionEvent e) -> {
                        exitButton.doClick();
                    };
            menuClose.addActionListener(closeActionListener);

            //----------------------------------------------------------//
            //set jmenus cursor
            menuRestore.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            menuMinimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            menuClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            //set popup background
            popupRightClick.setBackground(new Color(249, 250, 253));

            //add popup on click
            popupRightClick.removeAll();
            popupRightClick.add(menuRestore);
            popupRightClick.addSeparator();
            popupRightClick.add(ShortcutsLBL);
            popupRightClick.add(menuMinimize);
            popupRightClick.add(menuClose);
            popupRightClick.updateUI();

            //show popup on right click.
            //popupRightClick.show(guiTitle, evt.getXOnScreen(), evt.getYOnScreen()/3);
            popupRightClick.show(guiTitle, evt.getX(), evt.getY() / 3);
        }
    }//GEN-LAST:event_guiTitleMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        this.setState(Frame.NORMAL);
        this.setEnabled(true);
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setState(Frame.NORMAL);
        this.setEnabled(true);
    }//GEN-LAST:event_formWindowOpened

    private void guiTitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guiTitleMousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_guiTitleMousePressed

    private void guiTitleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guiTitleMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_guiTitleMouseDragged

    private void btnRestartTimerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRestartTimerMousePressed
        btnRestartTimer.doClick();
    }//GEN-LAST:event_btnRestartTimerMousePressed

    private void hideTimeRecordSWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideTimeRecordSWActionPerformed
        recordfieldPanelSW.setVisible(false);
    }//GEN-LAST:event_hideTimeRecordSWActionPerformed

    // set Color to pnlTop (EXIT)
    void setColor1(JButton btn1) {
        btn1.setBackground(new Color(255, 51, 51));
    }

    void resetColor1(JButton btn1) {
        btn1.setBackground(new Color(0, 102, 204));
    }

    // set Color to pnlTop (MINIMIZE, SETTINGS)
    void setColor2(JButton btn2) {
        btn2.setBackground(new Color(155, 182, 211));
    }

    void resetColor2(JButton btn2) {
        btn2.setBackground(new Color(0, 102, 204));
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
                new MainClockTimerStopwatchGUI().setVisible(true);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(MainClockTimerStopwatchGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutButton;
    private javax.swing.JButton btnPauseSW;
    private javax.swing.JButton btnRecordSW;
    private javax.swing.JButton btnResetSW;
    private javax.swing.JButton btnResetTimer;
    private javax.swing.JButton btnRestartTimer;
    private javax.swing.JButton btnStartSW;
    private javax.swing.JButton btnStartTimer;
    private javax.swing.JButton btnStopTimer;
    private javax.swing.JComboBox<String> cbMinTimer;
    private javax.swing.JComboBox<String> cbSecTimer;
    private javax.swing.JLabel currenttimeLABEL;
    private javax.swing.JLabel daydateLABEL;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel guiTitle;
    private javax.swing.JButton hideTimeRecordSW;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCSecSW;
    private javax.swing.JLabel lblHourSW;
    private javax.swing.JLabel lblMin;
    private javax.swing.JLabel lblMinSW;
    private javax.swing.JLabel lblSec;
    private javax.swing.JLabel lblSecSW;
    private javax.swing.JButton minimizeButton;
    private javax.swing.JLabel minutesTimerLABEL;
    private javax.swing.JLabel pagitan;
    private javax.swing.JPanel pnlClock;
    private javax.swing.JPanel pnlStopwatch;
    private javax.swing.JPanel pnlTimer;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel recordfieldPanelSW;
    private javax.swing.JTextArea recordfieldSW;
    private javax.swing.JScrollPane recordfieldScrollSW;
    private javax.swing.JButton resetTimeRecordSW;
    private javax.swing.JLabel secondsTimerLABEL;
    // End of variables declaration//GEN-END:variables
}
