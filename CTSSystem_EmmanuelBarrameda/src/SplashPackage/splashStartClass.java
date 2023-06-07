// ©  M A D E		B Y		E M M A N		B A R R A M E D A  © //
//CTS System

package SplashPackage;

import com.formdev.flatlaf.FlatLightLaf;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author EMMANUEL PEÑAFLORIDA BARRAMEDA
 */
public class splashStartClass {

    public static void main(String args[]) {

        //select theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF" + ex);
        }

        //start the splash main class
        try {
            splashMainClass.main(new String[0]);
        } catch (IOException | SQLException e) {

        }

        //other statement here
    }
}
