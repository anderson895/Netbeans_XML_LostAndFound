package lostandfound;

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Use Nimbus for cross-platform consistency; buttons styled via BasicButtonUI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) { /* fallback */ }
        }

        // Global tweaks
        UIManager.put("Table.rowHeight", 26);

        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
