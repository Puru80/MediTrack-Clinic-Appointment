package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.MedicalEntity;
import com.airtribe.meditrack.exceptions.InvalidDataException;

import java.util.*;

public class DataStore<T extends MedicalEntity> {

    // Using Map for fast lookups by ID
    private Map<Long, T> dataMap = new HashMap<>();

    // Using List to maintain order or facilitate iteration (Requirement check)
    private List<T> dataList = new ArrayList<>();

    // CREATE
    public void add(T item) throws InvalidDataException {
        if (item == null || item.getId() == null) {
            throw new InvalidDataException("Cannot add null item or item with null ID to DataStore.");
        }
        if (dataMap.containsKey(item.getId())) {
            throw new InvalidDataException("Item with ID " + item.getId() + " already exists.");
        }
        dataMap.put(item.getId(), item);
        dataList.add(item);
    }

    // READ (Find by ID)
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(dataMap.get(id));
    }

    // READ (Get All)
    public List<T> getAll() {
        // Return a copy to prevent external modification of the internal structure
        return new ArrayList<>(dataList);
    }

    // UPDATE
    public void update(T item) throws InvalidDataException {
        if (!dataMap.containsKey(item.getId())) {
            throw new InvalidDataException("Cannot update. Item with ID " + item.getId() + " not found.");
        }
        // Update Map
        dataMap.put(item.getId(), item);

        // Update List (Linear operation, but necessary if keeping both synced)
        // Find index of existing item
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId().equals(item.getId())) {
                dataList.set(i, item);
                break;
            }
        }
    }

    // DELETE
    public void delete(Long id) throws InvalidDataException {
        if (!dataMap.containsKey(id)) {
            throw new InvalidDataException("Cannot delete. ID " + id + " not found.");
        }
        T itemToRemove = dataMap.get(id);
        dataMap.remove(id);
        dataList.remove(itemToRemove);
    }
}
