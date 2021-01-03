package net.andreinc.gzipserialization;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static net.andreinc.mockneat.unit.types.Ints.ints;

public abstract class Serializer {

    enum Type {
        CLASSIC,
        GZIP;
    }

    /**
     * Serialize an object {@code T} on the disk.
     *
     * @param type If 'GZIP' the object will be also zipped before serialisation.
     * @param object The object to be serialised
     * @param path The path where to save the object
     * @param <T> The generic type of the object
     *
     * @throws IOException If there are access problems with the specified path
     */
    public static <T> void save(Type type, T object, String path) throws IOException {
        try(
                ObjectOutputStream oos = (type == Type.GZIP) ?
                        new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(path))) :
                        new ObjectOutputStream(new FileOutputStream(path));
        ) {
            oos.writeObject(object);
            oos.flush();
        }
    }

    /**
     * Loads an object from the disk
     *
     * @param type If 'GZIP' the object will be first unzipped before deserialization
     * @param c The class of the object for safe-casting
     * @param path The path from where to read the object
     * @param <T> The generic type fo the object
     * @return The object from the disk
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T load(Type type, Class<T> c, String path) throws IOException, ClassNotFoundException {
        try(
                ObjectInputStream ois = (type == Type.GZIP) ?
                        new ObjectInputStream(new GZIPInputStream(new FileInputStream(path))) :
                        new ObjectInputStream(new FileInputStream(path));
        ) {
            return c.cast(ois.readObject());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        BigOne bigOne = new BigOne();

        String classic = "classic.out";
        String gzip = "gzip.out";

        // Saves the same object twice on the disk
        Serializer.save(Type.CLASSIC, bigOne, classic);
        Serializer.save(Type.GZIP, bigOne, gzip);

        // Loads the objects from the disk
        BigOne bigOne1 = Serializer.load(Type.GZIP, BigOne.class, gzip);
        BigOne bigOne2 = Serializer.load(Type.CLASSIC, BigOne.class, classic);

        // Compares for equality all the 3 objects
        System.out.println("bigOne .eq bigOne1 ->" + bigOne1.equals(bigOne));
        System.out.println("bigOne .eq bigOne2 ->" + bigOne2.equals(bigOne));
    }
}
