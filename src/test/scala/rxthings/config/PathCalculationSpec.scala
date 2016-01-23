package rxthings.config

import akka.http.scaladsl.model.Uri
import org.scalatest.{Matchers, WordSpec}

/**
 *
 */
class PathCalculationSpec extends WordSpec with Matchers {

    "Cloud Config Loader" should {
        val uristr = "http://localhost:8080"
        val uri = Uri(uristr)

        "specified profile" in {
            DefaultCloudConfig(uri, "m").fulluri("foo", "dev") shouldBe Uri(s"$uristr/m/foo-dev.json")
        }
    }
}
