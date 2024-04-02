package domain;

import java.io.FileNotFoundException;

public interface IEntityFactory {

    public iEntity createEntity(String line) throws FileNotFoundException;
}
