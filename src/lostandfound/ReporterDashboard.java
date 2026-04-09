package lostandfound;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReporterDashboard extends javax.swing.JFrame {

    private String loggedUser;

    public ReporterDashboard(String userName) {
        this.loggedUser = userName;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Reporter Dashboard - " + userName);
        lblUser.setText("Reporter: " + userName);
        txtReportedBy.setText(userName);
        loadTable();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeader = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        lblItemName = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        lblDesc = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        lblCategory = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox();
        lblLocation = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        lblDate = new javax.swing.JLabel();
        txtDateFound = new javax.swing.JTextField();
        lblReported = new javax.swing.JLabel();
        txtReportedBy = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reporter Dashboard");
        setResizable(false);

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(0, 123, 255));
        lblHeader.setText("REPORTER DASHBOARD");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("Reporter:");

        btnLogout.setBackground(new java.awt.Color(220, 53, 69));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        lblItemName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblItemName.setText("Item Name: *");

        txtItemName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblDesc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDesc.setText("Description:");

        txtDescription.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblCategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCategory.setText("Category:");

        cmbCategory.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Electronics", "Clothing", "Accessories", "Documents", "Others" }));

        lblLocation.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLocation.setText("Location Found: *");

        txtLocation.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDate.setText("Date Found (YYYY-MM-DD): *");

        txtDateFound.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblReported.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblReported.setText("Reported By:");

        txtReportedBy.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtReportedBy.setEditable(false);

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatus.setText("Status:");

        cmbStatus.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unclaimed", "Claim Pending", "Claimed" }));

        btnCreate.setBackground(new java.awt.Color(40, 167, 69));
        btnCreate.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnCreate.setForeground(new java.awt.Color(255, 255, 255));
        btnCreate.setText("CREATE");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(0, 123, 255));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(220, 53, 69));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(108, 117, 125));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btnSearch.setBackground(new java.awt.Color(0, 123, 255));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("SEARCH");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tblItems.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item Name", "Description", "Category", "Location", "Date Found", "Reported By", "Status", "Claimed By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItems.setRowHeight(24);
        tblItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblItems);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblItemName)
                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDesc)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCategory)
                            .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLocation)
                            .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDate)
                            .addComponent(txtDateFound, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblReported)
                            .addComponent(txtReportedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatus)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 400, Short.MAX_VALUE)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblItemName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDesc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCategory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLocation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateFound, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReported)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReportedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loadTable() {
        try {
            Connection conn = DBConnection.getConnection(); if(conn==null) return;
            DefaultTableModel m = (DefaultTableModel) tblItems.getModel(); m.setRowCount(0);
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM items ORDER BY id DESC");
            while(rs.next()) {
                m.addRow(new Object[]{rs.getInt("id"),rs.getString("item_name"),rs.getString("description"),
                    rs.getString("category"),rs.getString("location_found"),rs.getString("date_found"),
                    rs.getString("reported_by"),rs.getString("status"),
                    rs.getString("claimed_by")==null?"—":rs.getString("claimed_by")});
            }
            conn.close();
        } catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void clearFields() {
        txtItemName.setText(""); txtDescription.setText(""); cmbCategory.setSelectedIndex(0);
        txtLocation.setText(""); txtDateFound.setText(""); cmbStatus.setSelectedIndex(0); tblItems.clearSelection();
    }

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {
        String name=txtItemName.getText().trim(),loc=txtLocation.getText().trim(),date=txtDateFound.getText().trim();
        if(name.isEmpty()||loc.isEmpty()||date.isEmpty()){JOptionPane.showMessageDialog(this,"Fill required fields (*)!");return;}
        try{Connection conn=DBConnection.getConnection();if(conn==null)return;
            PreparedStatement p=conn.prepareStatement("INSERT INTO items(item_name,description,category,location_found,date_found,reported_by,status) VALUES(?,?,?,?,?,?,?)");
            p.setString(1,name);p.setString(2,txtDescription.getText().trim());p.setString(3,cmbCategory.getSelectedItem().toString());
            p.setString(4,loc);p.setString(5,date);p.setString(6,loggedUser);p.setString(7,cmbStatus.getSelectedItem().toString());
            p.executeUpdate();JOptionPane.showMessageDialog(this,"Item reported!");clearFields();loadTable();conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        int r=tblItems.getSelectedRow();if(r==-1){JOptionPane.showMessageDialog(this,"Select an item first!");return;}
        int id=Integer.parseInt(((DefaultTableModel)tblItems.getModel()).getValueAt(r,0).toString());
        try{Connection conn=DBConnection.getConnection();if(conn==null)return;
            PreparedStatement p=conn.prepareStatement("UPDATE items SET item_name=?,description=?,category=?,location_found=?,date_found=?,status=? WHERE id=?");
            p.setString(1,txtItemName.getText().trim());p.setString(2,txtDescription.getText().trim());
            p.setString(3,cmbCategory.getSelectedItem().toString());p.setString(4,txtLocation.getText().trim());
            p.setString(5,txtDateFound.getText().trim());p.setString(6,cmbStatus.getSelectedItem().toString());p.setInt(7,id);
            p.executeUpdate();JOptionPane.showMessageDialog(this,"Item updated!");clearFields();loadTable();conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int r=tblItems.getSelectedRow();if(r==-1){JOptionPane.showMessageDialog(this,"Select an item first!");return;}
        if(JOptionPane.showConfirmDialog(this,"Delete this item?","Confirm",JOptionPane.YES_NO_OPTION)!=0)return;
        int id=Integer.parseInt(((DefaultTableModel)tblItems.getModel()).getValueAt(r,0).toString());
        try{Connection conn=DBConnection.getConnection();if(conn==null)return;
            PreparedStatement p=conn.prepareStatement("DELETE FROM items WHERE id=?");p.setInt(1,id);p.executeUpdate();
            JOptionPane.showMessageDialog(this,"Item deleted!");clearFields();loadTable();conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) { clearFields(); }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String kw=txtSearch.getText().trim(); if(kw.isEmpty()){loadTable();return;}
        try{Connection conn=DBConnection.getConnection();if(conn==null)return;
            PreparedStatement p=conn.prepareStatement("SELECT * FROM items WHERE item_name LIKE ? OR description LIKE ? OR category LIKE ? OR location_found LIKE ? OR status LIKE ?");
            String s="%"+kw+"%";for(int i=1;i<=5;i++)p.setString(i,s);
            ResultSet rs=p.executeQuery();DefaultTableModel m=(DefaultTableModel)tblItems.getModel();m.setRowCount(0);
            while(rs.next()){m.addRow(new Object[]{rs.getInt("id"),rs.getString("item_name"),rs.getString("description"),
                rs.getString("category"),rs.getString("location_found"),rs.getString("date_found"),rs.getString("reported_by"),
                rs.getString("status"),rs.getString("claimed_by")==null?"—":rs.getString("claimed_by")});}
            conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {
        int r=tblItems.getSelectedRow();if(r==-1)return;DefaultTableModel m=(DefaultTableModel)tblItems.getModel();
        txtItemName.setText(m.getValueAt(r,1).toString());txtDescription.setText(m.getValueAt(r,2).toString());
        cmbCategory.setSelectedItem(m.getValueAt(r,3).toString());txtLocation.setText(m.getValueAt(r,4).toString());
        txtDateFound.setText(m.getValueAt(r,5).toString());cmbStatus.setSelectedItem(m.getValueAt(r,7).toString());
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",JOptionPane.YES_NO_OPTION)==0){
            dispose();new LoginForm().setVisible(true);}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblReported;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtDateFound;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtReportedBy;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
