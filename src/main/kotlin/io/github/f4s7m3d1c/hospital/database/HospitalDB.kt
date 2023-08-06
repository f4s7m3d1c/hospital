package io.github.f4s7m3d1c.hospital.database

import java.sql.Connection
import java.sql.Statement

class HospitalDB(conn: Connection) {

	companion object{
		private var instance: HospitalDB? = null

		val INSTANCE: HospitalDB get() = instance!!

		fun init(conn: Connection){
			val statement: Statement = conn.createStatement()

			statement.close()
			instance = HospitalDB(conn)
		}
	}
}