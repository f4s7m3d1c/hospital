package io.github.f4s7m3d1c.hospital.database

import io.github.f4s7m3d1c.hospital.hospital.Hospital
import io.github.f4s7m3d1c.hospital.hospital.OpenTime
import io.github.f4s7m3d1c.hospital.math.Vector2
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
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

	fun removeVersion(version: UInt) : Boolean {
		removeVersionStatement.setLong(1, version.toLong())
		return removeVersionStatement.execute()
	}

	private val removeLowerVersionStatement: PreparedStatement
		= conn.prepareStatement("DELETE FROM `hospital_info_db` WHERE `version`<=?;")

	fun removeLowerVersion(version: UInt) : Boolean {
		removeLowerVersionStatement.setLong(1, version.toLong())
		return removeLowerVersionStatement.execute()
	}

	private val getHospitalsStatement: PreparedStatement
		= conn.prepareStatement("SELECT `name`, `latitude`, `longitude`, `hasER`, `timeMon`, `timeTue`, `timeWen`, `timeThu`, `timeFri`, `timeSat`, `timeSun`\n" +
			"FROM `hospital_info_db`\n" +
			"WHERE `version` = ? AND\n" +
			"    (`latitude` BETWEEN ? AND ?) AND\n" +
			"    (`longitude` BETWEEN ? AND ?);")

	fun getHospitals(pair: Pair<Vector2, Vector2>): MutableList<Hospital> {
		val version: UInt = VersionLogDB.INSTANCE.getLatestStableVersion
		getHospitalsStatement.setLong(1, version.toLong())
		getHospitalsStatement.setDouble(2, pair.first.lat)
		getHospitalsStatement.setDouble(3, pair.second.lat)
		getHospitalsStatement.setDouble(4, pair.first.lon)
		getHospitalsStatement.setDouble(5, pair.second.lon)
		val rows: ResultSet = getHospitalsStatement.executeQuery()
		val hospitals: MutableList<Hospital> = mutableListOf()
		while (rows.next()){
			hospitals += Hospital(
				name = rows.getString("name"),
				latitude = rows.getDouble("latitude"),
				longitude = rows.getDouble("longitude"),
				hasER = rows.getBoolean("hasER"),
				OpenTime(
					mon = rows.getString("timeMon"),
					tue = rows.getString("timeTue"),
					wen = rows.getString("timeWen"),
					thu =  rows.getString("timeThu"),
					fri = rows.getString("timeFri"),
					sat = rows.getString("timeSat"),
					sun = rows.getString("timeSun")
				)
			)
		}
		return hospitals
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

	fun addHospital(version: UInt, hospital: Hospital) : Boolean {
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
		return addHospitalStatement.execute()
	}
}