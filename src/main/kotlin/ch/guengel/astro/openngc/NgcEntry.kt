package ch.guengel.astro.openngc

import ch.guengel.astro.coordinates.EquatorialCoordinates
import ch.guengel.astro.coordinates.GeographicCoordinates
import ch.guengel.astro.coordinates.HorizontalCoordinates
import java.time.OffsetDateTime

data class NgcEntry(
    val id: NgcEntryId,
    val objectType: ObjectType,
    val equatorialCoordinates: EquatorialCoordinates? = null,
    // Constellation where the object is located
    val constellation: Constellation? = null,
    // Major axis, expressed in arcmin
    val majorAxis: Double? = null,
    // Minor axis, expressed in arcmin
    val minorAxis: Double? = null,
    // Major axis position angle (North Eastwards)
    val positionAngle: Double? = null,
    // Apparent total magnitude in B filter
    val bMag: Double? = null,
    // Apparent total magnitude in V filter
    val vMag: Double? = null,
    // Apparent total magnitude in J filter
    val jMag: Double? = null,
    // Apparent total magnitude in H filter
    val hMag: Double? = null,
    // Apparent total magnitude in K filter
    val kMag: Double? = null,
    // Mean surface brightness within 25 mag isophot (B-band), expressed in mag/arcsec2
    val surfBr: Double? = null,
    // Morphological type (for galaxies)
    val hubble: String? = null,
    // Parallax, expressed in milliarcseconds
    val pax: Double? = null,
    // Proper motion in RA, expressed in milliarcseconds/year
    val pmRA: Double? = null,
    // Proper motion in Dec, expressed in milliarcseconds/year
    val pmDec: Double? = null,
    // Radial velocity (heliocentric), expressed in km/s
    val radVel: Double? = null,
    // Redshift (heliocentric)
    val redshift: Double? = null,
    // Apparent magnitude of central star in U filter
    val cstarUMag: Double? = null,
    // Apparent magnitude of central star in B filter
    val cstarBMag: Double? = null,
    // Apparent magnitude of central star in V filter
    val cstarVMag: Double? = null,
    // cross reference Messier number
    val messier: Int? = null,
    // other NGC identification, if the object is listed twice in the catalog
    val ngc: String? = null,
    // cross reference IC number, if the object is also listed with that identification
    val ic: String? = null,
    // central star identifications
    val cstarNames: List<String>? = null,
    // cross reference with other catalogs
    val identifiers: List<String>? = null,
    // Common names of the object if any
    val commonNames: List<String>? = null,
    // notes about object exported from NED
    val nedNotes: String? = null,
    // notes about the object data from OpenNGC catalog
    val openNGCNotes: String? = null,
) {
    val name: String get() = id.toString()
    fun isMessier() = messier != null
}

data class ExtendedNgcEntry(
    val ngcEntry: NgcEntry,
    val horizontalCoordinates: HorizontalCoordinates,
    val observerDateTime: OffsetDateTime,
    val observerCoordinates: GeographicCoordinates,
)
