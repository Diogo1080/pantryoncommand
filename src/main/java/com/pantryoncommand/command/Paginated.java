package com.pantryoncommand.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * Object for paginated lists
 *
 * @param <T> type of the list
 */
@Data
@AllArgsConstructor
public class Paginated<T> {

    private List<T> results;

    private int elementsCurrentPage;

    private int currentPage;

    private int numberOfPages;

    private long totalElements;

}