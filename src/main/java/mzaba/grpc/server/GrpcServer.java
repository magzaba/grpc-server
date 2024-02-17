package mzaba.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import mzaba.grpc.service.PingPongServiceImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcServer {
    private static final Logger LOGGER = Logger.getLogger(GrpcServer.class.getName());
    private Server server;

    public void startServer(){
        var port = 8080;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new PingPongServiceImpl())
                    .build()
                    .start();
            LOGGER.info("Server started on port " + 8080);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Clean server shutdown in case JVM was shutdown");
                try{
                    GrpcServer.this.stopServer();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Server shutdown", e);
                }
            }));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server did not start", e);
        }
    }

    public void stopServer() throws InterruptedException {
        if(server!=null){
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutDown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var grpcServer = new GrpcServer();
        grpcServer.startServer();
        grpcServer.blockUntilShutDown();
    }}
