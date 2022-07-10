package DL;

import BL.Gestor;
import BL.Robot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RobotsDAO extends DataAcessObject<String, Robot>{
    private static RobotsDAO singleton = new RobotsDAO();

    public RobotsDAO() {
        super(new Robot(), "Robots", Arrays.asList("codID", "ordens_feitas"));
    }

    public static RobotsDAO getInstance(){
        return RobotsDAO.singleton;
    }

    public Robot get(final String key) {
        return super.get(key);
    }

   /* public PriorityQueue<Robot> values(){
        return (PriorityQueue<Robot>) super.values();
    }*/

    public ArrayList<Robot> values2(){
        return (ArrayList<Robot>) super.values();
    }

    public Robot put(final Robot value) {
        return super.put(value, value.getCodeID());
    }

    public Robot remove(final String key) {
        return super.remove(key);
    }

    public Set<Robot> search(final String value) {
        return super.search(value, 0);
    }

    public Robot update(final Robot r) {
        Connection connection = BaseDados.getConnection();
        final String UPDATE = "UPDATE Robots SET codID=?, ordens_feitas=? WHERE ordens_feitas=?";

        try {

            PreparedStatement ps = connection.prepareStatement(UPDATE);

            ps.setString(1, r.getCodeID());
            System.out.println(r.getOrdensFeitas());
            ps.setInt(2, r.getOrdensFeitas());
            ps.setInt(3, r.getOrdensFeitas()-1);

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return r;
    }
}
