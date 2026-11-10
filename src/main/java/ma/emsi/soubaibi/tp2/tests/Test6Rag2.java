
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15.BgeSmallEnV15EmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.nio.file.Path;
import java.util.Scanner;

public class Test6Rag2 {

    public interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        // 1. Charger le PDF avec Apache Tika
        Document document = FileSystemDocumentLoader.loadDocument(
                Path.of("ml.pdf"),
                new ApacheTikaDocumentParser()
        );

        // 2. Vérifier l'extraction
        String texte = document.text();

        System.out.println("=== EXTRACTION DU PDF ===");
        System.out.println("Nombre de caractères : " + texte.length());
        System.out.println("Début du PDF :");
        System.out.println(
                texte.substring(0, Math.min(400, texte.length()))
        );

        // 3. Choisir explicitement le modèle d'embeddings
        EmbeddingModel modeleEmbedding =
                new BgeSmallEnV15EmbeddingModel();

        // 4. Créer le magasin d'embeddings
        EmbeddingStore<TextSegment> embeddingStore =
                new InMemoryEmbeddingStore<>();

        // 5. Indexer le document avec le modèle choisi
        EmbeddingStoreIngestor ingestor =
                EmbeddingStoreIngestor.builder()
                        .embeddingModel(modeleEmbedding)
                        .embeddingStore(embeddingStore)
                        .build();

        System.out.println();
        System.out.println("=== DÉBUT INDEXATION PDF ===");

        long debut = System.nanoTime();

        ingestor.ingest(document);

        double duree =
                (System.nanoTime() - debut) / 1_000_000_000.0;

        System.out.println("=== INDEXATION TERMINÉE ===");
        System.out.printf("Durée : %.2f secondes%n", duree);

        // 6. Créer le modèle Gemini
        ChatModel modele = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .build();

        // 7. Créer l'assistant avec mémoire et RAG
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(modele)
                .chatMemory(
                        MessageWindowChatMemory.withMaxMessages(10)
                )
                .contentRetriever(
                        EmbeddingStoreContentRetriever.builder()
                                .embeddingStore(embeddingStore)
                                .embeddingModel(modeleEmbedding)
                                .build()
                )
                .build();

        // 8. Conversation interactive avec l'assistant RAG
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println();
            System.out.println("=== CONVERSATION RAG SUR ML.PDF ===");
            System.out.println("Tapez 'fin' pour quitter.");

            while (true) {

                System.out.print("\nVous : ");

                String question = scanner.nextLine().trim();

                // Quitter la conversation
                if (question.equalsIgnoreCase("fin")) {
                    break;
                }

                // Ignorer les questions vides
                if (question.isEmpty()) {
                    continue;
                }

                // Envoyer la question à l'assistant
                String reponse = assistant.chat(question);

                // Afficher la réponse
                System.out.println("Assistant : " + reponse);
            }
        }

        System.out.println();
        System.out.println("Conversation terminée.");
    }
}
