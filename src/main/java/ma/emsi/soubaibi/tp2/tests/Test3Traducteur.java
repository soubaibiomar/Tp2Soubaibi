
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;

public class Test3Traducteur {

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        // Création du modèle
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .build();

        // TEST 1 : Template avec une seule variable
        PromptTemplate template1 = PromptTemplate.from(
                "Traduis le texte suivant en anglais : {{it}}"
        );

        // Remplacer la variable par le texte
        Prompt prompt1 = template1.apply(
                "Bonjour, je m'appelle Omar."
        );

        // Envoyer le premier prompt à Gemini
        String reponse1 = modele.chat(prompt1.text());

        System.out.println("=== TEST 1 : UNE VARIABLE ===");
        System.out.println("Prompt : " + prompt1.text());
        System.out.println("Réponse : " + reponse1);

        // TEST 2 : Template avec deux variables
        PromptTemplate template2 = PromptTemplate.from(
                "Traduis le texte suivant en {{langue}} : {{texte}}"
        );

        // Remplacer les deux variables
        Prompt prompt2 = template2.apply(Map.of(
                "langue", "espagnol",
                "texte", "Bonjour, je m'appelle Omar."
        ));

        // Envoyer le deuxième prompt à Gemini
        String reponse2 = modele.chat(prompt2.text());

        System.out.println();
        System.out.println("=== TEST 2 : DEUX VARIABLES ===");
        System.out.println("Prompt : " + prompt2.text());
        System.out.println("Réponse : " + reponse2);
    }
}
