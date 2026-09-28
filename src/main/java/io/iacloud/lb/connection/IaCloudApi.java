package io.iacloud.lb.connection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.iacloud.lb.factory.ConfigManager;
import io.iacloud.lb.factory.LoadBalancingPolicy;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * IaCloudApi connects the IA-Cloud framework to the deployed IA-Cloud
 * prediction model using HTTP/HTTPS.
 *
 * <p>The class loads QoS requirements from {@code config.properties},
 * converts them into a binary metrics array, builds a JSON request body,
 * and sends the request to the IA-Cloud Prediction API.</p>
 *
 * <p>It also supports an optional {@code allowed_algorithms} parameter.
 * When activated in the configuration file, the request is limited to a
 * specific list of load balancing algorithms selected by the user.</p>
 *
 * Example configuration:
 *
 * <pre>
 * api=https://iacloudac.ma/api/predictLb
 * api_key=pk_live_xxxxxxxxx
 *
 * use_allowed_algorithms=yes
 * allowed_algorithms=RoundRobin,JIQ,PALB,CARTON
 * </pre>
 *
 * @author Ben Halima Anouar
 * @version 1.0.0
 * @since 1.0
 */
public class IaCloudApi {

    private static final HttpClient client = HttpClient.newHttpClient();

    private static JsonObject optimizedAlgorithm = null;

    private static String formBody = null;

    private static ConfigManager config = null;

    private static int[] metrics;

    private static final int metricCount = 9;

    private static int AUTO = 0;

    /**
     * Returns the current QoS metrics array.
     *
     * @return current metrics array
     */
    public static int[] getMetrics() {
        return metrics;
    }

    /**
     * Updates the current QoS metrics array.
     *
     * @param metrics new metrics array
     */
    public static void setMetrics(int[] metrics) {
        IaCloudApi.metrics = metrics;
    }

    /**
     * Returns the last optimized algorithm response.
     *
     * @return IA-Cloud API response
     */
    public static JsonObject getOptimizedAlgorithm() {
        return optimizedAlgorithm;
    }

    /**
     * Sets the optimized algorithm response.
     *
     * @param optimizedAlgorithm IA-Cloud response
     */
    public static void setOptimizedAlgorithm(JsonObject optimizedAlgorithm) {
        IaCloudApi.optimizedAlgorithm = optimizedAlgorithm;
    }

    /**
     * Toggles a metric value between 0 and 1.
     *
     * <p>This method is used by the adaptive broker when runtime
     * thresholds are violated.</p>
     *
     * @param pos metric position
     */
    public static void adjustMetrics(int pos) {

        if (metrics == null) {
            throw new IllegalStateException("Metrics are not initialized.");
        }

        if (pos < 0 || pos >= metrics.length) {
            throw new IllegalArgumentException("Invalid metric position: " + pos);
        }

        metrics[pos] = metrics[pos] == 0 ? 1 : 0;
    }

    /**
     * Reads a string value from the configuration file.
     *
     * @param value property name
     * @return normalized property value
     */
    public static String getValue(String value) {
        return config.getString(value).trim().toLowerCase();
    }

    /**
     * Converts QoS properties from config file to binary metrics.
     *
     * <p>Each QoS value is converted as follows:</p>
     *
     * <ul>
     *   <li>{@code yes} becomes {@code 1}</li>
     *   <li>{@code no} becomes {@code 0}</li>
     * </ul>
     *
     * @param metricsMap map of configuration properties
     */
    public static void fromPropretiesToMetrics(Map<String, String> metricsMap) {

        String[] metricsEnum = {
                "Performance",
                "Throughtput",
                "Overhead",
                "Tolerant",
                "MigrationTime",
                "ResponseTime",
                "RessourceUtilization",
                "Scalability",
                "PowerSaving"
        };

        for (int i = 0; i < metricsEnum.length; i++) {

            String valueMetric = getValue(metricsEnum[i]);

            if (i < metricCount) {
                metrics[i] = valueMetric.equals("yes") ? 1 : 0;
            }
        }
    }

    /**
     * Checks if the metrics array has changed.
     *
     * @param newMetrics previous metrics array
     * @return true if metrics changed
     */
    public static boolean ifMetricsChanged(int[] newMetrics) {

        if (metrics == null || newMetrics == null) {
            return false;
        }

        if (metrics.length != newMetrics.length) {
            return true;
        }

        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i] != newMetrics[i]) {
                return true;
            }
        }

        return false;
    }


/**
 * Returns the correct API endpoint depending on the prediction mode.
 *
 * <p>If allowed algorithms mode is enabled, the customized prediction
 * endpoint is used. Otherwise, the standard prediction endpoint is used.</p>
 *
 * @return selected API endpoint
 */
