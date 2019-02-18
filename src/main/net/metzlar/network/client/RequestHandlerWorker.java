package net.metzlar.network.client;

import net.metzlar.network.RequestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class RequestHandlerWorker<ReturnType> {

    private RequestHandler<Optional<ReturnType>> handler;

    public RequestHandlerWorker(RequestHandler<Optional<ReturnType>> handler) {
        this.handler = handler;
    }

    public Optional<ReturnType> handle(ObjectOutputStream oos, ObjectInputStream ois) {
        try {
            this.handler.send(oos);
            return this.handler.receive(ois);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
