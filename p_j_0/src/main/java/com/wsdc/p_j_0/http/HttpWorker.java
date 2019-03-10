package com.wsdc.p_j_0.http;

public class HttpWorker implements Worker {
    Client client;

    public HttpWorker(Client client) {
        this.client = client;
    }

    @Override
    public void request(ICall call) {
        ConnectionPool pool = client.connectionPool();
        Connection connection = pool.getConnection(call);
    }

    @Override
    public void write(ICall call) {

    }

    @Override
    public void response(ICall call) {

    }

    @Override
    public void read(ICall call) {

    }
}
