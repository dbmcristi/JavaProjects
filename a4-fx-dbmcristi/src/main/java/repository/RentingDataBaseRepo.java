package repository;

import domain.Car;
import domain.Renting;
import repository.Exceptions.RepositoryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RentingDataBaseRepo extends DataBaseRepo<Renting> implements iRepository<Renting> {


    @Override
    void createTable() throws RepositoryException {
        String sql = "CREATE TABLE IF NOT EXISTS renting(id int, databegin int, dataend int, idcar int, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY(idcar) REFERENCES Car(id));";
        try {
            try (final Statement stmt = conn.createStatement()) {

                stmt.execute(sql);

            }
        } catch (SQLException e) {
            throw new RepositoryException("Error creating rentingTable", e);
        }
    }


    @Override
    public void initTable() throws RepositoryException {
        String sql = "INSERT INTO renting VALUES(?,?,?,?) ;";
        int max = getLastIdFromDB("Renting");

        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 1 + max; i <= 100 + max; i++) {
                    int c = getRandomYear();
                    stmt.setInt(1, i);
                    stmt.setInt(2, c);
                    stmt.setInt(3, c + 5);
                    stmt.setInt(4, getRandomId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error init renting table", e);
        }
    }

    private int getRandomId() {
        int min = 1;
        int max = 100;
        return (int) ((Math.random() * (max - min)) + min);
    }


    public int getRandomYear() {
        int min = 1980;
        int max = 2023;
        return (int) ((Math.random() * (max - min)) + min);
    }


    @Override
    public void add(Renting e) throws RepositoryException {
        String sql = "INSERT INTO renting VALUES(?,?,?,?) ;";
        System.out.println(e);
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, e.getId());
                stmt.setInt(2, e.getDate_begin());
                stmt.setInt(3, e.getDate_end());
                stmt.setInt(4, e.getMasina().getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error adding renting", ex);
        }
    }

    @Override
    public Renting getByPos(int pos) {
        return null;
    }

    @Override
    public Renting getById(int id) throws RepositoryException {
        String sql = "SELECT * FROM renting WHERE id = ?; ";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    Car car = new Car(resultSet.getInt(4));
                    return new Renting(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), car);
                }
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error gettingById renting", ex);
        }
        return null;
    }

    @Override
    public int dataLength() {
        return 0;
    }

    @Override
    public void delete(Renting renting) throws RepositoryException {
        String sql = "DELETE FROM renting WHERE id = ?;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, renting.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error deleting renting", ex);
        }

    }

    @Override
    public void update(Renting newEntity) throws RepositoryException {
        //databegin dataend  idcar
        String sql = "UPDATE renting SET databegin = ?, dataend = ?, idcar = ? WHERE id = ?;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, newEntity.getDate_begin());
                stmt.setInt(2, newEntity.getDate_end());
                stmt.setInt(3, newEntity.getMasina().getId());
                stmt.setInt(4, newEntity.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error update renting", ex);
        }
    }

    @Override
    public void deleteAll() throws RepositoryException {
        String sql = "DELETE FROM renting";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error deleteAll renting", ex);
        }
    }

    @Override
    public ArrayList<Renting> getAll() throws RepositoryException {

        String sql = "SELECT * FROM renting ";

        ArrayList<Renting> rentings = new ArrayList<>();
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    Car car = new Car(resultSet.getInt(4));
                    rentings.add(new Renting(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), car));
                }
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error getAll renting", ex);
        }
        return rentings;
    }

    public ArrayList<Renting> getRentingCar() throws RepositoryException {

        String sql = "SELECT renting.id, renting.databegin, renting.dataend, car.id, car.marca, car.model" +
                " FROM renting,car " +
                "WHERE renting.idcar = car.id;";

        ArrayList<Renting> rentings = new ArrayList<>();
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    Car car = new Car(resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
                    rentings.add(new Renting(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), car));
                }
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error getAll renting", ex);
        }
        return rentings;
    }
    //init db connection
    //create schema

}
