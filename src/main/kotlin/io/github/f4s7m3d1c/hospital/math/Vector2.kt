package io.github.f4s7m3d1c.hospital.math

import kotlin.math.*
import java.lang.Math.toRadians
import java.lang.Math.toDegrees

private const val EARTH_RADIUS = 6371.0 // 지구 반지름

class Vector2(val lat: Double, val lon: Double) {
	override fun toString(): String {
		return "Vector2($lat, $lon)"
	}

	fun distance(vector: Vector2): Double {
		val lat1Rad: Double = toRadians(lat)
		val lon1Rad: Double = toRadians(lon)
		val lat2Rad: Double = toRadians(vector.lat)
		val lon2Rad: Double = toRadians(vector.lon)

		val latDiff: Double = (lat2Rad - lat1Rad) / 2
		val lonDiff: Double = (lon2Rad - lon1Rad) / 2

		val a: Double = sin(latDiff) * sin(latDiff) + cos(lat1Rad) * cos(lat2Rad) * sin(lonDiff) * sin(lonDiff)
		val c: Double = 2 * atan2(sqrt(a), sqrt(1 - a))

		return EARTH_RADIUS * c
	}

	fun calculateBoundingBox(sideLength: Double = 2.0): Pair<Vector2, Vector2> {
		val latDiff: Double = toDegrees(sideLength / (2 * EARTH_RADIUS))
		val lonDiff: Double = toDegrees(sideLength / (2 * EARTH_RADIUS * cos(toRadians(lat))))

		val minLat: Double = lat - latDiff
		val maxLat: Double = lat + latDiff
		val minLon: Double = lon - lonDiff
		val maxLon: Double = lon + lonDiff

		return Pair(
			Vector2(minLat, minLon),
			Vector2(maxLat, maxLon)
		)
	}
}