package io.github.f4s7m3d1c.hospital.scheduler

import io.github.f4s7m3d1c.hospital.hospital.HospitalUpdater
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class HospitalUpdateSchedule {

	@Scheduled(cron = "0 0 0 * * MON")
	fun onUpdate() {
		HospitalUpdater.update()
	}
}