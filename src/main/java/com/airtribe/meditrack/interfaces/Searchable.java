package com.airtribe.meditrack.interfaces;

public interface Searchable {

    /**
     * Returns true if the object matches the query.
     * Implementation should handle filtering by Name, ID, etc.
     */
    boolean matches(String query);

}
