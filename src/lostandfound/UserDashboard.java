package lostandfound;

import java.awt.*;
import java.sql.*;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserDashboard extends javax.swing.JFrame {

    private int userId;
    private String userName;

    // ── Colors ──
    private static final Color PRIMARY     = new Color(25, 55, 109);
    private static final Color ACCENT      = new Color(0, 150, 136);
    private static final Color SIDEBAR_BG  = new Color(30, 42, 56);
    private static final Color SIDEBAR_SEL = new Color(44, 62, 80);
    private static final Color WHITE       = Color.WHITE;
    private static final Color BG          = new Color(236, 240, 245);
    private static final Color RED         = new Color(220, 53, 69);
    private static final Color GREEN       = new Color(40, 167, 69);
    private static final Color BLUE        = new Color(0, 123, 255);
    private static final Color GRAY        = new Color(108, 117, 125);
    private static final Color TEXT_DARK   = new Color(33, 37, 41);

    // ── Components ──
    private JTabbedPane tabbedPane;
    private JTextField txtItemName, txtDescription, txtSearchFound;
    private JComboBox<String> cmbCategory, cmbType, cmbLocation;
    private JComboBox<String> cmbYear, cmbMonth, cmbDay;
    private JTable tblMyReports, tblFoundItems, tblMyClaims;
    private JButton btnSubmit;

    // ── ASCOT Locations ──
    private static final String[] LOCATIONS = {
        "ASCOT ENTRANCE", "ASCOT EXIT", "ASCOT HOSTEL",
        "Information and Communication Technology Center (ICTC)",
        "Engineering Building", "General Education Buildings",
        "Senator Edgardo Angara Hall"
    };

    public UserDashboard(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        initComponents();
        setLocationRelativeTo(null);
        loadMyReports();
        loadFoundItems();
        loadMyClaims();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User Dashboard - " + userName);
        setMinimumSize(new Dimension(900, 550));
        setPreferredSize(new Dimension(1100, 650));

        // ====== SIDEBAR ======
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setMinimumSize(new Dimension(210, 0));
        sidebar.setMaximumSize(new Dimension(230, Integer.MAX_VALUE));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // ── Logo / header area ──
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblLogo = new JLabel("LOST & FOUND");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLogo.setForeground(WHITE);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRole = new JLabel("USER PANEL");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblRole.setForeground(new Color(100, 160, 220));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel(userName);
        lblName.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblName.setForeground(new Color(180, 190, 200));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(lblLogo);
        logoPanel.add(Box.createVerticalStrut(4));
        logoPanel.add(lblRole);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(lblName);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(60, 75, 95));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnReport    = makeSidebarBtn("Report Item");
        JButton btnBrowse    = makeSidebarBtn("Browse Found Items");
        JButton btnClaims    = makeSidebarBtn("My Claims");

        JButton btnLogout = new JButton("LOGOUT");
        UIHelper.styleLogoutButton(btnLogout, RED);

        sidebar.add(logoPanel);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnReport);
        sidebar.add(btnBrowse);
        sidebar.add(btnClaims);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(12));

        // ====== CONTENT AREA ======
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(BG);
        tabbedPane.addTab("Report Item", buildReportPanel());
        tabbedPane.addTab("Browse Found Items", buildBrowsePanel());
        tabbedPane.addTab("My Claims", buildClaimsPanel());

        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) loadMyReports();
            else if (idx == 1) loadFoundItems();
            else if (idx == 2) loadMyClaims();
        });

        // ── Sidebar button actions ──
        btnReport.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        btnBrowse.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        btnClaims.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                dispose(); new LoginForm().setVisible(true);
            }
        });

        // ── Main layout ──
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        pack();
    }

    // ==================== TAB 1: REPORT ITEM ====================
    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── Form on left ──
        JPanel form = new JPanel();
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        form.setPreferredSize(new Dimension(330, 0));
        form.setMinimumSize(new Dimension(280, 0));

        JLabel lblFormTitle = new JLabel("Report a Lost / Found Item");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setForeground(PRIMARY);

        txtItemName = makeField();
        txtDescription = makeField();
        cmbCategory = new JComboBox<>(new String[]{"Electronics", "Clothing", "Accessories", "Documents", "Others"});
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbType = new JComboBox<>(new String[]{"Lost", "Found"});
        cmbType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbLocation = new JComboBox<>(LOCATIONS);
        cmbLocation.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[6];
        for (int i = 0; i < 6; i++) years[i] = String.valueOf(curYear - i);
        cmbYear = new JComboBox<>(years);
        String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        cmbMonth = new JComboBox<>(months);
        cmbMonth.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = String.format("%02d", i + 1);
        cmbDay = new JComboBox<>(days);
        cmbDay.setSelectedIndex(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1);
        cmbYear.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbMonth.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbDay.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(WHITE);
        datePanel.add(cmbYear); datePanel.add(new JLabel("-")); datePanel.add(cmbMonth); datePanel.add(new JLabel("-")); datePanel.add(cmbDay);

        btnSubmit = new JButton("SUBMIT REPORT");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnSubmit, GREEN, WHITE);

        JButton btnClear = new JButton("CLEAR");
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnClear, GRAY, WHITE);

        GroupLayout fl = new GroupLayout(form);
        form.setLayout(fl);
        JLabel l1 = makeLabel("Item Name: *"), l2 = makeLabel("Description:"), l3 = makeLabel("Category:"),
               l4 = makeLabel("Type: *"), l5 = makeLabel("Location: *"), l6 = makeLabel("Date: *");

        fl.setHorizontalGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblFormTitle)
            .addComponent(l1).addComponent(txtItemName, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
            .addComponent(l2).addComponent(txtDescription)
            .addComponent(l3).addComponent(cmbCategory, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(l4).addComponent(cmbType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(l5).addComponent(cmbLocation, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(l6).addComponent(datePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
            .addComponent(lblFormTitle).addGap(12)
            .addComponent(l1).addGap(3).addComponent(txtItemName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(6)
            .addComponent(l2).addGap(3).addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(6)
            .addComponent(l3).addGap(3).addComponent(cmbCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(6)
            .addComponent(l4).addGap(3).addComponent(cmbType, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(6)
            .addComponent(l5).addGap(3).addComponent(cmbLocation, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(6)
            .addComponent(l6).addGap(3).addComponent(datePanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(15)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
        );

        // ── Table on right ──
        JPanel tablePanel = new JPanel(new BorderLayout(0, 5));
        tablePanel.setBackground(BG);
        JLabel lblMy = new JLabel("My Reports:");
        lblMy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMy.setForeground(PRIMARY);

        tblMyReports = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Description", "Category", "Type", "Location", "Date", "Status"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblMyReports.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblMyReports.setRowHeight(24);
        tblMyReports.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblMyReports.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tablePanel.add(lblMy, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(tblMyReports), BorderLayout.CENTER);

        panel.add(form, BorderLayout.WEST);
        panel.add(tablePanel, BorderLayout.CENTER);

        btnSubmit.addActionListener(e -> doSubmit());
        btnClear.addActionListener(e -> clearFields());

        return panel;
    }

    // ==================== TAB 2: BROWSE FOUND ITEMS ====================
    private JPanel buildBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblInfo = new JLabel("Found items reported by other users. Select one and click REQUEST CLAIM if it's yours.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(GRAY);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(BG);
        txtSearchFound = new JTextField(25);
        txtSearchFound.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnSearch = makeBtn("SEARCH", BLUE);
        JButton btnClaim = makeBtn("REQUEST CLAIM", GREEN);
        topBar.add(txtSearchFound); topBar.add(btnSearch); topBar.add(btnClaim);

        tblFoundItems = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Description", "Category", "Location", "Date Found", "Reported By", "Status"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblFoundItems.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblFoundItems.setRowHeight(24);
        tblFoundItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblFoundItems.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(lblInfo, BorderLayout.NORTH);
        top.add(topBar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblFoundItems), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadFoundItems());
        btnClaim.addActionListener(e -> doRequestClaim());

        return panel;
    }

    // ==================== TAB 3: MY CLAIMS ====================
    private JPanel buildClaimsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblInfo = new JLabel("Your claim requests and their status (Pending / Approved / Rejected):");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(GRAY);

        tblMyClaims = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Claim ID", "Item Name", "Category", "Your Message", "Status", "Date Requested"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblMyClaims.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblMyClaims.setRowHeight(24);
        tblMyClaims.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblMyClaims.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblMyClaims), BorderLayout.CENTER);
        return panel;
    }

    // ==================== DATA LOADING ====================
    private void loadMyReports() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, item_name, description, category, type, location, date_reported, status FROM items WHERE reported_by=? ORDER BY id DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblMyReports.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    rs.getString("date_reported"), rs.getString("status")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void loadFoundItems() {
        String kw = txtSearchFound.getText().trim();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            String sql = "SELECT i.id, i.item_name, i.description, i.category, i.location, i.date_reported, " +
                "u.full_name AS reporter, i.status FROM items i JOIN users u ON i.reported_by=u.id " +
                "WHERE i.type='Found' AND i.status='Open'";
            if (!kw.isEmpty()) {
                sql += " AND (i.item_name LIKE ? OR i.description LIKE ? OR i.category LIKE ? OR i.location LIKE ?)";
            }
            sql += " ORDER BY i.id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!kw.isEmpty()) { String s = "%" + kw + "%"; for (int i = 1; i <= 4; i++) ps.setString(i, s); }
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblFoundItems.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("location"), rs.getString("date_reported"),
                    rs.getString("reporter"), rs.getString("status")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void loadMyClaims() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT c.id, i.item_name, i.category, c.message, c.status, c.created_at " +
                "FROM claim_requests c JOIN items i ON c.item_id=i.id WHERE c.requested_by=? ORDER BY c.id DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblMyClaims.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("category"),
                    rs.getString("message"), rs.getString("status"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    // ==================== ACTIONS ====================
    private void doSubmit() {
        String name = txtItemName.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Fill in all required fields (*)!"); return; }
        String date = cmbYear.getSelectedItem() + "-" + cmbMonth.getSelectedItem() + "-" + cmbDay.getSelectedItem();
        String loc = (String) cmbLocation.getSelectedItem();

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, name); ps.setString(2, txtDescription.getText().trim());
            ps.setString(3, cmbCategory.getSelectedItem().toString());
            ps.setString(4, cmbType.getSelectedItem().toString());
            ps.setString(5, loc); ps.setString(6, date); ps.setInt(7, userId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item reported successfully!");
            clearFields(); loadMyReports(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void doRequestClaim() {
        int row = tblFoundItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a found item first!"); return; }
        int itemId = Integer.parseInt(tblFoundItems.getValueAt(row, 0).toString());
        String itemName = tblFoundItems.getValueAt(row, 1).toString();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;

            PreparedStatement chk = conn.prepareStatement(
                "SELECT id FROM claim_requests WHERE item_id=? AND requested_by=? AND status='Pending'");
            chk.setInt(1, itemId); chk.setInt(2, userId);
            if (chk.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "You already have a pending claim for this item!", "Info", JOptionPane.WARNING_MESSAGE);
                conn.close(); return;
            }

            PreparedStatement vq = conn.prepareStatement("SELECT verification_question FROM items WHERE id=?");
            vq.setInt(1, itemId);
            ResultSet vrs = vq.executeQuery();
            String verificationQ = null;
            if (vrs.next()) verificationQ = vrs.getString("verification_question");

            String verificationA = null;
            if (verificationQ != null && !verificationQ.isEmpty()) {
                verificationA = JOptionPane.showInputDialog(this,
                    "Verification Question:\n" + verificationQ + "\n\nYour Answer:",
                    "Claim Verification - " + itemName, JOptionPane.QUESTION_MESSAGE);
                if (verificationA == null) { conn.close(); return; }
            }

            String msg = JOptionPane.showInputDialog(this,
                "Claim \"" + itemName + "\"?\nProvide a short description to prove ownership:",
                "Request Claim", JOptionPane.QUESTION_MESSAGE);
            if (msg == null) { conn.close(); return; }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO claim_requests (item_id, requested_by, message, verification_answer) VALUES (?,?,?,?)");
            ps.setInt(1, itemId); ps.setInt(2, userId); ps.setString(3, msg); ps.setString(4, verificationA);
            ps.executeUpdate();

            PreparedStatement up = conn.prepareStatement("UPDATE items SET status='Claim Pending' WHERE id=?");
            up.setInt(1, itemId); up.executeUpdate();

            JOptionPane.showMessageDialog(this, "Claim request submitted! Admin will review it.");
            loadFoundItems(); loadMyClaims(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void clearFields() {
        txtItemName.setText(""); txtDescription.setText("");
        cmbCategory.setSelectedIndex(0); cmbType.setSelectedIndex(0); cmbLocation.setSelectedIndex(0);
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;
        cmbYear.setSelectedIndex(0);
        cmbMonth.setSelectedIndex(m);
        if (d >= 0 && d < cmbDay.getItemCount()) cmbDay.setSelectedIndex(d);
    }

    // ── Helper methods ──
    private JButton makeSidebarBtn(String text) {
        JButton btn = new JButton(text);
        UIHelper.styleSidebarButton(btn, SIDEBAR_BG, SIDEBAR_SEL);
        return btn;
    }
    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        UIHelper.styleButton(b, bg, WHITE);
        return b;
    }
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI", Font.BOLD, 12)); l.setForeground(TEXT_DARK); return l;
    }
    private JTextField makeField() {
        JTextField f = new JTextField(); f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return f;
    }
}