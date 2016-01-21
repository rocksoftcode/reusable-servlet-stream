package reusable

import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

class ReusableServletInputStream extends ServletInputStream {

  InputStream is

  ReusableServletInputStream(InputStream is) {
    this.is = is
  }

  @Override
  boolean isFinished() {
    return is.available()
  }

  @Override
  boolean isReady() {
    return is.available()
  }

  @Override
  void setReadListener(ReadListener listener) {
  }

  @Override
  int read() throws IOException {
    return is.read()
  }
}