package iut.nantes.project.products.exceptions

import java.lang.RuntimeException

// caught if product not found (HTTP 404)
class ProductNotFoundException(message: String?): RuntimeException(message ?: "")

// caught if products still here (>0) in a store (HTTP 409)
class ProductNotDeletableException(message: String?): RuntimeException(message ?: "")