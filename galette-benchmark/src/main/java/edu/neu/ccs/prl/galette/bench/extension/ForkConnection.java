package edu.neu.ccs.prl.galette.bench.extension;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public final class ForkConnection implements Closeable {
    private final Socket socket;
    private final DataInputStream is;
    private final DataOutputStream os;

    /**
     * Creates a new connection at the specified port on the loopback interface.
     *
     * @param port the port at which connection should be made
     * @throws IOException              if an I/O error occurs establishing the connection
     * @throws SecurityException        if a security manager exists and does not allow the connection
     * @throws IllegalArgumentException if the specified port value is invalid
     */
    public ForkConnection(int port) throws IOException {
        this(new Socket(InetAddress.getByName(null), port));
    }

    public ForkConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.os = new DataOutputStream(socket.getOutputStream());
        this.os.flush();
        this.is = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void close() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            //
        }
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void send(String s) throws IOException {
        os.writeInt(s.getBytes().length);
        os.write(s.getBytes());
        os.flush();
    }

    public void send(int i) throws IOException {
        os.writeInt(i);
        os.flush();
    }

    public String receiveString() throws IOException {
        byte[] buffer = new byte[is.readInt()];
        is.readFully(buffer);
        return new String(buffer);
    }

    public int receiveInt() throws IOException {
        return is.readInt();
    }
}
