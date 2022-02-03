package ch.guengel.astro.openngc.parser

import ch.guengel.astro.coordinates.EquatorialCoordinates
import ch.guengel.astro.openngc.Catalog
import ch.guengel.astro.openngc.Constellation
import ch.guengel.astro.openngc.NgcEntry
import ch.guengel.astro.openngc.ObjectType
import ch.guengel.astro.openngc.ParserError
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.slf4j.LoggerFactory
import java.io.File

object CSVParser {
    private val logger = LoggerFactory.getLogger(CSVParser::class.java)

    fun parse(file: File): Catalog {
        val list = mutableListOf<NgcEntry>()
        var lineNumber = 2

        csvReader {
            charset = "UTF-8"
            quoteChar = '"'
            delimiter = ';'
        }.open(file) {
            readAllWithHeaderAsSequence().forEach { row ->
                try {
                    val id =
                        NameParser.parseName(row["Name"] ?: throw ParserError("'Name' not found in line"))
                    val objectType =
                        ObjectType.findByAbbrev(row["Type"] ?: throw ParserError("'Type' not found in line"))

                    val equatorialCoordinates = if (
                        row.containsKey("RA") &&
                        row.containsKey("Dec") &&
                        row["RA"]!!.isNotBlank() &&
                        row["Dec"]!!.isNotBlank()
                    ) {
                        val rightAscension =
                            RightAscensionParser.parse(row["RA"]!!)
                        val declination =
                            DeclinationParser.parse(row["Dec"]!!)
                        EquatorialCoordinates(rightAscension, declination)
                    } else null
                    val constellation =
                        row["Const"].takeIf { !it.isNullOrBlank() }?.let { Constellation.findByAbbrev(it) }

                    list.add(
                        NgcEntry(
                            id = id,
                            objectType = objectType,
                            equatorialCoordinates = equatorialCoordinates,
                            constellation = constellation,
                            majorAxis = row["MajAx"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            minorAxis = row["MinAx"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            positionAngle = row["PosAng"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            bMag = row["B-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            vMag = row["V-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            jMag = row["J-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            hMag = row["H-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            kMag = row["K-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            surfBr = row["SurfBr"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            hubble = row["Hubble"].takeIf { !it.isNullOrBlank() },
                            pax = row["Pax"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            pmRA = row["Pm-RA"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            pmDec = row["Pm-Dec"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            radVel = row["RadVel"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            redshift = row["Redshift"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            cstarUMag = row["Cstar U-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            cstarBMag = row["Cstar B-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            cstarVMag = row["Cstar V-Mag"].takeIf { !it.isNullOrBlank() }?.toDouble(),
                            messier = row["M"].takeIf { !it.isNullOrBlank() }?.toInt(),
                            ngc = row["NGC"].takeIf { !it.isNullOrBlank() },
                            ic = row["IC"].takeIf { !it.isNullOrBlank() },
                            cstarNames = row["Cstar Names"].takeIf { !it.isNullOrBlank() }?.split(','),
                            identifiers = row["Identifiers"].takeIf { !it.isNullOrBlank() }?.split(','),
                            commonNames = row["Common names"].takeIf { !it.isNullOrBlank() }?.split(','),
                            nedNotes = row["NED Notes"].takeIf { !it.isNullOrBlank() },
                            openNGCNotes = row["OpenNGC Notes"].takeIf { !it.isNullOrBlank() }
                        )
                    )
                    lineNumber++
                } catch (ex: ParserError) {
                    logger.warn("Line {} could not be parsed", lineNumber, ex)
                } catch (ex: NoSuchElementException) {
                    logger.warn("Line {} could not be parsed", lineNumber, ex)
                }
            }
        }

        return Catalog(list)
    }


}
