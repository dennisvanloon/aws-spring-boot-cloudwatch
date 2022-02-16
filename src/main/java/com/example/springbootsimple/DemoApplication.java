package com.example.springbootsimple;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;
import java.util.Map;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CloudWatchAsyncClient cloudWatchAsyncClient() {
		return CloudWatchAsyncClient
				.builder()
				.region(Region.EU_WEST_1)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();
	}

	@Bean
	public MeterRegistry getMeterRegistry() {
		CloudWatchConfig cloudWatchConfig = setupCloudWatchConfig();

		return new CloudWatchMeterRegistry(
						cloudWatchConfig,
						Clock.SYSTEM,
						cloudWatchAsyncClient());
	}

	private CloudWatchConfig setupCloudWatchConfig() {
		return new CloudWatchConfig() {

			private final Map<String, String> configuration = Map.of(
					"cloudwatch.namespace", "springBootCloudwatchApp",
					"cloudwatch.step", Duration.ofSeconds(10).toString());

			@Override
			public String get(String key) {
				return configuration.get(key);
			}
		};
	}
}
