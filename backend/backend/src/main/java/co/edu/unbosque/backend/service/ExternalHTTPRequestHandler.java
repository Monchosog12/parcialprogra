package co.edu.unbosque.backend.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Utilidad para consumir APIs externas (por si el parcial lo pide). Ejemplo:
 *
 * FactDTO f = ExternalHTTPRequestHandler.doGetAndConvert(url, FactDTO.class);
 * List<FactDTO> l = ExternalHTTPRequestHandler.doGetAndConvertList(url,
 * FactDTO.class);
 */
public class ExternalHTTPRequestHandler {

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	private static final Gson GSON = new GsonBuilder().create();
	private static final String API_KEY_FOOD_DATA_CENTRAL = System.getenv("API_KEY_FOOD_DATA_CENTRAL");
	private static final String URL_BASE = "https://api.nal.usda.gov/fdc/v1/food";

	public static HttpClient getHttpClient() {
		return HTTP_CLIENT;
	}

	public static Gson getGson() {
		return GSON;
	}

	public static String getApiKeyFoodDataCentral() {
		return API_KEY_FOOD_DATA_CENTRAL;
	}

	public static String getUrlBase() {
		return URL_BASE;
	}

	private ExternalHTTPRequestHandler() {
	}

	private static HttpResponse<String> send(HttpRequest request) {
		try {
			return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			throw new RuntimeException("Error llamando " + request.uri(), e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Peticion interrumpida " + request.uri(), e);
		}
	}

	/** GET y convierte el JSON a un objeto (DTO). */
	public static <T> T doGetAndConvert(String url, String json, String apiKey, Class<T> clase) {
		return GSON.fromJson(doGetRaw(url, json, apiKey), clase);
	}

	/** GET y convierte un arreglo JSON a lista de DTOs. */
	public static <T> List<T> doGetAndConvertList(String url, String json, String apiKey, Class<T> clase) {
		return GSON.fromJson(doGetRaw(url, json, apiKey), TypeToken.getParameterized(List.class, clase).getType());
	}

	public static String doGetRaw(String url, String json, String apiKey) {
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).header("Accept", "application/json")
				.setHeader("Authorization", "Bearer " + apiKey).build();
		return send(request).body();
	}

	public static String doGet(String url, String json, String apiKey) {
		return prettyPrintUsingGson(doGetRaw(url, json, apiKey));
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

	// Lacteo

}
