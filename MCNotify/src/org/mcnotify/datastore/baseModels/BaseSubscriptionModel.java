package org.mcnotify.datastore.baseModels;

import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class BaseSubscriptionModel {

        public abstract ArrayList<Subscription> selectAll();

        public abstract Subscription selectWhereUuid(String uuid);

        public abstract boolean insert(Subscription subscription);

        public abstract boolean delete(Subscription subscription);


}
