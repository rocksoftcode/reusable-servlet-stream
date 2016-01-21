package reusable

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

@WebFilter(urlPatterns = '/*')
@Component
@Slf4j
class LogFilter implements Filter {

  @Override
  void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    String body = request.inputStream.text
    log.debug("Request body: $body")
    chain.doFilter(new HttpServletRequestWrapper(request as HttpServletRequest) {
      @Override
      ServletInputStream getInputStream() throws IOException {
        return new ReusableServletInputStream(new ByteArrayInputStream(body.bytes))
      }
    }, response)
  }

  @Override
  void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  void destroy() {}
}
