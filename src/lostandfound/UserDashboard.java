package lostandfound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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

    // ── Card names ──
    private static final String CARD_REPORT       = "REPORT";
    private static final String CARD_BROWSE_FOUND = "BROWSE_FOUND";
    private static final String CARD_BROWSE_LOST  = "BROWSE_LOST";
    private static final String CARD_CLAIMS      = "CLAIMS";
    private static final String CARD_PROFILE      = "PROFILE";

    // ── Components ──
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JTextField txtItemName, txtDescription, txtSearchFound, txtSearchLost, txtLocation;
    private JComboBox<String> cmbCategory, cmbType;
    private JComboBox<String> cmbYear, cmbMonth, cmbDay, cmbHour, cmbMinute, cmbAmPm;
    private JComboBox<String> cmbFilterCategoryFound, cmbFilterCategoryLost;
    private JTable tblMyReports, tblFoundItems, tblLostItems, tblMyClaims;
    private JButton btnSubmit;
    private JTextField txtProfileCourse, txtProfileYear, txtProfileSection;
    private JPasswordField txtProfileOldPass, txtProfilePass, txtProfileConf;
    private JLabel lblProfileName, lblProfileSid, lblProfilePic;
    private byte[] selectedProfilePic;

    private static final String[] DEFAULT_CATEGORIES = {"Electronics", "Clothing", "Accessories", "Documents", "Add Another..."};
    private final java.util.List<String> categories = new java.util.ArrayList<>(java.util.Arrays.asList(DEFAULT_CATEGORIES));

    public UserDashboard(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        initComponents();
        setLocationRelativeTo(null);
        loadMyReports();
        loadFoundItems();
        loadLostItems();
        loadMyClaims();
        loadProfileData();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User Dashboard - " + userName);
        setMinimumSize(new Dimension(900, 580));
        setPreferredSize(new Dimension(1150, 680));

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

        JButton btnReport      = makeSidebarBtn("Report Item");
        JButton btnBrowseFound = makeSidebarBtn("Browse Found Items");
        JButton btnBrowseLost  = makeSidebarBtn("Browse Lost Items");
        JButton btnClaims      = makeSidebarBtn("My Claims");
        JButton btnProfile     = makeSidebarBtn("Edit Profile");

        JButton btnLogout = new JButton("LOGOUT");
        UIHelper.styleLogoutButton(btnLogout, RED);

        sidebar.add(logoPanel);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnReport);
        sidebar.add(btnBrowseFound);
        sidebar.add(btnBrowseLost);
        sidebar.add(btnClaims);
        sidebar.add(btnProfile);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(12));

        // ====== CONTENT AREA (CardLayout — no top tabs) ======
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(BG);
        cardsPanel.add(buildReportPanel(),      CARD_REPORT);
        cardsPanel.add(buildBrowseFoundPanel(), CARD_BROWSE_FOUND);
        cardsPanel.add(buildBrowseLostPanel(),  CARD_BROWSE_LOST);
        cardsPanel.add(buildClaimsPanel(),      CARD_CLAIMS);
        cardsPanel.add(buildProfilePanel(),     CARD_PROFILE);

        btnReport.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_REPORT); loadMyReports(); });
        btnBrowseFound.addActionListener(e -> {
            if (!ensureUserHasReport("Browse Found Items")) return;
            cardLayout.show(cardsPanel, CARD_BROWSE_FOUND); loadFoundItems();
        });
        btnBrowseLost.addActionListener(e -> {
            if (!ensureUserHasReport("Browse Lost Items")) return;
            cardLayout.show(cardsPanel, CARD_BROWSE_LOST); loadLostItems();
        });
        btnClaims.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_CLAIMS); loadMyClaims(); });
        btnProfile.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_PROFILE); loadProfileData(); });
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

    // Block Browse views unless user has at least one submitted report
    private boolean ensureUserHasReport(String section) {
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return false;
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM items WHERE reported_by=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) count = rs.getInt(1);
            conn.close();
            if (count == 0) {
                JOptionPane.showMessageDialog(this,
                    "You need to submit at least one Lost or Found report before you can access \"" + section + "\".",
                    "Locked", JOptionPane.WARNING_MESSAGE);
                cardLayout.show(cardsPanel, CARD_REPORT);
                return false;
            }
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
            return false;
        }
    }

    // ==================== REPORT ITEM ====================
    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel();
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        form.setPreferredSize(new Dimension(360, 0));
        form.setMinimumSize(new Dimension(310, 0));

        JLabel lblFormTitle = new JLabel("Report a Lost / Found Item");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setForeground(PRIMARY);

        txtItemName = makeField();
        txtDescription = makeField();
        cmbCategory = new JComboBox<>(categories.toArray(new String[0]));
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCategory.addActionListener(e -> handleCategorySelection());
        cmbType = new JComboBox<>(new String[]{"Lost", "Found"});
        cmbType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtLocation = makeField();

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

        // 12-hour with AM/PM
        String[] hours = new String[12];
        for (int i = 0; i < 12; i++) hours[i] = String.format("%02d", i + 1);
        cmbHour = new JComboBox<>(hours);
        int curHour24 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curHour12 = curHour24 % 12; if (curHour12 == 0) curHour12 = 12;
        cmbHour.setSelectedIndex(curHour12 - 1);
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) minutes[i] = String.format("%02d", i);
        cmbMinute = new JComboBox<>(minutes);
        cmbMinute.setSelectedIndex(Calendar.getInstance().get(Calendar.MINUTE));
        cmbAmPm = new JComboBox<>(new String[]{"AM", "PM"});
        cmbAmPm.setSelectedIndex(curHour24 >= 12 ? 1 : 0);

        Font cmbFont = new Font("Segoe UI", Font.PLAIN, 12);
        cmbYear.setFont(cmbFont); cmbMonth.setFont(cmbFont); cmbDay.setFont(cmbFont);
        cmbHour.setFont(cmbFont); cmbMinute.setFont(cmbFont); cmbAmPm.setFont(cmbFont);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        datePanel.setBackground(WHITE);
        datePanel.add(cmbYear); datePanel.add(new JLabel("-")); datePanel.add(cmbMonth); datePanel.add(new JLabel("-")); datePanel.add(cmbDay);

        // Time panel with labels for clarity
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        timePanel.setBackground(WHITE);
        JLabel lblHr = new JLabel("Hour");
        lblHr.setFont(new Font("Segoe UI", Font.PLAIN, 10)); lblHr.setForeground(GRAY);
        JLabel lblMn = new JLabel("Min");
        lblMn.setFont(new Font("Segoe UI", Font.PLAIN, 10)); lblMn.setForeground(GRAY);
        JPanel hrBox = new JPanel(); hrBox.setBackground(WHITE);
        hrBox.setLayout(new BoxLayout(hrBox, BoxLayout.Y_AXIS));
        hrBox.add(lblHr); hrBox.add(cmbHour);
        JPanel mnBox = new JPanel(); mnBox.setBackground(WHITE);
        mnBox.setLayout(new BoxLayout(mnBox, BoxLayout.Y_AXIS));
        mnBox.add(lblMn); mnBox.add(cmbMinute);
        JPanel apBox = new JPanel(); apBox.setBackground(WHITE);
        apBox.setLayout(new BoxLayout(apBox, BoxLayout.Y_AXIS));
        JLabel lblAp = new JLabel("AM / PM");
        lblAp.setFont(new Font("Segoe UI", Font.PLAIN, 10)); lblAp.setForeground(GRAY);
        apBox.add(lblAp); apBox.add(cmbAmPm);
        timePanel.add(hrBox); timePanel.add(new JLabel(":")); timePanel.add(mnBox); timePanel.add(apBox);

        btnSubmit = new JButton("SUBMIT REPORT");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnSubmit, GREEN, WHITE);

        JButton btnClear = new JButton("CLEAR");
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnClear, GRAY, WHITE);

        GroupLayout fl = new GroupLayout(form);
        form.setLayout(fl);
        JLabel l1 = makeLabel("Item Name: *"), l2 = makeLabel("Description:"), l3 = makeLabel("Category:"),
               l4 = makeLabel("Type: *"), l5 = makeLabel("Location: *"), l6 = makeLabel("Date: * (no future dates)"), l7 = makeLabel("Time: * (12hr AM/PM)");

        fl.setHorizontalGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblFormTitle)
            .addComponent(l1).addComponent(txtItemName, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
            .addComponent(l2).addComponent(txtDescription)
            .addComponent(l3).addComponent(cmbCategory, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(l4).addComponent(cmbType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(l5).addComponent(txtLocation)
            .addComponent(l6).addComponent(datePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(l7).addComponent(timePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
            .addComponent(lblFormTitle).addGap(10)
            .addComponent(l1).addGap(3).addComponent(txtItemName, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(l2).addGap(3).addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(l3).addGap(3).addComponent(cmbCategory, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(l4).addGap(3).addComponent(cmbType, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(l5).addGap(3).addComponent(txtLocation, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(l6).addGap(3).addComponent(datePanel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(5)
            .addComponent(l7).addGap(3).addComponent(timePanel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE).addGap(12)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
        );

        JPanel tablePanel = new JPanel(new BorderLayout(0, 5));
        tablePanel.setBackground(BG);
        JLabel lblMy = new JLabel("My Reports:");
        lblMy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMy.setForeground(PRIMARY);

        tblMyReports = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"#", "Item Name", "Description", "Category", "Type", "Location", "Date & Time", "Approval", "Status"}
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

    private void handleCategorySelection() {
        Object sel = cmbCategory.getSelectedItem();
        if (sel == null) return;
        if ("Add Another...".equals(sel)) {
            String input = JOptionPane.showInputDialog(this,
                "Enter a new category name:", "Add Another Category",
                JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) {
                cmbCategory.setSelectedIndex(0);
                return;
            }
            String value = input.trim();
            if (!categories.contains(value)) {
                int idx = categories.size() - 1; // insert before "Add Another..."
                categories.add(idx, value);
                cmbCategory.insertItemAt(value, idx);
            }
            cmbCategory.setSelectedItem(value);
        }
    }

    // ==================== BROWSE FOUND ITEMS ====================
    private JPanel buildBrowseFoundPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblInfo = new JLabel("All found items reported by users. Select an Open one and click REQUEST CLAIM if it's yours.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(GRAY);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(BG);
        txtSearchFound = new JTextField(18);
        txtSearchFound.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cmbFilterCategoryFound = new JComboBox<>(buildFilterCategories());
        cmbFilterCategoryFound.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btnSearch = makeBtn("SEARCH", BLUE);
        JButton btnReset  = makeBtn("RESET", GRAY);
        JButton btnClaim  = makeBtn("REQUEST CLAIM", GREEN);
        topBar.add(new JLabel("Search:")); topBar.add(txtSearchFound);
        topBar.add(new JLabel("Category:")); topBar.add(cmbFilterCategoryFound);
        topBar.add(btnSearch); topBar.add(btnReset); topBar.add(btnClaim);

        // Per revision: only Item Name, Category, Location
        tblFoundItems = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Category", "Location"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblFoundItems.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblFoundItems.setRowHeight(24);
        tblFoundItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblFoundItems.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // Hide the ID column (used internally for actions)
        tblFoundItems.getColumnModel().getColumn(0).setMinWidth(0);
        tblFoundItems.getColumnModel().getColumn(0).setMaxWidth(0);
        tblFoundItems.getColumnModel().getColumn(0).setWidth(0);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(lblInfo, BorderLayout.NORTH);
        top.add(topBar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblFoundItems), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadFoundItems());
        btnReset.addActionListener(e -> { txtSearchFound.setText(""); cmbFilterCategoryFound.setSelectedIndex(0); loadFoundItems(); });
        btnClaim.addActionListener(e -> doRequestClaim());

        return panel;
    }

    // ==================== BROWSE LOST ITEMS ====================
    private JPanel buildBrowseLostPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblInfo = new JLabel("Lost items reported by users — useful for knowing what may have been turned in for you to claim.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(GRAY);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(BG);
        txtSearchLost = new JTextField(18);
        txtSearchLost.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cmbFilterCategoryLost = new JComboBox<>(buildFilterCategories());
        cmbFilterCategoryLost.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btnSearch = makeBtn("SEARCH", BLUE);
        JButton btnReset  = makeBtn("RESET", GRAY);
        topBar.add(new JLabel("Search:")); topBar.add(txtSearchLost);
        topBar.add(new JLabel("Category:")); topBar.add(cmbFilterCategoryLost);
        topBar.add(btnSearch); topBar.add(btnReset);

        tblLostItems = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Category", "Location"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblLostItems.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblLostItems.setRowHeight(24);
        tblLostItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblLostItems.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLostItems.getColumnModel().getColumn(0).setMinWidth(0);
        tblLostItems.getColumnModel().getColumn(0).setMaxWidth(0);
        tblLostItems.getColumnModel().getColumn(0).setWidth(0);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(lblInfo, BorderLayout.NORTH);
        top.add(topBar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblLostItems), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadLostItems());
        btnReset.addActionListener(e -> { txtSearchLost.setText(""); cmbFilterCategoryLost.setSelectedIndex(0); loadLostItems(); });

        return panel;
    }

    // ==================== MY CLAIMS ====================
    private JPanel buildClaimsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblInfo = new JLabel("Your claim requests and their status (Pending / Approved / Rejected). Rejected claims cannot be re-submitted for the same item.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(GRAY);

        tblMyClaims = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Claim ID", "Item Name", "Category", "Status", "Date Requested"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblMyClaims.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblMyClaims.setRowHeight(24);
        tblMyClaims.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblMyClaims.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JButton btnViewImg = makeBtn("VIEW MY PROOF", BLUE);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        bar.setBackground(BG);
        bar.add(btnViewImg);

        JPanel top = new JPanel(new BorderLayout(0, 5));
        top.setBackground(BG);
        top.add(lblInfo, BorderLayout.NORTH);
        top.add(bar, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblMyClaims), BorderLayout.CENTER);

        btnViewImg.addActionListener(e -> doViewMyClaimImage());
        return panel;
    }

    // ==================== EDIT PROFILE ====================
    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel();
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        JLabel lblTitle = new JLabel("Edit Profile");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(PRIMARY);

        JLabel lblHint = new JLabel("Update your profile photo, course, year, section, or change your password. Name and Student ID are read-only.");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(GRAY);

        // Read-only name/student id
        JLabel lblNameLbl = makeLabel("Full Name:");
        lblProfileName = new JLabel("-");
        lblProfileName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel lblSidLbl = makeLabel("Student ID:");
        lblProfileSid = new JLabel("-");
        lblProfileSid.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Profile picture
        JLabel lblPicLbl = makeLabel("Profile Picture:");
        lblProfilePic = new JLabel();
        lblProfilePic.setPreferredSize(new Dimension(110, 110));
        lblProfilePic.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        lblProfilePic.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfilePic.setText("(no photo)");
        JButton btnChangePic = new JButton("CHANGE PHOTO");
        btnChangePic.setFont(new Font("Segoe UI", Font.BOLD, 11));
        UIHelper.styleButton(btnChangePic, BLUE, WHITE);
        btnChangePic.addActionListener(e -> doPickProfilePic());

        JLabel lblCourse = makeLabel("Course:");
        txtProfileCourse = makeField();
        JLabel lblYear   = makeLabel("Year:");
        txtProfileYear   = makeField();
        JLabel lblSection = makeLabel("Section:");
        txtProfileSection = makeField();

        JLabel lblOld = makeLabel("Old Password:");
        txtProfileOldPass = new JPasswordField();
        styleField(txtProfileOldPass);

        JLabel lPw = makeLabel("New Password:");
        txtProfilePass = new JPasswordField();
        styleField(txtProfilePass);
        JLabel lConf = makeLabel("Confirm New Password:");
        txtProfileConf = new JPasswordField();
        styleField(txtProfileConf);

        JLabel lblPwHint = new JLabel("Leave password fields blank if you don't want to change the password.");
        lblPwHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblPwHint.setForeground(GRAY);

        JButton btnSave = new JButton("SAVE CHANGES");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnSave, GREEN, WHITE);

        JButton btnReset = new JButton("RESET");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnReset, GRAY, WHITE);

        GroupLayout fl = new GroupLayout(form);
        form.setLayout(fl);
        fl.setHorizontalGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle)
            .addComponent(lblHint)
            .addGroup(fl.createSequentialGroup()
                .addGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblPicLbl)
                    .addComponent(lblProfilePic, 110, 110, 110)
                    .addComponent(btnChangePic, 130, 130, 130))
                .addGap(20)
                .addGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblNameLbl).addComponent(lblProfileName)
                    .addComponent(lblSidLbl).addComponent(lblProfileSid)))
            .addComponent(lblCourse).addComponent(txtProfileCourse, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(fl.createSequentialGroup()
                .addGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblYear)
                    .addComponent(txtProfileYear, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(15)
                .addGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblSection)
                    .addComponent(txtProfileSection, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
            .addComponent(lblOld).addComponent(txtProfileOldPass)
            .addComponent(lPw).addComponent(txtProfilePass)
            .addComponent(lConf).addComponent(txtProfileConf)
            .addComponent(lblPwHint)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
            .addComponent(lblTitle).addGap(5)
            .addComponent(lblHint).addGap(15)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(fl.createSequentialGroup()
                    .addComponent(lblPicLbl).addGap(4)
                    .addComponent(lblProfilePic, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(5)
                    .addComponent(btnChangePic, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGroup(fl.createSequentialGroup()
                    .addComponent(lblNameLbl).addGap(3).addComponent(lblProfileName).addGap(8)
                    .addComponent(lblSidLbl).addGap(3).addComponent(lblProfileSid)))
            .addGap(15)
            .addComponent(lblCourse).addGap(3).addComponent(txtProfileCourse, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(8)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(fl.createSequentialGroup()
                    .addComponent(lblYear).addGap(3).addComponent(txtProfileYear, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addGroup(fl.createSequentialGroup()
                    .addComponent(lblSection).addGap(3).addComponent(txtProfileSection, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
            .addGap(12)
            .addComponent(lblOld).addGap(3).addComponent(txtProfileOldPass, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(8)
            .addComponent(lPw).addGap(3).addComponent(txtProfilePass, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(8)
            .addComponent(lConf).addGap(3).addComponent(txtProfileConf, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(4)
            .addComponent(lblPwHint).addGap(15)
            .addGroup(fl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
        );

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        wrapper.add(form, gbc);

        panel.add(new JScrollPane(wrapper), BorderLayout.CENTER);

        btnSave.addActionListener(e -> doSaveProfile());
        btnReset.addActionListener(e -> loadProfileData());
        return panel;
    }

    // ==================== DATA LOADING ====================
    private void loadMyReports() {
        if (tblMyReports == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, item_name, description, category, type, location, date_reported, approval_status, status FROM items WHERE reported_by=? ORDER BY id DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblMyReports.getModel(); m.setRowCount(0);
            int idx = 1;
            while (rs.next()) {
                // Update category list with any custom values from DB so they round-trip
                String cat = rs.getString("category");
                if (cat != null && !categories.contains(cat) && !"Add Another...".equals(cat)) {
                    int insertAt = categories.size() - 1;
                    categories.add(insertAt, cat);
                    if (cmbCategory != null) cmbCategory.insertItemAt(cat, insertAt);
                }
                m.addRow(new Object[]{idx++, rs.getString("item_name"), rs.getString("description"),
                    cat, rs.getString("type"), rs.getString("location"),
                    formatDateTime(rs.getString("date_reported")),
                    rs.getString("approval_status"), rs.getString("status")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void loadFoundItems() {
        if (tblFoundItems == null) return;
        String kw = txtSearchFound.getText().trim();
        String cat = (String) cmbFilterCategoryFound.getSelectedItem();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            StringBuilder sql = new StringBuilder(
                "SELECT i.id, i.item_name, i.category, i.location " +
                "FROM items i WHERE i.type='Found' AND i.approval_status='Approved'");
            java.util.List<String> params = new java.util.ArrayList<>();
            if (!kw.isEmpty()) {
                sql.append(" AND (i.item_name LIKE ? OR i.category LIKE ? OR i.location LIKE ?)");
                String s = "%" + kw + "%"; params.add(s); params.add(s); params.add(s);
            }
            if (cat != null && !"All Categories".equals(cat)) {
                sql.append(" AND i.category=?");
                params.add(cat);
            }
            sql.append(" ORDER BY i.id DESC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) ps.setString(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblFoundItems.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"),
                    rs.getString("category"), rs.getString("location")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void loadLostItems() {
        if (tblLostItems == null) return;
        String kw = txtSearchLost.getText().trim();
        String cat = (String) cmbFilterCategoryLost.getSelectedItem();
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            StringBuilder sql = new StringBuilder(
                "SELECT i.id, i.item_name, i.category, i.location " +
                "FROM items i WHERE i.type='Lost' AND i.approval_status='Approved'");
            java.util.List<String> params = new java.util.ArrayList<>();
            if (!kw.isEmpty()) {
                sql.append(" AND (i.item_name LIKE ? OR i.category LIKE ? OR i.location LIKE ?)");
                String s = "%" + kw + "%"; params.add(s); params.add(s); params.add(s);
            }
            if (cat != null && !"All Categories".equals(cat)) {
                sql.append(" AND i.category=?");
                params.add(cat);
            }
            sql.append(" ORDER BY i.id DESC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) ps.setString(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblLostItems.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"),
                    rs.getString("category"), rs.getString("location")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void loadMyClaims() {
        if (tblMyClaims == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT c.id, i.item_name, i.category, c.status, c.created_at " +
                "FROM claim_requests c JOIN items i ON c.item_id=i.id WHERE c.requested_by=? ORDER BY c.id DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblMyClaims.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("category"),
                    rs.getString("status"), rs.getString("created_at")});
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void loadProfileData() {
        if (lblProfileName == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT full_name, student_id, course, year_level, section, profile_picture FROM users WHERE id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblProfileName.setText(rs.getString("full_name"));
                lblProfileSid.setText(rs.getString("student_id"));
                txtProfileCourse.setText(nz(rs.getString("course")));
                txtProfileYear.setText(nz(rs.getString("year_level")));
                txtProfileSection.setText(nz(rs.getString("section")));
                txtProfileOldPass.setText("");
                txtProfilePass.setText("");
                txtProfileConf.setText("");
                byte[] data = rs.getBytes("profile_picture");
                selectedProfilePic = null;
                if (data != null && data.length > 0) {
                    setProfilePicPreview(data);
                } else {
                    lblProfilePic.setIcon(null);
                    lblProfilePic.setText("(no photo)");
                }
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private String nz(String s) { return s == null ? "" : s; }

    private void setProfilePicPreview(byte[] data) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            if (img == null) return;
            Image scaled = img.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
            lblProfilePic.setIcon(new ImageIcon(scaled));
            lblProfilePic.setText("");
        } catch (IOException e) { /* leave preview as-is */ }
    }

    private void doPickProfilePic() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Choose profile picture");
        fc.setFileFilter(new FileNameExtensionFilter("Image files (jpg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp"));
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if (f == null || !f.exists()) return;
        if (f.length() > 4L * 1024 * 1024) {
            JOptionPane.showMessageDialog(this, "Image is too large (max 4 MB).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (FileInputStream in = new FileInputStream(f); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096]; int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            selectedProfilePic = out.toByteArray();
            setProfilePicPreview(selectedProfilePic);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not read image:\n" + ex.getMessage());
        }
    }

    // ==================== ACTIONS ====================
    private void doSubmit() {
        String name = txtItemName.getText().trim();
        String loc  = txtLocation.getText().trim();
        if (name.isEmpty() || loc.isEmpty()) { JOptionPane.showMessageDialog(this, "Fill in all required fields (*)!"); return; }

        Object catSel = cmbCategory.getSelectedItem();
        if (catSel == null || "Add Another...".equals(catSel)) {
            JOptionPane.showMessageDialog(this, "Please choose a category (or add one).", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Build datetime — convert 12hr+AM/PM to 24hr storage
        int hr12 = Integer.parseInt(cmbHour.getSelectedItem().toString());
        boolean pm = "PM".equals(cmbAmPm.getSelectedItem());
        int hr24 = hr12 % 12;
        if (pm) hr24 += 12;
        String dateTime = cmbYear.getSelectedItem() + "-" + cmbMonth.getSelectedItem() + "-" + cmbDay.getSelectedItem()
            + " " + String.format("%02d", hr24) + ":" + cmbMinute.getSelectedItem() + ":00";

        // Block future dates
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date chosen = sdf.parse(dateTime);
            if (chosen.after(new java.util.Date())) {
                JOptionPane.showMessageDialog(this,
                    "You cannot report a date/time in the future.",
                    "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by, approval_status) VALUES (?,?,?,?,?,?,?, 'Pending')");
            ps.setString(1, name); ps.setString(2, txtDescription.getText().trim());
            ps.setString(3, catSel.toString());
            ps.setString(4, cmbType.getSelectedItem().toString());
            ps.setString(5, loc); ps.setString(6, dateTime); ps.setInt(7, userId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,
                "Item report submitted successfully!\n\n" +
                "Status: PENDING\n" +
                "Your report will be reviewed by the admin before it appears in the public list.",
                "Submitted", JOptionPane.INFORMATION_MESSAGE);
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

            // Allow claim requests from other users even when one is already pending.
            // Only block re-submission BY THE SAME USER for the same item.
            PreparedStatement chk = conn.prepareStatement(
                "SELECT status FROM claim_requests WHERE item_id=? AND requested_by=? AND status IN ('Pending','Rejected')");
            chk.setInt(1, itemId); chk.setInt(2, userId);
            ResultSet crs = chk.executeQuery();
            if (crs.next()) {
                String existing = crs.getString("status");
                String msg = "Pending".equals(existing)
                    ? "You already have a pending claim for this item!"
                    : "Your previous claim for this item was Rejected. You cannot submit another request for the same item.";
                JOptionPane.showMessageDialog(this, msg, "Cannot Submit Claim", JOptionPane.WARNING_MESSAGE);
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

            // Choose proof: image OR description
            String[] options = {"Upload Picture", "Write Description", "Cancel"};
            int proofChoice = JOptionPane.showOptionDialog(this,
                "Proof of ownership for \"" + itemName + "\":\n" +
                "Choose how to prove this item is yours.",
                "Proof of Ownership",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            byte[] imgBytes = null;
            String descProof = null;

            if (proofChoice == 0) {
                imgBytes = pickClaimImage(itemName);
                if (imgBytes == null) { conn.close(); return; }
            } else if (proofChoice == 1) {
                JTextArea ta = new JTextArea(6, 40);
                ta.setLineWrap(true); ta.setWrapStyleWord(true);
                int ok = JOptionPane.showConfirmDialog(this,
                    new JScrollPane(ta),
                    "Describe the item (color, marks, brand, contents…)",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (ok != JOptionPane.OK_OPTION) { conn.close(); return; }
                descProof = ta.getText().trim();
                if (descProof.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Description cannot be empty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    conn.close(); return;
                }
            } else {
                conn.close(); return;
            }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO claim_requests (item_id, requested_by, message, description_proof, claim_image, verification_answer) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, itemId); ps.setInt(2, userId);
            ps.setString(3, imgBytes != null ? "[image proof attached]" : "[description proof]");
            ps.setString(4, descProof);
            ps.setBytes(5, imgBytes);
            ps.setString(6, verificationA);
            ps.executeUpdate();

            // NOTE: Do NOT flip item status to "Claim Pending" — keep the item Open so other
            // users can still see and claim it. Admin decides which claim wins.

            showClaimInstructions(itemName);
            loadFoundItems(); loadMyClaims(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void showClaimInstructions(String itemName) {
        String html =
            "<html><div style='width:340px;font-family:Segoe UI;'>" +
            "<h3 style='color:#19376D;margin:0 0 6px 0;'>Claim Submitted</h3>" +
            "<p>Your request for <b>" + itemName + "</b> has been submitted. Please follow these instructions:</p>" +
            "<ul>" +
            "<li>Proceed to the <b>Office of Student Affairs</b> if you found an item, to turn it in.</li>" +
            "<li>You can claim your item at the <b>Office of Student Affairs</b>.</li>" +
            "<li><b>Office Hours:</b> 07:00 AM &ndash; 06:00 PM</li>" +
            "<li><b>Open:</b> Tuesday &ndash; Friday</li>" +
            "<li>For inquiries, please see the <b>front desk</b>.</li>" +
            "<li>Bring a <b>valid school ID</b> as additional proof of identity.</li>" +
            "</ul>" +
            "</div></html>";
        JOptionPane.showMessageDialog(this, new JLabel(html),
            "Next Steps", JOptionPane.INFORMATION_MESSAGE);
    }

    private byte[] pickClaimImage(String itemName) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select photo proving you own \"" + itemName + "\"");
        fc.setFileFilter(new FileNameExtensionFilter("Image files (jpg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp"));
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return null;
        File f = fc.getSelectedFile();
        if (f == null || !f.exists()) return null;
        if (f.length() > 8L * 1024 * 1024) {
            JOptionPane.showMessageDialog(this, "Image is too large (max 8 MB).", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try (FileInputStream in = new FileInputStream(f); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096]; int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            byte[] bytes = out.toByteArray();

            ImageIcon raw = new ImageIcon(bytes);
            Image scaled = raw.getImage().getScaledInstance(280, -1, Image.SCALE_SMOOTH);
            int ok = JOptionPane.showConfirmDialog(this, new JLabel(new ImageIcon(scaled)),
                "Use this image as proof?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok != JOptionPane.OK_OPTION) return null;
            return bytes;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not read image:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void doViewMyClaimImage() {
        int row = tblMyClaims.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a claim first!"); return; }
        int claimId = Integer.parseInt(tblMyClaims.getValueAt(row, 0).toString());
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "SELECT claim_image, description_proof FROM claim_requests WHERE id=? AND requested_by=?");
            ps.setInt(1, claimId); ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] data = rs.getBytes("claim_image");
                String descProof = rs.getString("description_proof");
                if (data != null && data.length > 0) {
                    showImageDialog(data, "Claim Image - #" + claimId);
                } else if (descProof != null && !descProof.isEmpty()) {
                    JTextArea ta = new JTextArea(descProof, 10, 40);
                    ta.setEditable(false); ta.setLineWrap(true); ta.setWrapStyleWord(true);
                    JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                        "Description Proof - Claim #" + claimId, JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No proof attached to this claim.");
                }
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void showImageDialog(byte[] data, String title) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            if (img == null) { JOptionPane.showMessageDialog(this, "Image could not be decoded."); return; }
            int maxW = 600, maxH = 500;
            int w = img.getWidth(), h = img.getHeight();
            double scale = Math.min(1.0, Math.min((double) maxW / w, (double) maxH / h));
            Image scaled = img.getScaledInstance((int)(w * scale), (int)(h * scale), Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(scaled)), title, JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not display image:\n" + ex.getMessage());
        }
    }

    private void doSaveProfile() {
        String course = txtProfileCourse.getText().trim();
        String year   = txtProfileYear.getText().trim();
        String sect   = txtProfileSection.getText().trim();
        String oldPw  = new String(txtProfileOldPass.getPassword()).trim();
        String pw     = new String(txtProfilePass.getPassword()).trim();
        String pwc    = new String(txtProfileConf.getPassword()).trim();

        boolean wantsPwChange = !pw.isEmpty() || !pwc.isEmpty() || !oldPw.isEmpty();
        if (wantsPwChange) {
            if (oldPw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your current (old) password to change it.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a new password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pw.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!pw.equals(pwc)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            if (wantsPwChange) {
                PreparedStatement chk = conn.prepareStatement("SELECT password FROM users WHERE id=?");
                chk.setInt(1, userId);
                ResultSet rs = chk.executeQuery();
                if (!rs.next() || !oldPw.equals(rs.getString("password"))) {
                    JOptionPane.showMessageDialog(this, "Old password is incorrect.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    conn.close(); return;
                }
            }

            // Build dynamic update
            StringBuilder sql = new StringBuilder(
                "UPDATE users SET course=?, year_level=?, section=?");
            java.util.List<Object> params = new java.util.ArrayList<>();
            params.add(course); params.add(year); params.add(sect);
            if (selectedProfilePic != null) {
                sql.append(", profile_picture=?");
                params.add(selectedProfilePic);
            }
            if (wantsPwChange) {
                sql.append(", password=?");
                params.add(pw);
            }
            sql.append(" WHERE id=?");
            params.add(userId);

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof byte[]) ps.setBytes(i + 1, (byte[]) p);
                else if (p instanceof Integer) ps.setInt(i + 1, (Integer) p);
                else ps.setString(i + 1, p == null ? "" : p.toString());
            }
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Profile updated!");
            txtProfileOldPass.setText(""); txtProfilePass.setText(""); txtProfileConf.setText("");
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    private void clearFields() {
        txtItemName.setText(""); txtDescription.setText(""); txtLocation.setText("");
        cmbCategory.setSelectedIndex(0); cmbType.setSelectedIndex(0);
        Calendar cal = Calendar.getInstance();
        cmbYear.setSelectedIndex(0);
        cmbMonth.setSelectedIndex(cal.get(Calendar.MONTH));
        int d = cal.get(Calendar.DAY_OF_MONTH) - 1;
        if (d >= 0 && d < cmbDay.getItemCount()) cmbDay.setSelectedIndex(d);
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        int curHour12 = curHour24 % 12; if (curHour12 == 0) curHour12 = 12;
        cmbHour.setSelectedIndex(curHour12 - 1);
        cmbMinute.setSelectedIndex(cal.get(Calendar.MINUTE));
        cmbAmPm.setSelectedIndex(curHour24 >= 12 ? 1 : 0);
    }

    // ── Helper methods ──
    private String formatDateTime(String raw) {
        if (raw == null) return "";
        if (raw.endsWith(".0")) raw = raw.substring(0, raw.length() - 2);
        return raw;
    }

    private String[] buildFilterCategories() {
        java.util.List<String> values = new java.util.ArrayList<>();
        values.add("All Categories");
        for (String c : categories) {
            if (!"Add Another...".equals(c)) values.add(c);
        }
        return values.toArray(new String[0]);
    }

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
    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }
}
