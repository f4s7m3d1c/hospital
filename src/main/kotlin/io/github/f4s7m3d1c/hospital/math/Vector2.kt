package io.github.f4s7m3d1c.hospital.math

import kotlin.math.*
import java.lang.Math.toRadians
import java.lang.Math.toDegrees

private const val EARTH_RADIUS = 6371.0 // 지구 반지름

class Vector2(val latitude: Double, val longitude: Double) {

	fun distance(vector: Vector2): Double {
		val lat1Rad: Double = toRadians(latitude)
		val lon1Rad: Double = toRadians(longitude)
		val lat2Rad: Double = toRadians(vector.latitude)
		val lon2Rad: Double = toRadians(vector.longitude)

		val latDiff: Double = (lat2Rad - lat1Rad) / 2
		val lonDiff: Double = (lon2Rad - lon1Rad) / 2

		val a: Double = sin(latDiff) * sin(latDiff) + cos(lat1Rad) * cos(lat2Rad) * sin(lonDiff) * sin(lonDiff)
		val c: Double = 2 * atan2(sqrt(a), sqrt(1 - a))

		return EARTH_RADIUS * c
	}

	fun calculateBoundingBox(sideLength: Double = 2.0): Pair<Vector2, Vector2> {
		val latDiff: Double = toDegrees(sideLength / (2 * EARTH_RADIUS))
		val lonDiff: Double = toDegrees(sideLength / (2 * EARTH_RADIUS * cos(toRadians(latitude))))

		val minLat: Double = latitude - latDiff
		val maxLat: Double = latitude + latDiff
		val minLon: Double = longitude - lonDiff
		val maxLon: Double = longitude + lonDiff

		return Pair(
			Vector2(minLat, minLon),
			Vector2(maxLat, maxLon)
		)
	}
}