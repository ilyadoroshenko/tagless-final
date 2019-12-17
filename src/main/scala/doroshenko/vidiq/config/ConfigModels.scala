package doroshenko.vidiq.config

case class DatabaseConfig(url: String, driver: String, user: String, password: String, poolSize: Int)
case class ServerConfig(host: String, port: Int)
case class AppConfig(db: DatabaseConfig, server: ServerConfig)