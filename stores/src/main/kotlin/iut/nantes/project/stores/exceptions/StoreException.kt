package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException

// when store not found (HTTP 404)
class StoreNotFoundException() : RuntimeException()

// when product is not present in a store(HTTP 404)
class ProductNotPresentInStoreException() : RuntimeException()

// when it tries to delete too much quantity at a product (HTTP 409)
class ExcessiveProductRemovalException() : RuntimeException()

// caught when it tries to delete duplicates elements (HTTP 400)
class DuplicateElementsException() : RuntimeException()
