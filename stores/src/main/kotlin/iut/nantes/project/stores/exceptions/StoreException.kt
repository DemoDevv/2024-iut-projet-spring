package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException

// Exception levée lorsqu'un store n'est pas trouvé (HTTP 404)
class StoreNotFoundException() : RuntimeException()

// Exception levé lorsqu'un produit n'est pas présent dans le store (HTTP 404)
class ProductNotPresentInStoreException() : RuntimeException()

// Exception levé lorsqu'on essaye d'enlever plus de produit présent dans le stock du magasin (HTTP 409)
class ExcessiveProductRemovalException() : RuntimeException()

// Exception levé lorsqu'on veut supprimer un produit en doublon (HTTP 400)
class DuplicateElementsException() : RuntimeException()
