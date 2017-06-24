package com.query.social.app.model;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class Question extends BaseQuestion {
    public Question() {
   super();
    }
    public Question(String uid ,String header,String userId,String userName) {
        super(uid,header,userId,userName);
    }
}
