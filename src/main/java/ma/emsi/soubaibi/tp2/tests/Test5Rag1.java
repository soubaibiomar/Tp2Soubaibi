
package ma.emsi.soubaibi.tp2.tests;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.nio.file.Path;
import java.util.List;

public class Test5Rag1 {

    public interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        // Charger les informations du fichier
        Document document = FileSystemDocumentLoader.loadDocument(
                Path.of("infos.txt")
        );

        List<Document> documents = List.of(document);

        // Créer le magasin d'embeddings en mémoire
        EmbeddingStore<TextSegment> embeddingStore =
                new InMemoryEmbeddingStore<>();

        // Ingérer les documents
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        // Créer le modèle Gemini
        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash-lite")
                .temperature(0.7)
                .build();

        // Créer l'assistant avec mémoire et RAG
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(
                        MessageWindowChatMemory.withMaxMessages(10)
                )
                .contentRetriever(
                        EmbeddingStoreContentRetriever.from(embeddingStore)
                )
                .build();



        // Première question
        String reponse1 = assistant.chat(
                "Comment s'appelle le chat de Pierre ?"
        );
        System.out.println("Réponse 1 : " + reponse1);

        // Deuxième question
        String reponse2 = assistant.chat(
                "Pierre appelle son chat. Qu'est-ce qu'il pourrait dire ?"
        );
        System.out.println("Réponse 2 : " + reponse2);

    }
}
