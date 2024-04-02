package repository;

import repository.Exceptions.RepositoryException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface iRepository<T> {
    int dataLength();
    void delete(T obj) throws RepositoryException,FileNotFoundException;
    void add(T e) throws RepositoryException,FileNotFoundException;
    T getByPos(int pos) throws RepositoryException,FileNotFoundException ;
    void update(T newEntity) throws RepositoryException, FileNotFoundException;
    void deleteAll() throws RepositoryException,FileNotFoundException;
    ArrayList<T> getAll() throws RepositoryException,FileNotFoundException;

    T getById(int id) throws RepositoryException;
}
