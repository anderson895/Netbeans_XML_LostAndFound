package lostandfound;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Utility class for consistent UI styling across all Look & Feels.
 * Uses BasicButtonUI to guarantee that setBackground/setForeground are respected.
 */
public class UIHelper {

    /** Style a regular action button so its colors render on every platform. */
    public static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setUI(new BasicButtonUI());
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    /** Style a sidebar navigation button — full-width, flat, with hover effect. */
    public static void styleSidebarButton(JButton btn, Color bg, Color hoverBg) {
        btn.setUI(new BasicButtonUI());
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Fill full width in BoxLayout Y_AXIS
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(220, 40));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hoverBg); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
    }

    /** Style the logout button in the sidebar — full-width, bold red. */
    public static void styleLogoutButton(JButton btn, Color bg) {
        btn.setUI(new BasicButtonUI());
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(220, 42));
        Color hoverBg = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hoverBg); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
    }
}
