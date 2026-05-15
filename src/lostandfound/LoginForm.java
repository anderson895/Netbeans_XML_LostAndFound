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
        setMinimumSize(new Dimension(440, 460));
        getContentPane().setBackground(BG_COLOR);

        // ── Header panel ──
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 110));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ASCOT logo — loaded from src/lostandfound/ascot_logo.png; falls back to a drawn logo if missing
        JLabel lblLogo = new JLabel(new ImageIcon(loadAscotLogo(70)));
        lblLogo.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("LOST AND FOUND SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(WHITE);

        JLabel lblSubtitle = new JLabel("Aurora State College of Technology");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(180, 210, 255));

        titleBox.add(Box.createVerticalGlue());
        titleBox.add(lblTitle);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(lblSubtitle);
        titleBox.add(Box.createVerticalGlue());

        headerPanel.add(lblLogo);
        headerPanel.add(Box.createHorizontalStrut(15));
        headerPanel.add(titleBox);
        headerPanel.add(Box.createHorizontalGlue());

        // ── Form panel ──
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

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
            .addComponent(lblSignIn, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(lblStudentId)
            .addComponent(txtStudentId, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(lblHint)
            .addComponent(lblPassword)
            .addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegister, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
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

        // ── Main layout using BorderLayout for responsiveness ──
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // Center the form in a wrapper
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 50, 25, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        wrapper.add(formPanel, gbc);

        getContentPane().add(wrapper, BorderLayout.CENTER);
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
                    BufferedImage whitened = whitenDarkBackground(raw);
                    return whitened.getScaledInstance(size, size, Image.SCALE_SMOOTH);
                }
            } catch (java.io.IOException ignore) { /* fall through to drawn logo */ }
        }
        return buildAscotLogo(size);
    }

    // Replace dark-navy background pixels with white so the inside of the logo reads as white.
    // Anything that's roughly dark blue is treated as background; gold/yellow/red/etc. is kept.
    private BufferedImage whitenDarkBackground(BufferedImage src) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int argb = src.getRGB(x, y);
                int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8)  & 0xff;
                int b = (argb)       & 0xff;
                if (a < 8) {
                    out.setRGB(x, y, argb);
                    continue;
                }
                // "Dark navy-ish": blue dominant and overall dark
                int max = Math.max(r, Math.max(g, b));
                boolean darkBlue = max < 110 && b >= r && b >= g - 15;
                if (darkBlue) {
                    out.setRGB(x, y, (a << 24) | 0xffffff); // opaque white
                } else {
                    out.setRGB(x, y, argb);
                }
            }
        }
        return out;
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
