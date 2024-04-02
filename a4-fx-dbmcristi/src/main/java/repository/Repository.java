package repository;

import domain.iEntity;

import java.util.ArrayList;

public class Repository<T extends iEntity> extends AbstractRepo<T>  {

    public Repository() {
        this.data = new ArrayList<>();
    }

}
