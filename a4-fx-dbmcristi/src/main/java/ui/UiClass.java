package ui;

import domain.Car;
import domain.Renting;
import repository.iRepository;
import service.ProbService;
import service.Service;

import java.util.Scanner;


public class UiClass {

    private final iRepository<Car> carRepository;
    private final iRepository<Renting> rentingRepository;
    private final Service service;
    private Scanner in = new Scanner(System.in);

    public UiClass(iRepository<Car> carRepository, iRepository<Renting> rentingRepository) {
        this.carRepository = carRepository;
        this.rentingRepository = rentingRepository;
        service = new Service(carRepository, rentingRepository);
    }

    public void hardCodeCars() throws Exception {

        Car car = new Car(1, "BMW", "325i");
        service.addCar(car);

        Car car1 = new Car(2, "BMW", "320i");
        service.addCar(car1);

        Car car2 = new Car(3, "MAZDA", "Miata");
        service.addCar(car2);

        Car car3 = new Car(4, "TOYOTA", "Celica");
        service.addCar(car3);
    }

    public void addCar() throws Exception {
        System.out.println("id: ");
        int id = Integer.parseInt(in.nextLine());
        System.out.println("marca: ");
        String marca = in.nextLine();
        System.out.println("model: ");
        String model = in.nextLine();

        Car car = new Car(id, marca, model);
        service.addCar(car);

    }

    public void deleteCar() throws Exception {
        System.out.println("id: ");
        int id = in.nextInt();
        service.deleteCar(id);
    }

    public void updateCar() throws Exception {
        System.out.println("id: ");
        int id = in.nextInt();
        System.out.println("new marca: ");
        String marca = in.next();
        System.out.println("new model: ");
        String model = in.next();

        Car newCar = new Car(id, marca, model);
        service.updateCar(newCar);
    }

    public void printCars() throws Exception {
        for (var c : service.getAllCar()) {
            System.out.println(c.toString());
        }
    }

    //renting ui -----------------------------------------
    public void hardcodeRenting() throws Exception {

        Car car = new Car(1, "BMW", "325i");
        Car car1 = new Car(2, "BMW", "320i");
        Car car2 = new Car(3, "MAZDA", "Miata");
        Car car3 = new Car(4, "TOYOTA", "Celica");

        service.addCar(car);
        service.addCar(car1);
        service.addCar(car2);
        service.addCar(car3);

        Renting renting = new Renting(1, 2016, 2020, car1);
        Renting renting1 = new Renting(2, 2016, 2020, car2);
        Renting renting2 = new Renting(3, 2009, 2011, car1);
        Renting renting3 = new Renting(4, 2016, 2020, car3);
        Renting renting4 = new Renting(5, 2000, 2002, car2);

        service.addRenting(renting);
        service.addRenting(renting1);
        service.addRenting(renting2);
        service.addRenting(renting3);
        service.addRenting(renting4);

    }

    public void addRenting() throws Exception {
        //first we have to select which car
        System.out.println("id: ");
        int id = Integer.parseInt(in.nextLine());
        System.out.println("date_begin: ");
        int date_begin = Integer.parseInt(in.nextLine());
        System.out.println("date_begin: ");
        int date_end = Integer.parseInt(in.nextLine());
        System.out.println("carId: ");
        int carId = Integer.parseInt(in.nextLine());

        for (var e : service.getAllCar()) {
            if (e.getId() == carId) {
                Renting renting = new Renting(id, date_begin, date_end, e);
                service.addRenting(renting);
                break;
            }
        }
        //throw new Exception("addRenting went wrong in uiClass");
    }

    public void printRenting() throws Exception {
        for (var r : service.getAllRenting()) {
            System.out.println(r.toString());
        }
    }

    public void deleteRent() throws Exception {
        System.out.println("id: ");
        int id = in.nextInt();
        service.deleteRenting(id);
    }

    public void updateRent() throws Exception {
        System.out.println("id: ");
        int id = in.nextInt();
        System.out.println("new date_begin: ");
        int date_begin = Integer.parseInt(in.next());
        System.out.println("new date_end: ");
        int date_end = Integer.parseInt(in.next());
        System.out.println("new carId: ");
        int carId = Integer.parseInt(in.next());

        for (var c : service.getAllCar()) {
            if (c.getId() == carId) {
                Renting renting = new Renting(id, date_begin, date_end, c);
                service.updateRenting(renting);
                break;
            }
        }
    }

    public void problema1() {
        ProbService probService = new ProbService();
        probService.inchiriatePb1();
    }

    public void problema2() {
        ProbService probService = new ProbService();
        probService.inchiriatePb2();
    }

    public void problema3() {
        ProbService probService = new ProbService();
        probService.inchiriatePb3();
    }

}
