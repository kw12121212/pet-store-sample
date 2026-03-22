## ADDED Requirements

### Requirement: search-products-by-keyword
The system MUST return a JSON list of products whose name or description contains the given keyword.

#### Scenario: keyword matches product name
- GIVEN the catalog contains a product with name "Angelfish"
- WHEN a client sends `GET /service/search_service/searchProducts?keyword=angel`
- THEN the response is a JSON object with a `products` array containing that product and its category info

#### Scenario: keyword matches product description
- GIVEN a product has description "saltwater fish"
- WHEN a client sends `GET /service/search_service/searchProducts?keyword=saltwater`
- THEN the response includes that product

#### Scenario: search is case-insensitive
- GIVEN a product named "Angelfish"
- WHEN a client searches with `keyword=ANGEL`
- THEN the response includes that product (same result as `keyword=angel`)

#### Scenario: no matching products
- GIVEN no product name or description contains "zzz"
- WHEN a client sends `GET /service/search_service/searchProducts?keyword=zzz`
- THEN the response is `{"products": []}` and the status is 200

---

### Requirement: search-items-by-keyword
The system MUST return a JSON list of items whose attribute description (attr1) contains the given keyword.

#### Scenario: keyword matches item attribute
- GIVEN an item with attr1 "Large"
- WHEN a client sends `GET /service/search_service/searchItems?keyword=large`
- THEN the response is a JSON object with an `items` array containing that item, its product name, and price

#### Scenario: search is case-insensitive
- GIVEN an item with attr1 "Adult Male"
- WHEN a client searches with `keyword=ADULT`
- THEN the response includes that item

#### Scenario: no matching items
- GIVEN no item attr1 contains "zzz"
- WHEN a client sends `GET /service/search_service/searchItems?keyword=zzz`
- THEN the response is `{"items": []}` and the status is 200
