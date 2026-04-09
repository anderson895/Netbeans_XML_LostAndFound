import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClaimerDashboard extends JFrame {
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private String loggedUser;

    public ClaimerDashboard(String userName) {
        this.loggedUser = userName;
        setTitle("Claimer Dashboard - " + userName);
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(245,245,250));

        // TOP BAR
        JPanel topBar = new JPanel(null);
        topBar.setBounds(0,0,950,55);
        topBar.setBackground(new Color(40,167,69));
        add(topBar);

        JLabel lblTitle = new JLabel("  CLAIMER DASHBOARD - Lost and Found");
        lblTitle.setFont(new Font("Segoe UI",Font.BOLD,18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(15,0,450,55);
        topBar.add(lblTitle);

        JLabel lblUser = new JLabel("Claimer: " + userName, SwingConstants.RIGHT);
        lblUser.setFont(new Font("Segoe UI",Font.PLAIN,12));
        lblUser.setForeground(new Color(220,255,230));
        lblUser.setBounds(580,0,200,55);
        topBar.add(lblUser);

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBounds(800,12,120,30);
        btnLogout.setFont(new Font("Segoe UI",Font.BOLD,11));
        btnLogout.setBackground(new Color(220,53,69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false); btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topBar.add(btnLogout);

        // INFO
        JLabel lblInfo = new JLabel("Browse items below and click CLAIM to request an unclaimed item.");
        lblInfo.setFont(new Font("Segoe UI",Font.PLAIN,13));
        lblInfo.setForeground(new Color(73,80,87));
        lblInfo.setBounds(25,65,600,25);
        add(lblInfo);

        // SEARCH
        txtSearch = new JTextField();
        txtSearch.setBounds(25,100,730,32);
        txtSearch.setFont(new Font("Segoe UI",Font.PLAIN,13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,210)),
            BorderFactory.createEmptyBorder(2,8,2,8)));
        add(txtSearch);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(765,100,80,32);
        btnSearch.setFont(new Font("Segoe UI",Font.BOLD,11));
        btnSearch.setBackground(new Color(0,123,255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false); btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnSearch);

        JButton btnClaim = new JButton("CLAIM SELECTED ITEM");
        btnClaim.setBounds(855,100,80,32);
        btnClaim.setFont(new Font("Segoe UI",Font.BOLD,10));
        btnClaim.setBackground(new Color(40,167,69));
        btnClaim.setForeground(Color.WHITE);
        btnClaim.setFocusPainted(false); btnClaim.setBorderPainted(false);
        btnClaim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnClaim);

        // TABLE
        tableModel = new DefaultTableModel(
            new String[]{"ID","Item Name","Description","Category","Location","Date Found","Reported By","Status","Claimed By"},0
        ){ public boolean isCellEditable(int r,int c){return false;} };
        tblItems = new JTable(tableModel);
        tblItems.setFont(new Font("Segoe UI",Font.PLAIN,11));
        tblItems.setRowHeight(24);
        tblItems.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));
        tblItems.setSelectionBackground(new Color(40,167,69));
        tblItems.setSelectionForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(tblItems);
        sp.setBounds(25,145,900,410);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200,200,210)));
        add(sp);

        // EVENTS
        btnSearch.addActionListener(e -> searchItems());
        btnClaim.addActionListener(e -> claimItem());
        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",JOptionPane.YES_NO_OPTION)==0){
                dispose(); new LoginForm().setVisible(true); }
        });

        loadTable();
    }

    private void loadTable(){
        try{
            Connection conn=DBConnection.getConnection(); if(conn==null) return;
            ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM items ORDER BY id DESC");
            tableModel.setRowCount(0);
            while(rs.next()){
                tableModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("location_found"), rs.getString("date_found"),
                    rs.getString("reported_by"), rs.getString("status"),
                    rs.getString("claimed_by")==null?"—":rs.getString("claimed_by")
                });
            }
            conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void claimItem(){
        int r = tblItems.getSelectedRow();
        if(r==-1){JOptionPane.showMessageDialog(this,"Please select an item to claim!"); return;}

        String status = tableModel.getValueAt(r,7).toString();
        if(status.equals("Claimed")){
            JOptionPane.showMessageDialog(this,"This item has already been claimed!","Already Claimed",JOptionPane.WARNING_MESSAGE); return;
        }
        if(status.equals("Claim Pending")){
            JOptionPane.showMessageDialog(this,"This item already has a pending claim!","Pending",JOptionPane.WARNING_MESSAGE); return;
        }

        String itemName = tableModel.getValueAt(r,1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to claim: \"" + itemName + "\"?\n\nYour name (" + loggedUser + ") will be recorded.",
            "Confirm Claim", JOptionPane.YES_NO_OPTION);

        if(confirm==0){
            int id = Integer.parseInt(tableModel.getValueAt(r,0).toString());
            try{
                Connection conn=DBConnection.getConnection(); if(conn==null) return;
                PreparedStatement p = conn.prepareStatement(
                    "UPDATE items SET status='Claim Pending', claimed_by=?, claim_date=NOW() WHERE id=?");
                p.setString(1, loggedUser); p.setInt(2, id);
                p.executeUpdate();
                JOptionPane.showMessageDialog(this,
                    "Claim request submitted!\nThe Reporter will review and approve your claim.",
                    "Claim Submitted", JOptionPane.INFORMATION_MESSAGE);
                loadTable(); conn.close();
            }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
        }
    }

    private void searchItems(){
        String kw=txtSearch.getText().trim();
        if(kw.isEmpty()){loadTable(); return;}
        try{
            Connection conn=DBConnection.getConnection(); if(conn==null) return;
            PreparedStatement p=conn.prepareStatement("SELECT * FROM items WHERE item_name LIKE ? OR description LIKE ? OR category LIKE ? OR location_found LIKE ? OR status LIKE ?");
            String s="%"+kw+"%"; for(int i=1;i<=5;i++) p.setString(i,s);
            ResultSet rs=p.executeQuery(); tableModel.setRowCount(0);
            while(rs.next()){
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),rs.getString("item_name"),rs.getString("description"),
                    rs.getString("category"),rs.getString("location_found"),rs.getString("date_found"),
                    rs.getString("reported_by"),rs.getString("status"),
                    rs.getString("claimed_by")==null?"—":rs.getString("claimed_by")
                });
            }
            conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }
}
