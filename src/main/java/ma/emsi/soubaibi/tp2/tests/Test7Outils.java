
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;

public class Test7Outils {

    public static void main(String[] args) {

        // 1. Récupérer la clé API Gemini
        String cle = System.getenv("GEMINI_KEY");

        if (cle == null || cle.isBlank()) {
            throw new IllegalStateException(
                    "La variable GEMINI_KEY est absente."
            );
        }

        // 2. Construire le modèle Gemini
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .build();

        // 3. Créer l'assistant et lui donner accès à MeteoTool
        AssistantMeteo assistant =
                AiServices.builder(AssistantMeteo.class)
                        .chatModel(modele)
                        .chatMemory(
                                MessageWindowChatMemory.withMaxMessages(20)
                        )
                        .tools(new MeteoTool())
                        .build();

        // 4. Démarrer la conversation interactive
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("=== TEST 7 : GEMINI AVEC OUTIL MÉTÉO ===");
            System.out.println("Tapez 'fin' pour quitter.");

            while (true) {

                System.out.print("\nVous : ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                String question = scanner.nextLine().trim();

                if (question.equalsIgnoreCase("fin")) {
                    break;
                }

                if (question.isEmpty()) {
                    continue;
                }

                try {

                    // Gemini peut demander à exécuter MeteoTool
                    String reponse = assistant.chat(question);

                    System.out.println(
                            "\nAssistant : " + reponse
                    );

                } catch (Exception e) {

                    System.err.println(
                            "Erreur : " + e.getMessage()
                    );
                }
            }
        }

        System.out.println("\nConversation terminée.");
    }
}
