package ui;

import domain.Car;
import domain.Renting;
import repository.iRepository;

import java.util.Objects;
import java.util.Scanner;

public class Console {
    private final iRepository<Car> carRepository;
    private final iRepository<Renting> rentingRepository;
    private final UiClass ui;
    private Scanner in = new Scanner(System.in);

    public Console(iRepository<Car> carRepository, iRepository<Renting> rentingRepository, UiClass ui) {
        this.carRepository = carRepository;
        this.rentingRepository = rentingRepository;
        this.ui = ui;
    }


    static void printMeniu() {
        System.out.println("1.CRUD Masina");
        System.out.println("2.CRUD Inchiriere");
        System.out.println("x.Exit");
    }

    static void printSubMeniuCar() {
        System.out.println("    1.Create");
        System.out.println("    2.Read");
        System.out.println("    3.Update");
        System.out.println("    4.Delete");
        System.out.println("    h.Hardcode cars");
        System.out.println("    x.Exit");
    }

    static void printSubMeniuRenting() {
        System.out.println("    1.Create");
        System.out.println("    2.Read");
        System.out.println("    3.Update");
        System.out.println("    4.Delete");
        System.out.println("    pb1.pb1");
        System.out.println("    pb2.pb2");
        System.out.println("    pb3.pb3");
        System.out.println("    h.Hardcode cars and entity");
        System.out.println("    x.Exit");
    }

    public void run() throws Exception {

        System.out.println("Input option:");

        while (true) {
            try {
                printMeniu();
                String option = in.nextLine();
                String optionSub;

                if (Objects.equals(option, "1")) {
                    //enter sub car submeniu
                    System.out.println("  Car Sub-Meniu");

                    while (true) {
                        printSubMeniuCar();
                        optionSub = in.nextLine();

                        if (Objects.equals(optionSub, "1")) {
                            System.out.println(" Create car");
                            ui.addCar();
                        } else if (Objects.equals(optionSub, "2")) {
                            System.out.println(" Read cars");
                            ui.printCars();
                        } else if (Objects.equals(optionSub, "3")) {
                            System.out.println(" Update car");
                            ui.updateCar();
                        } else if (Objects.equals(optionSub, "4")) {
                            System.out.println(" Delete car");
                            ui.deleteCar();
                        } else if (Objects.equals(optionSub, "x")) {
                            break;
                        } else if (Objects.equals(optionSub, "h")) {
                            ui.hardCodeCars();
                        } else {
                            System.out.println("wrong input");
                        }
                    }
                } else if (Objects.equals(option, "2")) {
                    //enter sub rent submeniu
                    System.out.println("  Renting Sub-Meniu");

                    while (true) {
                        printSubMeniuRenting();

                        optionSub = in.nextLine();

                        if (Objects.equals(optionSub, "1")) {
                            System.out.println(" Create rent");
                            ui.addRenting();
                        } else if (Objects.equals(optionSub, "2")) {
                            System.out.println(" Read rent");
                            ui.printRenting();
                        } else if (Objects.equals(optionSub, "3")) {
                            System.out.println(" Update rent");
                            ui.updateRent();
                        } else if (Objects.equals(optionSub, "4")) {
                            System.out.println(" Delete rent");
                            ui.deleteRent();
                        }else if (Objects.equals(optionSub, "pb1")) {
                            ui.problema1();
                        }else if (Objects.equals(optionSub, "pb2")) {
                            ui.problema2();
                        }else if (Objects.equals(optionSub, "pb3")) {
                            ui.problema3();
                        } else if (Objects.equals(optionSub, "x")) {
                            break;
                        } else if (Objects.equals(optionSub, "h")) {
                            ui.hardcodeRenting();
                        }
                    }
                } else if (Objects.equals(option, "close")) {
                    System.out.println("program has exited.");
                    break;
                }

            } catch (Exception e) {
                System.out.println(e);
                System.out.println(e.getStackTrace().toString());
            }
        }
    }
}
