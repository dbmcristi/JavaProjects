package repository;

import domain.iEntity;
import repository.Exceptions.RepositoryException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;

public abstract class
AbstractRepo<T extends iEntity> implements iRepository<T>, Iterable<T> {
                                                                    //Dc T nu extends iEntity?^^^^
    protected ArrayList<T> data;

    @Override
    public int dataLength() {
        int c = 0;
        for (var e : data)
            c++;
        return c;
    }

    @Override
    public void delete(T obj) throws RepositoryException, FileNotFoundException {
        data.remove(obj);
    }

    @Override
    public void add(T e) throws RepositoryException, FileNotFoundException {
        data.add(e);
    }

    @Override
    public T getByPos(int pos) throws RepositoryException, FileNotFoundException {
        if (pos > -1 || this.data.size() + 1 > pos)
            return data.get(pos);
        return null;
    }

    @Override
    public ArrayList<T> getAll() throws RepositoryException {
//        if (dataLength() == 0)
//            throw new RepositoryException("error getAll, dataLength = 0 ");
        return data;

    }

    @Override
    public void update(T newEntity) throws RepositoryException, FileNotFoundException {
        int id = newEntity.getId();
        var e = getById(id);
        if(e == null)
            throw new RepositoryException("There is no such entity line 55");
        int index = 0;

        for (var el : getAll()) {
            if (el.getId() == id) {
                index = data.indexOf(el);
            }
        }
        data.set(index, newEntity);
    }

    @Override
    public void deleteAll() throws RepositoryException, FileNotFoundException {
        this.data = new ArrayList<>();
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    public T getById(int id) throws RepositoryException {
//        if (getAll() != null) {
//            return data.stream().filter(elem -> elem.getId() == id).findFirst().get();
//        }
//        return null;
//    }
         if (getAll() != null) {
            Optional<T> opt = data.stream().filter(elem -> elem.getId() == id).findFirst();
            if(opt.isPresent()){
                return opt.get();
            }
        }
        throw new RepositoryException("id not exists!");
    }
}
