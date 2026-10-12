package com.laxmiclinic.config;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.Socket;

@Configuration
public class MongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    private MongoServer mongoServer;

    @PostConstruct
    public void init() {
        if (mongoUri != null && mongoUri.contains("localhost:27017") && !isPortInUse(27017)) {
            try {
                logger.info("Starting pure Java in-memory MongoServer on localhost:27017 for seamless local dev/offline execution...");
                mongoServer = new MongoServer(new MemoryBackend());
                mongoServer.bind("localhost", 27017);
                logger.info("In-memory MongoServer started and ready.");
            } catch (Exception ex) {
                logger.warn("Could not bind in-memory MongoServer to 27017: {}", ex.getMessage());
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (mongoServer != null) {
            logger.info("Shutting down in-memory MongoServer...");
            mongoServer.shutdown();
        }
    }

    private boolean isPortInUse(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
