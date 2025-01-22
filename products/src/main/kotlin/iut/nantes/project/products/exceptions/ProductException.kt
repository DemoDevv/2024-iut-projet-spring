package iut.nantes.project.products.exceptions

import java.lang.RuntimeException

// Exception levée lorsqu'un produit n'est pas trouvé (HTTP 404)
class ProductNotFoundException(message: String?): RuntimeException(message ?: "")

// Exception levée lorsque du stock est encore disponible pour ce produit
class ProductNotDeletableException(message: String?): RuntimeException(message ?: "")