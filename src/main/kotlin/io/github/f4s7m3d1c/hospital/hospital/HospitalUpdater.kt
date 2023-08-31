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
import java.lang.NullPointerException

object HospitalUpdater {

	private val logger: Logger = LoggerFactory.getLogger("HospitalUpdater")

	private var newVersion: UInt = 0u

	@OptIn(DelicateCoroutinesApi::class)
	fun update(): Job = GlobalScope.launch(Dispatchers.IO) {
		logger.info("Hospital data update started.")
		newVersion = VersionLogDB.INSTANCE.getLatestVersion + 1u
		VersionLogDB.INSTANCE.createVersion(newVersion)

		val client = OkHttpClient()
		var isStable = true

		try {
			getHospitalInfo(client, HospitalDiv.HOSPITAL)
			getHospitalInfo(client, HospitalDiv.CLINIC)
		} catch (e: Exception) {
			logger.warn("Update failed!!!", e)
			isStable = false
		}

		if(!isStable) {
			VersionLogDB.INSTANCE.setVersionStatus(newVersion, VersionStatus.ERROR)
			HospitalDB.INSTANCE.removeVersion(newVersion)
		}else {
			logger.info("Hospital data update completed")
			VersionLogDB.INSTANCE.setVersionStatus(newVersion, VersionStatus.STABLE)
			logger.info("Staring to remove old data.")
			HospitalDB.INSTANCE.removeLowerVersion(newVersion - 1u)
			VersionLogDB.INSTANCE.updateRemoveVersions(newVersion -1u)
			logger.info("Completed removing old data")
		}
	}

	private fun getHospitalInfo(client: OkHttpClient, div: HospitalDiv) {
		var page = 1
		while (true) {
			val request: Request = Request.Builder()
				.url(HospitalAPI.createUrl(page, div))
				.build()

			val response: Response = client.newCall(request).execute()
			if(!response.isSuccessful || response.body === null) {
				logger.warn("Update failed!!!\ncode: ${response.code}\nmessage: ${response.message}")
				throw HospitalUpdateFailedException("Response failed or No body")
			}
			@Suppress("UNCHECKED_CAST")
			val body: MutableMap<String, MutableMap<String, MutableMap<String, Any>>> = Gson()
				.fromJson(response.body!!.string(), MutableMap::class.java)
					as MutableMap<String, MutableMap<String, MutableMap<String, Any>>>
			if(body["response"]!!["header"]!!["resultMsg"] == HospitalAPI.SUCCESS_NO_DATA) {
				logger.info("Completed get hospitals info(${div.code})")
				break
			}
			if(body["response"]!!["header"]!!["resultMsg"] != HospitalAPI.SUCCESS_OK) {
				throw HospitalUpdateFailedException("Hospital API service is not success")
			}
			@Suppress("UNCHECKED_CAST")
			val items: MutableList<MutableMap<String, String?>> = body["response"]!!["body"]!!["items"]
					as MutableList<MutableMap<String, String?>>
			for (item: MutableMap<String, String?> in items) {
				try {
					HospitalDB.INSTANCE.addHospital(
						newVersion, Hospital(
							name = item["DUTYNAME"]!!,
							latitude = item["LAT"]!!.toDouble(),
							longitude = item["LON"]!!.toDouble(),
							hasER = item.getOrDefault("DUTYERYN", "2") == "1",
							openTime = OpenTime.createOpenTimeMap(
								timeMonS = item["DUTYTIME1S"], timeMonC = item["DUTYTIME1C"],
								timeTueS = item["DUTYTIME2S"], timeTueC = item["DUTYTIME2C"],
								timeWenS = item["DUTYTIME3S"], timeWenC = item["DUTYTIME3C"],
								timeThuS = item["DUTYTIME4S"], timeThuC = item["DUTYTIME4C"],
								timeFriS = item["DUTYTIME5S"], timeFriC = item["DUTYTIME5C"],
								timeSatS = item["DUTYTIME6S"], timeSatC = item["DUTYTIME6C"],
								timeSunS = item["DUTYTIME7S"], timeSunC = item["DUTYTIME7C"]
							)
						)
					)
				} catch (e: NullPointerException) {
					logger.warn("Fail!! $page:${item["DUTYNAME"]}'s latitude or longitude value is null")
				}
			}
			page++
		}
	}
}