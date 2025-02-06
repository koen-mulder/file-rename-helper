package com.github.koen_mulder.file_rename_helper.config;

public class AIConfig {

    private String modelName = "llama3.2:3b";
    private String ollamaEndpoint = "http://localhost:11434";
    private String embeddingStoreFile = "embedding_store.json";

    private String filenamePrompt = "U krijgt de inhoud van een dossier. Dit kunnen brieven of andere documenten zijn."
            + " Jouw taak is ten minste 10 suggesties voorde naamgeving van dit dossier te geven. Concentreer u op"
            + " het benadrukken van de belangrijkste informatie en beperk onnodige details tot een minimum."
            + " Identificeer en extraheer bovendien de datum die in het bestand wordt vermeld, en neem deze op"
            + " bij \"relevantDates\". Genereer een lijst met relevante tags die de hoofdthema's of onderwerpen"
            + " van het bestand weerspiegelen en plaats deze bij \"relevantWords\".";
//            "Je hebt een PDF bestand ontvangen met deze informatie. Geeft 10 suggesties voor"
//            + "de naamgeving van dit document.";
    private String additionalFilenamePrompt = "Suggereer nog 10 bestandsnamen.";

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOllamaEndpoint() {
        return ollamaEndpoint;
    }

    public void setOllamaEndpoint(String ollamaEndpoint) {
        this.ollamaEndpoint = ollamaEndpoint;
    }

    public String getEmbeddingStoreFile() {
        return embeddingStoreFile;
    }

    public void setEmbeddingStoreFile(String embeddingStoreFile) {
        this.embeddingStoreFile = embeddingStoreFile;
    }

    String getFilenamePrompt() {
        return filenamePrompt;
    }

    void setFilenamePrompt(String filenamePrompt) {
        this.filenamePrompt = filenamePrompt;
    }
    
    String getAdditionalFilenamePrompt() {
        return additionalFilenamePrompt;
    }
    
    void setAdditionalFilenamePrompt(String additionalFilenamePrompt) {
        this.additionalFilenamePrompt = additionalFilenamePrompt;
    }

}
