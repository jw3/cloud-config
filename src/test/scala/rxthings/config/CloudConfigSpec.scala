package rxthings.config

import net.ceedubs.ficus.Ficus._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/**
 * configuration spec based on the configuration
 * from https://github.com/spring-cloud-samples/config-repo @ 4f60e3d
 */
class CloudConfigSpec extends WordSpec with Matchers {
    val timeout = 10 seconds

    "get config" should {
        "lookup value in development profile" in {
            //https://github.com/spring-cloud-samples/config-repo/blob/master/foo-development.properties
            val future = CloudConfig("localhost").load("foo", "development")
            val config = Await.result(future, timeout)
            config.getString("foo") shouldBe "bar"
        }

        "lookup value in yml eureka docker profile" in {
            //https://github.com/spring-cloud-samples/config-repo/blob/master/eureka.yml
            val future = CloudConfig("localhost").load("eureka", "docker")
            val config = Await.result(future, timeout)
            config.getString("eureka.client.serviceUrl.defaultZone") shouldBe "http://localhost:8761/eureka/"
        }

        "lookup value in application file using default profile" in {
            //https://github.com/spring-cloud-samples/config-repo/blob/master/application.yml

            /**
             * no profile specified, defaults to [[CloudConfig.profileEnvDefault]]
             */
            val future = CloudConfig("localhost").load("application")
            val config = Await.result(future, timeout)
            config.getString("eureka.client.serviceUrl.defaultZone") shouldBe "http://localhost:8761/eureka/"
        }

        "not include value in application file using default profile" in {
            //https://github.com/spring-cloud-samples/config-repo/blob/master/application.yml
            val future = CloudConfig("localhost").load("application")
            val config = Await.result(future, timeout)
            config.getAs[String]("ribbon.ConnectTimeout") should not be defined
        }
    }
}
