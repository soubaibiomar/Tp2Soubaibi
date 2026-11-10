
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.model.output.TokenUsage;

public class Test2Couts {

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .build();

        // Question météo demandée pour le Test 7
        String question =
                "Je dois partir dans 2 jours à Paris ; "
                        + "j'y resterai 3 jours. "
                        + "Est-ce que je dois mettre un parapluie dans mes valises ?";

        UserMessage message = UserMessage.from(question);

        // Interroger Gemini sans aucun outil météo
        ChatResponse reponse = modele.chat(message);

        // Récupérer le texte de la réponse
        String texteReponse = reponse.aiMessage().text();

        // Obtenir le nombre de tokens utilisés
        TokenUsage usage = reponse.tokenUsage();

        int totalTokens = usage.totalTokenCount();
        int inputTokens = usage.inputTokenCount();
        int outputTokens = usage.outputTokenCount();

        System.out.println("=== TEST 7 : SANS OUTIL ===");
        System.out.println("Question : " + question);
        System.out.println("Réponse : " + texteReponse);

        System.out.println();
        System.out.println("Tokens entrée : " + inputTokens);
        System.out.println("Tokens sortie : " + outputTokens);
        System.out.println("Total tokens : " + totalTokens);

        // Estimation des coûts selon les tarifs utilisés au Test 2
        double coutEntree = inputTokens * 0.30 / 1_000_000;
        double coutSortie = outputTokens * 2.50 / 1_000_000;
        double coutTotal = coutEntree + coutSortie;

        System.out.printf("Coût total estimé : %.9f $%n", coutTotal);

        if (coutTotal > 0) {
            double nombreRequetes = 1.0 / coutTotal;

            System.out.printf(
                    "Requêtes pour 1 dollar : %.0f%n",
                    Math.ceil(nombreRequetes)
            );
        }
    }
}
