package lostandfound;

import java.awt.*;
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
        setMinimumSize(new Dimension(420, 400));
        getContentPane().setBackground(BG_COLOR);

        // ── Header panel ──
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel lblTitle = new JLabel("LOST AND FOUND SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitle = new JLabel("Aurora State College of Technology");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(180, 210, 255));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblSubtitle);
        headerPanel.add(Box.createVerticalGlue());

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

        JLabel lblStudentId = new JLabel("Student ID:");
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

        JLabel lblInfo = new JLabel("Admin = manage all  |  User = report & claim items");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblInfo.setForeground(TEXT_MUTED);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);

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
            .addComponent(lblInfo, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
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
            .addGap(12)
            .addComponent(lblInfo)
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
                JOptionPane.showMessageDialog(this, "Please enter Student ID and password!"); return;
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
                    JOptionPane.showMessageDialog(this, "Invalid Student ID or password!",
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
}
