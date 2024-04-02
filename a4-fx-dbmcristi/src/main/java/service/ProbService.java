package service;

import repository.CarDataBaseRepo;
import repository.RentingDataBaseRepo;

public class ProbService {
    //-------Cerinte Stream -----
    /*
    Cele mai des închiriate mașini.
    Se vor afișa datele pentru fiecare mașină precum și numărul de închirieri,
    în ordine descrescătoare a numărului de închirieri.

    Numărul de închirieri efectuate în fiecare an(data de inceput).
    Se vor afișa anii, precum și numărul de închirieri efectuate în fiecare an,
    în ordine descrescătoare a numărului de închirieri.
    O închiriere pentru care data de început și data de sfârșit se găsește în ano diferiti
    se va adăuga statisticii anului asociat datei de început.

    Mașinile care au fost închiriate cel mai mult timp.
    Se vor afișa datele pentru fiecare mașină precum și numărul total de zile de închiriere pentru fiecare.
     */


    private final CarDataBaseRepo carRepository = new CarDataBaseRepo();
    private final RentingDataBaseRepo rentingRepository = new RentingDataBaseRepo();

    public void inchiriatePb1(){
//        try {
//            Map<Integer, List<Renting>> rentingPerCar = rentingRepository.getRentingCar().stream()
//                    .collect(Collectors.groupingBy(rent->rent.getMasina().getId()));
//
//            Comparator<List<Renting>> comp = Comparator.comparingInt(List::size);
//            var list = rentingPerCar.values().stream().sorted(comp.reversed()).collect(Collectors.toList());
//
//            for(var renting : list ) {
//                System.out.println(renting.size());
//                System.out.println(renting.get(0).getMasina());
//            }
//        } catch (RepositoryException e) {
//            System.out.println("Problem 1 error");
//        }
    }

    public void inchiriatePb2(){
//        try {
//            Map<Integer,List<Renting>> rentingPerCar = rentingRepository.getAll().stream().
//                    collect(Collectors.groupingBy(Renting::getDate_begin));
//
//            Comparator<List<Renting>> comp = Comparator.comparingInt(List::size) ;
//            var list = rentingPerCar.values().stream().sorted(comp.reversed()).collect(Collectors.toList());
//
//            for(var renting : list ) {
//                System.out.println(renting.size());
//                System.out.println(renting.get(0).getDate_begin());
//            }
//        } catch (RepositoryException e) {
//            System.out.println("Problem 1 error");
//        }
    }
    public void inchiriatePb3(){
//        try {
//          var rentingPerCar = rentingRepository.getRentingCar().stream().
//                    collect(Collectors.groupingBy(r -> r.getDate_end() - r.getDate_begin(),
//                            Collectors.mapping(renting -> renting.getMasina(), Collectors.toSet())));
//
//            for(var durata : rentingPerCar.keySet() ) {
//                System.out.println("durata"+durata);
//                System.out.println(rentingPerCar.get(durata));
//            }
//        } catch (RepositoryException e) {
//            System.out.println("Problem 3 error");
//        }
    }

}
