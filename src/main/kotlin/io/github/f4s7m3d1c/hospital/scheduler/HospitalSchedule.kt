package io.github.f4s7m3d1c.hospital.scheduler

import io.github.f4s7m3d1c.hospital.service.HospitalUpdater
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class HospitalSchedule(
	private val updater: HospitalUpdater
) {

	@Scheduled(cron = "0 0 0 * * MON")
	fun onUpdate() {
		updater.update()
	}
}