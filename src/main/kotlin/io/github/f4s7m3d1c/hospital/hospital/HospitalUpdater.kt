package io.github.f4s7m3d1c.hospital.hospital

import com.google.gson.Gson
import io.github.f4s7m3d1c.hospital.database.HospitalDB
import io.github.f4s7m3d1c.hospital.database.VersionLogDB
import io.github.f4s7m3d1c.hospital.version.VersionStatus
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HospitalUpdater {

	private val logger: Logger = LoggerFactory.getLogger("HospitalUpdater")

	@OptIn(DelicateCoroutinesApi::class)
	fun update() {
		logger.info("Hospital data update started.")
		GlobalScope.launch(Dispatchers.IO) {
			val newVersion: UInt = VersionLogDB.INSTANCE.getLatestVersion + 1u
			VersionLogDB.INSTANCE.createVersion(newVersion)

			var page = 1
			val client = OkHttpClient()
			var isStable = true

			while (true) {
				val request: Request = Request.Builder()
					.url(HospitalAPI.createUrl(page))
					.build()

				val response: Response = client.newCall(request).execute()
				if(!response.isSuccessful || response.body === null) {
					logger.warn("Update failed!!!\ncode: ${response.code}\nmessage: ${response.message}")
					isStable = false
					break
				}
				try {
					@Suppress("UNCHECKED_CAST")
					val body: MutableMap<String, MutableMap<String, MutableMap<String, Any>>> = Gson()
						.fromJson(response.body!!.string(), MutableMap::class.java)
							as MutableMap<String, MutableMap<String, MutableMap<String, Any>>>
					if(body["response"]!!["header"]!!["resultMsg"] == HospitalAPI.SUCCESS_NO_DATA) {
						logger.info("Update Success!!")
						break
					}
					if(body["response"]!!["header"]!!["resultMsg"] != HospitalAPI.SUCCESS_OK) {
						logger.warn("Hospital API service is not success")
						isStable = false
						break
					}
					@Suppress("UNCHECKED_CAST")
					val items: MutableList<MutableMap<String, String?>> = body["response"]!!["body"]!!["items"]
							as MutableList<MutableMap<String, String?>>
					for (item: MutableMap<String, String?> in items) {
						//TODO: Hospital DB에 저장
					}
				} catch (e: Exception) {
					logger.warn("Update failed!!!", e)
					isStable = false
					break
				}
				page++
			}
			if(!isStable) {
				VersionLogDB.INSTANCE.setVersionStatus(newVersion, VersionStatus.ERROR)
				HospitalDB.INSTANCE.removeVersion(newVersion)
			}
			logger.info("Hospital data update completed")
			VersionLogDB.INSTANCE.setVersionStatus(newVersion, VersionStatus.STABLE)
			logger.info("Staring to remove old data.")
			HospitalDB.INSTANCE.removeLowerVersion(newVersion - 2u)
			VersionLogDB.INSTANCE.updateRemoveVersions(newVersion -2u)
			logger.info("Completed removing old data")
		}
	}
}