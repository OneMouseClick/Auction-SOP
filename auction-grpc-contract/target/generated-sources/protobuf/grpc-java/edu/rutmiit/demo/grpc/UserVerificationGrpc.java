package edu.rutmiit.demo.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: user_verification.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UserVerificationGrpc {

  private UserVerificationGrpc() {}

  public static final java.lang.String SERVICE_NAME = "userverification.UserVerification";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.VerifyUserRequest,
      edu.rutmiit.demo.grpc.VerifyUserResponse> getVerifyUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "VerifyUser",
      requestType = edu.rutmiit.demo.grpc.VerifyUserRequest.class,
      responseType = edu.rutmiit.demo.grpc.VerifyUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.VerifyUserRequest,
      edu.rutmiit.demo.grpc.VerifyUserResponse> getVerifyUserMethod() {
    io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.VerifyUserRequest, edu.rutmiit.demo.grpc.VerifyUserResponse> getVerifyUserMethod;
    if ((getVerifyUserMethod = UserVerificationGrpc.getVerifyUserMethod) == null) {
      synchronized (UserVerificationGrpc.class) {
        if ((getVerifyUserMethod = UserVerificationGrpc.getVerifyUserMethod) == null) {
          UserVerificationGrpc.getVerifyUserMethod = getVerifyUserMethod =
              io.grpc.MethodDescriptor.<edu.rutmiit.demo.grpc.VerifyUserRequest, edu.rutmiit.demo.grpc.VerifyUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "VerifyUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.VerifyUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.VerifyUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserVerificationMethodDescriptorSupplier("VerifyUser"))
              .build();
        }
      }
    }
    return getVerifyUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserVerificationStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserVerificationStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserVerificationStub>() {
        @java.lang.Override
        public UserVerificationStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserVerificationStub(channel, callOptions);
        }
      };
    return UserVerificationStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserVerificationBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserVerificationBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserVerificationBlockingStub>() {
        @java.lang.Override
        public UserVerificationBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserVerificationBlockingStub(channel, callOptions);
        }
      };
    return UserVerificationBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserVerificationFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserVerificationFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserVerificationFutureStub>() {
        @java.lang.Override
        public UserVerificationFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserVerificationFutureStub(channel, callOptions);
        }
      };
    return UserVerificationFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Верифицирует пользователя при регистрации
     * </pre>
     */
    default void verifyUser(edu.rutmiit.demo.grpc.VerifyUserRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.VerifyUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVerifyUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserVerification.
   */
  public static abstract class UserVerificationImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserVerificationGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserVerification.
   */
  public static final class UserVerificationStub
      extends io.grpc.stub.AbstractAsyncStub<UserVerificationStub> {
    private UserVerificationStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserVerificationStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserVerificationStub(channel, callOptions);
    }

    /**
     * <pre>
     * Верифицирует пользователя при регистрации
     * </pre>
     */
    public void verifyUser(edu.rutmiit.demo.grpc.VerifyUserRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.VerifyUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVerifyUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserVerification.
   */
  public static final class UserVerificationBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserVerificationBlockingStub> {
    private UserVerificationBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserVerificationBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserVerificationBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Верифицирует пользователя при регистрации
     * </pre>
     */
    public edu.rutmiit.demo.grpc.VerifyUserResponse verifyUser(edu.rutmiit.demo.grpc.VerifyUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVerifyUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserVerification.
   */
  public static final class UserVerificationFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserVerificationFutureStub> {
    private UserVerificationFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserVerificationFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserVerificationFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Верифицирует пользователя при регистрации
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.rutmiit.demo.grpc.VerifyUserResponse> verifyUser(
        edu.rutmiit.demo.grpc.VerifyUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVerifyUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VERIFY_USER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VERIFY_USER:
          serviceImpl.verifyUser((edu.rutmiit.demo.grpc.VerifyUserRequest) request,
              (io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.VerifyUserResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getVerifyUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.rutmiit.demo.grpc.VerifyUserRequest,
              edu.rutmiit.demo.grpc.VerifyUserResponse>(
                service, METHODID_VERIFY_USER)))
        .build();
  }

  private static abstract class UserVerificationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserVerificationBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.rutmiit.demo.grpc.UserVerificationOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserVerification");
    }
  }

  private static final class UserVerificationFileDescriptorSupplier
      extends UserVerificationBaseDescriptorSupplier {
    UserVerificationFileDescriptorSupplier() {}
  }

  private static final class UserVerificationMethodDescriptorSupplier
      extends UserVerificationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserVerificationMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserVerificationGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserVerificationFileDescriptorSupplier())
              .addMethod(getVerifyUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
