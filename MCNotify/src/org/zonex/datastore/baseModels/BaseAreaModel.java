package org.zonex.datastore.baseModels;

import org.zonex.areas.Area;

import java.util.ArrayList;

/**
 * Abstract class to allow flat file and database methods implement the same methods
 */
public abstract class BaseAreaModel {

    /**
     * Gets all player areas.
     * @return A list of all player areas
     */
    public abstract ArrayList<Area> selectAll();

    /**
     * Returns areas that are owned by the given player uuid
     * @param uuid the player uuid to find areas for
     * @return A list of areas owned by the player with the given uuid
     */
    public abstract ArrayList<Area> selectWhereUuid(String uuid);

    /**
     * Gets an area defined by the specific id
     * @param id the id of the area to find
     * @return The area of the given id. Null if none found.
     */
    public abstract Area selectWhereId(int id);

    /**
     * Inserts a new area into storage
     * @param area the area to add to storage
     * @return if the area was successfully added
     */
    public abstract boolean insert(Area area);

    /**
     * updates the area within storage
     * @param area the area to update
     * @return if the area was successfully updated
     */
    public abstract boolean update(Area area);

    /**
     * Deletes an area from storage
     * @param area the area to remove from storage
     * @return if the area was removed from storage
     */
    public abstract boolean delete(Area area);

}
