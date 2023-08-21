package io.github.f4s7m3d1c.hospital

import io.github.f4s7m3d1c.hospital.database.HospitalDB
import io.github.f4s7m3d1c.hospital.database.VersionLogDB
import io.github.f4s7m3d1c.hospital.hospital.HospitalAPI
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

	@Value("\${spring.datasource.username}")
	private lateinit var dbUserName: String

	@Value("\${spring.datasource.password}")
	private lateinit var dbPassword: String

	private lateinit var dbConn: Connection

	@Value("\${hospital.api.key}")
	private lateinit var key: String

	@PostConstruct
	fun onEnable() {
		connectionDB()
		HospitalAPI.initialKey(key)
		runBlocking {
			if(VersionLogDB.INSTANCE.getLatestVersion == 0u) {
				val job = HospitalUpdater.update()
				job.join()
			}
		}
	}

	@PreDestroy
	fun onDisable() {
		dbConn.close()
	}

	fun connectionDB() {
		Class.forName("org.mariadb.jdbc.Driver")
		dbConn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)
		VersionLogDB.init(dbConn)
		HospitalDB.init(dbConn)
	}
}

@Configuration
@EnableScheduling
class SchedulerConfig

fun main(args: Array<String>) {
	runApplication<HospitalApplication>(*args)
}
