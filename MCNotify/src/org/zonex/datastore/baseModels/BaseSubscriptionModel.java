package org.zonex.datastore.baseModels;

import org.zonex.subscriptions.Subscription;

import java.util.ArrayList;

/**
 * Abstract class to allow flat file and database methods implement the same methods
 */
public abstract class BaseSubscriptionModel {

        /**
         * Gets all subscriptions from storage
         * @return A list of all subscriptions
         */
        public abstract ArrayList<Subscription> selectAll();

        /**
         * Gets a list of subscriptions owned by the player with the uuid
         * @param uuid the uuid of the player to get subscriptions for
         * @return A list of the player's subscriptions
         */
        public abstract ArrayList<Subscription> selectWhereUuid(String uuid);

        /**
         * Inserts a new subscription to storage
         * @param subscription the subscription to add
         * @return if the subscription was added
         */
        public abstract boolean insert(Subscription subscription);

        /**
         * Deletes a subscription from storage
         * @param subscription the subscription to delete
         * @return if the subscription was deleted
         */
        public abstract boolean delete(Subscription subscription);


}
