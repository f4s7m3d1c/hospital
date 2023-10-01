package io.github.f4s7m3d1c.hospital.service

import com.google.gson.Gson
import io.github.f4s7m3d1c.hospital.entity.Hospital
import io.github.f4s7m3d1c.hospital.entity.OpenTime
import io.github.f4s7m3d1c.hospital.entity.Version
import io.github.f4s7m3d1c.hospital.hospital.HospitalAPI
import io.github.f4s7m3d1c.hospital.hospital.HospitalDiv
import io.github.f4s7m3d1c.hospital.hospital.HospitalUpdateFailedException
import io.github.f4s7m3d1c.hospital.repository.HospitalRepo
import io.github.f4s7m3d1c.hospital.repository.VersionRepo
import io.github.f4s7m3d1c.hospital.version.VersionStatus
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.*

@Service
class HospitalUpdater(
	private val versionRepo: VersionRepo,
	private val hospitalRepo: HospitalRepo
) {

	private val logger: Logger = LoggerFactory.getLogger("HospitalUpdater")

	fun checkUpdate() {
		if(versionRepo.findFirstByStatusOrderByVersionDesc(VersionStatus.STABLE) === null) {
			update()
		}
	}

	@OptIn(DelicateCoroutinesApi::class)
	fun update(): Job = GlobalScope.launch(Dispatchers.IO) {
		logger.info("Hospital data update started.")
		val newVersion: Long = (versionRepo.findFirstByOrderByVersionDesc()?.version ?: 0) + 1
		val version = Version(newVersion, Date())
		versionRepo.save(version)

		val client = OkHttpClient()
		var isStable = true

		try {
			getHospitalInfo(newVersion, client, HospitalDiv.GENERAL)
			getHospitalInfo(newVersion, client, HospitalDiv.HOSPITAL)
			getHospitalInfo(newVersion, client, HospitalDiv.CLINIC)
		} catch (e: Exception) {
			logger.warn("Update failed!!!", e)
			isStable = false
		}

		if(!isStable) {
			versionRepo.save(version.setStatus(VersionStatus.ERROR))
			hospitalRepo.deleteByVersion(newVersion)
		}else {
			logger.info("Hospital data update completed")
			versionRepo.findFirstByStatusOrderByVersionDesc(VersionStatus.STABLE).let {
				if(it !== null) {
					versionRepo.save(it.setStatus(VersionStatus.DELETE))
				}
			}
			versionRepo.save(version.setStatus(VersionStatus.STABLE))
			logger.info("Staring to remove old data.")
			hospitalRepo.deleteByVersionLessThan(newVersion)
			logger.info("Completed removing old data")
		}
	}

	private fun getHospitalInfo(newVersion: Long, client: OkHttpClient, div: HospitalDiv) {
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
				logger.info("Completed get hospitals info(${div.code}: $page)")
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
					hospitalRepo.save(
						Hospital(
							name = item["DUTYNAME"]!!,
							version = newVersion,
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