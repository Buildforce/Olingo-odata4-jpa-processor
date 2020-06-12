package nl.buildforce.sequoia.jpa.processor.core.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServletInputStreamDouble extends ServletInputStream {
    private final InputStream stream;

    public ServletInputStreamDouble(ServletInputStream stream) {
        super();
        this.stream = stream;
    }

    public ServletInputStreamDouble(StringBuffer stream) {
        super();
        if (stream != null)
            this.stream = new ByteArrayInputStream(stream.toString().getBytes());
        else
            this.stream = null;
    }

    @Override
    public int read() throws IOException {
        return stream.read();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}