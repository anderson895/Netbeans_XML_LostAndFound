package lostandfound;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends javax.swing.JFrame {

    private int adminId;
    private String adminName;

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
    private static final Color YELLOW      = new Color(255, 193, 7);
    private static final Color PURPLE      = new Color(111, 66, 193);
    private static final Color TEAL        = new Color(23, 162, 184);
    private static final Color TEXT_DARK   = new Color(33, 37, 41);

    // ── Components ──
    private JTabbedPane tabbedPane;
    private JTextField txtSearch;
    private JTable tblItems, tblClaims, tblUsers, tblRecycleBin;

    // ── ASCOT Locations ──
    private static final String[] LOCATIONS = {
        "ASCOT ENTRANCE", "ASCOT EXIT", "ASCOT HOSTEL",
        "Information and Communication Technology Center (ICTC)",
        "Engineering Building", "General Education Buildings",
        "Senator Edgardo Angara Hall"
    };

    public AdminDashboard(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        initComponents();
        setLocationRelativeTo(null);
        loadItems();
        loadClaims();
        loadUsers();
        loadRecycleBin();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard - " + adminName);
        setMinimumSize(new Dimension(950, 600));
        setPreferredSize(new Dimension(1150, 700));

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

        JLabel lblRole = new JLabel("ADMIN PANEL");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblRole.setForeground(new Color(255, 120, 120));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel(adminName);
        lblName.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblName.setForeground(new Color(180, 190, 200));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(lblLogo);
        logoPanel.add(Box.createVerticalStrut(4));
        logoPanel.add(lblRole);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(lblName);

        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep1.setForeground(new Color(60, 75, 95));
        sep1.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Nav buttons ──
        JButton btnSAnalytics = makeSidebarBtn("Analytics");
        JButton btnSItems     = makeSidebarBtn("All Items");
        JButton btnSClaims    = makeSidebarBtn("Claim Requests");
        JButton btnSUsers     = makeSidebarBtn("Manage Users");
        JButton btnSRecycle   = makeSidebarBtn("Recycle Bin");
        JButton btnSBackup    = makeSidebarBtn("Backup & Restore");

        JButton btnLogout = new JButton("LOGOUT");
        UIHelper.styleLogoutButton(btnLogout, RED);

        sidebar.add(logoPanel);
        sidebar.add(sep1);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnSAnalytics);
        sidebar.add(btnSItems);
        sidebar.add(btnSClaims);
        sidebar.add(btnSUsers);
        sidebar.add(btnSRecycle);
        sidebar.add(btnSBackup);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(12));

        // ====== TABBED CONTENT ======
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(BG);
        tabbedPane.addTab("Analytics", buildAnalyticsPanel());
        tabbedPane.addTab("All Items", buildItemsPanel());
        tabbedPane.addTab("Claim Requests", buildClaimsPanel());
        tabbedPane.addTab("Manage Users", buildUsersPanel());
        tabbedPane.addTab("Recycle Bin", buildRecycleBinPanel());
        tabbedPane.addTab("Backup & Restore", buildBackupPanel());

        tabbedPane.addChangeListener(e -> {
            int i = tabbedPane.getSelectedIndex();
            if (i == 0) refreshAnalytics();
            else if (i == 1) loadItems();
            else if (i == 2) loadClaims();
            else if (i == 3) loadUsers();
            else if (i == 4) loadRecycleBin();
        });

        // Sidebar nav
        btnSAnalytics.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        btnSItems.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        btnSClaims.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        btnSUsers.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        btnSRecycle.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        btnSBackup.addActionListener(e -> tabbedPane.setSelectedIndex(5));
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                dispose(); new LoginForm().setVisible(true);
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        pack();
    }

    // ========================= TAB 0: ANALYTICS =========================
    private JPanel analyticsPanel;
    private JLabel lblLostCount, lblFoundCount, lblClaimedCount, lblTotalCount;

    private JPanel buildAnalyticsPanel() {
        analyticsPanel = new JPanel(new BorderLayout(15, 15));
        analyticsPanel.setBackground(BG);
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Dashboard Analytics");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PRIMARY);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(BG);

        lblLostCount    = new JLabel("0", SwingConstants.CENTER);
        lblFoundCount   = new JLabel("0", SwingConstants.CENTER);
        lblClaimedCount = new JLabel("0", SwingConstants.CENTER);
        lblTotalCount   = new JLabel("0", SwingConstants.CENTER);

        cardsPanel.add(makeStatCard("Lost Items",    lblLostCount,    RED));
        cardsPanel.add(makeStatCard("Found Items",   lblFoundCount,   BLUE));
        cardsPanel.add(makeStatCard("Claimed Items", lblClaimedCount, GREEN));
        cardsPanel.add(makeStatCard("Total Items",   lblTotalCount,   PURPLE));

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart((Graphics2D) g);
            }
        };
        chartPanel.setBackground(WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(20, 30, 30, 30)));
        chartPanel.setPreferredSize(new Dimension(0, 320));

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(BG);
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(cardsPanel, BorderLayout.CENTER);

        analyticsPanel.add(topPanel, BorderLayout.NORTH);
        analyticsPanel.add(chartPanel, BorderLayout.CENTER);

        refreshAnalytics();
        return analyticsPanel;
    }

    private int statLost = 0, statFound = 0, statClaimed = 0, statTotal = 0;

    private void refreshAnalytics() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM items WHERE type='Lost'");
            if (rs1.next()) statLost = rs1.getInt(1);
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM items WHERE type='Found'");
            if (rs2.next()) statFound = rs2.getInt(1);
            ResultSet rs3 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM items WHERE status='Claimed'");
            if (rs3.next()) statClaimed = rs3.getInt(1);
            ResultSet rs4 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM items");
            if (rs4.next()) statTotal = rs4.getInt(1);
            conn.close();
        } catch (SQLException e) { /* ignore */ }

        lblLostCount.setText(String.valueOf(statLost));
        lblFoundCount.setText(String.valueOf(statFound));
        lblClaimedCount.setText(String.valueOf(statClaimed));
        lblTotalCount.setText(String.valueOf(statTotal));
        if (analyticsPanel != null) analyticsPanel.repaint();
    }

    private void drawBarChart(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Component chartComp = analyticsPanel.getComponent(1);
        int w = chartComp.getWidth() - 60;
        int h = chartComp.getHeight() - 80;
        int x0 = 50, y0 = 30;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(TEXT_DARK);
        g2.drawString("Item Statistics", x0, y0 - 5);

        int max = Math.max(1, Math.max(statLost, Math.max(statFound, Math.max(statClaimed, statTotal))));
        int[] values = {statLost, statFound, statClaimed, statTotal};
        Color[] colors = {RED, BLUE, GREEN, PURPLE};
        String[] labels = {"Lost", "Found", "Claimed", "Total"};

        int barW = Math.min(80, (w - 60) / 4);
        int gap = (w - barW * 4) / 5;

        for (int i = 0; i < 4; i++) {
            int barH = (int) ((double) values[i] / max * (h - 40));
            int bx = x0 + gap + i * (barW + gap);
            int by = y0 + (h - 40) - barH + 20;

            g2.setColor(colors[i]);
            g2.fillRoundRect(bx, by, barW, barH, 6, 6);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.setColor(TEXT_DARK);
            String val = String.valueOf(values[i]);
            int tw = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, bx + (barW - tw) / 2, by - 5);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.setColor(GRAY);
            tw = g2.getFontMetrics().stringWidth(labels[i]);
            g2.drawString(labels[i], bx + (barW - tw) / 2, y0 + h - 5);
        }
    }

    private JPanel makeStatCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accent);

        card.add(t, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ========================= TAB 1: ALL ITEMS =========================
    private JPanel buildItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Manage all lost and found items. Admin can edit, delete, resolve, and find matches.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        // ── Toolbar with wrapping ──
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        txtSearch = new JTextField(16);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton bSearch   = makeBtn("SEARCH", BLUE);
        JButton bEdit     = makeBtn("EDIT", YELLOW); bEdit.setForeground(Color.BLACK);
        JButton bDelete   = makeBtn("DELETE", RED);
        JButton bResolve  = makeBtn("RESOLVED", TEAL);
        JButton bMatch    = makeBtn("MATCH", PURPLE);
        JButton bAddVerif = makeBtn("VERIFY", new Color(102, 51, 153));
        JButton bRefresh  = makeBtn("REFRESH", GRAY);

        bar.add(txtSearch); bar.add(bSearch); bar.add(bEdit); bar.add(bDelete);
        bar.add(bResolve); bar.add(bMatch); bar.add(bAddVerif); bar.add(bRefresh);

        tblItems = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Description", "Category", "Type", "Location", "Date", "Reported By", "Status", "Verification Q"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblItems.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblItems.setRowHeight(24);
        tblItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblItems.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(info, BorderLayout.NORTH);
        top.add(bar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblItems), BorderLayout.CENTER);

        bSearch.addActionListener(e -> loadItems());
        bRefresh.addActionListener(e -> { txtSearch.setText(""); loadItems(); });
        bDelete.addActionListener(e -> doDeleteItem());
        bResolve.addActionListener(e -> doMarkResolved());
        bMatch.addActionListener(e -> doFindMatch());
        bEdit.addActionListener(e -> doEditItem());
        bAddVerif.addActionListener(e -> doSetVerification());

        return panel;
    }

    // ========================= TAB 2: CLAIMS =========================
    private JPanel buildClaimsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Review and approve/reject user claim requests. Verification answers shown if available.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        JButton bApprove = makeBtn("APPROVE", GREEN);
        JButton bReject  = makeBtn("REJECT", RED);
        JButton bRefresh = makeBtn("REFRESH", GRAY);
        bar.add(bApprove); bar.add(bReject); bar.add(bRefresh);

        tblClaims = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Claim ID", "Item Name", "Category", "Claimant", "Message", "Verif. Answer", "Status", "Date"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblClaims.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblClaims.setRowHeight(24);
        tblClaims.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblClaims.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(info, BorderLayout.NORTH);
        top.add(bar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblClaims), BorderLayout.CENTER);

        bApprove.addActionListener(e -> doResolveClaim("Approved"));
        bReject.addActionListener(e -> doResolveClaim("Rejected"));
        bRefresh.addActionListener(e -> loadClaims());

        return panel;
    }

    // ========================= TAB 3: USERS =========================
    private JPanel buildUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Manage user accounts. Delete or reset passwords.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        JButton bDelete  = makeBtn("DELETE USER", RED);
        JButton bReset   = makeBtn("RESET PASSWORD", YELLOW); bReset.setForeground(Color.BLACK);
        JButton bRefresh = makeBtn("REFRESH", GRAY);
        bar.add(bDelete); bar.add(bReset); bar.add(bRefresh);

        tblUsers = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Full Name", "Student ID", "Role", "Created At"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblUsers.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblUsers.setRowHeight(24);
        tblUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblUsers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(info, BorderLayout.NORTH);
        top.add(bar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        bDelete.addActionListener(e -> doDeleteUser());
        bReset.addActionListener(e -> doResetPassword());
        bRefresh.addActionListener(e -> loadUsers());

        return panel;
    }

    // ========================= TAB 4: RECYCLE BIN =========================
    private JPanel buildRecycleBinPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Deleted items are stored here. You can restore individual entries.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        JButton bRestore  = makeBtn("RESTORE ITEM", GREEN);
        JButton bPermDel  = makeBtn("PERMANENTLY DELETE", RED);
        JButton bRefresh  = makeBtn("REFRESH", GRAY);
        bar.add(bRestore); bar.add(bPermDel); bar.add(bRefresh);

        tblRecycleBin = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Orig. ID", "Item Name", "Category", "Type", "Location", "Date", "Deleted At"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblRecycleBin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblRecycleBin.setRowHeight(24);
        tblRecycleBin.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblRecycleBin.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(info, BorderLayout.NORTH);
        top.add(bar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblRecycleBin), BorderLayout.CENTER);

        bRestore.addActionListener(e -> doRestoreItem());
        bPermDel.addActionListener(e -> doPermanentDelete());
        bRefresh.addActionListener(e -> loadRecycleBin());

        return panel;
    }

    // ========================= TAB 5: BACKUP =========================
    private JPanel buildBackupPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel t = new JLabel("Database Backup & Restore");
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(PRIMARY);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html>Backup exports all data to a SQL file.<br>Restore will replace all current data with the backup file.</html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(TEXT_DARK);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton bBackup  = makeBtn("BACKUP DATABASE", BLUE);
        bBackup.setPreferredSize(new Dimension(250, 45));
        bBackup.setMaximumSize(new Dimension(250, 45));
        bBackup.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton bRestore = makeBtn("RESTORE DATABASE", YELLOW);
        bRestore.setForeground(Color.BLACK);
        bRestore.setPreferredSize(new Dimension(250, 45));
        bRestore.setMaximumSize(new Dimension(250, 45));
        bRestore.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(t); panel.add(Box.createVerticalStrut(15));
        panel.add(desc); panel.add(Box.createVerticalStrut(25));
        panel.add(bBackup); panel.add(Box.createVerticalStrut(15));
        panel.add(bRestore);

        bBackup.addActionListener(e -> doBackup());
        bRestore.addActionListener(e -> doRestore());

        return panel;
    }

    // ========================= DATA LOADING =========================
    private void loadItems() {
        String kw = txtSearch == null ? "" : txtSearch.getText().trim();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            String sql = "SELECT i.id, i.item_name, i.description, i.category, i.type, i.location, " +
                "i.date_reported, u.full_name AS reporter, i.status, i.verification_question " +
                "FROM items i JOIN users u ON i.reported_by=u.id";
            if (!kw.isEmpty())
                sql += " WHERE i.item_name LIKE ? OR i.description LIKE ? OR i.category LIKE ? OR i.location LIKE ? OR i.status LIKE ? OR i.type LIKE ?";
            sql += " ORDER BY i.id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!kw.isEmpty()) { String s = "%" + kw + "%"; for (int i = 1; i <= 6; i++) ps.setString(i, s); }
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblItems.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    rs.getString("date_reported"), rs.getString("reporter"), rs.getString("status"),
                    rs.getString("verification_question") != null ? rs.getString("verification_question") : ""});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadClaims() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT c.id, i.item_name, i.category, u.full_name AS claimant, c.message, " +
                "c.verification_answer, c.status, c.created_at " +
                "FROM claim_requests c JOIN items i ON c.item_id=i.id JOIN users u ON c.requested_by=u.id ORDER BY c.id DESC");
            DefaultTableModel m = (DefaultTableModel) tblClaims.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("category"),
                    rs.getString("claimant"), rs.getString("message"),
                    rs.getString("verification_answer") != null ? rs.getString("verification_answer") : "N/A",
                    rs.getString("status"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadUsers() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, full_name, student_id, role, created_at FROM users ORDER BY id");
            DefaultTableModel m = (DefaultTableModel) tblUsers.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("full_name"),
                    rs.getString("student_id"), rs.getString("role"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadRecycleBin() {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, original_id, item_name, category, type, location, date_reported, deleted_at FROM deleted_items ORDER BY deleted_at DESC");
            DefaultTableModel m = (DefaultTableModel) tblRecycleBin.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getInt("original_id"), rs.getString("item_name"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    rs.getString("date_reported"), rs.getString("deleted_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    // ========================= ITEM ACTIONS =========================
    private void doEditItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        String curName = tblItems.getValueAt(row, 1).toString();
        String curDesc = tblItems.getValueAt(row, 2).toString();
        String curCat  = tblItems.getValueAt(row, 3).toString();
        String curType = tblItems.getValueAt(row, 4).toString();
        String curLoc  = tblItems.getValueAt(row, 5).toString();

        JTextField fName = new JTextField(curName);
        JTextField fDesc = new JTextField(curDesc);
        JComboBox<String> fCat = new JComboBox<>(new String[]{"Electronics","Clothing","Accessories","Documents","Others"});
        fCat.setSelectedItem(curCat);
        JComboBox<String> fType = new JComboBox<>(new String[]{"Lost","Found"});
        fType.setSelectedItem(curType);
        JComboBox<String> fLoc = new JComboBox<>(LOCATIONS);
        fLoc.setSelectedItem(curLoc);

        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.add(new JLabel("Item Name:")); p.add(fName);
        p.add(new JLabel("Description:")); p.add(fDesc);
        p.add(new JLabel("Category:")); p.add(fCat);
        p.add(new JLabel("Type:")); p.add(fType);
        p.add(new JLabel("Location:")); p.add(fLoc);

        if (JOptionPane.showConfirmDialog(this, p, "Edit Item #" + id, JOptionPane.OK_CANCEL_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE items SET item_name=?, description=?, category=?, type=?, location=? WHERE id=?");
            ps.setString(1, fName.getText().trim()); ps.setString(2, fDesc.getText().trim());
            ps.setString(3, fCat.getSelectedItem().toString()); ps.setString(4, fType.getSelectedItem().toString());
            ps.setString(5, fLoc.getSelectedItem().toString()); ps.setInt(6, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item updated!"); loadItems(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void doDeleteItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Move this item to Recycle Bin?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO deleted_items (original_id, item_name, description, category, type, location, date_reported, reported_by, status, verification_question) " +
                "SELECT id, item_name, description, category, type, location, date_reported, reported_by, status, verification_question FROM items WHERE id=?");
            ins.setInt(1, id); ins.executeUpdate();
            conn.createStatement().executeUpdate("DELETE FROM claim_requests WHERE item_id=" + id);
            PreparedStatement del = conn.prepareStatement("DELETE FROM items WHERE id=?");
            del.setInt(1, id); del.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item moved to Recycle Bin!");
            loadItems(); loadClaims(); loadRecycleBin(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void doMarkResolved() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        String type = tblItems.getValueAt(row, 4).toString();
        String status = tblItems.getValueAt(row, 8).toString();
        if (!"Lost".equals(type)) { JOptionPane.showMessageDialog(this, "Only Lost items can be marked as Resolved.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return; }
        if ("Resolved".equals(status)) { JOptionPane.showMessageDialog(this, "Already Resolved."); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Mark as Resolved?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE items SET status='Resolved' WHERE id=? AND type='Lost'");
            ps.setInt(1, id); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item marked as Resolved!"); loadItems(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void doFindMatch() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a Lost item first!"); return; }
        String type = tblItems.getValueAt(row, 4).toString();
        if (!"Lost".equals(type)) { JOptionPane.showMessageDialog(this, "Please select a Lost item.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return; }
        String category = tblItems.getValueAt(row, 3).toString();
        String location = tblItems.getValueAt(row, 5).toString();
        String lostName = tblItems.getValueAt(row, 1).toString();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT i.id, i.item_name, i.description, i.location, i.date_reported, u.full_name AS reporter " +
                "FROM items i JOIN users u ON i.reported_by=u.id WHERE i.type='Found' AND i.status='Open' AND i.category=? ORDER BY i.id DESC");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            sb.append("Lost Item: \"").append(lostName).append("\" (").append(category).append(")\n");
            sb.append("Location: ").append(location).append("\n\n=== Possible Matches (Found, same category) ===\n\n");
            int count = 0;
            while (rs.next()) {
                count++;
                sb.append("ID #").append(rs.getInt("id")).append(" - ").append(rs.getString("item_name")).append("\n");
                sb.append("  Desc     : ").append(rs.getString("description")).append("\n");
                sb.append("  Location : ").append(rs.getString("location")).append("\n");
                sb.append("  Date     : ").append(rs.getString("date_reported")).append("\n");
                sb.append("  By       : ").append(rs.getString("reporter")).append("\n\n");
            }
            if (count == 0) sb.append("No open Found items in category: ").append(category);
            conn.close();
            JTextArea ta = new JTextArea(sb.toString(), 18, 55);
            ta.setEditable(false); ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Match Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) { showError(e); }
    }

    private void doSetVerification() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        String type = tblItems.getValueAt(row, 4).toString();
        if (!"Found".equals(type)) { JOptionPane.showMessageDialog(this, "Verification questions are for Found items (used when users claim).", "Info", JOptionPane.INFORMATION_MESSAGE); return; }
        String q = JOptionPane.showInputDialog(this, "Enter a verification question for claimants:\n(e.g., 'What color is the case?')",
            "Set Verification - Item #" + id, JOptionPane.QUESTION_MESSAGE);
        if (q == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE items SET verification_question=? WHERE id=?");
            ps.setString(1, q.trim()); ps.setInt(2, id); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Verification question set!"); loadItems(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    // ========================= CLAIM ACTIONS =========================
    private void doResolveClaim(String decision) {
        int row = tblClaims.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a claim request first!"); return; }
        String currentStatus = tblClaims.getValueAt(row, 6).toString();
        if (!"Pending".equals(currentStatus)) { JOptionPane.showMessageDialog(this, "Already resolved."); return; }
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
                    PreparedStatement rej = conn.prepareStatement("UPDATE claim_requests SET status='Rejected', resolved_at=NOW() WHERE item_id=? AND id!=? AND status='Pending'");
                    rej.setInt(1, itemId); rej.setInt(2, claimId); rej.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Claim " + decision.toLowerCase() + "!");
            loadClaims(); loadItems(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    // ========================= USER ACTIONS =========================
    private void doDeleteUser() {
        int row = tblUsers.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a user first!"); return; }
        int uid = Integer.parseInt(tblUsers.getValueAt(row, 0).toString());
        String role = tblUsers.getValueAt(row, 3).toString();
        if ("Admin".equals(role)) { JOptionPane.showMessageDialog(this, "Cannot delete an Admin!", "Not Allowed", JOptionPane.WARNING_MESSAGE); return; }
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

    private void doResetPassword() {
        int row = tblUsers.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a user first!"); return; }
        int uid = Integer.parseInt(tblUsers.getValueAt(row, 0).toString());
        String newPw = JOptionPane.showInputDialog(this, "Enter new password:", "Reset Password", JOptionPane.QUESTION_MESSAGE);
        if (newPw == null || newPw.trim().isEmpty()) return;
        if (newPw.trim().length() < 6) { JOptionPane.showMessageDialog(this, "Min 6 characters!", "Error", JOptionPane.ERROR_MESSAGE); return; }
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET password=? WHERE id=?");
            ps.setString(1, newPw.trim()); ps.setInt(2, uid); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Password reset!"); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    // ========================= RECYCLE BIN ACTIONS =========================
    private void doRestoreItem() {
        int row = tblRecycleBin.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item to restore!"); return; }
        int delId = Integer.parseInt(tblRecycleBin.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Restore this item?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by, status, verification_question) " +
                "SELECT item_name, description, category, type, location, date_reported, reported_by, status, verification_question FROM deleted_items WHERE id=?");
            ins.setInt(1, delId); ins.executeUpdate();
            PreparedStatement del = conn.prepareStatement("DELETE FROM deleted_items WHERE id=?");
            del.setInt(1, delId); del.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item restored!"); loadItems(); loadRecycleBin(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void doPermanentDelete() {
        int row = tblRecycleBin.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item!"); return; }
        int delId = Integer.parseInt(tblRecycleBin.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Permanently delete? This cannot be undone.", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("DELETE FROM deleted_items WHERE id=?");
            ps.setInt(1, delId); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Permanently deleted!"); loadRecycleBin(); conn.close();
        } catch (SQLException e) { showError(e); }
    }

    // ========================= BACKUP & RESTORE =========================
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
            pw.println("DELETE FROM deleted_items;");
            pw.println("DELETE FROM items;");
            pw.println("DELETE FROM users;");
            pw.println();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                pw.printf("INSERT INTO users (id,full_name,student_id,password,role,created_at) VALUES (%d,'%s','%s','%s','%s','%s');%n",
                    rs.getInt("id"), esc(rs.getString("full_name")), esc(rs.getString("student_id")),
                    esc(rs.getString("password")), rs.getString("role"), rs.getString("created_at"));
            }
            pw.println();
            rs = conn.createStatement().executeQuery("SELECT * FROM items");
            while (rs.next()) {
                String vq = rs.getString("verification_question");
                pw.printf("INSERT INTO items (id,item_name,description,category,type,location,date_reported,reported_by,status,verification_question,created_at) VALUES (%d,'%s','%s','%s','%s','%s','%s',%d,'%s',%s,'%s');%n",
                    rs.getInt("id"), esc(rs.getString("item_name")), esc(rs.getString("description")),
                    esc(rs.getString("category")), rs.getString("type"), esc(rs.getString("location")),
                    rs.getString("date_reported"), rs.getInt("reported_by"), rs.getString("status"),
                    vq == null ? "NULL" : "'" + esc(vq) + "'", rs.getString("created_at"));
            }
            pw.println();
            rs = conn.createStatement().executeQuery("SELECT * FROM claim_requests");
            while (rs.next()) {
                String ra = rs.getString("resolved_at");
                String va = rs.getString("verification_answer");
                pw.printf("INSERT INTO claim_requests (id,item_id,requested_by,message,verification_answer,status,created_at,resolved_at) VALUES (%d,%d,%d,'%s',%s,'%s','%s',%s);%n",
                    rs.getInt("id"), rs.getInt("item_id"), rs.getInt("requested_by"),
                    esc(rs.getString("message")), va == null ? "NULL" : "'" + esc(va) + "'",
                    rs.getString("status"), rs.getString("created_at"),
                    ra == null ? "NULL" : "'" + ra + "'");
            }
            pw.println();
            rs = conn.createStatement().executeQuery("SELECT * FROM deleted_items");
            while (rs.next()) {
                String vq = rs.getString("verification_question");
                pw.printf("INSERT INTO deleted_items (id,original_id,item_name,description,category,type,location,date_reported,reported_by,status,verification_question,deleted_at) VALUES (%d,%d,'%s','%s','%s','%s','%s','%s',%d,'%s',%s,'%s');%n",
                    rs.getInt("id"), rs.getInt("original_id"), esc(rs.getString("item_name")),
                    esc(rs.getString("description")), esc(rs.getString("category")), rs.getString("type"),
                    esc(rs.getString("location")), rs.getString("date_reported"), rs.getInt("reported_by"),
                    rs.getString("status"), vq == null ? "NULL" : "'" + esc(vq) + "'",
                    rs.getString("deleted_at"));
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
            loadItems(); loadClaims(); loadUsers(); loadRecycleBin();
            conn.close();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Restore failed:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    // ========================= HELPERS =========================
    private String esc(String s) { return s == null ? "" : s.replace("'", "\\'"); }
    private void showError(SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }

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
}