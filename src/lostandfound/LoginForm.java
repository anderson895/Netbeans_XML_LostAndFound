package lostandfound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import javax.swing.*;

public class LoginForm extends javax.swing.JFrame {

    // ── Color palette ──
    private static final Color PRIMARY   = new Color(25, 55, 109);
    private static final Color ACCENT    = new Color(0, 150, 136);
    private static final Color BG_COLOR  = new Color(236, 240, 245);
    private static final Color WHITE     = Color.WHITE;
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(108, 117, 125);

    public LoginForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASCOT - Lost and Found System");
        setMinimumSize(new Dimension(900, 560));

        // Root content pane — plain background; split layout below
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(BG_COLOR);
        setContentPane(root);

        // ── Left side: Background.png ──
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());

        // ── Right side: form area ──
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BG_COLOR);

        // ── Form panel ──
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        // Logo + title above the fields (kept inside the form on the right)
        JLabel lblLogo = new JLabel(new ImageIcon(loadAscotLogo(56)));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("LOST AND FOUND SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PRIMARY);

        JLabel lblSubtitle = new JLabel("Aurora State College of Technology");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(TEXT_MUTED);

        JLabel lblSignIn = new JLabel("Sign in to your account");
        lblSignIn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSignIn.setForeground(TEXT_MUTED);

        JLabel lblStudentId = new JLabel("Account:");
        lblStudentId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStudentId.setForeground(TEXT_DARK);

        JTextField txtStudentId = new JTextField();
        txtStudentId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtStudentId.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JLabel lblHint = new JLabel("Format: 21-01-1155");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblHint.setForeground(TEXT_MUTED);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(TEXT_DARK);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        UIHelper.styleButton(btnLogin, PRIMARY, WHITE);

        JButton btnRegister = new JButton("CREATE ACCOUNT");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        UIHelper.styleButton(btnRegister, ACCENT, WHITE);

        // ── Form layout ──
        GroupLayout fl = new GroupLayout(formPanel);
        formPanel.setLayout(fl);
        fl.setHorizontalGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo)
            .addComponent(lblTitle)
            .addComponent(lblSubtitle)
            .addComponent(lblSignIn, GroupLayout.DEFAULT_SIZE, 320, 320)
            .addComponent(lblStudentId)
            .addComponent(txtStudentId, GroupLayout.DEFAULT_SIZE, 320, 320)
            .addComponent(lblHint)
            .addComponent(lblPassword)
            .addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 320, 320)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegister, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
            .addComponent(lblLogo)
            .addGap(8)
            .addComponent(lblTitle)
            .addGap(2)
            .addComponent(lblSubtitle)
            .addGap(15)
            .addComponent(lblSignIn)
            .addGap(15)
            .addComponent(lblStudentId)
            .addGap(4)
            .addComponent(txtStudentId, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
            .addGap(2)
            .addComponent(lblHint)
            .addGap(10)
            .addComponent(lblPassword)
            .addGap(4)
            .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
            .addGap(18)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                .addComponent(btnRegister, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
        );

        // ── Split layout: background image on left, form on right ──
        // fill = NONE keeps the form card at its preferred width when the window
        // is maximized — the form stays centered instead of stretching across.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 40, 25, 40);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(formPanel, gbc);

        root.add(bgPanel);
        root.add(rightPanel);

        setPreferredSize(new Dimension(1000, 620));
        pack();

        // ── Actions ──
        btnLogin.addActionListener(e -> {
            String sid = txtStudentId.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();
            if (sid.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Account and password!"); return;
            }
            try {
                Connection conn = DBConnection.getConnection(); if (conn == null) return;
                PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, full_name, role FROM users WHERE student_id=? AND password=?");
                pst.setString(1, sid); pst.setString(2, pass);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String name = rs.getString("full_name");
                    String role = rs.getString("role");
                    JOptionPane.showMessageDialog(this, "Welcome, " + name + "!\nRole: " + role);
                    dispose();
                    if ("Admin".equals(role)) {
                        new AdminDashboard(userId, name).setVisible(true);
                    } else {
                        new UserDashboard(userId, name).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Account or password!",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error:\n" + ex.getMessage());
            }
        });

        btnRegister.addActionListener(e -> {
            dispose(); new RegisterForm().setVisible(true);
        });
    }

    // Load ASCOT logo from the classpath; if not present, draw a fallback so the UI never breaks.
    private Image loadAscotLogo(int size) {
        java.net.URL url = LoginForm.class.getResource("ascot_logo.png");
        if (url == null) url = LoginForm.class.getResource("/lostandfound/ascot_logo.png");
        if (url != null) {
            try {
                BufferedImage raw = javax.imageio.ImageIO.read(url);
                if (raw != null) {
                    return raw.getScaledInstance(size, size, Image.SCALE_SMOOTH);
                }
            } catch (java.io.IOException ignore) { /* fall through to drawn logo */ }
        }
        return buildAscotLogo(size);
    }

    // Paints Background.png as a cover-scaled photo backdrop. No stretching:
    // the image keeps its aspect ratio and is centered; any overflow is cropped.
    private static class BackgroundPanel extends JPanel {
        private final Image bg;
        BackgroundPanel() {
            setOpaque(true);
            setBackground(new Color(236, 240, 245));
            Image loaded = null;
            try {
                java.net.URL url = LoginForm.class.getResource("Background.png");
                if (url == null) url = LoginForm.class.getResource("/lostandfound/Background.png");
                if (url != null) loaded = javax.imageio.ImageIO.read(url);
            } catch (java.io.IOException ignore) { }
            this.bg = loaded;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bg == null) return;
            int pw = getWidth(), ph = getHeight();
            int iw = bg.getWidth(null), ih = bg.getHeight(null);
            if (iw <= 0 || ih <= 0) return;
            // "Cover" scaling: preserve aspect ratio, fill panel, crop edges if needed
            double scale = Math.max(pw / (double) iw, ph / (double) ih);
            int dw = (int) Math.round(iw * scale);
            int dh = (int) Math.round(ih * scale);
            int dx = (pw - dw) / 2;
            int dy = (ph - dh) / 2;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(bg, dx, dy, dw, dh, this);
            g2.dispose();
        }
    }

    // Programmatic ASCOT logo used as a fallback when no PNG is present.
    private BufferedImage buildAscotLogo(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Outer gold ring
        g.setColor(new Color(212, 175, 55));
        g.fillOval(0, 0, size, size);

        // Inner WHITE circle
        int pad = 4;
        g.setColor(Color.WHITE);
        g.fillOval(pad, pad, size - pad * 2, size - pad * 2);

        // ASCOT text (navy so it reads on white)
        g.setColor(new Color(15, 35, 85));
        g.setFont(new Font("Serif", Font.BOLD, Math.max(12, size / 4)));
        FontMetrics fm = g.getFontMetrics();
        String text = "ASCOT";
        int tw = fm.stringWidth(text);
        g.drawString(text, (size - tw) / 2, size / 2 + fm.getAscent() / 3);

        g.setFont(new Font("Serif", Font.PLAIN, Math.max(7, size / 9)));
        fm = g.getFontMetrics();
        String sub = "L&F";
        tw = fm.stringWidth(sub);
        g.drawString(sub, (size - tw) / 2, size / 2 + fm.getAscent() + fm.getHeight());

        g.dispose();
        return img;
    }
}
