package io.github.f4s7m3d1c.hospital.database

import io.github.f4s7m3d1c.hospital.hospital.Hospital
import java.sql.Connection
import java.sql.Statement

class HospitalDB(conn: Connection) {

	companion object{
		private var instance: HospitalDB? = null

		val INSTANCE: HospitalDB get() = instance!!

		fun init(conn: Connection){
			val statement: Statement = conn.createStatement()
			statement.execute("CREATE TABLE IF NOT EXISTS `hospital_info_db` (\n" +
					"    `version` INT UNSIGNED NOT NULL,\n" +
					"    `name` VARCHAR(50) NOT NULL,\n" +
					"    `latitude` DECIMAL(20, 16) NOT NULL ,\n" +
					"    `longitude` DECIMAL(20, 16) NOT NULL ,\n" +
					"    `hasER` TINYINT(1) NOT NULL,\n" +
					"    `timeMon` VARCHAR(20) NOT NULL,\n" +
					"    `timeTue` VARCHAR(20) NOT NULL,\n" +
					"    `timeWen` VARCHAR(20) NOT NULL,\n" +
					"    `timeThu` VARCHAR(20) NOT NULL,\n" +
					"    `timeFri` VARCHAR(20) NOT NULL,\n" +
					"    `timeSat` VARCHAR(20) NOT NULL,\n" +
					"    `timeSun` VARCHAR(20) NOT NULL\n" +
					")")
			statement.close()
			instance = HospitalDB(conn)
		}
	}

	fun removeVersion(version: UInt) {
		//TODO
	}

	fun getHospitals(latitude: Double, longitude: Double) {
		//TODO
	}

	fun addHospital(hospital: Hospital) {
		//TODO
	}
}