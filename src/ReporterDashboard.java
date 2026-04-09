import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReporterDashboard extends JFrame {
    private JTextField txtItemName, txtDescription, txtLocation, txtDateFound, txtReportedBy, txtSearch;
    private JComboBox<String> cmbCategory, cmbStatus;
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private String loggedUser;

    public ReporterDashboard(String userName) {
        this.loggedUser = userName;
        setTitle("Reporter Dashboard - " + userName);
        setSize(1080, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(245,245,250));

        // TOP BAR
        JPanel topBar = new JPanel(null);
        topBar.setBounds(0,0,1080,55);
        topBar.setBackground(new Color(0,123,255));
        add(topBar);

        JLabel lblTitle = new JLabel("  REPORTER DASHBOARD - Lost and Found");
        lblTitle.setFont(new Font("Segoe UI",Font.BOLD,18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(15,0,500,55);
        topBar.add(lblTitle);

        JLabel lblUser = new JLabel("Reporter: " + userName, SwingConstants.RIGHT);
        lblUser.setFont(new Font("Segoe UI",Font.PLAIN,12));
        lblUser.setForeground(new Color(220,230,255));
        lblUser.setBounds(700,0,200,55);
        topBar.add(lblUser);

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBounds(915,12,130,30);
        btnLogout.setFont(new Font("Segoe UI",Font.BOLD,11));
        btnLogout.setBackground(new Color(220,53,69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false); btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topBar.add(btnLogout);

        // LEFT PANEL
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(15,70,390,560);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,210)),
            BorderFactory.createEmptyBorder(15,15,15,15)));
        add(leftPanel);

        JLabel lblForm = new JLabel("Report an Item");
        lblForm.setFont(new Font("Segoe UI",Font.BOLD,16));
        lblForm.setForeground(new Color(0,123,255));
        lblForm.setBounds(15,10,200,25);
        leftPanel.add(lblForm);

        int y = 45;
        leftPanel.add(makeLabel("Item Name: *",15,y)); y+=20;
        txtItemName = makeField(15,y,350); leftPanel.add(txtItemName); y+=38;

        leftPanel.add(makeLabel("Description:",15,y)); y+=20;
        txtDescription = makeField(15,y,350); leftPanel.add(txtDescription); y+=38;

        leftPanel.add(makeLabel("Category:",15,y)); y+=20;
        cmbCategory = new JComboBox<>(new String[]{"Electronics","Clothing","Accessories","Documents","Others"});
        cmbCategory.setBounds(15,y,350,30); cmbCategory.setFont(new Font("Segoe UI",Font.PLAIN,13));
        leftPanel.add(cmbCategory); y+=38;

        leftPanel.add(makeLabel("Location Found: *",15,y)); y+=20;
        txtLocation = makeField(15,y,350); leftPanel.add(txtLocation); y+=38;

        leftPanel.add(makeLabel("Date Found (YYYY-MM-DD): *",15,y)); y+=20;
        txtDateFound = makeField(15,y,350); leftPanel.add(txtDateFound); y+=38;

        leftPanel.add(makeLabel("Reported By:",15,y)); y+=20;
        txtReportedBy = makeField(15,y,350); leftPanel.add(txtReportedBy);
        txtReportedBy.setText(userName); txtReportedBy.setEditable(false);
        txtReportedBy.setBackground(new Color(233,236,239)); y+=38;

        leftPanel.add(makeLabel("Status:",15,y)); y+=20;
        cmbStatus = new JComboBox<>(new String[]{"Unclaimed","Claim Pending","Claimed"});
        cmbStatus.setBounds(15,y,350,30); cmbStatus.setFont(new Font("Segoe UI",Font.PLAIN,13));
        leftPanel.add(cmbStatus); y+=45;

        JButton btnCreate = makeBtn("CREATE",new Color(40,167,69),15,y,82);
        JButton btnUpdate = makeBtn("UPDATE",new Color(0,123,255),102,y,82);
        JButton btnDelete = makeBtn("DELETE",new Color(220,53,69),189,y,82);
        JButton btnClear  = makeBtn("CLEAR",new Color(108,117,125),276,y,82);
        leftPanel.add(btnCreate); leftPanel.add(btnUpdate);
        leftPanel.add(btnDelete); leftPanel.add(btnClear);

        // RIGHT: SEARCH + TABLE
        txtSearch = new JTextField();
        txtSearch.setBounds(420,75,510,32);
        txtSearch.setFont(new Font("Segoe UI",Font.PLAIN,13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,210)),
            BorderFactory.createEmptyBorder(2,8,2,8)));
        add(txtSearch);

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(940,75,115,32);
        btnSearch.setFont(new Font("Segoe UI",Font.BOLD,12));
        btnSearch.setBackground(new Color(0,123,255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false); btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnSearch);

        tableModel = new DefaultTableModel(
            new String[]{"ID","Item Name","Description","Category","Location","Date Found","Reported By","Status","Claimed By"},0
        ){ public boolean isCellEditable(int r,int c){return false;} };
        tblItems = new JTable(tableModel);
        tblItems.setFont(new Font("Segoe UI",Font.PLAIN,11));
        tblItems.setRowHeight(24);
        tblItems.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));
        tblItems.setSelectionBackground(new Color(0,123,255));
        tblItems.setSelectionForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(tblItems);
        sp.setBounds(420,120,635,510);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200,200,210)));
        add(sp);

        // EVENTS
        btnCreate.addActionListener(e -> createItem());
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete.addActionListener(e -> deleteItem());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchItems());
        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",JOptionPane.YES_NO_OPTION)==0){
                dispose(); new LoginForm().setVisible(true); }
        });
        tblItems.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){ tableClicked(); }
        });

        loadTable();
    }

    private void loadTable() {
        try {
            Connection conn = DBConnection.getConnection(); if(conn==null) return;
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM items ORDER BY id DESC");
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
        } catch(SQLException e){ JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage()); }
    }

    private void clearFields(){
        txtItemName.setText(""); txtDescription.setText(""); cmbCategory.setSelectedIndex(0);
        txtLocation.setText(""); txtDateFound.setText(""); cmbStatus.setSelectedIndex(0);
        tblItems.clearSelection();
    }

    private void createItem(){
        String name=txtItemName.getText().trim(), loc=txtLocation.getText().trim(), date=txtDateFound.getText().trim();
        if(name.isEmpty()||loc.isEmpty()||date.isEmpty()){
            JOptionPane.showMessageDialog(this,"Fill in required fields (*)!"); return; }
        try{
            Connection conn=DBConnection.getConnection(); if(conn==null) return;
            PreparedStatement p=conn.prepareStatement("INSERT INTO items(item_name,description,category,location_found,date_found,reported_by,status) VALUES(?,?,?,?,?,?,?)");
            p.setString(1,name); p.setString(2,txtDescription.getText().trim());
            p.setString(3,cmbCategory.getSelectedItem().toString()); p.setString(4,loc);
            p.setString(5,date); p.setString(6,loggedUser);
            p.setString(7,cmbStatus.getSelectedItem().toString());
            p.executeUpdate();
            JOptionPane.showMessageDialog(this,"Item reported successfully!");
            clearFields(); loadTable(); conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void tableClicked(){
        int r=tblItems.getSelectedRow(); if(r==-1) return;
        txtItemName.setText(tableModel.getValueAt(r,1).toString());
        txtDescription.setText(tableModel.getValueAt(r,2).toString());
        cmbCategory.setSelectedItem(tableModel.getValueAt(r,3).toString());
        txtLocation.setText(tableModel.getValueAt(r,4).toString());
        txtDateFound.setText(tableModel.getValueAt(r,5).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(r,7).toString());
    }

    private void updateItem(){
        int r=tblItems.getSelectedRow();
        if(r==-1){JOptionPane.showMessageDialog(this,"Select an item first!"); return;}
        int id=Integer.parseInt(tableModel.getValueAt(r,0).toString());
        try{
            Connection conn=DBConnection.getConnection(); if(conn==null) return;
            PreparedStatement p=conn.prepareStatement("UPDATE items SET item_name=?,description=?,category=?,location_found=?,date_found=?,status=? WHERE id=?");
            p.setString(1,txtItemName.getText().trim()); p.setString(2,txtDescription.getText().trim());
            p.setString(3,cmbCategory.getSelectedItem().toString()); p.setString(4,txtLocation.getText().trim());
            p.setString(5,txtDateFound.getText().trim()); p.setString(6,cmbStatus.getSelectedItem().toString());
            p.setInt(7,id); p.executeUpdate();
            JOptionPane.showMessageDialog(this,"Item updated!");
            clearFields(); loadTable(); conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void deleteItem(){
        int r=tblItems.getSelectedRow();
        if(r==-1){JOptionPane.showMessageDialog(this,"Select an item first!"); return;}
        if(JOptionPane.showConfirmDialog(this,"Delete this item?","Confirm",JOptionPane.YES_NO_OPTION)!=0) return;
        int id=Integer.parseInt(tableModel.getValueAt(r,0).toString());
        try{
            Connection conn=DBConnection.getConnection(); if(conn==null) return;
            PreparedStatement p=conn.prepareStatement("DELETE FROM items WHERE id=?");
            p.setInt(1,id); p.executeUpdate();
            JOptionPane.showMessageDialog(this,"Item deleted!");
            clearFields(); loadTable(); conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
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

    private JLabel makeLabel(String t,int x,int y){JLabel l=new JLabel(t);l.setBounds(x,y,350,18);l.setFont(new Font("Segoe UI",Font.BOLD,12));l.setForeground(new Color(73,80,87));return l;}
    private JTextField makeField(int x,int y,int w){JTextField t=new JTextField();t.setBounds(x,y,w,30);t.setFont(new Font("Segoe UI",Font.PLAIN,13));t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200,200,210)),BorderFactory.createEmptyBorder(2,8,2,8)));return t;}
    private JButton makeBtn(String t,Color bg,int x,int y,int w){JButton b=new JButton(t);b.setBounds(x,y,w,32);b.setFont(new Font("Segoe UI",Font.BOLD,11));b.setBackground(bg);b.setForeground(Color.WHITE);b.setFocusPainted(false);b.setBorderPainted(false);b.setCursor(new Cursor(Cursor.HAND_CURSOR));return b;}
}
