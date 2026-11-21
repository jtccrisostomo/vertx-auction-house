
package io.github.jccrisostomo.auctionhouse.config;

import java.util.Map;

public record DatabaseConfig(
        String host,
        int port,
        String database,
        String username,
        String password
) {

    public static DatabaseConfig fromEnvironment(Map<String, String> env) {
        return new DatabaseConfig(
                env.getOrDefault("POSTGRES_HOST", "127.0.0.1"),
                Integer.parseInt(env.getOrDefault("POSTGRES_PORT", "5432")),
                requireVariable(env, "POSTGRES_DB"),
                requireVariable(env, "POSTGRES_USER"),
                requireVariable(env, "POSTGRES_PASSWORD")
        );
    }

    private static String requireVariable(
            Map<String, String> env,
            String name
    ) {
        String value = env.get(name);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing environment variable: " + name
            );
        }

        return value;
    }

    @Override
    public String toString() {
        return "DatabaseConfig[credentials redacted]";
    }
}
