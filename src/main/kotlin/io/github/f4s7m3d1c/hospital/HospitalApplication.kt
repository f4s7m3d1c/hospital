package io.github.f4s7m3d1c.hospital

import io.github.f4s7m3d1c.hospital.database.VersionLogDB
import io.github.f4s7m3d1c.hospital.hospital.HospitalUpdater
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import java.sql.Connection
import java.sql.DriverManager

@SpringBootApplication
class HospitalApplication {
	@Value("\${spring.datasource.url}")
	private lateinit var dbUrl: String

	@Value("\${spring.datasource.name}")
	private lateinit var dbName: String

	@Value("\${spring.datasource.username}")
	private lateinit var dbUserName: String

	@Value("\${spring.datasource.password}")
	private lateinit var dbPassword: String

	private lateinit var dbConn: Connection

	@PostConstruct
	fun onEnable() {
		connectionDB()
	}

	@PreDestroy
	fun onDisable() {
		dbConn.close()
	}

	fun connectionDB() {
		Class.forName("org.mariadb.jdbc.Driver")
		dbConn = DriverManager.getConnection("$dbUrl/$dbName", dbUserName, dbPassword)
		VersionLogDB.init(dbConn)
	}
}

@Configuration
@EnableScheduling
class SchedulerConfig

fun main(args: Array<String>) {
	runBlocking {
		val job = HospitalUpdater.update()
		job.join()
	}
	runApplication<HospitalApplication>(*args)
}