private static String getSelectedEndpoint() {

    if (isAllowedAlgorithmsEnabled()) {

        if (LoadBalancingPolicy.CUSTOMIZED_API == null
                || LoadBalancingPolicy.CUSTOMIZED_API.isBlank()) {

            throw new IllegalStateException(
                    "customized_api is required when use_allowed_algorithms=yes"
            );
        }

        return LoadBalancingPolicy.CUSTOMIZED_API;
    }

    return LoadBalancingPolicy.API;
}

    /**
     * Loads configuration from {@code config.properties}.
     *
     * @throws IOException if configuration file cannot be loaded
     */
    private static void loadPropreties() throws IOException {

        config = new ConfigManager("config.properties");

        Map<String, String> metricsMap = new LinkedHashMap<>();

        LoadBalancingPolicy.API =
        config.getString("api").trim();

        LoadBalancingPolicy.CUSTOMIZED_API =
        config.getString("customized_api", "").trim();

        LoadBalancingPolicy.API_KEY =
        config.getString("api_key").trim();

        metrics = new int[metricCount];

        System.out.println("======================= Load file config ==============================");
        config.printAll();
        System.out.println("======================= ---------------- ==============================");

        for (String key : config.getAllKeys()) {

            String value = config.getString(key).trim().toLowerCase();

            metricsMap.put(key, value);
        }

        fromPropretiesToMetrics(metricsMap);
    }

    /**
     * Initializes IA-Cloud API communication.
     *
     * <p>During the first call, the configuration file is loaded.
     * Then the JSON request body is built and sent to the IA-Cloud API.</p>
     *
     * @throws IOException if configuration or HTTP request fails
     */
    public static void init() throws IOException {

        if (IaCloudApi.AUTO == 0) {
            loadPropreties();
        }

        IaCloudApi.AUTO++;

        formBody = buildRequestBody();


        System.out.println("IA-Cloud endpoint: " + getSelectedEndpoint());
        System.out.println("IA-Cloud request body: " + formBody);

        connect();
    }

    /**
     * Builds the JSON request body.
     *
     * <p>If {@code use_allowed_algorithms=yes}, the request body includes
     * the {@code allowed_algorithms} array. Otherwise, only
     * {@code array_param} is sent.</p>
     *
     * @return JSON request body
     */
   private static String buildRequestBody() {

    JsonObject body = new JsonObject();

    JsonArray arrayParam = new JsonArray();

    for (int metric : metrics) {
        arrayParam.add(metric);
    }

    body.add("array_param", arrayParam);

    if (isAllowedAlgorithmsEnabled()) {

        JsonArray allowedAlgorithms = getAllowedAlgorithmsFromConfig();

        if (allowedAlgorithms.isEmpty()) {
            throw new IllegalStateException(
                    "use_allowed_algorithms=yes but allowed_algorithms is empty."
            );
        }

        body.add("allowed_algorithms", allowedAlgorithms);
    }

    return body.toString();
}

    /**
     * Checks whether allowed algorithms mode is enabled.
     *
     * <p>Accepted true values are: {@code yes}, {@code true}, and {@code 1}.</p>
     *
     * @return true if allowed algorithms mode is enabled
     */
    private static boolean isAllowedAlgorithmsEnabled() {

        String value = config.getString(
                "use_allowed_algorithms",
                "no"
        ).trim().toLowerCase();

        return value.equals("yes")
                || value.equals("true")
                || value.equals("1");
    }

    /**
     * Reads allowed algorithms from the configuration file.
     *
     * <p>The expected format is:</p>
     *
     * <pre>
     * allowed_algorithms=RoundRobin,JIQ,PALB,CARTON
     * </pre>
     *
     * @return JSON array of allowed algorithms
     */
    private static JsonArray getAllowedAlgorithmsFromConfig() {

        JsonArray allowedAlgorithms = new JsonArray();

        String value = config.getString(
                "allowed_algorithms",
                ""
        ).trim();

        if (value.isBlank()) {
            return allowedAlgorithms;
        }

        String[] algorithms = value.split(",");

        for (String algorithm : algorithms) {

            String cleanAlgorithm = algorithm.trim();

            if (!cleanAlgorithm.isBlank()) {
                allowedAlgorithms.add(cleanAlgorithm);
            }
        }

        return allowedAlgorithms;
    }

    /**
     * Sends the HTTP POST request to the IA-Cloud Prediction API.
     *
     * @return IA-Cloud JSON response
     * @throws IOException if the HTTP request fails
     */
    private static JsonObject connect() throws IOException {

        optimizedAlgorithm = null;

        HttpRequest request =
        HttpRequest.newBuilder()
                .uri(URI.create(getSelectedEndpoint()))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-API-Key", LoadBalancingPolicy.API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(formBody))
                .build();

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            optimizedAlgorithm =
                    JsonParser.parseString(response.body()).getAsJsonObject();

            if (!optimizedAlgorithm.has("success")) {
                System.err.println("Invalid IA-Cloud response: " + optimizedAlgorithm);
                return optimizedAlgorithm;
            }

            if (!optimizedAlgorithm.get("success").getAsBoolean()) {
                System.err.println("IA-Cloud error: " + optimizedAlgorithm);
            }

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            System.err.println("IA-Cloud request interrupted: " + ex.getMessage());
        }

        return optimizedAlgorithm;
    }
}