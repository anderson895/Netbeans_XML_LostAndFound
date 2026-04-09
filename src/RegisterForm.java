import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class RegisterForm extends JFrame {
    private JTextField txtFullName, txtUsername;
    private JPasswordField txtPassword, txtConfirm;
    private JComboBox<String> cmbRole;

    public RegisterForm() {
        setTitle("Lost and Found - Register");
        setSize(440, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(245,245,250));

        JLabel lblTitle = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(0,18,440,30);
        add(lblTitle);

        int x = 70, w = 300;

        add(makeLabel("Full Name:", x, 65));
        txtFullName = makeField(x, 87, w); add(txtFullName);

        add(makeLabel("Username:", x, 130));
        txtUsername = makeField(x, 152, w); add(txtUsername);

        add(makeLabel("Password:", x, 195));
        txtPassword = new JPasswordField(); style(txtPassword, x, 217, w); add(txtPassword);

        add(makeLabel("Confirm Password:", x, 260));
        txtConfirm = new JPasswordField(); style(txtConfirm, x, 282, w); add(txtConfirm);

        add(makeLabel("Role:", x, 325));
        cmbRole = new JComboBox<>(new String[]{"Reporter", "Claimer"});
        cmbRole.setBounds(x, 347, w, 36);
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(cmbRole);

        JButton btnReg = makeBtn("REGISTER", new Color(40,167,69), x, 400, 145);
        JButton btnBack = makeBtn("BACK TO LOGIN", new Color(108,117,125), x+155, 400, 145);
        add(btnReg); add(btnBack);

        btnReg.addActionListener(e -> register());
        btnBack.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });
    }

    private void register() {
        String name = txtFullName.getText().trim();
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String conf = new String(txtConfirm.getPassword()).trim();
        String role = cmbRole.getSelectedItem().toString();

        if (name.isEmpty()||user.isEmpty()||pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Fill in all fields!"); return; }
        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this,"Passwords do not match!","Error",JOptionPane.ERROR_MESSAGE); return; }
        if (pass.length()<6) {
            JOptionPane.showMessageDialog(this,"Password must be at least 6 characters!","Error",JOptionPane.ERROR_MESSAGE); return; }

        try {
            Connection conn = DBConnection.getConnection(); if(conn==null) return;
            PreparedStatement chk = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            chk.setString(1,user);
            if (chk.executeQuery().next()) {
                JOptionPane.showMessageDialog(this,"Username already taken!","Error",JOptionPane.ERROR_MESSAGE);
                conn.close(); return;
            }
            PreparedStatement pst = conn.prepareStatement("INSERT INTO users (full_name,username,password,role) VALUES (?,?,?,?)");
            pst.setString(1,name); pst.setString(2,user); pst.setString(3,pass); pst.setString(4,role);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"Account created! You can now login.\nYour role: "+role);
            dispose(); new LoginForm().setVisible(true);
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());
        }
    }

    private JLabel makeLabel(String t,int x,int y){JLabel l=new JLabel(t);l.setBounds(x,y,300,20);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(new Color(73,80,87));return l;}
    private JTextField makeField(int x,int y,int w){JTextField t=new JTextField();t.setBounds(x,y,w,36);t.setFont(new Font("Segoe UI",Font.PLAIN,14));t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,200,210)),BorderFactory.createEmptyBorder(2,10,2,10)));return t;}
    private void style(JPasswordField p,int x,int y,int w){p.setBounds(x,y,w,36);p.setFont(new Font("Segoe UI",Font.PLAIN,14));p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,200,210)),BorderFactory.createEmptyBorder(2,10,2,10)));}
    private JButton makeBtn(String t,Color bg,int x,int y,int w){JButton b=new JButton(t);b.setBounds(x,y,w,38);b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setBackground(bg);b.setForeground(Color.WHITE);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));return b;}
}
