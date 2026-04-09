import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LostAndFoundForm extends JFrame {

    // Input fields
    private JTextField txtItemName, txtDescription, txtLocation, txtDateFound, txtReportedBy, txtSearch;
    private JComboBox<String> cmbCategory, cmbStatus;
    // Table
    private JTable tblItems;
    private DefaultTableModel tableModel;
    // Buttons
    private JButton btnCreate, btnUpdate, btnDelete, btnClear, btnSearch;

    public LostAndFoundForm() {
        setTitle("Lost and Found System");
        setSize(1050, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 250));
        setLayout(null);

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("LOST AND FOUND SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setBounds(0, 15, 1050, 35);
        add(lblTitle);

        // ===== LEFT PANEL (Input Fields) =====
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(20, 65, 380, 500);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        add(leftPanel);

        String[] labels = {"Item Name:", "Description:", "Category:", "Location Found:", "Date Found (YYYY-MM-DD):", "Reported By:", "Status:"};
        int yPos = 15;

        // Item Name
        leftPanel.add(makeLabel(labels[0], 15, yPos));
        yPos += 22;
        txtItemName = makeTextField(15, yPos, 340);
        leftPanel.add(txtItemName);
        yPos += 40;

        // Description
        leftPanel.add(makeLabel(labels[1], 15, yPos));
        yPos += 22;
        txtDescription = makeTextField(15, yPos, 340);
        leftPanel.add(txtDescription);
        yPos += 40;

        // Category
        leftPanel.add(makeLabel(labels[2], 15, yPos));
        yPos += 22;
        cmbCategory = new JComboBox<>(new String[]{"Electronics", "Clothing", "Accessories", "Documents", "Others"});
        cmbCategory.setBounds(15, yPos, 340, 30);
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        leftPanel.add(cmbCategory);
        yPos += 40;

        // Location Found
        leftPanel.add(makeLabel(labels[3], 15, yPos));
        yPos += 22;
        txtLocation = makeTextField(15, yPos, 340);
        leftPanel.add(txtLocation);
        yPos += 40;

        // Date Found
        leftPanel.add(makeLabel(labels[4], 15, yPos));
        yPos += 22;
        txtDateFound = makeTextField(15, yPos, 340);
        leftPanel.add(txtDateFound);
        yPos += 40;

        // Reported By
        leftPanel.add(makeLabel(labels[5], 15, yPos));
        yPos += 22;
        txtReportedBy = makeTextField(15, yPos, 340);
        leftPanel.add(txtReportedBy);
        yPos += 40;

        // Status
        leftPanel.add(makeLabel(labels[6], 15, yPos));
        yPos += 22;
        cmbStatus = new JComboBox<>(new String[]{"Unclaimed", "Claimed"});
        cmbStatus.setBounds(15, yPos, 340, 30);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        leftPanel.add(cmbStatus);
        yPos += 45;

        // Buttons
        btnCreate = makeButton("CREATE", new Color(40, 167, 69), 15, yPos);
        btnUpdate = makeButton("UPDATE", new Color(0, 123, 255), 97, yPos);
        btnDelete = makeButton("DELETE", new Color(220, 53, 69), 179, yPos);
        btnClear  = makeButton("CLEAR",  new Color(108, 117, 125), 261, yPos);
        leftPanel.add(btnCreate);
        leftPanel.add(btnUpdate);
        leftPanel.add(btnDelete);
        leftPanel.add(btnClear);

        // ===== RIGHT PANEL (Search + Table) =====
        // Search
        txtSearch = new JTextField();
        txtSearch.setBounds(420, 70, 470, 32);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        add(txtSearch);

        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(900, 70, 120, 32);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSearch.setBackground(new Color(0, 123, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnSearch);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Item Name", "Description", "Category", "Location", "Date Found", "Reported By", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblItems = new JTable(tableModel);
        tblItems.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblItems.setRowHeight(24);
        tblItems.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblItems.setSelectionBackground(new Color(0, 123, 255));
        tblItems.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblItems);
        scrollPane.setBounds(420, 115, 600, 450);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        add(scrollPane);

        // ===== EVENT LISTENERS =====
        btnCreate.addActionListener(e -> createItem());
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete.addActionListener(e -> deleteItem());
        btnClear.addActionListener(e -> { clearFields(); tblItems.clearSelection(); });
        btnSearch.addActionListener(e -> searchItems());
        tblItems.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { tableClicked(); }
        });

        loadTable();
    }

    // ==================== HELPER: Label ====================
    private JLabel makeLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 340, 20);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(73, 80, 87));
        return lbl;
    }

    // ==================== HELPER: TextField ====================
    private JTextField makeTextField(int x, int y, int w) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, 30);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        return tf;
    }

    // ==================== HELPER: Button ====================
    private JButton makeButton(String text, Color bg, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 78, 32);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==================== LOAD TABLE ====================
    public void loadTable() {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            String sql = "SELECT * FROM items ORDER BY id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("description"),
                    rs.getString("category"),
                    rs.getString("location_found"),
                    rs.getString("date_found"),
                    rs.getString("reported_by"),
                    rs.getString("status")
                });
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data:\n" + e.getMessage());
        }
    }

    // ==================== CLEAR ====================
    public void clearFields() {
        txtItemName.setText("");
        txtDescription.setText("");
        cmbCategory.setSelectedIndex(0);
        txtLocation.setText("");
        txtDateFound.setText("");
        txtReportedBy.setText("");
        cmbStatus.setSelectedIndex(0);
    }

    // ==================== CREATE ====================
    private void createItem() {
        String itemName = txtItemName.getText().trim();
        String description = txtDescription.getText().trim();
        String category = cmbCategory.getSelectedItem().toString();
        String location = txtLocation.getText().trim();
        String dateFound = txtDateFound.getText().trim();
        String reportedBy = txtReportedBy.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();

        if (itemName.isEmpty() || location.isEmpty() || dateFound.isEmpty() || reportedBy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            String sql = "INSERT INTO items (item_name, description, category, location_found, date_found, reported_by, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, itemName);
            pst.setString(2, description);
            pst.setString(3, category);
            pst.setString(4, location);
            pst.setString(5, dateFound);
            pst.setString(6, reportedBy);
            pst.setString(7, status);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item added successfully!");
            clearFields();
            loadTable();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
        }
    }

    // ==================== TABLE CLICK ====================
    private void tableClicked() {
        int row = tblItems.getSelectedRow();
        if (row == -1) return;
        txtItemName.setText(tableModel.getValueAt(row, 1).toString());
        txtDescription.setText(tableModel.getValueAt(row, 2).toString());
        cmbCategory.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        txtLocation.setText(tableModel.getValueAt(row, 4).toString());
        txtDateFound.setText(tableModel.getValueAt(row, 5).toString());
        txtReportedBy.setText(tableModel.getValueAt(row, 6).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
    }

    // ==================== UPDATE ====================
    private void updateItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table first!");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            String sql = "UPDATE items SET item_name=?, description=?, category=?, location_found=?, date_found=?, reported_by=?, status=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtItemName.getText().trim());
            pst.setString(2, txtDescription.getText().trim());
            pst.setString(3, cmbCategory.getSelectedItem().toString());
            pst.setString(4, txtLocation.getText().trim());
            pst.setString(5, txtDateFound.getText().trim());
            pst.setString(6, txtReportedBy.getText().trim());
            pst.setString(7, cmbStatus.getSelectedItem().toString());
            pst.setInt(8, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item updated successfully!");
            clearFields();
            loadTable();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
        }
    }

    // ==================== DELETE ====================
    private void deleteItem() {
        int row = tblItems.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table first!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this item?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            try {
                Connection conn = DBConnection.getConnection();
                if (conn == null) return;
                String sql = "DELETE FROM items WHERE id=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item deleted successfully!");
                clearFields();
                loadTable();
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
            }
        }
    }

    // ==================== SEARCH ====================
    private void searchItems() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadTable(); return; }
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            String sql = "SELECT * FROM items WHERE item_name LIKE ? OR description LIKE ? OR category LIKE ? OR location_found LIKE ? OR reported_by LIKE ? OR status LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            String search = "%" + keyword + "%";
            for (int i = 1; i <= 6; i++) pst.setString(i, search);
            ResultSet rs = pst.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("description"),
                    rs.getString("category"),
                    rs.getString("location_found"),
                    rs.getString("date_found"),
                    rs.getString("reported_by"),
                    rs.getString("status")
                });
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error:\n" + e.getMessage());
        }
    }
}
