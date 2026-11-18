# Guía rápida del parcial

## 1. Antes de correr
- `src/main/resources/application.properties` → cambiar `spring.datasource.url`, `username`, `password` por la BD que den.
- Si el front se publica en otra URL, agregarla en `app.cors.allowed-origins` (separadas por coma).
- Correr: `BackendApplication` → Run As → Spring Boot App (o `mvnw spring-boot:run`). Swagger: http://localhost:8080/

## 2. Usuarios precargados
| usuario | clave | rol |
|---|---|---|
| admin | 1234567890 | ADMIN |
| normaluser | 1234567890 | USER |

## 3. Contrato con el front (NO cambiar)
| Endpoint | Body | Respuesta |
|---|---|---|
| `POST /auth/login` | `{"username","password"}` | 200 `{"token","role"}` / 401 texto |
| `POST /auth/register` | `{"username","password","role"}` | 201 texto / 409 ya existe (`role` opcional, por defecto USER) |
| Resto | header `Authorization: Bearer <token>` | sin token o token malo → 401, sin rol → 403 |

CORS ya habilitado para `http://localhost:4200` y `http://localhost:4201`.

## 4. Crear la entidad que pidan (copiar la plantilla Item)
Archivos a copiar/renombrar (Ctrl+F `Item` → `NuevaEntidad`, `item` → `nuevaentidad`):
1. `model/Item.java` → cambiar atributos, getters/setters.
2. `dto/ItemDTO.java` → **mismos nombres de atributos** que la entidad (ModelMapper).
3. `repository/ItemRepository.java` → métodos `findByX` que pidan.
4. `service/ItemService.java` → ajustar validación en `create` y los `set` de `updateById`.
5. `controller/ItemController.java` → cambiar `@RequestMapping("/item")`.
6. `security/SecurityConfig.java` → cambiar la regla `"/item/**"` (ahí está comentado cómo dejar escritura solo para ADMIN).

Fechas en JSON: `"2026-11-17"` (LocalDate).

## 5. Endpoints de la plantilla
`POST /item/createjson`, `GET /item/getall`, `GET /item/search?nombre=`, `GET /item/count`,
`GET /item/exists/{id}`, `GET /item/getbyid/{id}`, `PUT /item/updatejson?id=`, `DELETE /item/deletebyid/{id}`

Usuarios: `/user/getall`, `/user/count`, `/user/exists/{id}`, `/user/getbyid/{id}` (USER o ADMIN);
`/user/createjson`, `/user/updatejson?id=`, `/user/deletebyid/{id}`, `/user/deletebyname?name=` (solo ADMIN).
