package arora.software.monitoring_service.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class AlertService {
    
    private final RestClient restClient;
    private final String webhookUrl;
    private final Logger log = LoggerFactory.getLogger(AlertService.class);

    public AlertService(RestClient.Builder builder,  @Value("${slack.webhook.url:}") String webhookUrl){
        this.restClient = builder.build();
        this.webhookUrl = webhookUrl;
    }
    public void sendAlert(String message){
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.warn("No Slack URL CONFIGURED.");
            return;
        }
        try {
            String jsonPayload = """
                    { "text": "%s" }
                    """.formatted(message);
            restClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonPayload)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            log.error("FAILED TO SEND SLACK ALERT", e);
        }
    }


}
