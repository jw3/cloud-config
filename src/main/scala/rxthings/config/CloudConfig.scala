package rxthings.config

import java.net.URL

import akka.http.scaladsl.model.Uri
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigSyntax}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/**
 * Provides a mechanism through which to load configurations as
 * a [[Config]] from a Spring Cloud Configuration Server
 *
 * CLOUD_CONFIG_PROFILE environment variable can be set to override any profile specified in code
 */
trait CloudConfig {
    def load(app: String, profile: String = CloudConfig.profileEnvDefault)(implicit ec: ExecutionContext): Future[Config]
}

object CloudConfig {
    val profileEnvKey = "CLOUD_CONFIG_PROFILE"
    val profileEnvDefault = "development"

    /**
     * Specify the server, port, and label to build a [[CloudConfig]]
     *
     * @param host
     * @param port
     * @param branch optional, defaults to master
     * @return [[CloudConfig]] connection to the cloud server
     */
    def apply(host: String, port: Int = 8888, branch: String = "master"): CloudConfig = {
        DefaultCloudConfig(Uri(s"http://$host:$port"), branch)
    }
}

// default implementation of [[CloudConfig]]
private[config] case class DefaultCloudConfig(uri: Uri, branch: String) extends CloudConfig with LazyLogging {
    val parseOptions = ConfigParseOptions.defaults().setSyntax(ConfigSyntax.JSON)

    def load(app: String, profile: String)(implicit ec: ExecutionContext): Future[Config] = {
        val url = new URL(fulluri(app, profile).toString)
        Future {
            logger.debug(s"CloudConfig @ $url")
            ConfigFactory.parseURL(url, parseOptions)
        }
    }

    def fulluri(app: String, profile: String): Uri = {
        val fullpath = uri.path / branch / s"$app-${resolveProfile(profile)}.json"
        uri.withPath(fullpath)
    }

    // allows the profile to be overridden by env var
    def resolveProfile(profile: String) = {
        require(profile.nonEmpty, "profile cannot be empty")
        sys.env.getOrElse(CloudConfig.profileEnvKey, profile)
    }
}
