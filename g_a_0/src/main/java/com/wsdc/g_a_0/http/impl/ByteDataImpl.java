package com.wsdc.g_a_0.http.impl;

import com.wsdc.g_a_0.http.IByteData;
import com.wsdc.g_a_0.http.IClient;
import com.wsdc.g_a_0.http.bytes.Segment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteDataImpl implements IByteData {
    IClient client;
    Segment header;
    Segment footer;

    ByteDataImpl inner;

    int use = 0;

    InputStream is = new InputStream0();
    OutputStream os = new OutputStream0();

    public ByteDataImpl(IClient client,ByteDataImpl inner) {
        this.client = client;
        this.inner = inner;
    }

    @Override
    public InputStream inputStream() {
        return is;
    }

    @Override
    public OutputStream outputStream() {
        return os;
    }

    @Override
    public void write(byte[] data) throws IOException {
        os.write(data);
    }

    @Override
    public int read(byte[] data) throws IOException {
        return is.read(data);
    }

    @Override
    public byte[] bytes(int size) throws IOException{
        byte[] rtn = new byte[use];
        is.read(rtn);
        return rtn;
    }

    @Override
    public String string(int size) throws IOException{
        byte[] bytes = bytes(size);
        if(bytes.length == 0){
            return null;
        }
        return new String(bytes);
    }

    @Override
    public String readLine() throws IOException {
        while(true){
            try {
                int read = is.read();

                if(read != -1){
                    inner.os.write(read);
                    if(read == '\r'){
                        //  \r\n是连续在一起的所以需要连续一起读出来
                        inner.os.write(is.read());
                        break;
                    }

                    if(read == '\n'){
                        break;
                    }
                }else{
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //String rtn = inner.string();
        //return rtn;
        return "ok";
    }

    private final class InputStream0 extends InputStream{

        @Override
        public int read() throws IOException {
            if(header == null){
                return -1;
            }
            int read = -1;
            if(header.available() == 0){
                Segment next = header.next;
                if(next == null){
                    client.getSegmentPool().put(header);
                    return read;
                }
                header = next;
                read = header.read();
            }else{
                read = header.read();
            }
            use--;
            if(use == 0){
                client.getSegmentPool().put(header);
                header = null;
                footer = null;
            }
            return read;
        }

        @Override
        public int available() throws IOException {
            return use;
        }
    }

    private final class OutputStream0 extends OutputStream{

        @Override
        public void write(int b) throws IOException {
            if(footer == null){
                //  存在footer!=null 但是header==null的情况
                header = footer = client.getSegmentPool().get();
            }
            if(footer.write(b) == 0){
                //System.out.println("创建新的segment");
                Segment segment = client.getSegmentPool().get();
                footer.next = segment;
                segment.prev = footer;
                footer = segment;
                segment.write(b);
            }

            use++;
        }
    }
}
