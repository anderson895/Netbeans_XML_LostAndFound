import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        setTitle("Lost and Found - Login");
        setSize(440, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 250));

        // Header
        JLabel lblTitle = new JLabel("LOST AND FOUND", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setBounds(0, 18, 440, 30);
        add(lblTitle);

        JLabel lblSub = new JLabel("Sign in to your account", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(108, 117, 125));
        lblSub.setBounds(0, 48, 440, 20);
        add(lblSub);

        int x = 70, w = 300;

        add(makeLabel("Username:", x, 85));
        txtUsername = makeField(x, 107, w);
        add(txtUsername);

        add(makeLabel("Password:", x, 150));
        txtPassword = new JPasswordField();
        txtPassword.setBounds(x, 172, w, 36);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,210)),
            BorderFactory.createEmptyBorder(2,10,2,10)));
        add(txtPassword);

        JButton btnLogin = makeBtn("LOGIN", new Color(0,123,255), x, 228, 145);
        JButton btnReg = makeBtn("REGISTER", new Color(40,167,69), x+155, 228, 145);
        add(btnLogin);
        add(btnReg);

        JLabel lblInfo = new JLabel("Reporter = add/edit/delete items  |  Claimer = claim items", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(150,150,160));
        lblInfo.setBounds(0, 280, 440, 20);
        add(lblInfo);

        btnLogin.addActionListener(e -> login());
        btnReg.addActionListener(e -> { dispose(); new RegisterForm().setVisible(true); });
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode()==KeyEvent.VK_ENTER) login(); }
        });
    }

    private void login() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!"); return;
        }
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            pst.setString(1, user); pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String name = rs.getString("full_name");
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Welcome, " + name + "!\nRole: " + role);
                dispose();
                if (role.equals("Reporter")) {
                    new ReporterDashboard(name).setVisible(true);
                } else {
                    new ClaimerDashboard(name).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
        }
    }

    private JLabel makeLabel(String t, int x, int y) {
        JLabel l = new JLabel(t); l.setBounds(x,y,300,20);
        l.setFont(new Font("Segoe UI",Font.BOLD,12)); l.setForeground(new Color(73,80,87)); return l;
    }
    private JTextField makeField(int x, int y, int w) {
        JTextField t = new JTextField(); t.setBounds(x,y,w,36);
        t.setFont(new Font("Segoe UI",Font.PLAIN,14));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,210)),
            BorderFactory.createEmptyBorder(2,10,2,10))); return t;
    }
    private JButton makeBtn(String t, Color bg, int x, int y, int w) {
        JButton b = new JButton(t); b.setBounds(x,y,w,38);
        b.setFont(new Font("Segoe UI",Font.BOLD,13)); b.setBackground(bg);
        b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b;
    }
}
