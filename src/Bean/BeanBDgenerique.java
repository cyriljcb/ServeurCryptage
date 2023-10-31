package Bean;

import java.sql.*;

public class BeanBDgenerique {
    private Connection con;

    public BeanBDgenerique(String URLJDBC, String nom, String mdp) {
        try {
            con = DriverManager.getConnection(URLJDBC, nom, mdp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql, String nom) {
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nom); // Définissez le paramètre
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void executeUpdate(String sql, String nom,String password) {
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nom); // Définissez le paramètre
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(String sql, String nom) {
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nom);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
