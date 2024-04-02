package service;

import domain.Car;
import domain.Renting;
import repository.Exceptions.RepositoryException;
import repository.iRepository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final iRepository<Car> carRepository;
    private final iRepository<Renting> rentingRepository;

    public Service(iRepository<Car> carRepository, iRepository<Renting> rentingRepository) {
        this.carRepository = carRepository;
        this.rentingRepository = rentingRepository;
    }

    ///car service
    public void addCar(Car e) throws RepositoryException {
        try {
            carRepository.getById(e.getId());
        } catch (RepositoryException err) {
            try {
                carRepository.add(e);
            } catch (Exception ex) {
                throw new RepositoryException("Car already exists");
            }
        }
    }

    public void deleteCar(int id) throws RepositoryException {
        validateIdNonExistent(id);

        try {
            List<Renting> filteredRentings = rentingRepository.getAll().stream().filter(elem -> elem.getMasina().getId() == id).collect(Collectors.toList());
            Car carToDel = carRepository.getById(id);
            carRepository.delete(carToDel);

            for (Renting elem : filteredRentings) {
                rentingRepository.delete(elem);
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryException("file not found", e);
        }

    }

    public void updateCar(Car newCar) throws Exception {
        carRepository.update(newCar);
    }

    public ArrayList<Car> getAllCar() throws Exception {
        return carRepository.getAll();
    }

    ///renting service-------------------------------------------------------------
    public void addRenting(Renting newRent) throws Exception {
        validateByDateInteval(newRent);
        validateByOverlappingAndId(newRent);
        rentingRepository.add(newRent);
    }

    public void updateRenting(Renting newRenting) throws Exception {
        validateByDateInteval(newRenting);
        validateByOverlapping(newRenting);

        rentingRepository.update(newRenting);
    }

    public void deleteRenting(int id) throws RepositoryException {
        validateIdNonExistentRenting(id);
        Renting renting = rentingRepository.getById(id);
        try {
            rentingRepository.delete(renting);
        } catch (FileNotFoundException e) {
            throw new RepositoryException("file not found", e);
        }
    }

    public ArrayList<Renting> getAllRenting() throws RepositoryException {
        ArrayList<Renting> all = null;
        try {
            all = rentingRepository.getAll();
            for (Renting r : all) {
                if (r.getMasina().getMarca() == null || r.getMasina().getModel() == null) {
                    Car car = carRepository.getById(r.getMasina().getId());
                    r.setMasina(car);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryException("file not found", e);
        }

        return all;
    }




    private void validateByDateInteval(Renting newRent) throws RepositoryException {
        int begin = newRent.getDate_begin();
        int end = newRent.getDate_end();

        if (begin > end) {
            throw new RepositoryException("The order date is wrong");
        }
    }

    private void validateByOverlappingAndId(Renting newRent) throws RepositoryException, FileNotFoundException {
        int S = newRent.getDate_begin();
        int E = newRent.getDate_end();

        for (var r : rentingRepository.getAll()) {
            if (r.getId() == newRent.getId()) {
                throw new RepositoryException("Id already exists");
            }
            if (r.getMasina().getId() == newRent.getMasina().getId()) {
                var db = r.getDate_begin();
                var de = r.getDate_end();
                if (isOverlappingBoolean(db, S, de, E)) {
                    throw new RepositoryException("date already exists");
                }
            }
        }
        System.out.println("no overlapping");
    }

    private static boolean isOverlappingBoolean(int db, int S, int de, int E) {
        return (S <= db && de <= E) ||
                (S <= db && E <= de && db < E) ||
                (db <= S && E <= de) ||
                (db <= S && de <= E && S <= de);

    }

    private void validateIdNonExistent(int id) throws RepositoryException {
        boolean isValid = false;

        try {
            for (var e : carRepository.getAll()) {
                if (e.getId() == id) {
                    isValid = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryException("Bad id (from validator)", e);
        }
        if (!isValid) {
            throw new RepositoryException("Bad id (from validator)");
        }

    }

    private void validateIdNonExistentRenting(int id) throws RepositoryException {
        boolean isValid = false;

        try {
            for (var e : rentingRepository.getAll()) {
                if (e.getId() == id) {
                    isValid = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryException("Bad id (from validator)", e);
        }
        if (!isValid) {
            throw new RepositoryException("Bad id (from validator)");
        }

    }

    private void validateByOverlapping(Renting newRent) throws RepositoryException {

        try {
            List<Renting> rentings = rentingRepository.getAll().stream()
                    .filter(r -> r.getMasina().equals(newRent.getMasina()) && (!((newRent.getDate_end() <= r.getDate_begin()) || (r.getDate_end() <= newRent.getDate_begin())))).collect(Collectors.toList());
            if (!rentings.isEmpty()) {
                throw new RepositoryException("date already exists");
            }
        } catch (FileNotFoundException e) {
            throw new RepositoryException("file not found", e);
        }
        System.out.println("validation is okay!");
    }

}
