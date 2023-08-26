package io.github.f4s7m3d1c.hospital.database

import io.github.f4s7m3d1c.hospital.version.VersionInfo
import io.github.f4s7m3d1c.hospital.version.VersionStatus
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class VersionLogDB(conn: Connection) {

	companion object {
		private var instance: VersionLogDB? = null

		val INSTANCE: VersionLogDB get() = instance!!

		fun init(conn: Connection){
			val statement: Statement = conn.createStatement()
			statement.execute("CREATE TABLE IF NOT EXISTS `version_log_db` (\n" +
					"  `version` INT UNSIGNED PRIMARY KEY,\n" +
					"  `date` DATE NOT NULL,\n" +
					"  `status` TINYINT UNSIGNED NOT NULL\n" +
					");")
			statement.close()
			instance = VersionLogDB(conn)
		}
	}

	private val latestVersionStatement: PreparedStatement
		= conn.prepareStatement("SELECT COALESCE(MAX(`version`), 0) AS `latest_version` FROM `version_log_db`;")

	val getLatestVersion: UInt get() {
		val result: ResultSet = latestVersionStatement.executeQuery()
		result.next()
		return result.getLong("latest_version").toUInt()
	}

	private val latestStableVersionStatement: PreparedStatement
		= conn.prepareStatement("SELECT COALESCE(MAX(`version`), 0) AS `latest_version` FROM `version_log_db` WHERE `status` = 200;")

	val getLatestStableVersion: UInt get() {
		val result: ResultSet = latestStableVersionStatement.executeQuery()
		result.next()
		return result.getLong("latest_version").toUInt()
	}

	private val versionInfoStatement: PreparedStatement
		= conn.prepareStatement("SELECT * FROM `version_log_db` WHERE `version` = ?;")

	fun getVersionInfo(version: UInt): VersionInfo{
		versionInfoStatement.setLong(1, version.toLong())
		val result: ResultSet = versionInfoStatement.executeQuery()
		result.next()
		return VersionInfo(
			result.getLong("version").toUInt(),
			result.getDate("date"),
			VersionStatus.forCode(result.getInt("status").toUByte())
		)
	}

	private val createVersionStatement: PreparedStatement
		= conn.prepareStatement("INSERT INTO `version_log_db`(`version`, `date`, `status`) VALUES (?, NOW(), ?);")

	fun createVersion(version: UInt, status: VersionStatus = VersionStatus.LOADING) : Boolean {
		createVersionStatement.setLong(1, version.toLong())
		createVersionStatement.setInt(2, status.code.toInt())
		return createVersionStatement.execute()
	}

	private val setVersionStatusStatement
		= conn.prepareStatement("UPDATE `version_log_db` SET `status`=? WHERE `version`=?;")

	fun setVersionStatus(version: UInt, status: VersionStatus) : Boolean{
		setVersionStatusStatement.setInt(1, status.code.toInt())
		setVersionStatusStatement.setInt(2, version.toInt())
		return setVersionStatusStatement.execute()
	}

	private val updateRemoveVersionsStatement
			= conn.prepareStatement("UPDATE `version_log_db` SET `status`=44 WHERE `version` <= ?;")

	fun updateRemoveVersions(version: UInt) : Boolean{
		updateRemoveVersionsStatement.setInt(1, version.toInt())
		return updateRemoveVersionsStatement.execute()
	}
}