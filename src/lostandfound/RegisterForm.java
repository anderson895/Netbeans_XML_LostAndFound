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
        setMinimumSize(new Dimension(440, 520));
        getContentPane().setBackground(BG_COLOR);

        // ── Header ──
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        JLabel lblTitle = new JLabel("CREATE ACCOUNT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // ── Form ──
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220), 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)));

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
        fl.setHorizontalGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblNote, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
            .addComponent(lblLN).addComponent(txtLastName)
            .addComponent(lblGN).addComponent(txtGivenName)
            .addComponent(lblMN).addComponent(txtMiddleName)
            .addComponent(lblNameHint)
            .addComponent(lblSid).addComponent(txtStudentId)
            .addComponent(lblSidHint)
            .addComponent(lblPw).addComponent(txtPassword)
            .addComponent(lblConf).addComponent(txtConfirm)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnRegister, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
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

        // ── Main layout using BorderLayout for responsiveness ──
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 25, 20, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        wrapper.add(formPanel, gbc);

        getContentPane().add(wrapper, BorderLayout.CENTER);
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
}
