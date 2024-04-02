package domain;

public class Car implements iEntity {
    int id;
    String marca;
    String model;

    public Car(int id, String marca, String model) {
        this.id = id;
        this.marca = marca;
        this.model = model;
    }
    public Car(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public String getMarca(){ return this.marca; }

    public String getModel(){ return this.model; }

    public void setId(int newId){
        this.id = newId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id ;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
