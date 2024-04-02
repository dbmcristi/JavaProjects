package repository;

import domain.Car;
import domain.EntityFactory;
import domain.Renting;
import domain.iEntity;
import repository.Exceptions.RepositoryException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileRepository<T extends iEntity> extends AbstractRepo<T> {
    private static final String CARS_INPUT_TXT = "carsInput.txt";
    private static final String RENTINGS_INPUT_TXT = "rentingsInput.txt";
    private final String fileName;
    private final EntityFactory factory ;

    public FileRepository(String fileName) {
        this.fileName = fileName;
        this.factory = new EntityFactory(fileName);
    }

    @Override
    public ArrayList<T> getAll() throws RepositoryException {

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        data = readFromFile();
        return data;
    }


    private ArrayList<T> readFromFile() throws RepositoryException {
        //reads file, then adds each entity in the designated repository
        //folosim bufferClass
        //le punem in data pentru a  juca cu ele
//        try (Scanner myReader = new Scanner(new File(fileName)))
        try (Scanner myReader = new Scanner(new File(fileName))) {
            ArrayList<T> data = new ArrayList();
            while (myReader.hasNextLine()) {
                var str = myReader.nextLine();
//                System.out.println(str);
                var iEntity = (T) factory.createEntity(str);

                data.add(iEntity);
            }
            return data;
        } catch (FileNotFoundException fnf) {
            System.out.println("Se va crea fisierul " + fileName + " cu prima entitate adaugata");
        } catch (NumberFormatException e) {
            throw new RepositoryException("Error at converting parts to designated type : " + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void writeToFile() throws FileNotFoundException, RepositoryException {
        //each time this method is called, the data values are put in file
        //folosim write class sa citim

        //punem data in file
        try (FileWriter myWriter = new FileWriter(fileName)) {
            if (fileName.equals(CARS_INPUT_TXT)) {
                for (var e : data) {
                    Car car = (Car) e;
                    myWriter.append(String.valueOf(car.getId())).append(" ").append(car.getMarca()).append(" ").append(car.getModel()).append("\n");
                }
            }
            if (fileName.equals(RENTINGS_INPUT_TXT)) {
                for (var e : data) {
                    Renting renting = (Renting) e;
                    myWriter.append(String.valueOf(renting.getId())).append(" ").append(String.valueOf(renting.getDate_begin())).append(" ")
                            .append(String.valueOf(renting.getDate_end())).append(" ").append(String.valueOf(renting.getMasina().getId()))
                            .append("\n");
                }
            }

        } catch (IOException e) {
            throw new RepositoryException("Error at writing parts to designated txt : " + e);
        }
    }

    @Override
    public void delete(T obj) throws RepositoryException, FileNotFoundException {
        readFromFile();
        super.delete(obj);
        writeToFile();
    }


    @Override
    public void add(T e) throws RepositoryException, FileNotFoundException {
        readFromFile();
        super.add(e);
        writeToFile();
    }

    @Override
    public T getByPos(int pos) throws RepositoryException, FileNotFoundException {
        readFromFile();
        return super.getByPos(pos);
    }

    @Override
    public void update(T newEntity) throws RepositoryException, FileNotFoundException {
        readFromFile();
        super.update(newEntity);
        writeToFile();
    }

    @Override
    public void deleteAll() throws RepositoryException, FileNotFoundException {
        readFromFile();
        super.deleteAll();
        writeToFile();
    }


}
