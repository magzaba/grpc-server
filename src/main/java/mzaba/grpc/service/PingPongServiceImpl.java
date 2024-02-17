package mzaba.grpc.service;

import io.grpc.stub.StreamObserver;
import mzaba.grpc.stubs.pingpong.PingPongServiceGrpc;
import mzaba.grpc.stubs.pingpong.PingRequest;
import mzaba.grpc.stubs.pingpong.PongResponse;

public class PingPongServiceImpl extends PingPongServiceGrpc.PingPongServiceImplBase {
    @Override
    public void playPingPong(PingRequest request, StreamObserver<PongResponse> responseObserver) {
        var messageFromClient = request.getMessage();
        var clientUsername = request.getUsername();
        var serverResponse = PongResponse.newBuilder()
                .setMessage("'PONG' from Java gRPC Server to " + clientUsername)
                .build();
        System.out.println("Received message: '" + messageFromClient + "' with username '" + clientUsername + "'");
        responseObserver.onNext(serverResponse);
        responseObserver.onCompleted();
    }
}
