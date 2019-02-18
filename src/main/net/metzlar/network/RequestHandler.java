package net.metzlar.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface RequestHandler <ReturnType> {
    ReturnType receive(ObjectInputStream ois) throws IOException;
    void send(ObjectOutputStream oos) throws IOException;
}
