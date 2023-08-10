package io.github.f4s7m3d1c.hospital.database

import io.github.f4s7m3d1c.hospital.hospital.Hospital
import java.sql.Connection
import java.sql.PreparedStatement
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

	private val removeVersionStatement: PreparedStatement
		= conn.prepareStatement("DELETE FROM `hospital_info_db` WHERE `version` = ?;")

	fun removeVersion(version: UInt) {
		removeVersionStatement.setLong(1, version.toLong())
		removeVersionStatement.executeQuery()
	}

	private val removeLowerVersionStatement: PreparedStatement
		= conn.prepareStatement("DELETE FROM `hospital_info_db` WHERE `version`<=?;")

	fun removeLowerVersion(version: UInt) {
		removeLowerVersionStatement.setLong(1, version.toLong())
		removeLowerVersionStatement.executeQuery()
	}

	fun getHospitals(latitude: Double, longitude: Double) {
		val version: UInt = VersionLogDB.INSTANCE.getLatestStableVersion
		//TODO
	}

	private val addHospitalStatement: PreparedStatement
		= conn.prepareStatement("INSERT INTO `hospital_info_db`(\n" +
			"    `version`,\n" +
			"    `name`,\n" +
			"    `latitude`,\n" +
			"    `longitude`,\n" +
			"    `hasER`,\n" +
			"    `timeMon`,\n" +
			"    `timeTue`,\n" +
			"    `timeWen`,\n" +
			"    `timeThu`,\n" +
			"    `timeFri`,\n" +
			"    `timeSat`,\n" +
			"    `timeSun`\n" +
			") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")

	fun addHospital(version: UInt, hospital: Hospital) {
		addHospitalStatement.setLong(1, version.toLong())
		addHospitalStatement.setString(2, hospital.name)
		addHospitalStatement.setDouble(3, hospital.latitude)
		addHospitalStatement.setDouble(4, hospital.longitude)
		addHospitalStatement.setBoolean(5, hospital.hasER)
		addHospitalStatement.setString(6, hospital.openTime.mon)
		addHospitalStatement.setString(7, hospital.openTime.tue)
		addHospitalStatement.setString(8, hospital.openTime.wen)
		addHospitalStatement.setString(9, hospital.openTime.thu)
		addHospitalStatement.setString(10, hospital.openTime.fri)
		addHospitalStatement.setString(11, hospital.openTime.sat)
		addHospitalStatement.setString(12, hospital.openTime.sun)
		addHospitalStatement.executeQuery()
	}
}