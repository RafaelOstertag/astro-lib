package ch.guengel.astro.openngc

class ParserError(message: String, throwable: Throwable? = null) : RuntimeException(message, throwable)
class InternalError(message: String) : RuntimeException(message)
