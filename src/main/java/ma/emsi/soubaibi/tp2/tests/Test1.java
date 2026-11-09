
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;

public class Test1 {

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");


        // Création du modèle avec un builder
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .build();

        // Pose une question au modèle

        // Première question
        String reponse1 = modele.chat("Bonjour, je m'appelle Omar.");
        System.out.println("Réponse 1 : " + reponse1);

        // Deuxième question
        String reponse2 = modele.chat("Comment je m'appelle ?");
        System.out.println("Réponse 2 : " + reponse2);

    }
}
