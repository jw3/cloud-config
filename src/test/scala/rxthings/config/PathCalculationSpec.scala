package rxthings.config

import akka.http.scaladsl.model.Uri
import org.scalatest.{Matchers, WordSpec}

/**
 *
 */
class PathCalculationSpec extends WordSpec with Matchers {

    "Cloud Config Loader" should {
        val uristr = "http://localhost:8080"

        "build full uri" in {
            DefaultCloudConfigSource(Uri(uristr), "master").fulluri("foo", "dev").toString shouldBe uristr + "/foo/dev/master"
        }
    }
}
