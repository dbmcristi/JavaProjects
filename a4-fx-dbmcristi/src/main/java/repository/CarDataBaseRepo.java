package repository;

import domain.Car;
import repository.Exceptions.RepositoryException;

import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class CarDataBaseRepo extends DataBaseRepo<Car> implements iRepository<Car> {


    @Override
    void createTable() throws RepositoryException {
        String sql = "CREATE TABLE IF NOT EXISTS car(id int, marca varchar(100), model varchar(100), " +
                "PRIMARY KEY (id)) ;";
        try {
            try (final Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error creating car table", e);
        }
    }

    @Override
    void initTable() throws RepositoryException {
        String sql = "INSERT INTO car VALUES(?,?,?) ;";
        Random random = new Random();
        int max = getLastIdFromDB("Car");
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 1 + max; i <= 100 + max; i++) {
                    stmt.setInt(1, i);
                    stmt.setString(2, generatingRandomString(4));
                    stmt.setString(3, generatingRandomString(5));
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error init car table", e);
        }
    }

    @Override
    public void add(Car e) throws RepositoryException {
        String sql = "INSERT INTO car VALUES(?,?,?) ;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, e.getId());
                stmt.setString(2, e.getMarca());
                stmt.setString(3, e.getModel());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error adding car", ex);
        }
    }

    @Override
    public Car getByPos(int pos) throws RepositoryException {
        return null;
    }

    @Override
    public Car getById(int id) throws RepositoryException {
        String sql = "SELECT * FROM car WHERE id = ? ;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    return new Car(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                }
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error gettingById car", ex);
        }
        return null;
    }

    @Override
    public int dataLength() {
        return 0;
    }

    @Override
    public void delete(Car car) throws RepositoryException {
        String sql = "DELETE FROM car WHERE id = ? ;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, car.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error delete " + car, ex);
        }
    }

    @Override
    public void update(Car newEntity) throws RepositoryException {
        String sql = "UPDATE car SET marca = ?, model = ? WHERE id = ?;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newEntity.getMarca());
                stmt.setString(2, newEntity.getModel());
                stmt.setInt(3, newEntity.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error updating car", ex);
        }
    }

    @Override
    public void deleteAll() throws RepositoryException {
        String sql = "DELETE FROM car  ;";
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error deleteAll car", ex);
        }
    }

    @Override
    public ArrayList<Car> getAll() throws RepositoryException {
        String sql = "SELECT * FROM car ;";
        ArrayList<Car> cars = new ArrayList<>();
        try {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    Car car = new Car(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                    cars.add(car);
                }
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Error getAll car", ex);
        }
        return cars;
    }




    private String generatingRandomString(int num) {
        byte[] array = new byte[num]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
