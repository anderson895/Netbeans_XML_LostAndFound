package lostandfound;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserDashboard extends javax.swing.JFrame {

    private int userId;
    private String userName;
    private boolean editMode = false;
    private int editId = -1;

    public UserDashboard(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("User Dashboard - " + userName);
        lblUser.setText("User: " + userName);
        loadMyReports();
        loadFoundItems();
        loadMyClaims();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeader = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();

        // === Tab 1: Report Item ===
        panelReport = new javax.swing.JPanel();
        lblItemName = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        lblDesc = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lblCategory = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        lblType = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox<>();
        lblLocation = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        lblDate = new javax.swing.JLabel();
        txtDateReported = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnDeleteReport = new javax.swing.JButton();
        btnEditReport = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMyReports = new javax.swing.JTable();
        lblMyReports = new javax.swing.JLabel();

        // === Tab 2: Browse Found Items ===
        panelBrowse = new javax.swing.JPanel();
        lblBrowseInfo = new javax.swing.JLabel();
        txtSearchFound = new javax.swing.JTextField();
        btnSearchFound = new javax.swing.JButton();
        btnRequestClaim = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFoundItems = new javax.swing.JTable();

        // === Tab 3: My Claims ===
        panelClaims = new javax.swing.JPanel();
        lblClaimsInfo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMyClaims = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User Dashboard");
        setResizable(false);

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblHeader.setForeground(new java.awt.Color(0, 123, 255));
        lblHeader.setText("USER DASHBOARD");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("User:");

        btnLogout.setBackground(new java.awt.Color(220, 53, 69));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        // ====== TAB 1: REPORT ITEM ======
        lblItemName.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblItemName.setText("Item Name: *");
        txtItemName.setFont(new java.awt.Font("Segoe UI", 0, 13));

        lblDesc.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblDesc.setText("Description:");
        txtDescription.setFont(new java.awt.Font("Segoe UI", 0, 13));

        lblCategory.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblCategory.setText("Category:");
        cmbCategory.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Electronics", "Clothing", "Accessories", "Documents", "Others" }));

        lblType.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblType.setText("Type: *");
        cmbType.setFont(new java.awt.Font("Segoe UI", 0, 13));
        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lost", "Found" }));

        lblLocation.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblLocation.setText("Location: *");
        txtLocation.setFont(new java.awt.Font("Segoe UI", 0, 13));

        lblDate.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblDate.setText("Date (YYYY-MM-DD): *");
        txtDateReported.setFont(new java.awt.Font("Segoe UI", 0, 13));

        btnSubmit.setBackground(new java.awt.Color(40, 167, 69));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("SUBMIT");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(108, 117, 125));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnDeleteReport.setBackground(new java.awt.Color(220, 53, 69));
        btnDeleteReport.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnDeleteReport.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteReport.setText("DELETE");
        btnDeleteReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteReportActionPerformed(evt);
            }
        });

        btnEditReport.setBackground(new java.awt.Color(255, 193, 7));
        btnEditReport.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnEditReport.setForeground(new java.awt.Color(0, 0, 0));
        btnEditReport.setText("EDIT");
        btnEditReport.setEnabled(false);
        btnEditReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditReportActionPerformed(evt);
            }
        });

        lblMyReports.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblMyReports.setText("My Reports:");

        tblMyReports.setFont(new java.awt.Font("Segoe UI", 0, 11));
        tblMyReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Item Name", "Description", "Category", "Type", "Location", "Date", "Status" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        tblMyReports.setRowHeight(24);
        tblMyReports.getSelectionModel().addListSelectionListener(e -> {
            int row = tblMyReports.getSelectedRow();
            if (row != -1) {
                String status = tblMyReports.getValueAt(row, 7).toString();
                btnEditReport.setEnabled("Open".equals(status));
            } else {
                btnEditReport.setEnabled(false);
            }
        });
        jScrollPane1.setViewportView(tblMyReports);

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblItemName).addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDesc).addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategory).addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblType).addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLocation).addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDate).addComponent(txtDateReported, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditReport, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteReport, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMyReports)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        panelReportLayout.setVerticalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addComponent(lblItemName).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDesc).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCategory).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblType).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLocation).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDate).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateReported, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditReport, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteReport, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addComponent(lblMyReports)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );

        tabbedPane.addTab("Report Item", panelReport);

        // ====== TAB 2: BROWSE FOUND ITEMS ======
        lblBrowseInfo.setFont(new java.awt.Font("Segoe UI", 2, 12));
        lblBrowseInfo.setForeground(new java.awt.Color(108, 117, 125));
        lblBrowseInfo.setText("Found items reported by other users. Select one and click REQUEST CLAIM if it's yours.");

        txtSearchFound.setFont(new java.awt.Font("Segoe UI", 0, 13));

        btnSearchFound.setBackground(new java.awt.Color(0, 123, 255));
        btnSearchFound.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnSearchFound.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchFound.setText("SEARCH");
        btnSearchFound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchFoundActionPerformed(evt);
            }
        });

        btnRequestClaim.setBackground(new java.awt.Color(40, 167, 69));
        btnRequestClaim.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnRequestClaim.setForeground(new java.awt.Color(255, 255, 255));
        btnRequestClaim.setText("REQUEST CLAIM");
        btnRequestClaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequestClaimActionPerformed(evt);
            }
        });

        tblFoundItems.setFont(new java.awt.Font("Segoe UI", 0, 11));
        tblFoundItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Item Name", "Description", "Category", "Location", "Date Found", "Reported By", "Status" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        tblFoundItems.setRowHeight(24);
        jScrollPane2.setViewportView(tblFoundItems);

        javax.swing.GroupLayout panelBrowseLayout = new javax.swing.GroupLayout(panelBrowse);
        panelBrowse.setLayout(panelBrowseLayout);
        panelBrowseLayout.setHorizontalGroup(
            panelBrowseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBrowseLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelBrowseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBrowseInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelBrowseLayout.createSequentialGroup()
                        .addComponent(txtSearchFound, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchFound, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRequestClaim, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addGap(15, 15, 15))
        );
        panelBrowseLayout.setVerticalGroup(
            panelBrowseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBrowseLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblBrowseInfo)
                .addGap(10, 10, 10)
                .addGroup(panelBrowseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchFound, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchFound, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRequestClaim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        tabbedPane.addTab("Browse Found Items", panelBrowse);

        // ====== TAB 3: MY CLAIMS ======
        lblClaimsInfo.setFont(new java.awt.Font("Segoe UI", 2, 12));
        lblClaimsInfo.setForeground(new java.awt.Color(108, 117, 125));
        lblClaimsInfo.setText("Your claim requests and their status (Pending / Approved / Rejected):");

        tblMyClaims.setFont(new java.awt.Font("Segoe UI", 0, 11));
        tblMyClaims.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Claim ID", "Item Name", "Category", "Your Message", "Status", "Date Requested" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        tblMyClaims.setRowHeight(24);
        jScrollPane3.setViewportView(tblMyClaims);

        javax.swing.GroupLayout panelClaimsLayout = new javax.swing.GroupLayout(panelClaims);
        panelClaims.setLayout(panelClaimsLayout);
        panelClaimsLayout.setHorizontalGroup(
            panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClaimsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblClaimsInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(15, 15, 15))
        );
        panelClaimsLayout.setVerticalGroup(
            panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClaimsLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblClaimsInfo)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        tabbedPane.addTab("My Claims", panelClaims);

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        // ====== MAIN LAYOUT ======
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabbedPane))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        setPreferredSize(new java.awt.Dimension(1000, 620));
        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            if (!kw.isEmpty()) {
                String s = "%" + kw + "%";
                for (int i = 1; i <= 4; i++) ps.setString(i, s);
            }
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
    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {
        String name = txtItemName.getText().trim();
        String loc = txtLocation.getText().trim();
        String date = txtDateReported.getText().trim();
        if (name.isEmpty() || loc.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in all required fields (*)!"); return;
        }
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            if (editMode) {
                // UPDATE — only own posts
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE items SET item_name=?, description=?, category=?, type=?, location=?, date_reported=? WHERE id=? AND reported_by=?");
                ps.setString(1, name); ps.setString(2, txtDescription.getText().trim());
                ps.setString(3, cmbCategory.getSelectedItem().toString());
                ps.setString(4, cmbType.getSelectedItem().toString());
                ps.setString(5, loc); ps.setString(6, date);
                ps.setInt(7, editId); ps.setInt(8, userId);
                int rows = ps.executeUpdate();
                if (rows > 0) JOptionPane.showMessageDialog(this, "Report updated successfully!");
                else JOptionPane.showMessageDialog(this, "Update failed. You can only edit your own Open reports.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // INSERT — new report
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by) VALUES (?,?,?,?,?,?,?)");
                ps.setString(1, name); ps.setString(2, txtDescription.getText().trim());
                ps.setString(3, cmbCategory.getSelectedItem().toString());
                ps.setString(4, cmbType.getSelectedItem().toString());
                ps.setString(5, loc); ps.setString(6, date); ps.setInt(7, userId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item reported successfully!");
            }
            clearFields(); loadMyReports(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {
        clearFields();
    }

    private void btnEditReportActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblMyReports.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select one of your reports first!"); return; }
        String status = tblMyReports.getValueAt(row, 7).toString();
        if (!"Open".equals(status)) {
            JOptionPane.showMessageDialog(this, "You can only edit reports with 'Open' status.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return;
        }
        // Populate form fields from selected row
        editId = Integer.parseInt(tblMyReports.getValueAt(row, 0).toString());
        txtItemName.setText(tblMyReports.getValueAt(row, 1).toString());
        txtDescription.setText(tblMyReports.getValueAt(row, 2).toString());
        String cat = tblMyReports.getValueAt(row, 3).toString();
        for (int i = 0; i < cmbCategory.getItemCount(); i++) {
            if (cmbCategory.getItemAt(i).equals(cat)) { cmbCategory.setSelectedIndex(i); break; }
        }
        String type = tblMyReports.getValueAt(row, 4).toString();
        for (int i = 0; i < cmbType.getItemCount(); i++) {
            if (cmbType.getItemAt(i).equals(type)) { cmbType.setSelectedIndex(i); break; }
        }
        txtLocation.setText(tblMyReports.getValueAt(row, 5).toString());
        txtDateReported.setText(tblMyReports.getValueAt(row, 6).toString());
        editMode = true;
        btnSubmit.setText("UPDATE");
        btnSubmit.setBackground(new java.awt.Color(255, 193, 7));
        btnSubmit.setForeground(new java.awt.Color(0, 0, 0));
        JOptionPane.showMessageDialog(this, "Editing report ID " + editId + ". Make your changes and click UPDATE.", "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnDeleteReportActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblMyReports.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select one of your reports first!"); return; }
        int id = Integer.parseInt(tblMyReports.getValueAt(row, 0).toString());
        String status = tblMyReports.getValueAt(row, 7).toString();
        if (!"Open".equals(status)) {
            JOptionPane.showMessageDialog(this, "You can only delete reports with 'Open' status.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this report?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("DELETE FROM items WHERE id=? AND reported_by=?");
            ps.setInt(1, id); ps.setInt(2, userId); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Report deleted!"); loadMyReports(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void btnSearchFoundActionPerformed(java.awt.event.ActionEvent evt) {
        loadFoundItems();
    }

    private void btnRequestClaimActionPerformed(java.awt.event.ActionEvent evt) {
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
            String msg = JOptionPane.showInputDialog(this,
                "Claim \"" + itemName + "\"?\nProvide a short description to prove ownership:",
                "Request Claim", JOptionPane.QUESTION_MESSAGE);
            if (msg == null) { conn.close(); return; }
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO claim_requests (item_id, requested_by, message) VALUES (?,?,?)");
            ps.setInt(1, itemId); ps.setInt(2, userId); ps.setString(3, msg);
            ps.executeUpdate();
            PreparedStatement up = conn.prepareStatement("UPDATE items SET status='Claim Pending' WHERE id=?");
            up.setInt(1, itemId); up.executeUpdate();
            JOptionPane.showMessageDialog(this, "Claim request submitted! Admin will review it.");
            loadFoundItems(); loadMyClaims(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            dispose(); new LoginForm().setVisible(true);
        }
    }

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
        int idx = tabbedPane.getSelectedIndex();
        if (idx == 0) loadMyReports();
        else if (idx == 1) loadFoundItems();
        else if (idx == 2) loadMyClaims();
    }

    private void clearFields() {
        txtItemName.setText(""); txtDescription.setText(""); cmbCategory.setSelectedIndex(0);
        cmbType.setSelectedIndex(0); txtLocation.setText(""); txtDateReported.setText("");
        editMode = false; editId = -1;
        btnSubmit.setText("SUBMIT");
        btnSubmit.setBackground(new java.awt.Color(40, 167, 69));
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnEditReport.setEnabled(false);
        tblMyReports.clearSelection();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDeleteReport;
    private javax.swing.JButton btnEditReport;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRequestClaim;
    private javax.swing.JButton btnSearchFound;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBrowseInfo;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblClaimsInfo;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblMyReports;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel panelBrowse;
    private javax.swing.JPanel panelClaims;
    private javax.swing.JPanel panelReport;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tblFoundItems;
    private javax.swing.JTable tblMyClaims;
    private javax.swing.JTable tblMyReports;
    private javax.swing.JTextField txtDateReported;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtSearchFound;
    // End of variables declaration//GEN-END:variables
}