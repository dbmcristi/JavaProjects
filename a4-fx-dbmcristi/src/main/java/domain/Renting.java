package domain;


import java.util.Objects;

public class Renting implements iEntity {

    private int id;
    private int date_begin;
    private int date_end;
    private Car masina;

    public Renting(int id, int date_begin, int date_end, Car masina) {
        this.id = id;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.masina = masina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renting renting = (Renting) o;
        return id == renting.id && date_begin == renting.date_begin && date_end == renting.date_end && masina.equals(renting.masina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date_begin, date_end, masina);
    }

    public int getId() {
        return id;
    }

    public Car getMasina() {
        return masina;
    }

    public int getDate_begin() {
        return date_begin;
    }

    public int getDate_end() {
        return date_end;
    }

    public void setMasina(Car masina) {
        this.masina = masina;
    }

    @Override
    public String toString() {
        return "Renting{" +
                "id=" + id +
                ", date_begin=" + date_begin +
                ", date_end=" + date_end +
                ", masina=" + masina +
                '}';
    }
}
