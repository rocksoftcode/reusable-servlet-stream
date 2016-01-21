package reusable

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import static org.junit.Assert.assertEquals

public class TestAppTests {

  private static ConfigurableApplicationContext context

  @BeforeClass
  public static void start() throws Exception {
    Future<ConfigurableApplicationContext> future = Executors
        .newSingleThreadExecutor().submit(
        new Callable<ConfigurableApplicationContext>() {
          @Override
          public ConfigurableApplicationContext call() throws Exception {
            return SpringApplication
                .run(TestApp.class)
          }
        })
    context = future.get(60, TimeUnit.SECONDS)
  }

  @AfterClass
  public static void stop() {
    if (context != null) {
      context.close()
    }
  }

  @Test
  void "Servlet allows pre-read of body"() throws Exception {
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = getRestTemplate().postForEntity(
        "http://localhost:8080", [name: 'Foo'], Map)
    assertEquals(HttpStatus.OK, entity.getStatusCode())
    assertEquals([name: 'Foo'], entity.body)
  }

  private RestTemplate getRestTemplate() {
    RestTemplate restTemplate = new RestTemplate()
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
      @Override
      public void handleError(ClientHttpResponse response) throws IOException {
      }
    })
    restTemplate
  }
}