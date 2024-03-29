package com.ludovic.vimont.nasaapod

import com.ludovic.vimont.nasaapod.model.Photo

object MockModel {
    private const val DEFAULT_TITLE = "Ou4: A Giant Squid in a Flying Bat"
    private const val DEFAULT_EXPLANATION = "A very faint but very large squid-like nebula is visible in planet Earth's sky " +
            "-- but inside a still larger bat.  The Giant Squid Nebula cataloged as Ou4, and Sh2-129 " +
            "also known as the Flying Bat Nebula, are both caught in this cosmic scene toward the royal " +
            "royal constellation Cepheus. Composed with 55 hours of narrowband image data, the telescopic " +
            "field of view is 3 degrees or 6 Full Moons across. Discovered in 2011 by French astro-imager " +
            "Nicolas Outters, the Squid Nebula's alluring bipolar shape is distinguished here by the telltale " +
            "blue-green emission from doubly ionized oxygen atoms. Though apparently completely surrounded by " +
            "the reddish hydrogen emission region Sh2-129, the true distance and nature of the Squid Nebula have" +
            " been difficult to determine. Still, a more recent investigation suggests Ou4 really does lie within" +
            " Sh2-129 some 2,300 light-years away. Consistent with that scenario, Ou4 would represent a spectacular " +
            "outflow driven by HR8119, a triple system of hot, massive stars seen near the center of the nebula. " +
            "The truly giant Squid Nebula would physically be nearly 50 light-years across.   " +
            "New: APOD Mirror in Turkish from Rasyonalist"

    fun buildPhoto(
        title: String = DEFAULT_TITLE,
        explanation: String = DEFAULT_EXPLANATION,
        url: String = "https://google.fr/test.png",
        mediaType: String = "image",
        date: String = "2020-10-07",
    ) = Photo(
        title = title,
        date = date,
        url = url,
        hdurl = "https://apod.nasa.gov/apod/image/2010/SquidBat_Akar_4485.jpg",
        explanation = explanation,
        mediaType = mediaType,
        serviceVersion = "v1",
        copyright = "Yannick Akar",
    )
}