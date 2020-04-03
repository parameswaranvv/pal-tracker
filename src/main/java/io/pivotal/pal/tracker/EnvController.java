package io.pivotal.pal.tracker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvController {


	private final String port;

	private final String memoryLimit;

	private final String cfInstanceIndex;

	private final String cfInstanceAddress;

	public EnvController(@Value("${port:NOT_SET}") String port,
						 @Value("${memory.limit:NOT_SET}") String memoryLimit,
						 @Value("${cf.instance.index:NOT_SET}") String cfInstanceIndex,
						 @Value("${cf.instance.addr:NOT_SET}") String cfInstanceAddress) {

		this.port = port;
		this.memoryLimit = memoryLimit;
		this.cfInstanceIndex = cfInstanceIndex;
		this.cfInstanceAddress = cfInstanceAddress;
	}

	@GetMapping("/env")
	public Map<String, String> getEnv() {
		return Map.of("PORT", port, "MEMORY_LIMIT", memoryLimit, "CF_INSTANCE_INDEX", cfInstanceIndex, "CF_INSTANCE_ADDR", cfInstanceAddress);
	}
}
