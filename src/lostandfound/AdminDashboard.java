package lostandfound;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends javax.swing.JFrame {

    private int adminId;
    private String adminName;

    public AdminDashboard(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Admin Dashboard - " + adminName);
        lblUser.setText("Admin: " + adminName);
        loadItems();
        loadClaims();
        loadUsers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeader = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();

        // Tab 1: All Items
        panelItems = new javax.swing.JPanel();
        lblItemsInfo = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnDeleteItem = new javax.swing.JButton();
        btnMarkResolved = new javax.swing.JButton();
        btnFindMatch = new javax.swing.JButton();
        btnRefreshItems = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();

        // Tab 2: Claims
        panelClaims = new javax.swing.JPanel();
        lblClaimsInfo = new javax.swing.JLabel();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnRefreshClaims = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClaims = new javax.swing.JTable();

        // Tab 3: Users
        panelUsers = new javax.swing.JPanel();
        lblUsersInfo = new javax.swing.JLabel();
        btnDeleteUser = new javax.swing.JButton();
        btnResetPw = new javax.swing.JButton();
        btnRefreshUsers = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();

        // Tab 4: Backup
        panelBackup = new javax.swing.JPanel();
        lblBackupTitle = new javax.swing.JLabel();
        lblBackupInfo = new javax.swing.JLabel();
        btnBackup = new javax.swing.JButton();
        btnRestore = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setResizable(false);

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblHeader.setForeground(new java.awt.Color(220, 53, 69));
        lblHeader.setText("ADMIN DASHBOARD");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("Admin:");

        btnLogout.setBackground(new java.awt.Color(220, 53, 69));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        // ====== TAB 1: ALL ITEMS ======
        lblItemsInfo.setFont(new java.awt.Font("Segoe UI", 2, 11));
        lblItemsInfo.setForeground(new java.awt.Color(108, 117, 125));
        lblItemsInfo.setText("Select a Lost item: use FIND MATCH to see possible Found matches, or MARK RESOLVED when the item has been returned.");

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 13));

        btnSearch.setBackground(new java.awt.Color(0, 123, 255));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("SEARCH");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnDeleteItem.setBackground(new java.awt.Color(220, 53, 69));
        btnDeleteItem.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnDeleteItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteItem.setText("DELETE ITEM");
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });

        btnRefreshItems.setBackground(new java.awt.Color(108, 117, 125));
        btnRefreshItems.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnRefreshItems.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshItems.setText("REFRESH");
        btnRefreshItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearch.setText(""); loadItems();
            }
        });

        btnMarkResolved.setBackground(new java.awt.Color(23, 162, 184));
        btnMarkResolved.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnMarkResolved.setForeground(new java.awt.Color(255, 255, 255));
        btnMarkResolved.setText("MARK RESOLVED");
        btnMarkResolved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarkResolvedActionPerformed(evt);
            }
        });

        btnFindMatch.setBackground(new java.awt.Color(111, 66, 193));
        btnFindMatch.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnFindMatch.setForeground(new java.awt.Color(255, 255, 255));
        btnFindMatch.setText("FIND MATCH");
        btnFindMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindMatchActionPerformed(evt);
            }
        });

        tblItems.setFont(new java.awt.Font("Segoe UI", 0, 11));
        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Item Name", "Description", "Category", "Type", "Location", "Date", "Reported By", "Status" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        tblItems.setRowHeight(24);
        jScrollPane1.setViewportView(tblItems);

        javax.swing.GroupLayout panelItemsLayout = new javax.swing.GroupLayout(panelItems);
        panelItems.setLayout(panelItemsLayout);
        panelItemsLayout.setHorizontalGroup(
            panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblItemsInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelItemsLayout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMarkResolved, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFindMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefreshItems, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(15, 15, 15))
        );
        panelItemsLayout.setVerticalGroup(
            panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemsLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblItemsInfo)
                .addGap(8, 8, 8)
                .addGroup(panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMarkResolved, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshItems, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        tabbedPane.addTab("All Items", panelItems);

        // ====== TAB 2: CLAIM REQUESTS ======
        lblClaimsInfo.setFont(new java.awt.Font("Segoe UI", 2, 11));
        lblClaimsInfo.setForeground(new java.awt.Color(108, 117, 125));
        lblClaimsInfo.setText("Review and approve/reject user claim requests for found items.");

        btnApprove.setBackground(new java.awt.Color(40, 167, 69));
        btnApprove.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnApprove.setForeground(new java.awt.Color(255, 255, 255));
        btnApprove.setText("APPROVE");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doResolveClaim("Approved");
            }
        });

        btnReject.setBackground(new java.awt.Color(220, 53, 69));
        btnReject.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("REJECT");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doResolveClaim("Rejected");
            }
        });

        btnRefreshClaims.setBackground(new java.awt.Color(108, 117, 125));
        btnRefreshClaims.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnRefreshClaims.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshClaims.setText("REFRESH");
        btnRefreshClaims.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadClaims();
            }
        });

        tblClaims.setFont(new java.awt.Font("Segoe UI", 0, 11));
        tblClaims.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Claim ID", "Item Name", "Category", "Claimant", "Message", "Status", "Date" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        tblClaims.setRowHeight(24);
        jScrollPane2.setViewportView(tblClaims);

        javax.swing.GroupLayout panelClaimsLayout = new javax.swing.GroupLayout(panelClaims);
        panelClaims.setLayout(panelClaimsLayout);
        panelClaimsLayout.setHorizontalGroup(
            panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClaimsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblClaimsInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelClaimsLayout.createSequentialGroup()
                        .addComponent(btnApprove, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReject, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefreshClaims, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addGap(15, 15, 15))
        );
        panelClaimsLayout.setVerticalGroup(
            panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClaimsLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblClaimsInfo)
                .addGap(8, 8, 8)
                .addGroup(panelClaimsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApprove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReject, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshClaims, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        tabbedPane.addTab("Claim Requests", panelClaims);

        // ====== TAB 3: MANAGE USERS ======
        lblUsersInfo.setFont(new java.awt.Font("Segoe UI", 2, 11));
        lblUsersInfo.setForeground(new java.awt.Color(108, 117, 125));
        lblUsersInfo.setText("Manage user accounts. Delete or reset passwords.");

        btnDeleteUser.setBackground(new java.awt.Color(220, 53, 69));
        btnDeleteUser.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnDeleteUser.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteUser.setText("DELETE USER");
        btnDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteUserActionPerformed(evt);
            }
        });

        btnResetPw.setBackground(new java.awt.Color(255, 193, 7));
        btnResetPw.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnResetPw.setForeground(new java.awt.Color(0, 0, 0));
        btnResetPw.setText("RESET PASSWORD");
        btnResetPw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPwActionPerformed(evt);
            }
        });

        btnRefreshUsers.setBackground(new java.awt.Color(108, 117, 125));
        btnRefreshUsers.setFont(new java.awt.Font("Segoe UI", 1, 11));
        btnRefreshUsers.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshUsers.setText("REFRESH");
        btnRefreshUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadUsers();
            }
        });

        tblUsers.setFont(new java.awt.Font("Segoe UI", 0, 11));
        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Full Name", "Username", "Role", "Created At" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        tblUsers.setRowHeight(24);
        jScrollPane3.setViewportView(tblUsers);

        javax.swing.GroupLayout panelUsersLayout = new javax.swing.GroupLayout(panelUsers);
        panelUsers.setLayout(panelUsersLayout);
        panelUsersLayout.setHorizontalGroup(
            panelUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsersLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsersInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelUsersLayout.createSequentialGroup()
                        .addComponent(btnDeleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResetPw, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefreshUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3))
                .addGap(15, 15, 15))
        );
        panelUsersLayout.setVerticalGroup(
            panelUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsersLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblUsersInfo)
                .addGap(8, 8, 8)
                .addGroup(panelUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetPw, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        tabbedPane.addTab("Manage Users", panelUsers);

        // ====== TAB 4: BACKUP & RESTORE ======
        lblBackupTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblBackupTitle.setText("Database Backup & Restore");

        lblBackupInfo.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblBackupInfo.setText("<html>Backup exports all data to a SQL file.<br>Restore will replace all current data with the backup file.</html>");

        btnBackup.setBackground(new java.awt.Color(0, 123, 255));
        btnBackup.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnBackup.setForeground(new java.awt.Color(255, 255, 255));
        btnBackup.setText("BACKUP DATABASE");
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doBackup();
            }
        });

        btnRestore.setBackground(new java.awt.Color(255, 193, 7));
        btnRestore.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnRestore.setForeground(new java.awt.Color(0, 0, 0));
        btnRestore.setText("RESTORE DATABASE");
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doRestore();
            }
        });

        javax.swing.GroupLayout panelBackupLayout = new javax.swing.GroupLayout(panelBackup);
        panelBackup.setLayout(panelBackupLayout);
        panelBackupLayout.setHorizontalGroup(
            panelBackupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBackupLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(panelBackupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBackupTitle)
                    .addComponent(lblBackupInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBackupLayout.setVerticalGroup(
            panelBackupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBackupLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(lblBackupTitle)
                .addGap(15, 15, 15)
                .addComponent(lblBackupInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Backup & Restore", panelBackup);

        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                int i = tabbedPane.getSelectedIndex();
                if (i == 0) loadItems();
                else if (i == 1) loadClaims();
                else if (i == 2) loadUsers();
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
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        setPreferredSize(new java.awt.Dimension(1050, 650));
        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // ==================== DATA LOADING ====================
    private void loadItems() {
        String kw = txtSearch.getText().trim();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            String sql = "SELECT i.id, i.item_name, i.description, i.category, i.type, i.location, " +
                "i.date_reported, u.full_name AS reporter, i.status FROM items i JOIN users u ON i.reported_by=u.id";
            if (!kw.isEmpty()) {
                sql += " WHERE i.item_name LIKE ? OR i.description LIKE ? OR i.category LIKE ? OR i.location LIKE ? OR i.status LIKE ? OR i.type LIKE ?";
            }
            sql += " ORDER BY i.id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!kw.isEmpty()) { String s = "%" + kw + "%"; for (int i = 1; i <= 6; i++) ps.setString(i, s); }
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblItems.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    rs.getString("date_reported"), rs.getString("reporter"), rs.getString("status")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadClaims() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT c.id, i.item_name, i.category, u.full_name AS claimant, c.message, c.status, c.created_at " +
                "FROM claim_requests c JOIN items i ON c.item_id=i.id JOIN users u ON c.requested_by=u.id ORDER BY c.id DESC");
            DefaultTableModel m = (DefaultTableModel) tblClaims.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("category"),
                    rs.getString("claimant"), rs.getString("message"), rs.getString("status"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadUsers() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, full_name, username, role, created_at FROM users ORDER BY id");
            DefaultTableModel m = (DefaultTableModel) tblUsers.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("full_name"),
                    rs.getString("username"), rs.getString("role"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    // ==================== ACTIONS ====================
    private void btnMarkResolvedActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        String type = tblItems.getValueAt(row, 4).toString();
        String status = tblItems.getValueAt(row, 8).toString();
        if (!"Lost".equals(type)) {
            JOptionPane.showMessageDialog(this, "Only Lost items can be marked as Resolved.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return;
        }
        if ("Resolved".equals(status)) {
            JOptionPane.showMessageDialog(this, "This item is already Resolved.", "Info", JOptionPane.INFORMATION_MESSAGE); return;
        }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        String itemName = tblItems.getValueAt(row, 1).toString();
        if (JOptionPane.showConfirmDialog(this,
            "Mark \"" + itemName + "\" as Resolved?\n(This means the lost item has been returned to the owner.)",
            "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE items SET status='Resolved' WHERE id=? AND type='Lost'");
            ps.setInt(1, id); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item marked as Resolved!");
            loadItems(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void btnFindMatchActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a Lost item first!"); return; }
        String type = tblItems.getValueAt(row, 4).toString();
        if (!"Lost".equals(type)) {
            JOptionPane.showMessageDialog(this, "Please select a Lost item to find matches for.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return;
        }
        String category = tblItems.getValueAt(row, 3).toString();
        String location = tblItems.getValueAt(row, 5).toString();
        String lostName = tblItems.getValueAt(row, 1).toString();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT i.id, i.item_name, i.description, i.location, i.date_reported, u.full_name AS reporter " +
                "FROM items i JOIN users u ON i.reported_by=u.id " +
                "WHERE i.type='Found' AND i.status='Open' AND i.category=? ORDER BY i.id DESC");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("Lost Item: \"").append(lostName).append("\" (").append(category).append(")\n");
            sb.append("Location reported: ").append(location).append("\n\n");
            sb.append("=== Possible Matches (Found, same category) ===\n\n");
            int count = 0;
            while (rs.next()) {
                count++;
                sb.append("ID #").append(rs.getInt("id")).append(" — ").append(rs.getString("item_name")).append("\n");
                sb.append("  Description : ").append(rs.getString("description")).append("\n");
                sb.append("  Location    : ").append(rs.getString("location")).append("\n");
                sb.append("  Date Found  : ").append(rs.getString("date_reported")).append("\n");
                sb.append("  Reported By : ").append(rs.getString("reporter")).append("\n\n");
            }
            if (count == 0) sb.append("No open Found items found with category: ").append(category);
            conn.close();

            JTextArea ta = new JTextArea(sb.toString(), 18, 55);
            ta.setEditable(false);
            ta.setFont(new java.awt.Font("Monospaced", 0, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                "Match Results for \"" + lostName + "\"", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) { showError(e); }
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) { loadItems(); }

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Delete this item and all its claim requests?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("DELETE FROM items WHERE id=?");
            ps.setInt(1, id); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item deleted!"); loadItems(); loadClaims(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void doResolveClaim(String decision) {
        int row = tblClaims.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a claim request first!"); return; }
        String currentStatus = tblClaims.getValueAt(row, 5).toString();
        if (!"Pending".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, "This claim has already been resolved.", "Info", JOptionPane.WARNING_MESSAGE); return;
        }
        int claimId = Integer.parseInt(tblClaims.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, decision + " this claim?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE claim_requests SET status=?, resolved_at=NOW() WHERE id=?");
            ps.setString(1, decision); ps.setInt(2, claimId); ps.executeUpdate();

            PreparedStatement getItem = conn.prepareStatement("SELECT item_id FROM claim_requests WHERE id=?");
            getItem.setInt(1, claimId); ResultSet rs = getItem.executeQuery();
            if (rs.next()) {
                int itemId = rs.getInt("item_id");
                String newStatus = "Approved".equals(decision) ? "Claimed" : "Open";
                PreparedStatement upItem = conn.prepareStatement("UPDATE items SET status=? WHERE id=?");
                upItem.setString(1, newStatus); upItem.setInt(2, itemId); upItem.executeUpdate();
                if ("Approved".equals(decision)) {
                    PreparedStatement rejectOthers = conn.prepareStatement(
                        "UPDATE claim_requests SET status='Rejected', resolved_at=NOW() WHERE item_id=? AND id!=? AND status='Pending'");
                    rejectOthers.setInt(1, itemId); rejectOthers.setInt(2, claimId); rejectOthers.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Claim " + decision.toLowerCase() + "!");
            loadClaims(); loadItems(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void btnDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblUsers.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a user first!"); return; }
        int uid = Integer.parseInt(tblUsers.getValueAt(row, 0).toString());
        String role = tblUsers.getValueAt(row, 3).toString();
        if ("Admin".equals(role)) {
            JOptionPane.showMessageDialog(this, "Cannot delete an Admin account!", "Not Allowed", JOptionPane.WARNING_MESSAGE); return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this user and all their data?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            conn.createStatement().executeUpdate("DELETE FROM claim_requests WHERE requested_by=" + uid);
            conn.createStatement().executeUpdate("DELETE cr FROM claim_requests cr JOIN items i ON cr.item_id=i.id WHERE i.reported_by=" + uid);
            conn.createStatement().executeUpdate("DELETE FROM items WHERE reported_by=" + uid);
            conn.createStatement().executeUpdate("DELETE FROM users WHERE id=" + uid);
            JOptionPane.showMessageDialog(this, "User deleted!"); loadUsers(); loadItems(); loadClaims(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void btnResetPwActionPerformed(java.awt.event.ActionEvent evt) {
        int row = tblUsers.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a user first!"); return; }
        int uid = Integer.parseInt(tblUsers.getValueAt(row, 0).toString());
        String newPw = JOptionPane.showInputDialog(this, "Enter new password:", "Reset Password", JOptionPane.QUESTION_MESSAGE);
        if (newPw == null || newPw.trim().isEmpty()) return;
        if (newPw.trim().length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET password=? WHERE id=?");
            ps.setString(1, newPw.trim()); ps.setInt(2, uid); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Password reset successfully!"); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            dispose(); new LoginForm().setVisible(true);
        }
    }

    // ==================== BACKUP & RESTORE ====================
    private void doBackup() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Backup File");
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fc.setSelectedFile(new java.io.File("lf_backup_" + ts + ".sql"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();
        try (Connection conn = DBConnection.getConnection(); PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            if (conn == null) return;
            pw.println("-- Lost and Found Backup " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            pw.println("USE lost_and_found_db;");
            pw.println("DELETE FROM claim_requests;");
            pw.println("DELETE FROM items;");
            pw.println("DELETE FROM users;");
            pw.println();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                pw.printf("INSERT INTO users (id,full_name,username,password,role,created_at) VALUES (%d,'%s','%s','%s','%s','%s');%n",
                    rs.getInt("id"), esc(rs.getString("full_name")), esc(rs.getString("username")),
                    esc(rs.getString("password")), rs.getString("role"), rs.getString("created_at"));
            }
            pw.println();
            rs = conn.createStatement().executeQuery("SELECT * FROM items");
            while (rs.next()) {
                pw.printf("INSERT INTO items (id,item_name,description,category,type,location,date_reported,reported_by,status,created_at) VALUES (%d,'%s','%s','%s','%s','%s','%s',%d,'%s','%s');%n",
                    rs.getInt("id"), esc(rs.getString("item_name")), esc(rs.getString("description")),
                    esc(rs.getString("category")), rs.getString("type"), esc(rs.getString("location")),
                    rs.getString("date_reported"), rs.getInt("reported_by"), rs.getString("status"), rs.getString("created_at"));
            }
            pw.println();
            rs = conn.createStatement().executeQuery("SELECT * FROM claim_requests");
            while (rs.next()) {
                String ra = rs.getString("resolved_at");
                pw.printf("INSERT INTO claim_requests (id,item_id,requested_by,message,status,created_at,resolved_at) VALUES (%d,%d,%d,'%s','%s','%s',%s);%n",
                    rs.getInt("id"), rs.getInt("item_id"), rs.getInt("requested_by"),
                    esc(rs.getString("message")), rs.getString("status"), rs.getString("created_at"),
                    ra == null ? "NULL" : "'" + ra + "'");
            }
            JOptionPane.showMessageDialog(this, "Backup saved to:\n" + file.getAbsolutePath());
            conn.close();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Backup failed:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void doRestore() {
        if (JOptionPane.showConfirmDialog(this, "WARNING: This will REPLACE all current data!\nContinue?",
            "Confirm Restore", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) return;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open Backup File");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();
        try (Connection conn = DBConnection.getConnection(); BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (conn == null) return;
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) continue;
                sb.append(line);
                if (line.endsWith(";")) {
                    String sql = sb.toString(); sb.setLength(0);
                    try { stmt.executeUpdate(sql); } catch (SQLException e) {
                        if (!sql.toUpperCase().startsWith("USE")) throw e;
                    }
                }
            }
            conn.commit();
            JOptionPane.showMessageDialog(this, "Database restored from:\n" + file.getName());
            loadItems(); loadClaims(); loadUsers();
            conn.close();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Restore failed:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private String esc(String s) { return s == null ? "" : s.replace("'", "\\'"); }
    private void showError(SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnMarkResolved;
    private javax.swing.JButton btnFindMatch;
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRefreshClaims;
    private javax.swing.JButton btnRefreshItems;
    private javax.swing.JButton btnRefreshUsers;
    private javax.swing.JButton btnReject;
    private javax.swing.JButton btnResetPw;
    private javax.swing.JButton btnRestore;
    private javax.swing.JButton btnSearch;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBackupInfo;
    private javax.swing.JLabel lblBackupTitle;
    private javax.swing.JLabel lblClaimsInfo;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblItemsInfo;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUsersInfo;
    private javax.swing.JPanel panelBackup;
    private javax.swing.JPanel panelClaims;
    private javax.swing.JPanel panelItems;
    private javax.swing.JPanel panelUsers;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tblClaims;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}