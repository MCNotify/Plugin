package org.zonex.datastore.baseModels;

import org.zonex.subscriptions.Subscription;

import java.util.ArrayList;

public abstract class BaseSubscriptionModel {

        public abstract ArrayList<Subscription> selectAll();

        public abstract ArrayList<Subscription> selectWhereUuid(String uuid);

        public abstract boolean insert(Subscription subscription);

        public abstract boolean delete(Subscription subscription);


}
