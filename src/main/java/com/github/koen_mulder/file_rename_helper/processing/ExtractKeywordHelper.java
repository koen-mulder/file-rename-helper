package com.github.koen_mulder.file_rename_helper.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;

/**
 * Helper for extracting keywords from a file.
 */
public class ExtractKeywordHelper {
    
    private ExtractKeywordHelper() {
        // Private constructor to prevent instantiation
    }

    public static List<String> getUsedKeywords(String absolutePath) {
        // Load document
        Document document = FileSystemDocumentLoader.loadDocument(absolutePath);
        
        return getWordsSortedByFrequency(document.text());
    }
    
    /**
     * Takes a string, extracts words, and returns a list of unique words
     * sorted by their frequency in descending order (most frequent first).
     * Words are considered case-insensitive and punctuation is ignored.
     *
     * @param content The input string containing text.
     * @return A List of unique words sorted by frequency (most frequent first).
     *         Returns an empty list if the input is null, empty, or contains no words.
     */
    public static List<String> getWordsSortedByFrequency(String content) {
        // 1. Handle edge cases: null or empty input
        if (content == null || content.trim().isEmpty()) {
            return Collections.emptyList(); // Return an immutable empty list
        }

        // 2. Define what constitutes a "word" (using regex)
        //    \\w+ matches sequences of one or more "word characters" (letters, numbers, underscore)
        //    We convert the content to lowercase first for case-insensitive matching.
        Pattern wordPattern = Pattern.compile("\\w+");
        Matcher matcher = wordPattern.matcher(content.toLowerCase());

        // 3. Count the frequency of each word
        Map<String, Integer> wordCounts = new HashMap<>();
        while (matcher.find()) {
            String word = matcher.group();
            // Increment count for the word, defaulting to 0 if not present
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        // Handle case where no words were found (e.g., input was only punctuation)
        if (wordCounts.isEmpty()) {
             return Collections.emptyList();
        }

        // 4. Sort the unique words based on their counts (frequency)
        //    Get the list of unique words (the keys of the map)
        List<String> uniqueWords = new ArrayList<>(wordCounts.keySet());

        //    Sort the list using a custom comparator
        //    The comparator compares words based on their counts stored in the map
        uniqueWords.sort((word1, word2) -> {
            int count1 = wordCounts.get(word1);
            int count2 = wordCounts.get(word2);
            // Sort in descending order of frequency
            // If frequencies are equal, maintain original relative order (or sort alphabetically - optional)
            // Using Integer.compare is safer than subtraction for large numbers
            return Integer.compare(count2, count1); 
            
            // Optional: Secondary sort alphabetically if counts are equal
            // if (countComparison != 0) {
            //     return countComparison;
            // } else {
            //     return word1.compareTo(word2); // Ascending alphabetical
            // }
        });

        // 5. Return the sorted list of unique words
        return uniqueWords;
    }

    // --- Optional: Alternative implementation using Java Streams ---
    public static List<String> getWordsSortedByFrequencyStreams(String content) {
        if (content == null || content.trim().isEmpty()) {
            return Collections.emptyList();
        }

        Pattern wordPattern = Pattern.compile("\\w+");

        return wordPattern.matcher(content.toLowerCase())
                .results() // Stream<MatchResult>
                .map(MatchResult::group) // Stream<String>
                .collect(Collectors.groupingBy( // Group words and count occurrences
                        Function.identity(), // Key is the word itself
                        Collectors.counting()  // Value is the count (as Long)
                )) // -> Map<String, Long>
                .entrySet() // Get entries to sort
                .stream()   // Stream<Map.Entry<String, Long>>
                // Sort by value (count) descending. Map.Entry.comparingByValue() sorts ascending.
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                 // Optional secondary sort:
                 // .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                 //         .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey) // Extract the word (key) from the sorted entry
                .collect(Collectors.toList()); // Collect the sorted words into a List
    }


}
