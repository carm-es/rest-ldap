package es.carm.openldap;

import java.io.File;

public interface Factory<T> {

    public T fromString(String data);

    public T fromFile(File file);

    public T fromFile(String pathFile);

    public T fromURL(String urlFile);

    public String toString(T object);
}
