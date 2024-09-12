package dev.samueljunnior.config.rabbit;

public final class RabbitMQConstants {

    public static final String PRIMARY_QUEUE_NAME = "primaryAnalysis";
    public static final String SECONDARY_QUEUE_NAME = "secondaryAnalysis";
    public static final String TERTIARY_QUEUE_NAME = "tertiaryAnalysis";
    public static final String DLQ_QUEUE_NAME = "dead-letter-queue";

    public static final String HEADER_ORIGINAL_QUEUE = "queue";
    public static final String HEADER_REASON = "reason";

    private RabbitMQConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
