package repository;

import domain.iEntity;
import repository.Exceptions.RepositoryException;

import java.io.*;
import java.util.ArrayList;

public class BinaryFileRepository<T extends iEntity> extends AbstractRepo<T> {

    private String fileName;

    public BinaryFileRepository(String fileName) {
        this.fileName = fileName;
    }

    private void saveFile() throws RepositoryException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName)) ){
            oos.writeObject(data);
        } catch (IOException  e) {
            throw new RepositoryException("saveFile error : " + e.getMessage());
        }
    }

    private void loadFile() throws RepositoryException {

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object dataIn = ois.readObject();
            data = (ArrayList<T>) dataIn;
        }catch (FileNotFoundException fnf){
            System.out.println("Se va crea fisierul " + fileName + " cu prima entitate adaugata");
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("loadFile() error : " + e + " message : " + e.getMessage());
        }
    }

    @Override
    public void add(T e) throws RepositoryException {

        if(data ==null || data.isEmpty()){
            data = new ArrayList<>();
        }
        data.add(e);
        //saveFile se executa doar daca in add NU este aruncata vre-o excepti
        saveFile();
    }

    @Override
    public void delete(T obj) throws RepositoryException, FileNotFoundException {
        super.delete(obj);
        saveFile();
    }

    @Override
    public T getByPos(int pos) throws RepositoryException, FileNotFoundException {
        return super.getByPos(pos);
    }

    @Override
    public void update(T newEntity) throws RepositoryException, FileNotFoundException {
        super.update( newEntity);
        saveFile();
    }

    @Override
    public void deleteAll() throws RepositoryException, FileNotFoundException {
        super.deleteAll();
        saveFile();
    }

    @Override
    public ArrayList<T> getAll() throws RepositoryException {


        if(data ==null || data.isEmpty()){
            data = new ArrayList<>();
        }
        loadFile();
        return data;
    }

}
