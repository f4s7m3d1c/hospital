package io.github.f4s7m3d1c.hospital.scheduler

import io.github.f4s7m3d1c.hospital.hospital.HospitalUpdater
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.Statement

@Component
@EnableScheduling
class HospitalSchedule {
	companion object {
		private lateinit var conn: Connection

		fun registerConnection(conn: Connection) {
			this.conn = conn
		}
	}

	@Scheduled(cron = "0 0 0 * * MON")
	fun onUpdate() {
		HospitalUpdater.update()
	}

	@Scheduled(fixedRate = 1800000)
	fun performDatabaseHeartbeat() {
		val statement: Statement = conn.createStatement()
		statement.execute("SELECT 1")
		statement.close()
	}
}