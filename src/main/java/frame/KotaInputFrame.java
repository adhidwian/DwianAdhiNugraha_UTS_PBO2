package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class KotaInputFrame extends JFrame{
    private JTextField idTextField;
    private JTextField namaTextField;
    private JTextField luasTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;

    private int id;

    public void setId(int id){this.id = id;}

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kota WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                luasTextField.setText(String.valueOf(rs.getDouble("luas")));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public KotaInputFrame(){
        batalButton.addActionListener(e ->{
            dispose();
        });

        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            double luas = Float.parseFloat(luasTextField.getText());

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQL = "SELECT * FROM kota WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String insertSQL = "INSERT INTO kota (id, nama, luas) " +
                                "VALUES (NULL, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setDouble(2, luas);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String cekSQL = "SELECT * FROM kota WHERE nama = ? AND id !=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String updateSQL =  "UPDATE kota SET nama = ?, luas = ? WHERE id = ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setDouble(2, luas);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                        dispose();
                    }
                }

            } catch (SQLException ex){
                throw new RuntimeException(ex);
            }
        });

        init();
    }
    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Kota");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }


    }


