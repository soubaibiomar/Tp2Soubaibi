
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15.BgeSmallEnV15EmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test4Embeddings {

    public static void main(String[] args) {

        // Modèle local BGE
        EmbeddingModel modeleBge =
                new BgeSmallEnV15EmbeddingModel();

        // Modèle Google
        EmbeddingModel modeleGoogle =
                GoogleGenAiEmbeddingModel.builder()
                        .apiKey(System.getenv("GEMINI_KEY"))
                        .modelName("gemini-embedding-001")
                        .taskType(
                                GoogleGenAiEmbeddingModel.TaskTypeEnum
                                        .SEMANTIC_SIMILARITY
                        )
                        .timeout(Duration.ofSeconds(30))
                        .build();

        // Six couples de phrases
        String[][] phrases = {
                {
                        "Le chat dort sur le canapé.",
                        "Un félin se repose sur le sofa."
                },
                {
                        "Il fait très chaud aujourd'hui.",
                        "Il fait extrêmement froid aujourd'hui."
                },
                {
                        "Je développe une application Java.",
                        "Je programme un logiciel avec Java."
                },
                {
                        "La voiture roule sur la route.",
                        "Je prépare une salade de tomates."
                },
                {
                        "L'intelligence artificielle analyse des données.",
                        "Les systèmes d'IA traitent des informations."
                },
                {
                        "Le serveur est en fonctionnement.",
                        "Le serveur est complètement arrêté."
                }
        };

        for (int i = 0; i < phrases.length; i++) {

            String phrase1 = phrases[i][0];
            String phrase2 = phrases[i][1];

            // Embeddings BGE
            Embedding embBge1 = modeleBge.embed(phrase1).content();
            Embedding embBge2 = modeleBge.embed(phrase2).content();

            double similariteBge =
                    CosineSimilarity.between(embBge1, embBge2);

            // Embeddings Google
            Embedding embGoogle1 =
                    modeleGoogle.embed(phrase1).content();
            Embedding embGoogle2 =
                    modeleGoogle.embed(phrase2).content();

            double similariteGoogle =
                    CosineSimilarity.between(embGoogle1, embGoogle2);

            // Affichage
            System.out.println("=== Couple " + (i + 1) + " ===");
            System.out.println("Phrase 1 : " + phrase1);
            System.out.println("Phrase 2 : " + phrase2);

            System.out.printf("BGE    : %.4f%n", similariteBge);
            System.out.printf("Google : %.4f%n%n", similariteGoogle);
        }
    }
}
