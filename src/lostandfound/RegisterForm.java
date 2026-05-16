package lostandfound;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class RegisterForm extends javax.swing.JFrame {

    private static final Color PRIMARY   = new Color(25, 55, 109);
    private static final Color ACCENT    = new Color(0, 150, 136);
    private static final Color BG_COLOR  = new Color(236, 240, 245);
    private static final Color WHITE     = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(108, 117, 125);
    private static final Color GRAY_BTN  = new Color(108, 117, 125);

    private JTextField txtLastName, txtGivenName, txtMiddleName, txtStudentId;
    private JPasswordField txtPassword, txtConfirm;

    public RegisterForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASCOT - Create Account");
        setMinimumSize(new Dimension(900, 620));

        // Root content pane — split layout (background left, form right)
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(BG_COLOR);
        setContentPane(root);

        // ── Left side: Background.png ──
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());

        // ── Right side: form area ──
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(BG_COLOR);

        // ── Form ──
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)));

        // Title inside the form (replaces the old top blue header)
        JLabel lblTitle = new JLabel("CREATE ACCOUNT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(PRIMARY);

        JLabel lblNote = new JLabel("(All new accounts are registered as User)");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(TEXT_MUTED);

        JLabel lblLN = makeLabel("Last Name: *");
        txtLastName = makeField();
        JLabel lblGN = makeLabel("Given Name: *");
        txtGivenName = makeField();
        JLabel lblMN = makeLabel("Middle Name:");
        txtMiddleName = makeField();
        JLabel lblNameHint = new JLabel("Please enter full middle name (not initials). Stored as: Last Name, Given Name, Middle Name");
        lblNameHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblNameHint.setForeground(TEXT_MUTED);

        JLabel lblSid = makeLabel("Student ID: *");
        txtStudentId = makeField();
        JLabel lblSidHint = new JLabel("Format: 21-01-1155");
        lblSidHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblSidHint.setForeground(TEXT_MUTED);

        JLabel lblPw = makeLabel("Password: *");
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JLabel lblConf = makeLabel("Confirm Password: *");
        txtConfirm = new JPasswordField();
        txtConfirm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        UIHelper.styleButton(btnRegister, ACCENT, WHITE);

        JButton btnBack = new JButton("BACK TO LOGIN");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        UIHelper.styleButton(btnBack, GRAY_BTN, WHITE);

        // ── Form layout ──
        GroupLayout fl = new GroupLayout(formPanel);
        formPanel.setLayout(fl);
        final int FIELD_W = 380;
        fl.setHorizontalGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle)
            .addComponent(lblNote, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblLN)
            .addComponent(txtLastName, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblGN)
            .addComponent(txtGivenName, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblMN)
            .addComponent(txtMiddleName, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblNameHint, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblSid)
            .addComponent(txtStudentId, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblSidHint)
            .addComponent(lblPw)
            .addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addComponent(lblConf)
            .addComponent(txtConfirm, GroupLayout.DEFAULT_SIZE, FIELD_W, FIELD_W)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnRegister, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
            .addComponent(lblTitle).addGap(8)
            .addComponent(lblNote).addGap(10)
            .addComponent(lblLN).addGap(3).addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(lblGN).addGap(3).addComponent(txtGivenName, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(lblMN).addGap(3).addComponent(txtMiddleName, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(2)
            .addComponent(lblNameHint).addGap(10)
            .addComponent(lblSid).addGap(3).addComponent(txtStudentId, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(2)
            .addComponent(lblSidHint).addGap(10)
            .addComponent(lblPw).addGap(3).addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(lblConf).addGap(3).addComponent(txtConfirm, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(15)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnRegister, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                .addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
        );

        // ── Split layout: background image on left, form on right ──
        // fill = NONE keeps the form card at its preferred width when the window
        // is maximized — the form stays centered instead of stretching across.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 35, 20, 35);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(formPanel, gbc);

        root.add(bgPanel);
        root.add(rightPanel);

        setPreferredSize(new Dimension(1000, 680));
        pack();

        // ── Actions ──
        btnRegister.addActionListener(e -> doRegister());
        btnBack.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });
    }

    private void doRegister() {
        String last  = txtLastName.getText().trim();
        String given = txtGivenName.getText().trim();
        String mid   = txtMiddleName.getText().trim();
        String sid   = txtStudentId.getText().trim();
        String pass  = new String(txtPassword.getPassword()).trim();
        String conf  = new String(txtConfirm.getPassword()).trim();

        if (last.isEmpty() || given.isEmpty() || sid.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all required fields (*)!"); return;
        }
        if (last.length() < 2 || given.length() < 2) {
            JOptionPane.showMessageDialog(this,
                "Last Name and Given Name must be at least 2 letters long.",
                "Invalid Name", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!mid.isEmpty() && mid.length() < 2) {
            JOptionPane.showMessageDialog(this,
                "Middle Name must be at least 2 letters (do not use initials).",
                "Invalid Name", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!sid.matches("\\d{2}-\\d{2}-\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Student ID must follow format: 21-01-1155", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        if (pass.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE); return;
        }

        String fullName = last + ", " + given;
        if (!mid.isEmpty()) fullName += ", " + mid;

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement chk = conn.prepareStatement("SELECT id FROM users WHERE student_id=?");
            chk.setString(1, sid);
            if (chk.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "Student ID already registered!", "Error", JOptionPane.ERROR_MESSAGE);
                conn.close(); return;
            }
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO users (full_name, student_id, password, role) VALUES (?,?,?,'User')");
            pst.setString(1, fullName); pst.setString(2, sid); pst.setString(3, pass);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account created! You can now login.\nYour role: User\nStudent ID: " + sid);
            dispose(); new LoginForm().setVisible(true); conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
        }
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_DARK);
        return l;
    }
    private JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return f;
    }

    // Paints Background.png as a cover-scaled photo backdrop (same as LoginForm).
    private static class BackgroundPanel extends JPanel {
        private final Image bg;
        BackgroundPanel() {
            setOpaque(true);
            setBackground(new Color(236, 240, 245));
            Image loaded = null;
            try {
                java.net.URL url = RegisterForm.class.getResource("Background.png");
                if (url == null) url = RegisterForm.class.getResource("/lostandfound/Background.png");
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
}
