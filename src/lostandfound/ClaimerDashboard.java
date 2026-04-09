package lostandfound;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClaimerDashboard extends javax.swing.JFrame {

    private String loggedUser;

    public ClaimerDashboard(String userName) {
        this.loggedUser = userName;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Claimer Dashboard - " + userName);
        lblUser.setText("Claimer: " + userName);
        loadTable();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeader = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        lblInfo = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnClaim = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Claimer Dashboard");
        setResizable(false);

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(40, 167, 69));
        lblHeader.setText("CLAIMER DASHBOARD");

        lblUser.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setText("Claimer:");

        btnLogout.setBackground(new java.awt.Color(220, 53, 69));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LOGOUT");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        lblInfo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfo.setText("Browse items below and click CLAIM to request an unclaimed item.");

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

        btnClaim.setBackground(new java.awt.Color(40, 167, 69));
        btnClaim.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnClaim.setForeground(new java.awt.Color(255, 255, 255));
        btnClaim.setText("CLAIM SELECTED ITEM");
        btnClaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClaimActionPerformed(evt);
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
        jScrollPane1.setViewportView(tblItems);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClaim, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(lblInfo)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClaim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loadTable(){
        try{Connection conn=DBConnection.getConnection();if(conn==null)return;
            DefaultTableModel m=(DefaultTableModel)tblItems.getModel();m.setRowCount(0);
            ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM items ORDER BY id DESC");
            while(rs.next()){m.addRow(new Object[]{rs.getInt("id"),rs.getString("item_name"),rs.getString("description"),
                rs.getString("category"),rs.getString("location_found"),rs.getString("date_found"),rs.getString("reported_by"),
                rs.getString("status"),rs.getString("claimed_by")==null?"—":rs.getString("claimed_by")});}
            conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void btnClaimActionPerformed(java.awt.event.ActionEvent evt){
        int r=tblItems.getSelectedRow();
        if(r==-1){JOptionPane.showMessageDialog(this,"Select an item to claim!");return;}
        DefaultTableModel m=(DefaultTableModel)tblItems.getModel();
        String status=m.getValueAt(r,7).toString();
        if(status.equals("Claimed")){JOptionPane.showMessageDialog(this,"Already claimed!","Info",JOptionPane.WARNING_MESSAGE);return;}
        if(status.equals("Claim Pending")){JOptionPane.showMessageDialog(this,"Already has a pending claim!","Info",JOptionPane.WARNING_MESSAGE);return;}
        String itemName=m.getValueAt(r,1).toString();
        if(JOptionPane.showConfirmDialog(this,"Claim \""+itemName+"\"?\nYour name ("+loggedUser+") will be recorded.","Confirm",JOptionPane.YES_NO_OPTION)!=0)return;
        int id=Integer.parseInt(m.getValueAt(r,0).toString());
        try{Connection conn=DBConnection.getConnection();if(conn==null)return;
            PreparedStatement p=conn.prepareStatement("UPDATE items SET status='Claim Pending',claimed_by=?,claim_date=NOW() WHERE id=?");
            p.setString(1,loggedUser);p.setInt(2,id);p.executeUpdate();
            JOptionPane.showMessageDialog(this,"Claim submitted! Reporter will review.");
            loadTable();conn.close();
        }catch(SQLException e){JOptionPane.showMessageDialog(this,"Error:\n"+e.getMessage());}
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt){
        String kw=txtSearch.getText().trim();if(kw.isEmpty()){loadTable();return;}
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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt){
        if(JOptionPane.showConfirmDialog(this,"Logout?","Confirm",JOptionPane.YES_NO_OPTION)==0){
            dispose();new LoginForm().setVisible(true);}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClaim;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSearch;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
