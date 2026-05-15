package lostandfound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
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

    // ── Card names ──
    private static final String CARD_ANALYTICS = "ANALYTICS";
    private static final String CARD_ITEMS     = "ITEMS";
    private static final String CARD_CLAIMS    = "CLAIMS";
    private static final String CARD_USERS     = "USERS";
    private static final String CARD_RECYCLE   = "RECYCLE";
    private static final String CARD_AUDIT     = "AUDIT";
    private static final String CARD_BACKUP    = "BACKUP";

    // ── Components ──
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JTextField txtSearch;
    private JComboBox<String> cmbFilterType, cmbFilterCategory, cmbFilterApproval;
    private JTable tblItems, tblClaims, tblUsers, tblRecycleBin, tblAudit;

    // Analytics filter components
    private JComboBox<String> cmbAnalyticsType;
    private JTextField txtAnalyticsFrom, txtAnalyticsTo;

    // Backup format magic header
    private static final String ENC_MAGIC = "LFENC1:";

    private static final String[] CATEGORIES = {"Electronics", "Clothing", "Accessories", "Documents", "Others"};

    public AdminDashboard(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        initComponents();
        setLocationRelativeTo(null);
        loadItems();
        loadClaims();
        loadUsers();
        loadRecycleBin();
        loadAuditTrail();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard - " + adminName);
        setMinimumSize(new Dimension(950, 600));
        setPreferredSize(new Dimension(1200, 720));

        // ====== SIDEBAR ======
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setMinimumSize(new Dimension(210, 0));
        sidebar.setMaximumSize(new Dimension(230, Integer.MAX_VALUE));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

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

        JButton btnSAnalytics = makeSidebarBtn("Analytics");
        JButton btnSItems     = makeSidebarBtn("All Items");
        JButton btnSClaims    = makeSidebarBtn("Claim Requests");
        JButton btnSUsers     = makeSidebarBtn("Manage Users");
        JButton btnSRecycle   = makeSidebarBtn("Recycle Bin");
        JButton btnSAudit     = makeSidebarBtn("Audit Trail");
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
        sidebar.add(btnSAudit);
        sidebar.add(btnSBackup);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(12));

        // ====== CARDS (no JTabbedPane → no top tabs) ======
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(BG);
        cardsPanel.add(buildAnalyticsPanel(),  CARD_ANALYTICS);
        cardsPanel.add(buildItemsPanel(),      CARD_ITEMS);
        cardsPanel.add(buildClaimsPanel(),     CARD_CLAIMS);
        cardsPanel.add(buildUsersPanel(),      CARD_USERS);
        cardsPanel.add(buildRecycleBinPanel(), CARD_RECYCLE);
        cardsPanel.add(buildAuditTrailPanel(), CARD_AUDIT);
        cardsPanel.add(buildBackupPanel(),     CARD_BACKUP);

        btnSAnalytics.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_ANALYTICS); refreshAnalytics(); });
        btnSItems.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_ITEMS); loadItems(); });
        btnSClaims.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_CLAIMS); loadClaims(); });
        btnSUsers.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_USERS); loadUsers(); });
        btnSRecycle.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_RECYCLE); loadRecycleBin(); });
        btnSAudit.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_AUDIT); loadAuditTrail(); });
        btnSBackup.addActionListener(e -> cardLayout.show(cardsPanel, CARD_BACKUP));
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                dispose(); new LoginForm().setVisible(true);
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(cardsPanel, BorderLayout.CENTER);
        pack();
    }

    // ========================= ANALYTICS =========================
    private JPanel analyticsPanel;
    private JPanel chartPanel;
    private JLabel lblLostCount, lblFoundCount, lblClaimedCount, lblTotalCount;

    private JPanel buildAnalyticsPanel() {
        analyticsPanel = new JPanel(new BorderLayout(15, 15));
        analyticsPanel.setBackground(BG);
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Dashboard Analytics");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PRIMARY);

        // Filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBar.setBackground(BG);

        cmbAnalyticsType = new JComboBox<>(new String[]{"All Types", "Lost", "Found"});
        cmbAnalyticsType.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtAnalyticsFrom = new JTextField(10);
        txtAnalyticsFrom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAnalyticsFrom.setToolTipText("From date: yyyy-MM-dd (leave blank for no lower bound)");

        txtAnalyticsTo = new JTextField(10);
        txtAnalyticsTo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAnalyticsTo.setToolTipText("To date: yyyy-MM-dd (leave blank for no upper bound)");

        JButton btnApply = makeBtn("APPLY", BLUE);
        JButton btnReset = makeBtn("RESET", GRAY);

        filterBar.add(new JLabel("Type:")); filterBar.add(cmbAnalyticsType);
        filterBar.add(new JLabel("From:")); filterBar.add(txtAnalyticsFrom);
        filterBar.add(new JLabel("To:"));   filterBar.add(txtAnalyticsTo);
        filterBar.add(btnApply); filterBar.add(btnReset);

        btnApply.addActionListener(e -> refreshAnalytics());
        btnReset.addActionListener(e -> {
            cmbAnalyticsType.setSelectedIndex(0);
            txtAnalyticsFrom.setText("");
            txtAnalyticsTo.setText("");
            refreshAnalytics();
        });

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setBackground(BG);

        lblLostCount    = new JLabel("0", SwingConstants.CENTER);
        lblFoundCount   = new JLabel("0", SwingConstants.CENTER);
        lblClaimedCount = new JLabel("0", SwingConstants.CENTER);
        lblTotalCount   = new JLabel("0", SwingConstants.CENTER);

        cards.add(makeStatCard("Lost Items",    lblLostCount,    RED));
        cards.add(makeStatCard("Found Items",   lblFoundCount,   BLUE));
        cards.add(makeStatCard("Claimed Items", lblClaimedCount, GREEN));
        cards.add(makeStatCard("Total Items",   lblTotalCount,   PURPLE));

        chartPanel = new JPanel() {
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

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(BG);
        JPanel headerRow = new JPanel(new BorderLayout(10, 0));
        headerRow.setBackground(BG);
        headerRow.add(lblTitle, BorderLayout.WEST);
        headerRow.add(filterBar, BorderLayout.EAST);
        topPanel.add(headerRow, BorderLayout.NORTH);
        topPanel.add(cards, BorderLayout.CENTER);

        analyticsPanel.add(topPanel, BorderLayout.NORTH);
        analyticsPanel.add(chartPanel, BorderLayout.CENTER);

        refreshAnalytics();
        return analyticsPanel;
    }

    private int statLost = 0, statFound = 0, statClaimed = 0, statTotal = 0;

    private void refreshAnalytics() {
        String typeFilter = cmbAnalyticsType == null ? "All Types" : (String) cmbAnalyticsType.getSelectedItem();
        String from = txtAnalyticsFrom == null ? "" : txtAnalyticsFrom.getText().trim();
        String to   = txtAnalyticsTo   == null ? "" : txtAnalyticsTo.getText().trim();

        if (!from.isEmpty() && !from.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "From date must be yyyy-MM-dd",
                "Filter Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!to.isEmpty() && !to.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "To date must be yyyy-MM-dd",
                "Filter Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;

            statLost    = countItems(conn, "Lost",    from, to, typeFilter);
            statFound   = countItems(conn, "Found",   from, to, typeFilter);
            statClaimed = countClaimed(conn, from, to, typeFilter);
            statTotal   = statLost + statFound;

            conn.close();
        } catch (SQLException e) { /* ignore */ }

        lblLostCount.setText(String.valueOf(statLost));
        lblFoundCount.setText(String.valueOf(statFound));
        lblClaimedCount.setText(String.valueOf(statClaimed));
        lblTotalCount.setText(String.valueOf(statTotal));
        if (chartPanel != null) chartPanel.repaint();
    }

    private int countItems(Connection conn, String type, String from, String to, String typeFilter) throws SQLException {
        if (!"All Types".equals(typeFilter) && !typeFilter.equals(type)) return 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM items WHERE type=?");
        java.util.List<String> params = new java.util.ArrayList<>();
        params.add(type);
        if (!from.isEmpty()) { sql.append(" AND date_reported >= ?"); params.add(from + " 00:00:00"); }
        if (!to.isEmpty())   { sql.append(" AND date_reported <= ?"); params.add(to + " 23:59:59"); }
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) ps.setString(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    private int countClaimed(Connection conn, String from, String to, String typeFilter) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM items WHERE status='Claimed'");
        java.util.List<String> params = new java.util.ArrayList<>();
        if (!"All Types".equals(typeFilter)) { sql.append(" AND type=?"); params.add(typeFilter); }
        if (!from.isEmpty()) { sql.append(" AND date_reported >= ?"); params.add(from + " 00:00:00"); }
        if (!to.isEmpty())   { sql.append(" AND date_reported <= ?"); params.add(to + " 23:59:59"); }
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) ps.setString(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    private void drawBarChart(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = chartPanel.getWidth() - 60;
        int h = chartPanel.getHeight() - 80;
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

    // ========================= ITEMS =========================
    private JPanel buildItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Manage all lost and found items. Approve/Reject pending user reports, filter, edit, delete, resolve, or find matches.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        txtSearch = new JTextField(12);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cmbFilterType = new JComboBox<>(new String[]{"All Types", "Lost", "Found"});
        cmbFilterType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFilterCategory = new JComboBox<>(buildFilterCategories());
        cmbFilterCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFilterApproval = new JComboBox<>(new String[]{"All Approvals", "Pending", "Approved", "Rejected"});
        cmbFilterApproval.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton bSearch   = makeBtn("SEARCH", BLUE);
        JButton bApprove  = makeBtn("APPROVE", GREEN);
        JButton bRejReport= makeBtn("REJECT", RED);
        JButton bEdit     = makeBtn("EDIT", YELLOW); bEdit.setForeground(Color.BLACK);
        JButton bDelete   = makeBtn("DELETE", RED);
        JButton bResolve  = makeBtn("RESOLVED", TEAL);
        JButton bMatch    = makeBtn("MATCH", PURPLE);
        JButton bAddVerif = makeBtn("VERIFY", new Color(102, 51, 153));
        JButton bRefresh  = makeBtn("REFRESH", GRAY);

        bar.add(new JLabel("Search:")); bar.add(txtSearch);
        bar.add(new JLabel("Type:")); bar.add(cmbFilterType);
        bar.add(new JLabel("Category:")); bar.add(cmbFilterCategory);
        bar.add(new JLabel("Approval:")); bar.add(cmbFilterApproval);
        bar.add(bSearch); bar.add(bApprove); bar.add(bRejReport);
        bar.add(bEdit); bar.add(bDelete);
        bar.add(bResolve); bar.add(bMatch); bar.add(bAddVerif); bar.add(bRefresh);

        tblItems = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Description", "Category", "Type", "Location", "Date & Time", "Reported By", "Approval", "Status", "Verification Q"}
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
        bRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cmbFilterType.setSelectedIndex(0);
            cmbFilterCategory.setSelectedIndex(0);
            cmbFilterApproval.setSelectedIndex(0);
            loadItems();
        });
        bApprove.addActionListener(e -> doApproveItem());
        bRejReport.addActionListener(e -> doRejectItem());
        bDelete.addActionListener(e -> doDeleteItem());
        bResolve.addActionListener(e -> doMarkResolved());
        bMatch.addActionListener(e -> doFindMatch());
        bEdit.addActionListener(e -> doEditItem());
        bAddVerif.addActionListener(e -> doSetVerification());

        return panel;
    }

    // ========================= CLAIMS =========================
    private JPanel buildClaimsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Review and approve/reject user claim requests. View image or description proof attached by claimant.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        JButton bView    = makeBtn("VIEW PROOF", BLUE);
        JButton bApprove = makeBtn("APPROVE", GREEN);
        JButton bReject  = makeBtn("REJECT", RED);
        JButton bRefresh = makeBtn("REFRESH", GRAY);
        bar.add(bView); bar.add(bApprove); bar.add(bReject); bar.add(bRefresh);

        tblClaims = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Claim ID", "Item Name", "Category", "Claimant", "Verif. Answer", "Proof Type", "Status", "Date"}
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

        bView.addActionListener(e -> doViewClaimProof());
        bApprove.addActionListener(e -> doResolveClaim("Approved"));
        bReject.addActionListener(e -> doResolveClaim("Rejected"));
        bRefresh.addActionListener(e -> loadClaims());

        return panel;
    }

    // ========================= USERS =========================
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
            new String[]{"ID", "Full Name", "Student ID", "Role", "Course", "Year", "Section", "Created At"}
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

    // ========================= RECYCLE BIN =========================
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
            new String[]{"ID", "Orig. ID", "Item Name", "Category", "Type", "Location", "Date & Time", "Deleted At"}
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

    // ========================= AUDIT TRAIL =========================
    private JPanel buildAuditTrailPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("Chronological log of administrative changes — who did what and when.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(GRAY);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        JButton bRefresh = makeBtn("REFRESH", GRAY);
        JButton bClear   = makeBtn("CLEAR ALL", RED);
        bar.add(bRefresh); bar.add(bClear);

        tblAudit = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Date & Time", "Admin", "Action", "Target", "Details"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblAudit.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblAudit.setRowHeight(24);
        tblAudit.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblAudit.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(info, BorderLayout.NORTH);
        top.add(bar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblAudit), BorderLayout.CENTER);

        bRefresh.addActionListener(e -> loadAuditTrail());
        bClear.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this,
                "Clear the entire audit trail? This cannot be undone.",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) return;
            try {
                Connection conn = DBConnection.getConnection(); if (conn == null) return;
                conn.createStatement().executeUpdate("DELETE FROM audit_trail");
                conn.close();
                loadAuditTrail();
            } catch (SQLException ex) { showError(ex); }
        });

        return panel;
    }

    // ========================= BACKUP =========================
    private JPanel buildBackupPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel t = new JLabel("Database Backup & Restore");
        t.setFont(new Font("Segoe UI", Font.BOLD, 28));
        t.setForeground(PRIMARY);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><div style='text-align:center;font-size:14pt;'>" +
            "Backup includes ALL data (records and images) and is encrypted with a password.<br>" +
            "Restore will replace all current data with the contents of the backup file." +
            "</div></html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        desc.setForeground(TEXT_DARK);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bBackup  = makeBtn("BACKUP DATABASE", BLUE);
        bBackup.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bBackup.setPreferredSize(new Dimension(320, 60));
        bBackup.setMaximumSize(new Dimension(320, 60));
        bBackup.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bRestore = makeBtn("RESTORE DATABASE", YELLOW);
        bRestore.setForeground(Color.BLACK);
        bRestore.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bRestore.setPreferredSize(new Dimension(320, 60));
        bRestore.setMaximumSize(new Dimension(320, 60));
        bRestore.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(t); panel.add(Box.createVerticalStrut(20));
        panel.add(desc); panel.add(Box.createVerticalStrut(35));
        panel.add(bBackup); panel.add(Box.createVerticalStrut(20));
        panel.add(bRestore);
        panel.add(Box.createVerticalGlue());

        bBackup.addActionListener(e -> doBackup());
        bRestore.addActionListener(e -> doRestore());

        return panel;
    }

    // ========================= DATA LOADING =========================
    private void loadItems() {
        if (tblItems == null) return;
        String kw = txtSearch == null ? "" : txtSearch.getText().trim();
        String filterType = cmbFilterType == null ? "All Types" : (String) cmbFilterType.getSelectedItem();
        String filterCat  = cmbFilterCategory == null ? "All Categories" : (String) cmbFilterCategory.getSelectedItem();
        String filterAppr = cmbFilterApproval == null ? "All Approvals" : (String) cmbFilterApproval.getSelectedItem();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            StringBuilder sql = new StringBuilder(
                "SELECT i.id, i.item_name, i.description, i.category, i.type, i.location, " +
                "i.date_reported, u.full_name AS reporter, i.approval_status, i.status, i.verification_question " +
                "FROM items i JOIN users u ON i.reported_by=u.id WHERE 1=1");
            java.util.List<String> params = new java.util.ArrayList<>();
            if (!kw.isEmpty()) {
                sql.append(" AND (i.item_name LIKE ? OR i.description LIKE ? OR i.category LIKE ? OR i.location LIKE ? OR i.status LIKE ?)");
                String s = "%" + kw + "%"; for (int j = 0; j < 5; j++) params.add(s);
            }
            if (!"All Types".equals(filterType)) {
                sql.append(" AND i.type=?"); params.add(filterType);
            }
            if (!"All Categories".equals(filterCat)) {
                sql.append(" AND i.category=?"); params.add(filterCat);
            }
            if (!"All Approvals".equals(filterAppr)) {
                sql.append(" AND i.approval_status=?"); params.add(filterAppr);
            }
            sql.append(" ORDER BY i.id DESC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int j = 0; j < params.size(); j++) ps.setString(j + 1, params.get(j));
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblItems.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    formatDateTime(rs.getString("date_reported")), rs.getString("reporter"),
                    rs.getString("approval_status"), rs.getString("status"),
                    rs.getString("verification_question") != null ? rs.getString("verification_question") : ""});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadClaims() {
        if (tblClaims == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT c.id, i.item_name, i.category, u.full_name AS claimant, " +
                "c.verification_answer, c.claim_image, c.description_proof, c.status, c.created_at " +
                "FROM claim_requests c JOIN items i ON c.item_id=i.id JOIN users u ON c.requested_by=u.id ORDER BY c.id DESC");
            DefaultTableModel m = (DefaultTableModel) tblClaims.getModel(); m.setRowCount(0);
            while (rs.next()) {
                byte[] img = rs.getBytes("claim_image");
                String desc = rs.getString("description_proof");
                String proofType = img != null && img.length > 0
                    ? "Image"
                    : (desc != null && !desc.isEmpty() ? "Description" : "None");
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("category"),
                    rs.getString("claimant"),
                    rs.getString("verification_answer") != null ? rs.getString("verification_answer") : "N/A",
                    proofType,
                    rs.getString("status"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadUsers() {
        if (tblUsers == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, full_name, student_id, role, course, year_level, section, created_at FROM users ORDER BY id");
            DefaultTableModel m = (DefaultTableModel) tblUsers.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("full_name"),
                    rs.getString("student_id"), rs.getString("role"),
                    rs.getString("course"), rs.getString("year_level"), rs.getString("section"),
                    rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadRecycleBin() {
        if (tblRecycleBin == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, original_id, item_name, category, type, location, date_reported, deleted_at FROM deleted_items ORDER BY deleted_at DESC");
            DefaultTableModel m = (DefaultTableModel) tblRecycleBin.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getInt("original_id"), rs.getString("item_name"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    formatDateTime(rs.getString("date_reported")), rs.getString("deleted_at")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void loadAuditTrail() {
        if (tblAudit == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, created_at, admin_name, action, target, details FROM audit_trail ORDER BY id DESC");
            DefaultTableModel m = (DefaultTableModel) tblAudit.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), formatDateTime(rs.getString("created_at")),
                    rs.getString("admin_name"), rs.getString("action"),
                    rs.getString("target"), rs.getString("details")});
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void logAudit(String action, String target, String details) {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO audit_trail (admin_id, admin_name, action, target, details) VALUES (?,?,?,?,?)");
            ps.setInt(1, adminId); ps.setString(2, adminName);
            ps.setString(3, action); ps.setString(4, target); ps.setString(5, details);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) { /* never block primary action */ }
    }

    // ========================= ITEM ACTIONS =========================
    private void doApproveItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        String name = tblItems.getValueAt(row, 1).toString();
        String approval = tblItems.getValueAt(row, 8).toString();
        if ("Approved".equals(approval)) { JOptionPane.showMessageDialog(this, "Already Approved."); return; }
        if (JOptionPane.showConfirmDialog(this, "Approve this report?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE items SET approval_status='Approved' WHERE id=?");
            ps.setInt(1, id); ps.executeUpdate();
            conn.close();
            logAudit("APPROVE_REPORT", "items#" + id, name);
            JOptionPane.showMessageDialog(this, "Report approved. It will now appear to users.");
            loadItems();
        } catch (SQLException e) { showError(e); }
    }

    private void doRejectItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        String name = tblItems.getValueAt(row, 1).toString();
        String approval = tblItems.getValueAt(row, 8).toString();
        if ("Rejected".equals(approval)) { JOptionPane.showMessageDialog(this, "Already Rejected."); return; }
        if (JOptionPane.showConfirmDialog(this, "Reject this report?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE items SET approval_status='Rejected' WHERE id=?");
            ps.setInt(1, id); ps.executeUpdate();
            conn.close();
            logAudit("REJECT_REPORT", "items#" + id, name);
            JOptionPane.showMessageDialog(this, "Report rejected.");
            loadItems();
        } catch (SQLException e) { showError(e); }
    }

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
        JComboBox<String> fCat = new JComboBox<>(CATEGORIES);
        fCat.setSelectedItem(curCat);
        JComboBox<String> fType = new JComboBox<>(new String[]{"Lost","Found"});
        fType.setSelectedItem(curType);
        JTextField fLoc = new JTextField(curLoc);

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
            ps.setString(5, fLoc.getText().trim()); ps.setInt(6, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item updated!"); loadItems(); conn.close();
            logAudit("EDIT_ITEM", "items#" + id, fName.getText().trim());
        } catch (SQLException e) { showError(e); }
    }

    private void doDeleteItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        String name = tblItems.getValueAt(row, 1).toString();
        if (JOptionPane.showConfirmDialog(this, "Move this item to Recycle Bin?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO deleted_items (original_id, item_name, description, category, type, location, date_reported, reported_by, status, approval_status, verification_question) " +
                "SELECT id, item_name, description, category, type, location, date_reported, reported_by, status, approval_status, verification_question FROM items WHERE id=?");
            ins.setInt(1, id); ins.executeUpdate();
            conn.createStatement().executeUpdate("DELETE FROM claim_requests WHERE item_id=" + id);
            PreparedStatement del = conn.prepareStatement("DELETE FROM items WHERE id=?");
            del.setInt(1, id); del.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item moved to Recycle Bin!");
            loadItems(); loadClaims(); loadRecycleBin(); conn.close();
            logAudit("DELETE_ITEM", "items#" + id, name);
        } catch (SQLException e) { showError(e); }
    }

    private void doMarkResolved() {
        int row = tblItems.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item first!"); return; }
        String type = tblItems.getValueAt(row, 4).toString();
        String status = tblItems.getValueAt(row, 9).toString();
        if (!"Lost".equals(type)) { JOptionPane.showMessageDialog(this, "Only Lost items can be marked as Resolved.", "Not Allowed", JOptionPane.WARNING_MESSAGE); return; }
        if ("Resolved".equals(status)) { JOptionPane.showMessageDialog(this, "Already Resolved."); return; }
        int id = Integer.parseInt(tblItems.getValueAt(row, 0).toString());
        if (JOptionPane.showConfirmDialog(this, "Mark as Resolved?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("UPDATE items SET status='Resolved' WHERE id=? AND type='Lost'");
            ps.setInt(1, id); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item marked as Resolved!"); loadItems(); conn.close();
            logAudit("RESOLVE_ITEM", "items#" + id, "");
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
                sb.append("  Date     : ").append(formatDateTime(rs.getString("date_reported"))).append("\n");
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
            logAudit("SET_VERIFICATION", "items#" + id, q.trim());
        } catch (SQLException e) { showError(e); }
    }

    // ========================= CLAIM ACTIONS =========================
    private void doViewClaimProof() {
        int row = tblClaims.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a claim first!"); return; }
        int claimId = Integer.parseInt(tblClaims.getValueAt(row, 0).toString());
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT claim_image, description_proof FROM claim_requests WHERE id=?");
            ps.setInt(1, claimId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] data = rs.getBytes("claim_image");
                String desc = rs.getString("description_proof");
                if (data != null && data.length > 0) {
                    showImageDialog(data, "Claim Proof Image - #" + claimId);
                } else if (desc != null && !desc.isEmpty()) {
                    JTextArea ta = new JTextArea(desc, 10, 40);
                    ta.setEditable(false); ta.setLineWrap(true); ta.setWrapStyleWord(true);
                    JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                        "Description Proof - Claim #" + claimId, JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No proof attached to this claim.");
                }
            }
            conn.close();
        } catch (SQLException e) { showError(e); }
    }

    private void showImageDialog(byte[] data, String title) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            if (img == null) { JOptionPane.showMessageDialog(this, "Image could not be decoded."); return; }
            int maxW = 700, maxH = 580;
            int w = img.getWidth(), h = img.getHeight();
            double scale = Math.min(1.0, Math.min((double) maxW / w, (double) maxH / h));
            Image scaled = img.getScaledInstance((int)(w * scale), (int)(h * scale), Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(scaled)), title, JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not display image:\n" + ex.getMessage());
        }
    }

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
                if ("Approved".equals(decision)) {
                    PreparedStatement upItem = conn.prepareStatement("UPDATE items SET status='Claimed' WHERE id=?");
                    upItem.setInt(1, itemId); upItem.executeUpdate();
                    PreparedStatement rej = conn.prepareStatement(
                        "UPDATE claim_requests SET status='Rejected', resolved_at=NOW() WHERE item_id=? AND id!=? AND status='Pending'");
                    rej.setInt(1, itemId); rej.setInt(2, claimId); rej.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Claim " + decision.toLowerCase() + "!");
            loadClaims(); loadItems(); conn.close();
            logAudit(decision.toUpperCase() + "_CLAIM", "claim_requests#" + claimId, "");
        } catch (SQLException e) { showError(e); }
    }

    // ========================= USER ACTIONS =========================
    private void doDeleteUser() {
        int row = tblUsers.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a user first!"); return; }
        int uid = Integer.parseInt(tblUsers.getValueAt(row, 0).toString());
        String role = tblUsers.getValueAt(row, 3).toString();
        String name = tblUsers.getValueAt(row, 1).toString();
        if ("Admin".equals(role)) { JOptionPane.showMessageDialog(this, "Cannot delete an Admin!", "Not Allowed", JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this user and all their data?", "Confirm", JOptionPane.YES_NO_OPTION) != 0) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            conn.createStatement().executeUpdate("DELETE FROM claim_requests WHERE requested_by=" + uid);
            conn.createStatement().executeUpdate("DELETE cr FROM claim_requests cr JOIN items i ON cr.item_id=i.id WHERE i.reported_by=" + uid);
            conn.createStatement().executeUpdate("DELETE FROM items WHERE reported_by=" + uid);
            conn.createStatement().executeUpdate("DELETE FROM users WHERE id=" + uid);
            JOptionPane.showMessageDialog(this, "User deleted!"); loadUsers(); loadItems(); loadClaims(); conn.close();
            logAudit("DELETE_USER", "users#" + uid, name);
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
            logAudit("RESET_PASSWORD", "users#" + uid, "");
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
                "INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by, status, approval_status, verification_question) " +
                "SELECT item_name, description, category, type, location, date_reported, reported_by, status, approval_status, verification_question FROM deleted_items WHERE id=?");
            ins.setInt(1, delId); ins.executeUpdate();
            PreparedStatement del = conn.prepareStatement("DELETE FROM deleted_items WHERE id=?");
            del.setInt(1, delId); del.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item restored!"); loadItems(); loadRecycleBin(); conn.close();
            logAudit("RESTORE_ITEM", "deleted_items#" + delId, "");
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
            logAudit("PERM_DELETE_ITEM", "deleted_items#" + delId, "");
        } catch (SQLException e) { showError(e); }
    }

    // ========================= BACKUP & RESTORE =========================
    private void doBackup() {
        char[] pw = promptPassword("Enter password to ENCRYPT the backup:");
        if (pw == null) return;
        if (pw.length < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        char[] confirm = promptPassword("Confirm password:");
        if (confirm == null) return;
        if (!java.util.Arrays.equals(pw, confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save Encrypted Backup File");
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fc.setSelectedFile(new java.io.File("lf_backup_" + ts + ".lfenc"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            // Build plaintext backup payload (includes images as base64)
            StringWriter sw = new StringWriter();
            PrintWriter pwOut = new PrintWriter(sw);
            pwOut.println("-- Lost and Found Backup " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            pwOut.println("USE lost_and_found_db;");
            pwOut.println("DELETE FROM audit_trail;");
            pwOut.println("DELETE FROM claim_requests;");
            pwOut.println("DELETE FROM deleted_items;");
            pwOut.println("DELETE FROM items;");
            pwOut.println("DELETE FROM users;");
            pwOut.println();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                byte[] pic = rs.getBytes("profile_picture");
                String picSql = pic == null ? "NULL" : "FROM_BASE64('" + Base64.getEncoder().encodeToString(pic) + "')";
                pwOut.printf("INSERT INTO users (id,full_name,student_id,password,role,course,year_level,section,profile_picture,created_at) VALUES (%d,'%s','%s','%s','%s',%s,%s,%s,%s,'%s');%n",
                    rs.getInt("id"), esc(rs.getString("full_name")), esc(rs.getString("student_id")),
                    esc(rs.getString("password")), rs.getString("role"),
                    nullOrQuoted(rs.getString("course")),
                    nullOrQuoted(rs.getString("year_level")),
                    nullOrQuoted(rs.getString("section")),
                    picSql,
                    rs.getString("created_at"));
            }
            pwOut.println();

            rs = conn.createStatement().executeQuery("SELECT * FROM items");
            while (rs.next()) {
                String vq = rs.getString("verification_question");
                pwOut.printf("INSERT INTO items (id,item_name,description,category,type,location,date_reported,reported_by,status,approval_status,verification_question,created_at) VALUES (%d,'%s','%s','%s','%s','%s','%s',%d,'%s','%s',%s,'%s');%n",
                    rs.getInt("id"), esc(rs.getString("item_name")), esc(rs.getString("description")),
                    esc(rs.getString("category")), rs.getString("type"), esc(rs.getString("location")),
                    rs.getString("date_reported"), rs.getInt("reported_by"), rs.getString("status"),
                    rs.getString("approval_status"),
                    vq == null ? "NULL" : "'" + esc(vq) + "'", rs.getString("created_at"));
            }
            pwOut.println();

            rs = conn.createStatement().executeQuery("SELECT * FROM claim_requests");
            while (rs.next()) {
                String ra = rs.getString("resolved_at");
                String va = rs.getString("verification_answer");
                String dp = rs.getString("description_proof");
                byte[] img = rs.getBytes("claim_image");
                String imgSql = img == null ? "NULL" : "FROM_BASE64('" + Base64.getEncoder().encodeToString(img) + "')";
                pwOut.printf("INSERT INTO claim_requests (id,item_id,requested_by,message,description_proof,claim_image,verification_answer,status,created_at,resolved_at) VALUES (%d,%d,%d,'%s',%s,%s,%s,'%s','%s',%s);%n",
                    rs.getInt("id"), rs.getInt("item_id"), rs.getInt("requested_by"),
                    esc(rs.getString("message")),
                    dp == null ? "NULL" : "'" + esc(dp) + "'",
                    imgSql,
                    va == null ? "NULL" : "'" + esc(va) + "'",
                    rs.getString("status"), rs.getString("created_at"),
                    ra == null ? "NULL" : "'" + ra + "'");
            }
            pwOut.println();

            rs = conn.createStatement().executeQuery("SELECT * FROM deleted_items");
            while (rs.next()) {
                String vq = rs.getString("verification_question");
                pwOut.printf("INSERT INTO deleted_items (id,original_id,item_name,description,category,type,location,date_reported,reported_by,status,approval_status,verification_question,deleted_at) VALUES (%d,%d,'%s','%s','%s','%s','%s','%s',%d,'%s','%s',%s,'%s');%n",
                    rs.getInt("id"), rs.getInt("original_id"), esc(rs.getString("item_name")),
                    esc(rs.getString("description")), esc(rs.getString("category")), rs.getString("type"),
                    esc(rs.getString("location")), rs.getString("date_reported"), rs.getInt("reported_by"),
                    rs.getString("status"), rs.getString("approval_status"),
                    vq == null ? "NULL" : "'" + esc(vq) + "'",
                    rs.getString("deleted_at"));
            }
            pwOut.println();

            rs = conn.createStatement().executeQuery("SELECT * FROM audit_trail");
            while (rs.next()) {
                pwOut.printf("INSERT INTO audit_trail (id,admin_id,admin_name,action,target,details,created_at) VALUES (%d,%s,'%s','%s','%s','%s','%s');%n",
                    rs.getInt("id"),
                    rs.getObject("admin_id") == null ? "NULL" : String.valueOf(rs.getInt("admin_id")),
                    esc(rs.getString("admin_name")), esc(rs.getString("action")),
                    esc(rs.getString("target")), esc(rs.getString("details")),
                    rs.getString("created_at"));
            }

            pwOut.flush();
            byte[] plain = sw.toString().getBytes("UTF-8");

            // Encrypt and write
            byte[] encrypted = encrypt(plain, pw);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(ENC_MAGIC.getBytes("UTF-8"));
                fos.write(encrypted);
            }

            JOptionPane.showMessageDialog(this, "Encrypted backup saved to:\n" + file.getAbsolutePath() +
                "\n\nKeep your password safe — without it, this file cannot be restored.");
            logAudit("BACKUP", file.getName(), "encrypted backup");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Backup failed:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            java.util.Arrays.fill(pw, '\0');
            java.util.Arrays.fill(confirm, '\0');
        }
    }

    private void doRestore() {
        if (JOptionPane.showConfirmDialog(this, "WARNING: This will REPLACE all current data!\nContinue?",
            "Confirm Restore", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) return;

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open Backup File");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            byte[] all = readAll(file);
            String sqlText;
            byte[] magic = ENC_MAGIC.getBytes("UTF-8");
            boolean encrypted = all.length >= magic.length;
            for (int i = 0; encrypted && i < magic.length; i++) {
                if (all[i] != magic[i]) encrypted = false;
            }
            if (encrypted) {
                char[] pw = promptPassword("Enter password to DECRYPT the backup:");
                if (pw == null) return;
                byte[] payload = new byte[all.length - magic.length];
                System.arraycopy(all, magic.length, payload, 0, payload.length);
                try {
                    sqlText = new String(decrypt(payload, pw), "UTF-8");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "Decryption failed. Wrong password or corrupted file.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } finally {
                    java.util.Arrays.fill(pw, '\0');
                }
            } else {
                sqlText = new String(all, "UTF-8");
            }

            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            BufferedReader br = new BufferedReader(new StringReader(sqlText));
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
            loadItems(); loadClaims(); loadUsers(); loadRecycleBin(); loadAuditTrail();
            logAudit("RESTORE", file.getName(), encrypted ? "encrypted" : "plain");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Restore failed:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private char[] promptPassword(String message) {
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(this, pf, message,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return null;
        return pf.getPassword();
    }

    // AES-CBC with PBKDF2-derived key. File layout (after ENC_MAGIC):
    // [16-byte salt][16-byte IV][ciphertext...]
    private byte[] encrypt(byte[] plaintext, char[] password) throws Exception {
        java.security.SecureRandom rng = new java.security.SecureRandom();
        byte[] salt = new byte[16]; rng.nextBytes(salt);
        byte[] iv = new byte[16];   rng.nextBytes(iv);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKeySpec key = new SecretKeySpec(kf.generateSecret(spec).getEncoded(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] cipherText = c.doFinal(plaintext);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(salt); out.write(iv); out.write(cipherText);
        return out.toByteArray();
    }

    private byte[] decrypt(byte[] payload, char[] password) throws Exception {
        if (payload.length < 32) throw new IllegalArgumentException("Backup payload too short");
        byte[] salt = new byte[16]; System.arraycopy(payload, 0, salt, 0, 16);
        byte[] iv = new byte[16];   System.arraycopy(payload, 16, iv, 0, 16);
        byte[] cipherText = new byte[payload.length - 32];
        System.arraycopy(payload, 32, cipherText, 0, cipherText.length);
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKeySpec key = new SecretKeySpec(kf.generateSecret(spec).getEncoded(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return c.doFinal(cipherText);
    }

    private byte[] readAll(File f) throws IOException {
        try (FileInputStream in = new FileInputStream(f); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192]; int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            return out.toByteArray();
        }
    }

    private String nullOrQuoted(String s) { return s == null ? "NULL" : "'" + esc(s) + "'"; }

    // ========================= HELPERS =========================
    private String formatDateTime(String raw) {
        if (raw == null) return "";
        if (raw.endsWith(".0")) raw = raw.substring(0, raw.length() - 2);
        return raw;
    }

    private String[] buildFilterCategories() {
        String[] arr = new String[CATEGORIES.length + 1];
        arr[0] = "All Categories";
        System.arraycopy(CATEGORIES, 0, arr, 1, CATEGORIES.length);
        return arr;
    }

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
