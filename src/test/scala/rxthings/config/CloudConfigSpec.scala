package rxthings.config

import com.typesafe.config.ConfigRenderOptions
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/**
 *
 */
class CloudConfigSpec extends WordSpec with Matchers {
    "get config" should {
        "work" in {
            val config = CloudConfig.source("localhost").load("foo", "cloud")
            val result = Await.result(config, 10 seconds)
            println(result.root().render(ConfigRenderOptions.defaults().setOriginComments(false)))
        }
    }
}
