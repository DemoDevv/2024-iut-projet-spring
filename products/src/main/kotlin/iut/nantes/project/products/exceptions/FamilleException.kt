package iut.nantes.project.products.exceptions

// Exception levée lorsqu'il y a un conflit de nom (HTTP 409)
class FamilleNameConflictException(message: String) : RuntimeException(message)

// Exception levée lorsqu'une famille n'est pas trouvée (HTTP 404)
class FamilleNotFoundException(message: String) : RuntimeException(message)

// Exception levée si l'ID est invalide (HTTP 400)
class InvalidIdFormatException(message: String) : RuntimeException(message)

// Exception levée si des produits sont encore liés à la famille (HTTP 409)
class FamilleHasLinkedProductsException(message: String) : RuntimeException(message)
