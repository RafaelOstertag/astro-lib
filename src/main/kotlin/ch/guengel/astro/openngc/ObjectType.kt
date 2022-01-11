package ch.guengel.astro.openngc

enum class ObjectType(
    val abbrev: String,
    val description: String,
) {
    STAR("*", "Star"),
    DOUBLE_STAR("**", "Double star"),
    STAR_ASSOC("*Ass", "Association of stars"),
    O_CLUSTER("OCl", "Open Cluster"),
    G_CLUSTER("GCl", "Globular Cluster"),
    CL_NEB("Cl+N", "Star cluster + Nebula"),
    GALAXY("G", "Galaxy"),
    GALAXY_PAIR("GPair", "Galaxy Pair"),
    GALAXY_TRIPLET("GTrpl", "Galaxy Triplet"),
    GALAXY_GROUP("GGroup", "Group of galaxies"),
    P_NEBULA("PN", "Planetary Nebula"),
    HII("HII", "HII Ionized region"),
    DARK_NEBULA("DrkN", "Dark Nebula"),
    EM_NEBULA("EmN", "Emission Nebula"),
    NEBULA("Neb", "Nebula"),
    REF_NEBULA("RfN", "Reflection Nebula"),
    SNR("SNR", "Supernova remnant"),
    NOVA("Nova", "Nova star"),
    NON_EXISTENT("NonEx", "Nonexistent object"),
    DUP("Dup", "Duplicated object"),
    OTHER("Other", "Other classification");

    companion object {
        fun findByAbbrev(needle: String): ObjectType =
            ObjectType.values().find { it.abbrev == needle }
                ?: throw NoSuchElementException("Cannot find objectType with abbreviation $needle")
    }
}
