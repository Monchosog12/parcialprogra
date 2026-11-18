package co.edu.unbosque.backend.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.backend.dto.FoodDTO;
import co.edu.unbosque.backend.dto.NutrienteDTO;

/**
 * Utilidad para consumir APIs externas. Incluye la busqueda en FoodData Central
 * (USDA) usada por CarneController y LacteoController.
 *
 * La API key se lee de la variable de entorno API_KEY_FOOD_DATA_CENTRAL; si no
 * existe se usa DEMO_KEY (limite bajo de peticiones por hora).
 */
public class ExternalHTTPRequestHandler {

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	private static final Gson GSON = new GsonBuilder().create();
	private static final String API_KEY_FOOD_DATA_CENTRAL = resolveApiKey();
	private static final String URL_BASE = "https://api.nal.usda.gov/fdc/v1";

	/** Categorias (foodCategory) de USDA que cuentan como carne. */
	public static final List<String> CATEGORIAS_CARNE = List.of("Beef Products", "Pork Products",
			"Poultry Products", "Lamb, Veal, and Game Products", "Sausages and Luncheon Meats");

	/** Categorias (foodCategory) de USDA que cuentan como lacteo. */
	public static final List<String> CATEGORIAS_LACTEO = List.of("Dairy and Egg Products");

	public static String getApiKeyFoodDataCentral() {
		return API_KEY_FOOD_DATA_CENTRAL;
	}

	public static String getUrlBase() {
		return URL_BASE;
	}

	private ExternalHTTPRequestHandler() {
	}

	private static String resolveApiKey() {
		String key = System.getenv("API_KEY_FOOD_DATA_CENTRAL");
		return (key == null || key.isBlank()) ? "DEMO_KEY" : key;
	}

	private static HttpResponse<String> send(HttpRequest request) {
		try {
			HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				// Se usa getPath() para no exponer la api_key en logs/errores
				throw new RuntimeException(
						"La API externa respondio " + response.statusCode() + " en " + request.uri().getPath());
			}
			return response;
		} catch (IOException e) {
			throw new RuntimeException("Error llamando " + request.uri().getPath(), e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Peticion interrumpida " + request.uri().getPath(), e);
		}
	}

	// ===================== Metodos genericos =====================

	/** GET y convierte el JSON a un objeto (DTO). */
	public static <T> T doGetAndConvert(String url, Class<T> clase) {
		return GSON.fromJson(doGetRaw(url), clase);
	}

	/** GET y convierte un arreglo JSON a lista de DTOs. */
	public static <T> List<T> doGetAndConvertList(String url, Class<T> clase) {
		return GSON.fromJson(doGetRaw(url), TypeToken.getParameterized(List.class, clase).getType());
	}

	public static String doGetRaw(String url) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).header("Accept", "application/json")
				.timeout(Duration.ofSeconds(20)).build();
		return send(request).body();
	}

	public static String doGet(String url) {
		return prettyPrintUsingGson(doGetRaw(url));
	}

	public static String doPost(String url, String json) {
		HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(url)).header("Content-Type", "application/json").build();
		return prettyPrintUsingGson(send(request).body());
	}

	public static String doPut(String url, String json) {
		HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(url)).header("Content-Type", "application/json").build();
		return prettyPrintUsingGson(send(request).body());
	}

	public static String doDelete(String url) {
		HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(url))
				.header("Content-Type", "application/json").build();
		return prettyPrintUsingGson(send(request).body());
	}

	public static String prettyPrintUsingGson(String uglyJson) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(JsonParser.parseString(uglyJson));
	}

	// ===================== FoodData Central (Carne / Lacteo) =====================

	/**
	 * Busca alimentos por nombre en FoodData Central y deja solo los que
	 * pertenecen a las categorias indicadas.
	 *
	 * Se consultan los tipos "Foundation" y "SR Legacy" porque son los que traen
	 * foodCategory. Como el filtro por categoria se aplica despues, se piden mas
	 * resultados de los necesarios y luego se recorta a pageSize.
	 */
	public static List<FoodDTO> searchFoods(String query, int pageSize, List<String> categorias) {
		int fetchSize = Math.min(200, Math.max(50, pageSize * 5));
		String url = URL_BASE + "/foods/search" + "?api_key=" + encode(API_KEY_FOOD_DATA_CENTRAL) + "&query="
				+ encode(query) + "&dataType=" + encode("Foundation,SR Legacy") + "&pageSize=" + fetchSize;

		JsonObject root = JsonParser.parseString(doGetRaw(url)).getAsJsonObject();
		JsonArray foods = root.has("foods") ? root.getAsJsonArray("foods") : new JsonArray();

		List<FoodDTO> result = new ArrayList<>();
		for (JsonElement el : foods) {
			JsonObject food = el.getAsJsonObject();
			String categoria = getString(food, "foodCategory");
			if (categoria == null || !categorias.contains(categoria)) {
				continue;
			}
			result.add(toFoodDTO(food, categoria));
			if (result.size() >= pageSize) {
				break;
			}
		}
		return result;
	}

	private static FoodDTO toFoodDTO(JsonObject food, String categoria) {
		List<NutrienteDTO> nutrientes = new ArrayList<>();
		if (food.has("foodNutrients") && food.get("foodNutrients").isJsonArray()) {
			for (JsonElement n : food.getAsJsonArray("foodNutrients")) {
				JsonObject nut = n.getAsJsonObject();
				Double valor = nut.has("value") && !nut.get("value").isJsonNull() ? nut.get("value").getAsDouble()
						: null;
				nutrientes.add(new NutrienteDTO(getString(nut, "nutrientName"), valor, getString(nut, "unitName")));
			}
		}
		Long fdcId = food.has("fdcId") ? food.get("fdcId").getAsLong() : null;
		return new FoodDTO(fdcId, getString(food, "description"), categoria, getString(food, "brandOwner"),
				nutrientes);
	}

	private static String getString(JsonObject obj, String field) {
		JsonElement el = obj.get(field);
		return (el == null || el.isJsonNull()) ? null : el.getAsString();
	}

	private static String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

}
