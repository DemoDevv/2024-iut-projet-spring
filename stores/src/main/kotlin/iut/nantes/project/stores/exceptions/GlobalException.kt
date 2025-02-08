package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException

// caught is the id is invalid (HTTP 400)
class InvalidIdFormatException(): RuntimeException()

// caught when there is conflict(HTTP 409)
class ConflictException() : RuntimeException()

// caught when a parameter is incorrect (HTTP 400)
class InvalidRequestParameters() : RuntimeException()