package rxthings.config

import java.net.URL

import akka.http.scaladsl.model.Uri
import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigSyntax}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Reusable connection to a configuration server
 */
trait CloudConfigSource {
    def load(app: String, profile: String)(implicit ec: ExecutionContext): Future[Config]
}

/**
 * Load Config from Spring Cloud Configuration Server
 */
object CloudConfig {
    val profileEnvKey = "APP_CONFIG_PROFILE"
    val profileEnvDefault = "development"

    /**
     * create a connection to the server and specify a branch
     */
    def source(host: String, port: Int = 8888, branch: String = "master"): CloudConfigSource = {
        DefaultCloudConfigSource(Uri(s"http://$host:$port"), branch)
    }
}

private[config] case class DefaultCloudConfigSource(uri: Uri, branch: String) extends CloudConfigSource {
    val parseOptions = ConfigParseOptions.defaults().setSyntax(ConfigSyntax.JSON)

    def load(app: String, profile: String)(implicit ec: ExecutionContext): Future[Config] = {
        val url = new URL(fulluri(app, profile).toString)
        Future {
            ConfigFactory.parseURL(url, parseOptions)
        }
    }

    def fulluri(app: String, profile: String = ""): Uri = {
        val fullpath = uri.path / app / resolveProfile(profile) / branch
        uri.withPath(fullpath)
    }

    def resolveProfile(profile: String) = {
        if (profile.isEmpty) sys.env.getOrElse(CloudConfig.profileEnvKey, CloudConfig.profileEnvDefault) else profile
    }
}
