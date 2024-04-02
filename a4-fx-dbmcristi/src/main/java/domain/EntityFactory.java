package domain;

import java.io.FileNotFoundException;

public class EntityFactory<T extends iEntity> implements IEntityFactory {
    private static final String CARS_INPUT_TXT = "carsInput.txt";
    private static final String RENTINGS_INPUT_TXT = "rentingsInput.txt";
    private final String fileName;

    public EntityFactory(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public T createEntity(String line) throws NumberFormatException {
        String[] parts = line.split(" ");
        if (fileName.equals(CARS_INPUT_TXT)) {
            Car car = buildCar(parts);
            return (T) car;
        }
        if (fileName.equals(RENTINGS_INPUT_TXT)) {
            Renting renting = buildRenting(parts);
            return (T) renting;
        }
        return null;
    }

    private Renting buildRenting(String[] parts) throws NumberFormatException {
        int id;
        int date_begin;
        int date_end;
        int idMasina;
        id = Integer.parseInt(parts[0]);
        date_begin = Integer.parseInt(parts[1]);
        date_end = Integer.parseInt(parts[2]);
        idMasina = Integer.parseInt(parts[3]);
        Car car = new Car(idMasina);
        Renting renting = new Renting(id, date_begin, date_end, car);
        return renting;
    }

    private Car buildCar(String[] parts) throws NumberFormatException {
        String marca;
        String model;
        int nr;
        nr = Integer.parseInt(parts[0]);
        marca = parts[1];
        model = parts[2];
        Car car = new Car(nr, marca, model);
        return car;
    }
}
