package app.jobs.queue;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.net.URI;

public class Queue {

    private static SqsClient queueClient;
    private static String queueUrl;

    public static void initQueue() {
        String endpoint = System.getenv("LOCALSTACK_URL");
        String queueName = System.getenv("QUEUE_NAME");

        queueClient = SqsClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(System.getenv("AWS_REGION")))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        System.getenv("AWS_ACCESS_KEY_ID"),
                                        System.getenv("AWS_SECRET_ACCESS_KEY")
                                )
                        )
                )
                .build();

        queueUrl = queueClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();
    }

    public static void sendMessage(String message) {
        queueClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build());

    }

    public static String receiveMessage() {
        ReceiveMessageResponse resp = queueClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(1)
                .waitTimeSeconds(10)
                .visibilityTimeout(30)
                .build());

        String message = null;
        if (!resp.messages().isEmpty()) {
            Message m = resp.messages().get(0);

            queueClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(m.receiptHandle())
                    .build());

            message = m.body();
        }
        return message;
    }
}
