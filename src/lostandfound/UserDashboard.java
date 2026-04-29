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
    private JComboBox<String> cmbYear, cmbMonth, cmbDay, cmbHour, cmbMinute;
    private JComboBox<String> cmbFilterCategoryFound, cmbFilterCategoryLost;
    private JTable tblMyReports, tblFoundItems, tblLostItems, tblMyClaims;
    private JButton btnSubmit;
    private JTextField txtProfileLast, txtProfileGiven, txtProfileMiddle, txtProfileSid;
    private JPasswordField txtProfilePass, txtProfileConf;

    // Picture chosen for claim (kept transient)
    private byte[] selectedClaimImage;

    private static final String[] CATEGORIES = {"Electronics", "Clothing", "Accessories", "Documents", "Others"};

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
        btnBrowseFound.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_BROWSE_FOUND); loadFoundItems(); });
        btnBrowseLost.addActionListener(e -> { cardLayout.show(cardsPanel, CARD_BROWSE_LOST); loadLostItems(); });
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
        cmbCategory = new JComboBox<>(CATEGORIES);
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) hours[i] = String.format("%02d", i);
        cmbHour = new JComboBox<>(hours);
        cmbHour.setSelectedIndex(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) minutes[i] = String.format("%02d", i);
        cmbMinute = new JComboBox<>(minutes);
        cmbMinute.setSelectedIndex(Calendar.getInstance().get(Calendar.MINUTE));

        Font cmbFont = new Font("Segoe UI", Font.PLAIN, 12);
        cmbYear.setFont(cmbFont); cmbMonth.setFont(cmbFont); cmbDay.setFont(cmbFont);
        cmbHour.setFont(cmbFont); cmbMinute.setFont(cmbFont);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        datePanel.setBackground(WHITE);
        datePanel.add(cmbYear); datePanel.add(new JLabel("-")); datePanel.add(cmbMonth); datePanel.add(new JLabel("-")); datePanel.add(cmbDay);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        timePanel.setBackground(WHITE);
        timePanel.add(cmbHour); timePanel.add(new JLabel(":")); timePanel.add(cmbMinute);

        btnSubmit = new JButton("SUBMIT REPORT");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnSubmit, GREEN, WHITE);

        JButton btnClear = new JButton("CLEAR");
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 12));
        UIHelper.styleButton(btnClear, GRAY, WHITE);

        GroupLayout fl = new GroupLayout(form);
        form.setLayout(fl);
        JLabel l1 = makeLabel("Item Name: *"), l2 = makeLabel("Description:"), l3 = makeLabel("Category:"),
               l4 = makeLabel("Type: *"), l5 = makeLabel("Location: *"), l6 = makeLabel("Date: *"), l7 = makeLabel("Time: *");

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
            .addComponent(l7).addGap(3).addComponent(timePanel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(12)
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
            new String[]{"ID", "Item Name", "Description", "Category", "Type", "Location", "Date & Time", "Status"}
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

        tblFoundItems = new JTable(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Item Name", "Description", "Category", "Location", "Date & Time", "Reported By", "Status"}
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
            new String[]{"ID", "Item Name", "Description", "Category", "Location", "Date & Time", "Reported By", "Status"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        tblLostItems.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblLostItems.setRowHeight(24);
        tblLostItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tblLostItems.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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

        JButton btnViewImg = makeBtn("VIEW MY PROOF IMAGE", BLUE);
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

        JLabel lblHint = new JLabel("Update your name, student ID, or password. Leave password blank to keep current.");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(GRAY);

        JLabel lLast = makeLabel("Last Name: *");
        txtProfileLast = makeField();
        JLabel lGiven = makeLabel("Given Name: *");
        txtProfileGiven = makeField();
        JLabel lMid = makeLabel("Middle Name:");
        txtProfileMiddle = makeField();
        JLabel lMidHint = new JLabel("Please use full middle name (not initials)");
        lMidHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lMidHint.setForeground(GRAY);

        JLabel lSid = makeLabel("Student ID: *");
        txtProfileSid = makeField();
        JLabel lSidHint = new JLabel("Format: 21-01-1155");
        lSidHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lSidHint.setForeground(GRAY);

        JLabel lPw = makeLabel("New Password:");
        txtProfilePass = new JPasswordField();
        txtProfilePass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtProfilePass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        JLabel lConf = makeLabel("Confirm Password:");
        txtProfileConf = new JPasswordField();
        txtProfileConf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtProfileConf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

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
            .addComponent(lLast).addComponent(txtProfileLast, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(lGiven).addComponent(txtProfileGiven)
            .addComponent(lMid).addComponent(txtProfileMiddle).addComponent(lMidHint)
            .addComponent(lSid).addComponent(txtProfileSid).addComponent(lSidHint)
            .addComponent(lPw).addComponent(txtProfilePass)
            .addComponent(lConf).addComponent(txtProfileConf)
            .addGroup(fl.createSequentialGroup()
                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        );
        fl.setVerticalGroup(fl.createSequentialGroup()
            .addComponent(lblTitle).addGap(5)
            .addComponent(lblHint).addGap(15)
            .addComponent(lLast).addGap(3).addComponent(txtProfileLast, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(8)
            .addComponent(lGiven).addGap(3).addComponent(txtProfileGiven, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(8)
            .addComponent(lMid).addGap(3).addComponent(txtProfileMiddle, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(2)
            .addComponent(lMidHint).addGap(8)
            .addComponent(lSid).addGap(3).addComponent(txtProfileSid, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(2)
            .addComponent(lSidHint).addGap(8)
            .addComponent(lPw).addGap(3).addComponent(txtProfilePass, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(8)
            .addComponent(lConf).addGap(3).addComponent(txtProfileConf, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(15)
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
                "SELECT id, item_name, description, category, type, location, date_reported, status FROM items WHERE reported_by=? ORDER BY id DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel m = (DefaultTableModel) tblMyReports.getModel(); m.setRowCount(0);
            while (rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("type"), rs.getString("location"),
                    formatDateTime(rs.getString("date_reported")), rs.getString("status")});
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
                "SELECT i.id, i.item_name, i.description, i.category, i.location, i.date_reported, " +
                "u.full_name AS reporter, i.status FROM items i JOIN users u ON i.reported_by=u.id WHERE i.type='Found'");
            java.util.List<String> params = new java.util.ArrayList<>();
            if (!kw.isEmpty()) {
                sql.append(" AND (i.item_name LIKE ? OR i.description LIKE ? OR i.category LIKE ? OR i.location LIKE ?)");
                String s = "%" + kw + "%"; params.add(s); params.add(s); params.add(s); params.add(s);
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
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("location"), formatDateTime(rs.getString("date_reported")),
                    rs.getString("reporter"), rs.getString("status")});
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
                "SELECT i.id, i.item_name, i.description, i.category, i.location, i.date_reported, " +
                "u.full_name AS reporter, i.status FROM items i JOIN users u ON i.reported_by=u.id WHERE i.type='Lost'");
            java.util.List<String> params = new java.util.ArrayList<>();
            if (!kw.isEmpty()) {
                sql.append(" AND (i.item_name LIKE ? OR i.description LIKE ? OR i.category LIKE ? OR i.location LIKE ?)");
                String s = "%" + kw + "%"; params.add(s); params.add(s); params.add(s); params.add(s);
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
                m.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getString("description"),
                    rs.getString("category"), rs.getString("location"), formatDateTime(rs.getString("date_reported")),
                    rs.getString("reporter"), rs.getString("status")});
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
        if (txtProfileLast == null) return;
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement("SELECT full_name, student_id FROM users WHERE id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String full = rs.getString("full_name");
                String[] parts = full.split(",\\s*");
                txtProfileLast.setText(parts.length > 0 ? parts[0] : "");
                txtProfileGiven.setText(parts.length > 1 ? parts[1] : "");
                txtProfileMiddle.setText(parts.length > 2 ? parts[2] : "");
                txtProfileSid.setText(rs.getString("student_id"));
                txtProfilePass.setText("");
                txtProfileConf.setText("");
            }
            conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
    }

    // ==================== ACTIONS ====================
    private void doSubmit() {
        String name = txtItemName.getText().trim();
        String loc  = txtLocation.getText().trim();
        if (name.isEmpty() || loc.isEmpty()) { JOptionPane.showMessageDialog(this, "Fill in all required fields (*)!"); return; }
        String dateTime = cmbYear.getSelectedItem() + "-" + cmbMonth.getSelectedItem() + "-" + cmbDay.getSelectedItem()
            + " " + cmbHour.getSelectedItem() + ":" + cmbMinute.getSelectedItem() + ":00";

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO items (item_name, description, category, type, location, date_reported, reported_by) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, name); ps.setString(2, txtDescription.getText().trim());
            ps.setString(3, cmbCategory.getSelectedItem().toString());
            ps.setString(4, cmbType.getSelectedItem().toString());
            ps.setString(5, loc); ps.setString(6, dateTime); ps.setInt(7, userId);
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
        String itemStatus = tblFoundItems.getValueAt(row, 7).toString();
        if (!"Open".equals(itemStatus)) {
            JOptionPane.showMessageDialog(this, "This item is no longer Open (status: " + itemStatus + ").",
                "Cannot Claim", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;

            // Block re-claim if user has any Pending or Rejected claim already
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

            // Picture-based proof of ownership
            byte[] imgBytes = pickClaimImage(itemName);
            if (imgBytes == null) { conn.close(); return; }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO claim_requests (item_id, requested_by, message, claim_image, verification_answer) VALUES (?,?,?,?,?)");
            ps.setInt(1, itemId); ps.setInt(2, userId);
            ps.setString(3, "[image proof attached]");
            ps.setBytes(4, imgBytes);
            ps.setString(5, verificationA);
            ps.executeUpdate();

            PreparedStatement up = conn.prepareStatement("UPDATE items SET status='Claim Pending' WHERE id=?");
            up.setInt(1, itemId); up.executeUpdate();

            JOptionPane.showMessageDialog(this, "Claim request submitted with photo proof! Admin will review it.");
            loadFoundItems(); loadMyClaims(); conn.close();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage()); }
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

            // Confirm preview
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
            PreparedStatement ps = conn.prepareStatement("SELECT claim_image FROM claim_requests WHERE id=? AND requested_by=?");
            ps.setInt(1, claimId); ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] data = rs.getBytes("claim_image");
                if (data == null || data.length == 0) {
                    JOptionPane.showMessageDialog(this, "No image attached to this claim.");
                } else {
                    showImageDialog(data, "Claim Image - #" + claimId);
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
        String last = txtProfileLast.getText().trim();
        String given = txtProfileGiven.getText().trim();
        String mid = txtProfileMiddle.getText().trim();
        String sid = txtProfileSid.getText().trim();
        String pw = new String(txtProfilePass.getPassword()).trim();
        String pwc = new String(txtProfileConf.getPassword()).trim();

        if (last.isEmpty() || given.isEmpty() || sid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name, given name, and student ID are required."); return;
        }
        if (!sid.matches("\\d{2}-\\d{2}-\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Student ID must follow format: 21-01-1155", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        if (!pw.isEmpty()) {
            if (pw.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            if (!pw.equals(pwc)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
        }

        String fullName = last + ", " + given;
        if (!mid.isEmpty()) fullName += ", " + mid;

        try {
            Connection conn = DBConnection.getConnection(); if (conn == null) return;
            PreparedStatement chk = conn.prepareStatement("SELECT id FROM users WHERE student_id=? AND id<>?");
            chk.setString(1, sid); chk.setInt(2, userId);
            if (chk.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "That Student ID is already used by another account.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                conn.close(); return;
            }
            if (pw.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement("UPDATE users SET full_name=?, student_id=? WHERE id=?");
                ps.setString(1, fullName); ps.setString(2, sid); ps.setInt(3, userId); ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement("UPDATE users SET full_name=?, student_id=?, password=? WHERE id=?");
                ps.setString(1, fullName); ps.setString(2, sid); ps.setString(3, pw); ps.setInt(4, userId); ps.executeUpdate();
            }
            this.userName = fullName;
            setTitle("User Dashboard - " + userName);
            JOptionPane.showMessageDialog(this, "Profile updated!");
            txtProfilePass.setText(""); txtProfileConf.setText("");
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
        cmbHour.setSelectedIndex(cal.get(Calendar.HOUR_OF_DAY));
        cmbMinute.setSelectedIndex(cal.get(Calendar.MINUTE));
    }

    // ── Helper methods ──
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
