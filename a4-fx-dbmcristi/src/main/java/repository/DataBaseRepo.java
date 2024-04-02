package repository;

import domain.iEntity;
import org.sqlite.SQLiteDataSource;
import repository.Exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class DataBaseRepo<T extends iEntity> {
    //CRUD
    private final String DB_URL = "jdbc:sqlite:carRenting.db";
    protected Connection conn = null;

    public DataBaseRepo() {
        try {
            initDbConnection();
            createTable();
//            initTable();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    public int getLastIdFromDB(String tableName) throws RepositoryException {
        int max = 0;
        String sql = "SELECT MAX(id) as maxId FROM " + tableName;
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            max = rs.getInt("maxId");

        } catch (SQLException e) {
            throw new RepositoryException("error in maxId", e);
        }
        return max;
    }
    abstract void createTable() throws RepositoryException;
    abstract void initTable() throws RepositoryException;

    abstract void add(T e) throws RepositoryException;

    abstract T getById(int id) throws RepositoryException;

    abstract void delete(T obj) throws RepositoryException;

    abstract void update(T newEntity) throws RepositoryException;

    abstract void deleteAll() throws Exception;

    abstract ArrayList<T> getAll() throws Exception;


    //metoda pt deschiderea conexiunii
    private void initDbConnection() throws RepositoryException {
        try {
            // with DataSource
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(DB_URL);

            conn = ds.getConnection();
            if (conn == null || conn.isClosed()) {
                conn = ds.getConnection();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error creating DB connection", e);
        }
    }



}
