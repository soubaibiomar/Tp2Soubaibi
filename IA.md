# Utilisation de l'IA pendant le TP2

**Problème rencontré :**

Pendant le Test 6 du TP2, j'ai rencontré une erreur Java lors de l'utilisation de la classe `ApacheTikaDocumentParser` pour lire un fichier PDF. IntelliJ affichait le message suivant :

`package dev.langchain4j.data.document.parser.apache.tika does not exist`

**Pourquoi j'ai utilisé l'IA :**

J'ai essayé de comprendre pourquoi Java ne reconnaissait pas ce package, mais je ne savais pas si le problème venait du code, de Maven ou d'une dépendance manquante. J'ai donc demandé de l'aide à ChatGPT.

**Prompt utilisé :**

« Comment résoudre l'erreur Java `package dev.langchain4j.data.document.parser.apache.tika does not exist` dans mon projet Maven avec LangChain4j ? »

**Réponse de l'IA :**

ChatGPT m'a expliqué que la classe `ApacheTikaDocumentParser` nécessitait une dépendance Maven spécifique qui n'était pas correctement disponible dans mon projet. Il m'a proposé d'ajouter la dépendance `langchain4j-document-parser-apache-tika` dans le fichier `pom.xml`, puis de recharger les dépendances Maven et de recompiler le projet.

Après cette modification, la compilation a réussi et j'ai pu utiliser le parseur pour extraire le texte de mon fichier PDF.

**Ce que j'ai appris :**

J'ai compris qu'un import Java ne suffit pas pour utiliser une bibliothèque externe. Il faut également vérifier que la dépendance correspondante est présente dans le fichier `pom.xml` et correctement chargée par Maven. Ce problème m'a aussi aidé à mieux comprendre le fonctionnement des dépendances dans un projet Java et l'utilisation d'Apache Tika avec LangChain4j.