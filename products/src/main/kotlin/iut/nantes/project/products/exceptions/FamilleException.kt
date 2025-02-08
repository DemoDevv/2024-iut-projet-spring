package iut.nantes.project.products.exceptions

// caught if there is name  conflict (HTTP 409)
class FamilleNameConflictException(message: String) : RuntimeException(message)

// caught when family not found (HTTP 404)
class FamilleNotFoundException(message: String?) : RuntimeException(message ?: "")

// caught  if invalid id(HTTP 400)
class InvalidIdFormatException(message: String) : RuntimeException(message)

// caught if family has linkeds products (HTTP 409)
class FamilleHasLinkedProductsException(message: String) : RuntimeException(message)
